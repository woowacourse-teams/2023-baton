import Modal from '@/components/common/Modal/Modal';
import React, { createContext, useCallback, useEffect, useState } from 'react';

interface Props {
  children: React.ReactNode;
}

export const ModalContext = createContext({
  openModal: (contents: React.ReactNode) => {},
  closeModal: () => {},
});

const ModalProvider = ({ children }: Props) => {
  const [contents, setContents] = useState<React.ReactNode>(null);
  const [isOpen, setIsOpen] = useState<boolean>(false);

  const openModal = useCallback(
    (contents: React.ReactNode) => {
      setContents(contents);
      setIsOpen(true);
    },

    [contents],
  );

  const closeModal = useCallback(() => {
    setIsOpen(false);
  }, [contents]);

  return (
    <ModalContext.Provider value={{ openModal, closeModal }}>
      {children}
      {isOpen && <Modal closeModal={closeModal}>{contents}</Modal>}
    </ModalContext.Provider>
  );
};

export default ModalProvider;
