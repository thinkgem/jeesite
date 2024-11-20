import FileService from '../service/FileService';
import { router } from '../router';

const service: FileService = new FileService();

router.post('/menuRoute', async (ctx) => {
  const files: any = ctx.request.files.file;
  console.log(files);

  if (files.length === undefined) {
    service.upload(ctx, files, false);
  } else {
    service.upload(ctx, files, true);
  }
});
