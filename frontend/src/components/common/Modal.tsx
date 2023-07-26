import React from 'react';
import { createPortal } from 'react-dom';
import styled from 'styled-components';

interface Props extends React.HTMLProps<HTMLDivElement> {
  children: React.ReactNode;
  closeModal: () => void;
}

const Modal = ({ children, closeModal, width, height }: Props) => {
  return createPortal(
    <S.ModalContainer>
      <S.BackDrop onClick={closeModal} />
      <S.ModalViewContainer $width={width} $height={height}>
        {children}
      </S.ModalViewContainer>
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

  ModalViewContainer: styled.div<{ $width?: string | number; $height?: string | number }>`
    position: fixed;
    top: 50%;
    left: 50%;

    width: ${({ $width }) => $width || '300px'};
    height: ${({ $height }) => $height || '300px'};
    padding: 20px;

    border-radius: 8px;
    background: white;

    z-index: 999;
    transform: translate(-50%, -50%);
  `,
};
