import React, { useContext } from 'react';
import { APIError } from '@/types/error';
import { ToastContext } from '@/contexts/ToastContext';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';
import { useLogin } from '@/hooks/useLogin';
import { usePageRouter } from '@/hooks/usePageRouter';

interface Props {
  children: React.ReactNode;
}

interface State {
  hasError: boolean;
  error: Error | APIError | null;
}

class LoginErrorBoundary extends React.Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = {
      hasError: false,
      error: null,
    };
  }

  static getDerivedStateFromError(error: Error | APIError) {
    return { hasError: true, error: error };
  }

  render() {
    const { hasError, error } = this.state;
    const { showErrorToast } = useContext(ToastContext);
    const { silentLogin } = useLogin();
    const { goToLoginPage } = usePageRouter();

    if (!hasError) return this.props.children;

    if (error instanceof APIError) {
      // 기간이 만료된 JWT
      if (error.errorCode === 'JW005') {
        silentLogin();

        return this.props.children;
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

    return this.props.children;
  }
}

export default LoginErrorBoundary;
