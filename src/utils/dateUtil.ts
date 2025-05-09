/**
 * Independent time operation tool to facilitate subsequent switch to dayjs
 */
import dayjs from 'dayjs';

const DATE_TIME_FORMAT = 'YYYY-MM-DD HH:mm:ss';
const DATE_FORMAT = 'YYYY-MM-DD';

export function formatToDateTime(
  date: dayjs.ConfigType | undefined = undefined,
  format = DATE_TIME_FORMAT,
): string | undefined {
  if (!date) return undefined;
  return dayjs(date).format(format);
}

export function formatToDate(date: dayjs.ConfigType | undefined = undefined, format = DATE_FORMAT): string | undefined {
  if (!date) return undefined;
  return dayjs(date).format(format);
}

export const dateUtil = dayjs;
