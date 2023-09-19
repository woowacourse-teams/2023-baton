import { ACCESS_TOKEN_LOCAL_STORAGE_KEY } from '@/constants';
import { useRef, useState } from 'react';
import { useFetch } from './useFetch';
import { getExpiration } from '@/utils/jwt';

const getAccessToken = () => localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);

export const useLogin = () => {
  const [isLogin, setIsLogin] = useState<boolean>(!!getAccessToken());

  const timer = useRef<number | null>(null);

  const { getRequestWithAuth, postRequestWithCookie } = useFetch();

  const login = async (code: string) => {
    try {
      await getRequestWithAuth(`/oauth/login/github?code=${code}`, (response) => {
        const jwt = response.headers.get('Authorization');

        if (!jwt) return;

        localStorage.setItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY, jwt);
        setIsLogin(true);
      });
    } catch (error) {
      setIsLogin(false);
    }
  };

  const silentLogin = async () => {
    try {
      await postRequestWithCookie('/oauth/refresh', (response) => {
        const jwt = response.headers.get('Authorization');

        if (!jwt) return;

        localStorage.setItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY, jwt);
        setIsLogin(true);
      });
    } catch (error) {
      setIsLogin(false);
    }
  };

  const logout = () => {
    localStorage.removeItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);

    setIsLogin(false);
  };

  const checkLoginToken = () => {
    const token = getAccessToken();

    if (timer.current) return;
    if (!token || !isLogin) return;

    const exp = getExpiration(token);
    const restMinute = (exp.getTime() - new Date().getTime()) / 1000 / 60;

    if (restMinute <= 2) silentLogin();

    timer.current = window.setTimeout(
      () => {
        checkLoginToken();
      },
      restMinute <= 2 ? 28 * 60 * 1000 : restMinute * 60 * 1000,
    );
  };

  return { isLogin, login, logout, silentLogin, checkLoginToken };
};
