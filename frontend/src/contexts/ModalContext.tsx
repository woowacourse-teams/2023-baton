import Modal from '@/components/common/Modal/Modal';
import React, { createContext, useCallback, useEffect, useState } from 'react';

interface Props {
  children: React.ReactNode;
}

export const ModalContext = createContext({
  open: (contents: React.ReactNode) => {},
  close: () => {},
});

const ModalProvider = ({ children }: Props) => {
  const [contents, setContents] = useState<React.ReactNode>(null);
  const [isOpen, setIsOpen] = useState<boolean>(false);

  const open = useCallback(
    (contents: React.ReactNode) => {
      setContents(contents);
      setIsOpen(true);
    },

    [contents],
  );

  const close = useCallback(() => {
    setIsOpen(false);
  }, [contents]);

  return (
    <ModalContext.Provider value={{ open, close }}>
      {children}
      {isOpen && <Modal closeModal={close}>{contents}</Modal>}
    </ModalContext.Provider>
  );
};

export default ModalProvider;
