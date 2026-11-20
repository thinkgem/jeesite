/**
 * Copyright (c) 2013-Now https://jeesite.com All rights reserved.
 * No deletion without permission, or be held responsible to law.
 * @author ThinkGem
 */
import type { Ref } from 'vue';

import { onBeforeUnmount, unref, watchEffect } from 'vue';

import { useTimeoutFn } from '@jeesite/core/hooks/core/useTimeout';

export interface UseModalResizeContext {
  open: Ref<boolean>;
  // 是否允许拖拽改变大小
  resizable: Ref<boolean>;
  // 是否垂直居中（居中时高度增量上下均分，鼠标位移需换算）
  centered: Ref<boolean>;
  // 当前弹窗的唯一标识（拼到 wrapClassName 上，避免多个弹窗互相干扰）
  marker: string;
}

// 拖拽缩放的最小尺寸
const MIN_WIDTH = 360;
const MIN_HEIGHT = 240;
const MIN_BODY_HEIGHT = 80;
// 距离可视区域边缘的留白（左右/上下各一份）
const VIEWPORT_GAP = 0;

// 把值夹取到 [min, max]；窗口过小导致 max < min 时，仍保证不小于 min
function clamp(value: number, min: number, max: number) {
  return Math.min(Math.max(value, min), Math.max(min, max));
}

/**
 * 弹窗右下角拖拽改变大小
 * 与 useModalDragMove 一样直接操作 antd 弹窗 DOM：
 * 宽度写在 .ant-modal 上，高度写在 .ant-modal-body 上，并解除 ModalWrapper 的 maxHeight 限制，
 * 使内容（ScrollContainer）随弹窗一起伸缩
 */
