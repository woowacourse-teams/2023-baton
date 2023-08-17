import Toast from '@/components/common/Toast/Toast';
import React, { createContext, useRef, useState } from 'react';

interface ToastMessage {
  title: string;
  description: string;
  ms?: number;
}

interface Props {
  children: React.ReactNode;
}

const DEFAULT_TIMEOUT = 3000;

export const ToastContext = createContext({
  showCompletionToast: (message: ToastMessage) => {},
  showErrorToast: (message: ToastMessage) => {},
});

const ToastProvider = ({ children }: Props) => {
  const [completionToast, setCompletionToast] = useState<ToastMessage | null>(null);
  const [errorToast, setErrorToast] = useState<ToastMessage | null>(null);
  const timer = useRef<number | null>(null);

  const showCompletionToast = (message: ToastMessage) => {
    if (timer.current) {
      window.clearTimeout(timer.current);

      setCompletionToast(null);
      setErrorToast(null);
    }

    setCompletionToast(message);

    timer.current = window.setTimeout(() => {
      setCompletionToast(null);
    }, message.ms || DEFAULT_TIMEOUT);
  };

  const showErrorToast = (message: ToastMessage) => {
    if (timer.current) {
      window.clearTimeout(timer.current);

      setCompletionToast(null);
      setErrorToast(null);
    }

    setErrorToast(message);

    timer.current = window.setTimeout(() => {
      setErrorToast(null);
    }, message.ms || DEFAULT_TIMEOUT);
  };

  return (
    <ToastContext.Provider value={{ showCompletionToast, showErrorToast }}>
      {children}
      {completionToast && (
        <Toast
          colorTheme="COMPLETION"
          title={completionToast.title}
          description={completionToast.description}
          ms={completionToast.ms || DEFAULT_TIMEOUT}
        />
      )}
      {errorToast && (
        <Toast
          colorTheme="ERROR"
          title={errorToast.title}
          description={errorToast.description}
          ms={errorToast.ms || DEFAULT_TIMEOUT}
        />
      )}
    </ToastContext.Provider>
  );
};

export default ToastProvider;
