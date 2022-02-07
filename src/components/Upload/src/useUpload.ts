import { Ref, unref, computed } from 'vue';
import { useI18n } from '/@/hooks/web/useI18n';
const { t } = useI18n();

const imageAllowSuffixes = '.gif,.bmp,.jpeg,.jpg,.ico,.png,.tif,.tiff'.split(',');
const mediaAllowSuffixes =
  '.flv,.swf,.mkv,webm,.mid,.mov,.mp3,.mp4,.m4v,.mpc,.mpeg,.mpg,.swf,.wav,.wma,.wmv,.avi,.rm,.rmi,.rmvb,.aiff,.asf,.ogg,.ogv'.split(
    ',',
  );
const fileAllowSuffixes =
  '.doc,.docx,.rtf,.xls,.xlsx,.csv,.ppt,.pptx,.pdf,.vsd,.txt,.md,.xml,.rar,.zip,7z,.tar,.tgz,.jar,.gz,.gzip,.bz2,.cab,.iso'.split(
    ',',
  );
const allowSuffixes = [...imageAllowSuffixes, ...mediaAllowSuffixes, ...fileAllowSuffixes];

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
  // 文件类型限制
  const getAccept = computed(() => {
    const accept = unref(acceptRef);
    if (accept && accept.length > 0) {
      return accept;
    }
    const uploadType = unref(uploadTypeRef);
    if (uploadType == 'image') {
      return imageAllowSuffixes;
    } else if (uploadType == 'media') {
      return mediaAllowSuffixes;
    } else if (uploadType == 'file') {
      return fileAllowSuffixes;
    } else {
      return allowSuffixes;
    }
  });
  const getStringAccept = computed(() => {
    return unref(getAccept)
      .map((item) => {
        if (item.indexOf('/') > 0 || item.startsWith('.')) {
          return item;
        } else {
          return `.${item}`;
        }
      })
      .join(',');
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
