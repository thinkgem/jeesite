import { Ref, unref, computed, onMounted, ref } from 'vue';
import { uploadParams } from '/@/api/sys/upload';
import { useI18n } from '/@/hooks/web/useI18n';
const { t } = useI18n();

export function useUploadType({
  uploadTypeRef,
  acceptRef,
  helpTextRef,
  maxNumberRef,
  maxSizeRef,
}: {
  uploadTypeRef: Ref<string>;
  acceptRef: Ref<string[]>;
  helpTextRef: Ref<string>;
  maxNumberRef: Ref<number>;
  maxSizeRef: Ref<number>;
}) {
  const imageAllowSuffixes = ref<string[]>([]);
  const mediaAllowSuffixes = ref<string[]>([]);
  const fileAllowSuffixes = ref<string[]>([]);
  const allowSuffixes = ref<string[]>([]);

  onMounted(async () => {
    const params = await uploadParams();
    imageAllowSuffixes.value = params.imageAllowSuffixes.split(',');
    mediaAllowSuffixes.value = params.mediaAllowSuffixes.split(',');
    fileAllowSuffixes.value = params.fileAllowSuffixes.split(',');
    allowSuffixes.value = [...unref(imageAllowSuffixes), ...unref(mediaAllowSuffixes), ...unref(fileAllowSuffixes)];
  });

  // 文件类型限制
  const getAccept = computed(() => {
    const accept = unref(acceptRef);
    if (accept && accept.length > 0) {
      return accept;
    }
    const uploadType = unref(uploadTypeRef);
    if (uploadType == 'image') {
      return unref(imageAllowSuffixes);
    } else if (uploadType == 'media') {
      return unref(mediaAllowSuffixes);
    } else if (uploadType == 'file') {
      return unref(fileAllowSuffixes);
    } else {
      return unref(allowSuffixes);
    }
  });

  const getStringAccept = computed(() => {
    const accept = unref(getAccept)
      .map((item: any) => {
        if (item.indexOf('/') > 0 || item.startsWith('.')) {
          return item;
        } else {
          return `.${item}`;
        }
      })
      .join(',');
    return accept;
  });

  // 支持jpg、jpeg、png格式，不超过2M，最多可选择10张图片，。
  const getHelpText = computed(() => {
    const helpText = unref(helpTextRef);
    if (helpText) {
      return helpText;
    }
    const helpTexts: string[] = [];

    const accept = unref(acceptRef);
    if (accept.length > 0) {
      helpTexts.push(t('component.upload.accept', [accept.join(',')]));
    }

    const maxSize = unref(maxSizeRef);
    if (maxSize) {
      helpTexts.push(t('component.upload.maxSize', [maxSize]));
    }

    const maxNumber = unref(maxNumberRef);
    if (maxNumber && maxNumber !== Infinity) {
      helpTexts.push(t('component.upload.maxNumber', [maxNumber]));
    }
    return helpTexts.join('，');
  });
  return { getAccept, getStringAccept, getHelpText };
}
