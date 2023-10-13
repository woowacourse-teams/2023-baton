import React, { useEffect } from 'react';
import { createPortal } from 'react-dom';
import styled from 'styled-components';

interface Props extends React.HTMLProps<HTMLDivElement> {
  children: React.ReactNode;
  closeModal: () => void;
}

const Modal = ({ children, closeModal, ...rest }: Props) => {
  useEffect(() => {
    document.body.style.cssText = `
    position: fixed; 
    top: -${window.scrollY}px;
    overflow-y: scroll;
    width: 100%;`;

    return () => {
      const scrollY = document.body.style.top;
      document.body.style.cssText = '';
      window.scrollTo(0, parseInt(scrollY || '0', 10) * -1);
    };
  });

  return createPortal(
    <S.ModalContainer aria-modal="true">
      <S.BackDrop onClick={closeModal} />
      <S.ModalViewContainer {...rest}>{children}</S.ModalViewContainer>
    </S.ModalContainer>,
    document.getElementById('modal-root') as HTMLDivElement,
  );
};

export default Modal;

const S = {
  ModalContainer: styled.div``,

  BackDrop: styled.div`
    position: fixed;
    top: 0;
    right: 0;
    bottom: 0;
    left: 0;

    background: rgba(0, 0, 0, 0.25);
  `,

  ModalViewContainer: styled.div`
    position: fixed;
    top: 50%;
    left: 50%;

    border-radius: 8px;
    background: white;
    box-shadow: 1px 2px 4px rgba(0, 0, 0, 0.2);

    z-index: 999;
    transform: translate(-50%, -50%);
  `,
};
