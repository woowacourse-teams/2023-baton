import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { useLogin } from '@/hooks/useLogin';
import { usePageRouter } from '@/hooks/usePageRouter';
import { APIError } from '@/types/error';
import React, { useContext } from 'react';

interface Props {
  children: React.ReactNode;
  error: Error | APIError;
}

const LoginError = ({ children, error }: Props) => {
  const { showErrorToast } = useContext(ToastContext);
  const { silentLogin } = useLogin();
  const { goToLoginPage } = usePageRouter();

  if (error instanceof APIError) {
    // 기간이 만료된 JWT
    if (error.errorCode === 'JW005') {
      silentLogin();

      return children;
    }

    // 기간이 만료된 Refresh Token
    if (error.errorCode === 'JW009') {
      showErrorToast({ title: ERROR_TITLE.NO_PERMISSION, description: ERROR_DESCRIPTION.TOKEN_EXPIRATION });
      goToLoginPage();
    }

    // 이외 모든 로그인 관련 오류
    if (error.errorCode.includes('JW') || error.errorCode.includes('OA')) {
      showErrorToast({ title: ERROR_TITLE.NO_PERMISSION, description: ERROR_DESCRIPTION.NO_TOKEN });
      goToLoginPage();
    }

    return;
  }

  showErrorToast({ title: ERROR_TITLE.ERROR, description: error?.message ?? '알 수 없는 오류' });

  return children;
};

export default LoginError;
