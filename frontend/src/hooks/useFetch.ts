import { APIError } from '@/types/error';
import { ACCESS_TOKEN_LOCAL_STORAGE_KEY, BATON_BASE_URL } from '@/constants';
import { useContext } from 'react';
import { ToastContext } from '@/contexts/ToastContext';
import { usePageRouter } from './usePageRouter';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';

export const useFetch = () => {
  const { showErrorToast } = useContext(ToastContext);

  const getAccessToken = () => localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);
  const removeAccessToken = () => localStorage.removeItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);

  const fetchAPI = (url: string, options: RequestInit) =>
    fetch(`${BATON_BASE_URL}${url}`, options)
      .then(async (response) => {
        if (!response.ok) {
          const apiError: APIError = await response.json();

          if (apiError.errorCode.includes('JW') || apiError.errorCode.includes('OA')) {
            removeAccessToken();

            showErrorToast({
              title: ERROR_TITLE.NO_PERMISSION,
              description: ['JW007', 'JW008', 'JW009', 'JW010'].includes(apiError.errorCode)
                ? ERROR_DESCRIPTION.TOKEN_EXPIRATION
                : ERROR_DESCRIPTION.NO_TOKEN,
            });

            return;
          }

          showErrorToast({ title: ERROR_TITLE.REQUEST, description: apiError.message });

          return;
        }

        return response;
      })
      .catch(() => {
        showErrorToast({
          title: ERROR_TITLE.ERROR,
          description: ERROR_DESCRIPTION.UNEXPECTED,
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
        credentials: 'include',
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
        credentials: 'include',
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

  const patchRequestWithAuth = async (url: string, onSuccess: (response: Response) => void, body?: BodyInit) => {
    const result = await fetchAPI(url, {
      method: 'PATCH',
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
