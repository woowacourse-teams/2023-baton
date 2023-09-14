import { ACCESS_TOKEN_LOCAL_STORAGE_KEY, REFRESH_TOKEN_COOKIE_NAME } from '@/constants';
import { getRequest, postRequestWithCookie } from './fetch';
import { ERROR_DESCRIPTION } from '@/constants/message';
import { getCookie } from '@/utils/cookie';

export const login = async (code: string) => {
  getRequest(`/oauth/login/github?code=${code}`).then(async (response) => {
    const jwt = await response.headers.get('Authorization');
    const cookies = response.headers.getSetCookie();

    if (!jwt || cookies.length < 1) throw new Error(ERROR_DESCRIPTION.LOGIN);

    cookies.forEach((cookie) => {
      document.cookie = cookie;
    });

    localStorage.setItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY, jwt);
  });
};

export const silentLogin = async () => {
  const accessToken = localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);
  const refreshToken = getCookie(REFRESH_TOKEN_COOKIE_NAME);

  if (!accessToken || !refreshToken) throw new Error(ERROR_DESCRIPTION.TOKEN_EXPIRATION);

  postRequestWithCookie('/oauth/refresh', accessToken).then(async (response) => {
    const jwt = await response.headers.get('Authorization');
    const cookies = response.headers.getSetCookie();

    if (!jwt || cookies.length < 1) throw new Error(ERROR_DESCRIPTION.LOGIN);

    cookies.forEach((cookie) => {
      document.cookie = cookie;
    });

    localStorage.setItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY, jwt);
  });
};
