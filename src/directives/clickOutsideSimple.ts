import type { DirectiveBinding, ObjectDirective } from 'vue';

type DocumentHandler = <T extends MouseEvent>(mouseup: T, mousedown: T) => void;

type FlushList = Map<
  HTMLElement,
  {
    documentHandler: DocumentHandler;
    bindingFn: (...args: unknown[]) => unknown;
  }
>;

const nodeList: FlushList = new Map();

function createDocumentHandler(_el: HTMLElement, binding: DirectiveBinding): DocumentHandler {
  return function () {
    binding.value();
  };
}

// 创建新组件，调用之前创建的组件事件
const ClickOutside: ObjectDirective = {
  beforeMount(el, binding) {
    for (const { documentHandler } of nodeList.values()) {
      documentHandler(el, binding);
    }
    nodeList.set(el, {
      documentHandler: createDocumentHandler(el, binding),
      bindingFn: binding.value,
    });
  },
  unmounted(el) {
    nodeList.delete(el);
  },
};

export default ClickOutside;
