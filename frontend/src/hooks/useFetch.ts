import { APIError } from '@/types/error';
import { ACCESS_TOKEN_LOCAL_STORAGE_KEY, BATON_BASE_URL } from '@/constants';
import { useContext } from 'react';
import { ToastContext } from '@/contexts/ToastContext';
import { usePageRouter } from './usePageRouter';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';

export const useFetch = () => {
  const { goToLoginPage } = usePageRouter();
  const { showErrorToast } = useContext(ToastContext);

  const getAccessToken = () => localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);

  const fetchAPI = (url: string, options: RequestInit) =>
    fetch(`${BATON_BASE_URL}${url}`, options)
      .then(async (response) => {
        if (!response.ok) {
          const apiError: APIError = await response.json();

          if (apiError.errorCode.includes('JW') || apiError.errorCode.includes('OA')) {
            showErrorToast({ title: apiError.message, description: apiError.message });
            goToLoginPage();

            return;
          }

          showErrorToast({ title: apiError.message, description: apiError.message });

          return;
        }

        return response;
      })
      .catch((error) => {
        showErrorToast({
          title: ERROR_TITLE.NETWORK,
          description: error instanceof Error ? error.message : ERROR_DESCRIPTION.UNEXPECTED,
        });
      });

  const getRequest = async (url: string, onSuccess: (response: Response) => void) => {
    const result = await fetchAPI(url, {
      method: 'GET',
    });

    if (result) onSuccess(result);
  };

  const getRequestWithAuth = async (url: string, onSuccess: (response: Response) => void) => {
    const result = await fetchAPI(url, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    });

    if (result) onSuccess(result);
  };

  const postRequestWithAuth = async (url: string, onSuccess: (response: Response) => void, body?: BodyInit) => {
    const result = await fetchAPI(url, {
      method: 'POST',
      headers: body
        ? {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${getAccessToken()}`,
          }
        : {
            Authorization: `Bearer ${getAccessToken()}`,
          },
      body,
    });

    if (result) onSuccess(result);
  };

  const postRequestWithCookie = async (url: string, onSuccess: (response: Response) => void, body?: BodyInit) => {
    const result = await fetchAPI(url, {
      method: 'POST',
      headers: body
        ? {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${getAccessToken()}`,
            credentials: 'include',
          }
        : {
            Authorization: `Bearer ${getAccessToken()}`,
            credentials: 'include',
          },
      body,
    });

    if (result) onSuccess(result);
  };

  const deleteRequestWithAuth = async (url: string, onSuccess: (response: Response) => void) => {
    const result = await fetchAPI(url, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    });

    if (result) onSuccess(result);
  };

  const putRequestWithAuth = async (url: string, onSuccess: (response: Response) => void, body?: BodyInit) => {
    const result = await fetchAPI(url, {
      method: 'PUT',
      headers: body
        ? {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${getAccessToken()}`,
          }
        : {
            Authorization: `Bearer ${getAccessToken()}`,
          },
      body,
    });

    if (result) onSuccess(result);
  };

  const patchRequestWithAuth = async (url: string, onSuccess: (response: Response) => void, body?: BodyInit) => {
    const result = await fetchAPI(url, {
      method: 'PATCH',
      headers: body
        ? {
            'Content-Type': 'application/json',
            Authorization: `Bearer ${getAccessToken()}`,
          }
        : {
            Authorization: `Bearer ${getAccessToken()}`,
          },
      body,
    });

    if (result) onSuccess(result);
  };

  return {
    getRequest,
    getRequestWithAuth,
    postRequestWithAuth,
    postRequestWithCookie,
    deleteRequestWithAuth,
    patchRequestWithAuth,
    putRequestWithAuth,
  };
};
