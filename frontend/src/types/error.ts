export interface APIError {
  errorCode: string;
  message: string;
}

export class CustomApiError extends Error {
  errorCode;
  message;

  constructor(errorCode: string, message: string) {
    super();
    this.errorCode = errorCode;
    this.message = message;
  }
}
