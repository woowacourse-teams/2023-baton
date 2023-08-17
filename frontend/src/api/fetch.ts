import { BATON_BASE_URL } from '@/constants';
interface customError {
  errorCode: string;
  message: string;
}

const fetchAPI = async (url: string, options: RequestInit) => {
  const response = await fetch(`${BATON_BASE_URL}${url}`, options);

  if (!response.ok) {
    const error: customError = await response.json();

    throw new Error(error.message);
  }
  return response;
};

export const getRequest = async (url: string, authorization?: string) => {
  const response = await fetchAPI(
    url,
    authorization
      ? {
          method: 'GET',
          headers: {
            'Content-Type': 'application/json',
            Authorization: authorization,
          },
        }
      : {
          method: 'GET',
        },
  );

  return response;
};

export const postRequest = async (url: string, authorization: string, body: BodyInit) => {
  const response = await fetchAPI(url, {
    method: 'POST',
    headers: {
      Authorization: authorization,
    },
    body,
  });

  return response;
};

export const deleteRequest = async (url: string, authorization: string) => {
  const response = await fetchAPI(url, {
    method: 'DELETE',
    headers: {
      Authorization: authorization,
    },
  });

  return response;
};

export const putRequest = async (url: string, authorization: string, body: BodyInit) => {
  const response = await fetchAPI(url, {
    method: 'PUT',
    headers: {
      'Content-Type': 'application/json',
      Authorization: authorization,
    },
    body,
  });

  return response;
};

export const patchRequest = async (url: string, authorization: string, body?: BodyInit) => {
  const response = await fetchAPI(url, {
    method: 'PATCH',
    headers: body
      ? {
          'Content-Type': 'application/json',
          Authorization: authorization,
        }
      : {
          Authorization: authorization,
        },
    body,
  });

  return response;
};
