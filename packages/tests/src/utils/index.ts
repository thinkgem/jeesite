export class Result {
  static success(data: any) {
    return {
      code: 0,
      success: true,
      result: data,
    };
  }
  static error(data: any) {
    return {
      code: 0,
      success: false,
      result: data,
    };
  }
}