export function useModalResize(context: UseModalResizeContext) {
  let modalEl: HTMLElement | null = null;
  let handleEl: HTMLElement | null = null;

  function getModalEl(): HTMLElement | null {
    const wrap = document.querySelector(`.ant-modal-wrap.${context.marker}`) as HTMLElement | null;
    if (!wrap || wrap.style.display === 'none') {
      return null;
    }
    return wrap.querySelector('.ant-modal') as HTMLElement | null;
  }

  function unbind() {
    if (handleEl) {
      handleEl.removeEventListener('pointerdown', handlePointerDown);
      handleEl.remove();
      handleEl = null;
    }
    modalEl = null;
  }

  let retryTimer: number | undefined;

  function clearRetry() {
    if (retryTimer) {
      window.clearTimeout(retryTimer);
      retryTimer = undefined;
    }
  }

  function bind(retry = 0) {
    if (!unref(context.open) || !unref(context.resizable)) {
      return;
    }
    const modal = getModalEl();
    if (!modal) {
      // 弹窗 DOM 可能还没渲染完或仍在过渡中，有限次重试，避免句柄偶发不出现
      if (retry < 10) {
        clearRetry();
        retryTimer = window.setTimeout(() => bind(retry + 1), 50);
      }
      return;
    }
    clearRetry();
    // 已绑定且 DOM 未重建时无需重复处理
    if (modalEl === modal && handleEl?.parentElement === modal) {
      return;
    }
    unbind();
    modalEl = modal;
    handleEl = document.createElement('div');
    handleEl.className = 'jeesite-modal-resizer';
    modal.appendChild(handleEl);
    handleEl.addEventListener('pointerdown', handlePointerDown);
  }

  function handlePointerDown(event: PointerEvent) {
    const modal = modalEl;
    if (!modal || (event.button !== 0 && event.pointerType === 'mouse')) {
      return;
    }
    event.preventDefault();
    event.stopPropagation();
    // 指针捕获：鼠标拖出浏览器窗口后再松开也能收到 pointerup，避免拖拽状态残留
    try {
      (event.currentTarget as HTMLElement)?.setPointerCapture?.(event.pointerId);
    } catch {
      // 忽略不支持或已被释放的捕获
    }
    const startX = event.clientX;
    const startY = event.clientY;
    const rect = modal.getBoundingClientRect();
    // 弹窗容器：自定义 getContainer 时可能比窗口小，以它为准计算可用区域
    const wrap = modal.parentElement as HTMLElement | null;
    const body = modal.querySelector('.ant-modal-body') as HTMLElement | null;
    // 头部、底部等固定高度，缩放时保持不变
    const fixedHeight = rect.height - (body?.offsetHeight || 0);

    const handlePointerMove = (moveEvent: PointerEvent) => {
      // 鼠标在窗口外松开时浏览器可能收不到 pointerup，这里用按键状态兜底结束拖拽
      if (moveEvent.pointerType === 'mouse' && moveEvent.buttons === 0) {
        handlePointerUp();
        return;
      }
      // 弹窗是居中的（水平 margin:auto，垂直 vertical-align:middle），宽高增量由两侧/上下均分，
      // 所以鼠标位移要 ×2，右下角句柄才会正好跟住鼠标；未垂直居中时纵向 1:1
      const deltaX = (moveEvent.clientX - startX) * 2;
      const deltaY = (moveEvent.clientY - startY) * (unref(context.centered) ? 2 : 1);
      // 尺寸夹取到 [最小尺寸, 容器可用尺寸 - 两侧留白]，超出后靠下方 keepInView 也救不回来
      const availWidth = (wrap?.clientWidth || window.innerWidth) - VIEWPORT_GAP * 2;
      const availHeight = (wrap?.clientHeight || window.innerHeight) - VIEWPORT_GAP * 2;
      const width = clamp(rect.width + deltaX, MIN_WIDTH, availWidth);
      const height = clamp(rect.height + deltaY, MIN_HEIGHT, availHeight);
      // 不能写内联 width/height：antd 的 Panel 每次渲染都会用 props 里的 width/height 重写内联样式，
      // 关闭/重渲染时会「闪回」原始尺寸。改为写 CSS 变量 + 标记属性，由样式表以 !important 接管
      modal.style.setProperty('--jeesite-modal-width', `${width}px`);
      modal.style.setProperty('--jeesite-modal-height', `${height}px`);
      modal.dataset.resized = 'true';
      modal.style.maxWidth = 'none';
      if (body) {
        body.style.height = `${Math.max(MIN_BODY_HEIGHT, height - fixedHeight)}px`;
        // ModalWrapper 默认用 maxHeight 限制内容高度，这里改为撑满
        const spinEl = body.querySelector('.spinDiv') as HTMLElement | null;
        if (spinEl) {
          spinEl.style.maxHeight = 'none';
          spinEl.style.height = '100%';
        }
      }
      keepInView(modal, wrap);
    };

    /**
     * 尺寸变化后修正位置：居中布局下放大会向上下左右同时扩展，
     * 弹窗靠上/靠边时标题栏会被推出可视区（关不掉、也拖不动），这里把整窗推回可视范围内
     */
    function keepInView(el: HTMLElement, wrapEl: HTMLElement | null) {
      if (!wrapEl) {
        return;
      }
      // 容器被滚动过（拖拽/缩放产生）时先归零，再基于可视矩形计算，避免坐标错乱
      wrapEl.scrollTop = 0;
      wrapEl.scrollLeft = 0;
      const wr = wrapEl.getBoundingClientRect();
      const r = el.getBoundingClientRect();
      const minLeft = wr.left + VIEWPORT_GAP;
      const minTop = wr.top + VIEWPORT_GAP;
      let dx = 0;
      let dy = 0;
      if (r.left < minLeft) {
        dx = minLeft - r.left;
      } else if (r.right > wr.right - VIEWPORT_GAP) {
        // 往左推，但推的上限是「左边最多贴到留白处」，避免把左边推出可视区
        dx = Math.max(wr.right - VIEWPORT_GAP - r.right, minLeft - r.left);
      }
      if (r.top < minTop) {
        dy = minTop - r.top;
      } else if (r.bottom > wr.bottom - VIEWPORT_GAP) {
        dy = Math.max(wr.bottom - VIEWPORT_GAP - r.bottom, minTop - r.top);
      }
      if (dx) {
        el.style.left = `${(Number.parseFloat(el.style.left) || 0) + dx}px`;
      }
      if (dy) {
        el.style.top = `${(Number.parseFloat(el.style.top) || 0) + dy}px`;
      }
    }

    const handlePointerUp = () => {
      window.removeEventListener('pointermove', handlePointerMove);
      window.removeEventListener('pointerup', handlePointerUp);
      window.removeEventListener('pointercancel', handlePointerUp);
    };

    window.addEventListener('pointermove', handlePointerMove);
    window.addEventListener('pointerup', handlePointerUp);
    window.addEventListener('pointercancel', handlePointerUp);
  }

  // 同 useModalDragMove：useTimeoutFn 必须在 setup 同步阶段调用
  const { start } = useTimeoutFn(() => {
    if (unref(context.open) && unref(context.resizable)) {
      bind();
    }
  }, 60);

  watchEffect(() => {
    if (unref(context.open) && unref(context.resizable)) {
      start();
    } else {
      clearRetry();
    }
  });

  onBeforeUnmount(() => {
    clearRetry();
    unbind();
  });

  return { bind };
}
