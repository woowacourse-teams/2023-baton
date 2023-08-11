import { BATON_BASE_URL } from '@/constants';

const fetchAPI = async (url: string, options: RequestInit) => {
  const response = await fetch(`${BATON_BASE_URL}${url}`, options);

  if (!response.ok) throw new Error(`Error: ${response.text}`);

  return response.json();
};

export const getRequest = async <T>(url: string, authorization?: string) => {
  const response: T = await fetchAPI(
    url,
    authorization
      ? {
          method: 'GET',
          headers: {
            Authorization: authorization,
          },
        }
      : {
          method: 'GET',
        },
  );

  return response;
};

export const postRequest = async <T>(url: string, authorization: string, body: BodyInit) => {
  const response: T = await fetchAPI(url, {
    method: 'POST',
    headers: {
      Authorization: authorization,
    },
    body,
  });

  return response;
};

export const deleteRequest = async <T>(url: string, authorization: string, body: BodyInit) => {
  const response: T = await fetchAPI(url, {
    method: 'DELETE',
    headers: {
      Authorization: authorization,
    },
    body,
  });

  return response;
};

export const putRequest = async <T>(url: string, authorization: string, body: BodyInit) => {
  const response: T = await fetchAPI(url, {
    method: 'PUT',
    headers: {
      Authorization: authorization,
    },
    body,
  });

  return response;
};

export const patchRequest = async <T>(url: string, authorization: string, body: BodyInit) => {
  const response: T = await fetchAPI(url, {
    method: 'PATCH',
    headers: {
      Authorization: authorization,
    },
    body,
  });

  return response;
};
