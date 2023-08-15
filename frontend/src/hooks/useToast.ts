import { useRef, useState } from 'react';

export interface ToastMessage {
  title: string;
  description: string;
}

export const useToast = (
  ms: number = 3000,
): [toastMessage: ToastMessage | null, setToastMessage: (toastMessage: ToastMessage | null) => void] => {
  const [message, setMessage] = useState<ToastMessage | null>(null);
  const timer = useRef<number | null>(null);

  const setToastMessage = (toastMessage: ToastMessage | null) => {
    if (timer.current) {
      window.clearTimeout(timer.current);
      setMessage(null);
    }

    if (!toastMessage) return;

    setMessage(toastMessage);

    timer.current = window.setTimeout(() => {
      setMessage(null);
    }, ms);
  };

  return [message, setToastMessage];
};
