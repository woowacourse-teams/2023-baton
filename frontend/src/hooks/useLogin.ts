import { getLoginToken } from '@/apis/apis';
import { ACCESS_TOKEN_LOCAL_STORAGE_KEY } from '@/constants';
import { useState } from 'react';

const getAccessToken = () => localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);

export const useLogin = () => {
  const [isLogin, setIsLogin] = useState(Boolean(getAccessToken()));

  const saveAccessToken = (response: Response) => {
    const jwt = response.headers.get('Authorization');

    if (jwt) {
      localStorage.setItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY, jwt);
      setIsLogin(true);

      return;
    }

    setIsLogin(false);
  };

  const login = async (authCode: string) => {
    await getLoginToken(authCode).then((response) => {
      saveAccessToken(response);

      setIsLogin(true);
    });
  };

  const logout = () => {
    localStorage.removeItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);

    setIsLogin(false);
  };

  return { isLogin, login, logout };
};
