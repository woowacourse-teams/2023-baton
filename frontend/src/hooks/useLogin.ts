import { ACCESS_TOKEN_LOCAL_STORAGE_KEY, REFRESH_TOKEN_COOKIE_NAME } from '@/constants';
import { getCookie } from '@/utils/cookie';
import { useRef, useState } from 'react';
import { useFetch } from './useFetch';
import { APIError } from '@/types/error';

const getAccessToken = () => localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);

const getRefreshToken = () => getCookie(REFRESH_TOKEN_COOKIE_NAME);

export const useLogin = () => {
  const [isLogin, setIsLogin] = useState<boolean>(!!getRefreshToken());
  const [error, setError] = useState<Error | APIError | null>(null);

  const timer = useRef<number | null>(null);

  const { getRequestWithAuth, postRequestWithCookie } = useFetch();

  const login = async (code: string) => {
    getRequestWithAuth(`/oauth/login/github?code=${code}`, (response) => {
      try {
        const jwt = response.headers.get('Authorization');
        const cookies = response.headers.getSetCookie();

        if (!jwt || cookies.length < 1) return;

        cookies.forEach((cookie) => {
          document.cookie = cookie;
        });

        localStorage.setItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY, jwt);
        setIsLogin(true);
      } catch (error) {
        if (error instanceof APIError || error instanceof Error) setError(error);
        setIsLogin(false);
      }
    });
  };

  const silentLogin = async () => {
    postRequestWithCookie('/oauth/refresh', (response) => {
      try {
        const jwt = response.headers.get('Authorization');
        const cookies = response.headers.getSetCookie();

        if (!jwt || cookies.length < 1) return;

        cookies.forEach((cookie) => {
          document.cookie = cookie;
        });

        localStorage.setItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY, jwt);
        setIsLogin(true);
      } catch (error) {
        if (error instanceof APIError || error instanceof Error) setError(error);
        setIsLogin(false);
      }
    });
  };

  const checkLoginToken = () => {
    if (!isLogin) return;

    if (!getRefreshToken()) {
      setIsLogin(false);

      return;
    }

    if (timer.current) return;

    silentLogin();

    timer.current = window.setTimeout(() => {
      checkLoginToken();
    }, 29 * 60 * 1000);
  };

  if (error) throw error;

  return { isLogin, login, silentLogin, checkLoginToken };
};
