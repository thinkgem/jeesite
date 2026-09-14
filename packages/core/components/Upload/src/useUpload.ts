import { computed, ComputedRef, onMounted, Ref, ref, ToRefs, unref } from 'vue';
import { UploadParams, uploadParams } from '@jeesite/core/api/sys/upload';
import { useI18n } from '@jeesite/core/hooks/web/useI18n';

export function useUpload(props: any) {
  const { t } = useI18n();

  const uploadParamsRef = ref<UploadParams>({} as any);

  onMounted(async () => {
    uploadParamsRef.value = await uploadParams();
  });

  const getUploadParams = computed(() => {
    const params = unref(uploadParamsRef);
    if (props.checkmd5 != null) {
      params.checkmd5 = props.checkmd5;
    }
    if (props.imageMaxWidth) {
      params.imageMaxWidth = props.imageMaxWidth;
    }
    if (props.imageMaxHeight) {
      params.imageMaxHeight = props.imageMaxHeight;
    }
    if (props.chunked != null) {
      params.chunked = props.chunked;
    }
    if (props.chunkSize != null) {
      params.chunkSize = props.chunkSize;
    }
    if (props.threads != null) {
      params.threads = props.threads;
    }
    return params;
  });

  // 上传最大文件大小（字节）
  const getMaxFileSize = computed(() => {
    const maxSize = props.maxSize;
    if (maxSize) {
      return maxSize * 1024 * 1024;
    } else {
      const { maxFileSize } = unref(uploadParamsRef);
      if (maxFileSize) {
        return maxFileSize;
      }
    }
    return 500 * 1024 * 1024;
  });

  // 文件类型限制
  const getAccept = computed(() => {
    const accept = props.accept;
    if (accept && accept.length > 0) {
      return accept;
    }
    const params = unref(uploadParamsRef);
    // 上传参数尚未加载完成时，不限制文件类型
    if (!params || !params.imageAllowSuffixes) {
      return [];
    }
    const { imageAllowSuffixes, mediaAllowSuffixes, fileAllowSuffixes } = params;
    const uploadType = props.uploadType;
    const splitSuffixes = (v: string | undefined) => (v ? v.split(',') : []);
    if (uploadType == 'image') {
      return splitSuffixes(imageAllowSuffixes);
    } else if (uploadType == 'media') {
      return splitSuffixes(mediaAllowSuffixes);
    } else if (uploadType == 'file') {
      return splitSuffixes(fileAllowSuffixes);
    } else {
      return [
        ...splitSuffixes(imageAllowSuffixes),
        ...splitSuffixes(mediaAllowSuffixes),
        ...splitSuffixes(fileAllowSuffixes),
      ];
    }
  });

  // 文件类型限制（文件选择框类型）
  const getStringAccept = computed(() => {
    return unref(getAccept)
      .map((item: any) => {
        if (item.indexOf('/') > 0 || item.startsWith('.')) {
          return item;
        } else {
          return `.${item}`;
        }
      })
      .join(',');
  });

  // 支持jpg、jpeg、png格式，不超过2M，最多可选择10张图片
  const getHelpText = computed(() => {
    const helpText = props.helpText;
    if (helpText) {
      return helpText;
    }
    const helpTexts: string[] = [];

    const accept = props.accept;
    if (accept.length > 0) {
      helpTexts.push(t('component.upload.accept', [accept.join(',')]));
    }

    const maxSize = unref(getMaxFileSize);
    if (maxSize) {
      helpTexts.push(t('component.upload.maxSize', [maxSize / 1024 / 1024]));
    }

    const maxNumber = props.maxNumber;
    if (maxNumber && maxNumber !== Infinity) {
      helpTexts.push(t('component.upload.maxNumber', [maxNumber]));
    }
    return helpTexts.join('，');
  });

  return { getUploadParams, getMaxFileSize, getAccept, getStringAccept, getHelpText };
}
