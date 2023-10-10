import React from 'react';
import { CustomApiError } from '@/types/error';
import NotFoundPage from '@/pages/NotFoundPage';

interface Props {
  children: React.ReactNode;
  onReset: () => void;
}

interface State {
  hasError: boolean;
  error: Error | CustomApiError | null;
}

class ErrorBoundary extends React.Component<Props, State> {
  constructor(props: Props) {
    super(props);
    this.state = {
      hasError: false,
      error: null,
    };

    this.resetErrorBoundary = this.resetErrorBoundary.bind(this);
  }

  resetErrorBoundary() {
    this.props.onReset();

    this.state = {
      hasError: false,
      error: null,
    };
  }

  static getDerivedStateFromError(error: Error | CustomApiError) {
    return { hasError: true, error: error };
  }

  render() {
    const { hasError, error } = this.state;

    return hasError ? <NotFoundPage reset={this.resetErrorBoundary} error={error} /> : this.props.children;
  }
}

export default ErrorBoundary;
