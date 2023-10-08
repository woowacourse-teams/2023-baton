import { usePageRouter } from '@/hooks/usePageRouter';
import React, { useContext, useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { ToastContext } from '@/contexts/ToastContext';
import { TOAST_ERROR_MESSAGE } from '@/constants/message';
import LoadingPage from './LoadingPage';
import { issueLoginToken } from '@/apis/auth';

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
      issueLoginToken(code)
        .then(() => {
          goToMainPage();
        })
        .catch(() => {
          showErrorToast(TOAST_ERROR_MESSAGE.LOGIN);

          goToLoginPage();
        });
    }
  }, [location]);

  return <LoadingPage />;
}

export default GithubCallbackPage;
