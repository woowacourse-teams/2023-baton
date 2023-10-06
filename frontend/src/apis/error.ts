import { ACCESS_TOKEN_LOCAL_STORAGE_KEY } from '@/constants';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';
import { APIError, CustomApiError } from '@/types/error';

const removeAccessToken = () => localStorage.removeItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);

export const validateResponse = async (response: Response) => {
  if (response.ok) return;

  try {
    const apiError: APIError = await response.json();

    if (apiError.errorCode.includes('JW') || apiError.errorCode.includes('OA')) {
      removeAccessToken();
      window.location.reload();

      const authErrorCode = ['JW007', 'JW008', 'JW009', 'JW010'];
      throw new CustomApiError(
        ERROR_TITLE.NO_PERMISSION,
        authErrorCode.includes(apiError.errorCode) ? ERROR_DESCRIPTION.TOKEN_EXPIRATION : ERROR_DESCRIPTION.NO_TOKEN,
      );
    }
  } catch (error) {
    throw new CustomApiError(ERROR_TITLE.REQUEST, `${response.status}: ` + ERROR_DESCRIPTION.UNEXPECTED);
  }

  throw new CustomApiError(ERROR_TITLE.REQUEST, `${response.status}: ` + ERROR_DESCRIPTION.UNEXPECTED);
};

export const throwErrorBadRequest = () => {
  throw new CustomApiError('', '요청이 잘못되었어요');
};
