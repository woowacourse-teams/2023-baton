import { APIError } from '@/types/error';
import { ACCESS_TOKEN_LOCAL_STORAGE_KEY, BATON_BASE_URL } from '@/constants';
import { useState } from 'react';

export const useFetch = () => {
  const [error, setError] = useState<Error | APIError | null>(null);

  const getAccessToken = () => localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);

  const fetchAPI = (url: string, options: RequestInit) =>
    fetch(`${BATON_BASE_URL}${url}`, options)
      .then(async (response) => {
        if (!response.ok) {
          const error: APIError = await response.json();

          throw error;
        }

        return response;
      })
      .catch((error: Error | APIError) => {
        setError(error);
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

  if (error) throw error;

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
