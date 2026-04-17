/**
 * @description attachment element
 * @author wangfupeng
 */

type EmptyText = {
  text: '';
};

export type AttachmentElement = {
  type: 'attachment';
  fileName: string;
  link: string;
  children: EmptyText[];
};
