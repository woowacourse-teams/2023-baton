import { usePageRouter } from '@/hooks/usePageRouter';
import React, { useContext, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { APIError } from '@/api/fetch';
import { ToastContext } from '@/contexts/ToastContext';
import { ERROR_TITLE, TOAST_ERROR_MESSAGE } from '@/constants/message';
import { login } from '@/api/login';

function GithubCallbackPage() {
  const location = useLocation();

  const { showErrorToast } = useContext(ToastContext);

  const { goToMainPage, goToLoginPage } = usePageRouter();

  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const code = searchParams.get('code');
    const error = searchParams.get('error');

    if (error) {
      showErrorToast(TOAST_ERROR_MESSAGE.LOGIN);

      goToLoginPage();
    }

    if (code) {
      login(code)
        .then(() => {
          goToMainPage();
        })
        .catch((error: Error | APIError) => {
          const description = error instanceof Error ? error.message : error.message;
          showErrorToast({ title: ERROR_TITLE.REQUEST, description });

          goToLoginPage();
        });
    }
  }, [location]);

  return <div>GithubRedirect...</div>;
}

export default GithubCallbackPage;
