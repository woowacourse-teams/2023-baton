import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';
import { APIError, CustomApiError } from '@/types/error';
import { logout } from './auth';

export const validateResponse = async (response: Response) => {
  if (response.ok) return;

  try {
    const apiError: APIError = await response.json();

    if (apiError.errorCode.includes('JW') || apiError.errorCode.includes('OA')) {
      logout();

      throw new CustomApiError(ERROR_TITLE.NO_PERMISSION, ERROR_DESCRIPTION.NO_TOKEN);
    }
  } catch (error) {
    throw new CustomApiError(ERROR_TITLE.REQUEST, `${response.status}: ` + ERROR_DESCRIPTION.UNEXPECTED);
  }

  throw new CustomApiError(ERROR_TITLE.REQUEST, `${response.status}: ` + ERROR_DESCRIPTION.UNEXPECTED);
};

export const throwErrorBadRequest = () => {
  throw new CustomApiError('', '요청이 잘못되었어요');
};
