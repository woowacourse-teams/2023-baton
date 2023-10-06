import { ACCESS_TOKEN_LOCAL_STORAGE_KEY, BATON_BASE_URL } from '@/constants';
import { Method } from '@/types/api';
import { throwErrorBadRequest, validateResponse } from './error';
import { getExpiration } from '@/utils/jwt';
import { postRefreshToken } from './apis';

const getAccessToken = () => localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);

const saveAccessToken = (response: Response) => {
  const jwt = response.headers.get('Authorization');

  if (jwt) {
    localStorage.setItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY, jwt);

    return;
  }

  localStorage.removeItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);
};

const parseJson = async (response: Response): Promise<any> => {
  await validateResponse(response);

  try {
    return await response.json();
  } catch (error) {
    return;
  }
};

const fetchJson = async <T>(url: string, options?: RequestInit): Promise<T> => {
  return await fetch(`${BATON_BASE_URL}${url}`, options)
    .catch(() => throwErrorBadRequest())
    .then(async (response) => parseJson(response));
};

const fetchApi = <T>(url: string, method: Method, isAuth: boolean, body?: BodyInit) => {
  const fetchFunction = () =>
    fetchJson<T>(url, {
      method,
      ...(isAuth && {
        headers: {
          ...(body && { 'Content-Type': 'application/json' }),
          Authorization: `Bearer ${getAccessToken()}`,
          credentials: 'include',
        },
      }),
      body,
    });

  const jwt = getAccessToken();

  if (isAuth && jwt) {
    const exp = getExpiration(jwt);
    const restMinute = (exp.getTime() - new Date().getTime()) / 1000 / 60;

    if (restMinute <= 2) {
      postRefreshToken().then((response) => {
        saveAccessToken(response);

        return fetchFunction();
      });
    }
  }

  return fetchFunction();
};

export const request = {
  get: <T>(url: string, isAuth: boolean) => fetchApi<T>(url, 'GET', isAuth),

  post: <T>(url: string, body?: BodyInit) => fetchApi<T>(url, 'POST', true, body),

  put: <T>(url: string, body?: BodyInit) => fetchApi<T>(url, 'PUT', true, body),

  patch: <T>(url: string, body?: BodyInit) => fetchApi<T>(url, 'PATCH', true, body),

  delete: <T>(url: string) => fetchApi<T>(url, 'DELETE', true),
};
