import { ACCESS_TOKEN_LOCAL_STORAGE_KEY } from '@/constants';
import { useRef, useState } from 'react';
import { useFetch } from './useFetch';

const getAccessToken = () => localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);

export const useLogin = () => {
  const [isLogin, setIsLogin] = useState<boolean>(!!getAccessToken());

  const timer = useRef<number | null>(null);

  const { getRequestWithAuth, postRequestWithCookie } = useFetch();

  const login = async (code: string) => {
    getRequestWithAuth(`/oauth/login/github?code=${code}`, (response) => {
      try {
        const jwt = response.headers.get('Authorization');

        if (!jwt) return;

        localStorage.setItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY, jwt);
        setIsLogin(true);
      } catch (error) {
        setIsLogin(false);
      }
    });
  };

  const silentLogin = async () => {
    postRequestWithCookie('/oauth/refresh', (response) => {
      try {
        const jwt = response.headers.get('Authorization');

        if (!jwt) return;

        localStorage.setItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY, jwt);
        setIsLogin(true);
      } catch (error) {
        setIsLogin(false);
      }
    });
  };

  const logout = () => {
    localStorage.removeItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);

    setIsLogin(false);
  };

  const checkLoginToken = () => {
    if (!isLogin) return;

    if (timer.current) return;

    silentLogin();

    timer.current = window.setTimeout(() => {
      checkLoginToken();
    }, 29 * 60 * 1000);
  };

  return { isLogin, login, logout, silentLogin, checkLoginToken };
};
