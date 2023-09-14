import { APIError, getRequest, postRequestWithCookie } from '@/api/fetch';
import { ACCESS_TOKEN_LOCAL_STORAGE_KEY, REFRESH_TOKEN_COOKIE_NAME } from '@/constants';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { deleteCookie, getCookie } from '@/utils/cookie';
import { useContext, useState } from 'react';
import { usePageRouter } from './usePageRouter';

export const useToken = () => {
  const [isLogin, setIsLogin] = useState<boolean>(
    !!(getCookie(REFRESH_TOKEN_COOKIE_NAME) && localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY)),
  );
  const { showErrorToast } = useContext(ToastContext);
  const { goToLoginPage } = usePageRouter();

  const getToken = () => {
    const accessToken = localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);
    const refreshToken = getCookie(REFRESH_TOKEN_COOKIE_NAME);

    if (!accessToken || !refreshToken) {
      setIsLogin(false);
      showErrorToast({ title: ERROR_TITLE.NO_PERMISSION, description: ERROR_DESCRIPTION.NO_TOKEN });
      goToLoginPage();

      return null;
    }

    setIsLogin(true);

    return accessToken;
  };

  const removeToken = () => {
    localStorage.removeItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);
    deleteCookie('refreshToken');

    setIsLogin(false);
  };

  // const saveAccessToken = (token: string) => {
  //   localStorage.setItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY, token);
  //   setAccessToken(token);
  // };

  return { getToken, isLogin, removeToken };
};
