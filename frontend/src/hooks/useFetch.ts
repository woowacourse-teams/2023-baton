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

  const getRequest = async <T>(url: string, onSuccess: (result: T) => void) => {
    const response = await fetchAPI(url, {
      method: 'GET',
    });

    response
      ?.json()
      .then((data: T) => {
        onSuccess(data);
      })
      .catch((error) => {
        setError(error);
      });
  };

  const getRequestWithAuth = async <T>(url: string, onSuccess: (result: T) => void) => {
    const response = await fetchAPI(url, {
      method: 'GET',
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    });

    response
      ?.json()
      .then((data: T) => {
        onSuccess(data);
      })
      .catch((error) => {
        setError(error);
      });
  };

  const postRequestWithAuth = async <T>(url: string, onSuccess: (result: T) => void, body?: BodyInit) => {
    const response = await fetchAPI(url, {
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

    response
      ?.json()
      .then((data: T) => {
        onSuccess(data);
      })
      .catch((error) => {
        setError(error);
      });
  };

  const deleteRequestWithAuth = async <T>(url: string, onSuccess: (result: T) => void) => {
    const response = await fetchAPI(url, {
      method: 'DELETE',
      headers: {
        Authorization: `Bearer ${getAccessToken()}`,
      },
    });

    response
      ?.json()
      .then((data: T) => {
        onSuccess(data);
      })
      .catch((error) => {
        setError(error);
      });
  };

  const putRequestWithAuth = async <T>(url: string, onSuccess: (result: T) => void, body?: BodyInit) => {
    const response = await fetchAPI(url, {
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

    response
      ?.json()
      .then((data: T) => {
        onSuccess(data);
      })
      .catch((error) => {
        setError(error);
      });
  };

  const patchRequestWithAuth = async <T>(url: string, onSuccess: (result: T) => void, body?: BodyInit) => {
    const response = await fetchAPI(url, {
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

    response
      ?.json()
      .then((data: T) => {
        onSuccess(data);
      })
      .catch((error) => {
        setError(error);
      });
  };

  if (error) throw error;

  return {
    fetchAPI,
    getRequest,
    getRequestWithAuth,
    postRequestWithAuth,
    deleteRequestWithAuth,
    patchRequestWithAuth,
    putRequestWithAuth,
  };
};
