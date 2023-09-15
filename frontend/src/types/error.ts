export class APIError extends Error {
  errorCode;
  message;

  constructor(errorCode: string, message: string) {
    super();
    this.errorCode = errorCode;
    this.message = message;
  }
}
