import React from 'react';
import { APIError } from '@/types/error';
import LoginError from '../LoginError';

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

    if (!hasError) return this.props.children;

    return <LoginError error={error!}>{this.props.children}</LoginError>;
  }
}

export default LoginErrorBoundary;
