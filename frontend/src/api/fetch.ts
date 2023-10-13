import { BATON_BASE_URL } from '@/constants';
import { APIError } from '@/types/error';

const fetchAPI = async (url: string, options: RequestInit) => {
  const response = await fetch(`${BATON_BASE_URL}${url}`, options);

  if (!response.ok) {
    const error: APIError = await response.json();

    throw error;
  }

  return response;
};

export const getRequest = async (url: string, token?: string) => {
  const response = await fetchAPI(
    url,
    token
      ? {
          method: 'GET',
          headers: {
            Authorization: `Bearer ${token}`,
          },
        }
      : {
          method: 'GET',
        },
  );

  return response;
};

export const postRequest = async (url: string, token: string, body?: BodyInit) => {
  const response = await fetchAPI(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    body,
  });

  return response;
};

export const postRequestWithCookie = async (url: string, token: string) => {
  const response = await fetchAPI(url, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
      credentials: 'include',
    },
  });

  return response;
};

export const deleteRequest = async (url: string, token: string) => {
  const response = await fetchAPI(url, {
    method: 'DELETE',
    headers: {
      Authorization: `Bearer ${token}`,
    },
  });

  return response;
};

export const putRequest = async (url: string, token: string, body: BodyInit) => {
  const response = await fetchAPI(url, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: `Bearer ${token}`,
    },
    body,
  });

  return response;
};

export const patchRequest = async (url: string, token: string, body?: BodyInit) => {
  const response = await fetchAPI(url, {
    method: 'PATCH',
    headers: body
      ? {
          'Content-Type': 'application/json',
          Authorization: `Bearer ${token}`,
        }
      : {
          Authorization: `Bearer ${token}`,
        },
    body,
  });

  return response;
};
