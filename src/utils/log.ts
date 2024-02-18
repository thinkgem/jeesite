export const env = import.meta.env;

export function warn(message: string) {
  console.warn(`[${env.VITE_GLOB_APP_TITLE} warn]:${message}`);
}

export function error(message: string) {
  throw new Error(`[${env.VITE_GLOB_APP_TITLE} error]:${message}`);
}
