import { ACCESS_TOKEN_LOCAL_STORAGE_KEY } from '@/constants';
import { TOAST_ERROR_MESSAGE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { useContext, useState } from 'react';

export interface LoginToken {
  value: string;
  expirationDate: Date;
}

export const useToken = () => {
  const { showErrorToast } = useContext(ToastContext);
  const [isLogin, setIsLogin] = useState(false);

  const getToken = (): LoginToken | null => {
    try {
      const item = localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);

      if (!item) {
        showErrorToast(TOAST_ERROR_MESSAGE.NO_TOKEN);

        setIsLogin(false);
        return null;
      }

      setIsLogin(true);

      return JSON.parse(item);
    } catch {
      setIsLogin(false);

      return null;
    }
  };

  const saveToken = (token: string) => {
    const date = new Date();
    date.setDate(date.getDate() + 30);

    localStorage.setItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY, JSON.stringify({ value: token, expirationDate: date }));
  };

  const removeToken = () => {
    localStorage.removeItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);
  };

  const validateToken = () => {
    const item = localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);
    if (!item) return;

    const token: LoginToken = JSON.parse(item);
    const time = new Date(token.expirationDate);

    if (Number(time) - Date.now() < 0) {
      removeToken();

      showErrorToast(TOAST_ERROR_MESSAGE.TOKEN_EXPIRATION);
    }
  };

  return { getToken, removeToken, saveToken, validateToken, isLogin };
};
