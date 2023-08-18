import { ACCESS_TOKEN_LOCAL_STORAGE_KEY } from '@/constants';
import { useToken } from '@/hooks/useToken';
import { usePageRouter } from '@/hooks/usePageRouter';
import React, { useContext, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { getRequest } from '@/api/fetch';
import { ToastContext } from '@/contexts/ToastContext';
import { ERROR_DESCRIPTION, ERROR_TITLE, TOAST_ERROR_MESSAGE } from '@/constants/message';

function GithubCallbackPage() {
  const location = useLocation();

  const { showErrorToast } = useContext(ToastContext);

  const { goToMainPage, goToLoginPage } = usePageRouter();
  const { saveToken } = useToken();

  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const code = searchParams.get('code');
    const error = searchParams.get('error');

    if (error) {
      showErrorToast(TOAST_ERROR_MESSAGE.LOGIN);

      goToLoginPage();
    }

    if (code) {
      getToken(code);
    }
  }, [location]);

  const getToken = (code: string) => {
    if (localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY) === null) {
      getRequest(`/oauth/login/github?code=${code}`)
        .then(async (response) => {
          const jwt = await response.headers.get('Authorization');

          if (!jwt) {
            showErrorToast(TOAST_ERROR_MESSAGE.NO_TOKEN);

            return;
          }

          saveToken(jwt);
          goToMainPage();
        })
        .catch((error: Error) => {
          const description = error instanceof Error ? error.message : ERROR_DESCRIPTION.UNEXPECTED;
          showErrorToast({ title: ERROR_TITLE.REQUEST, description });

          goToLoginPage();
        });
    }
  };

  return <div>GithubRedirect...</div>;
}

export default GithubCallbackPage;
