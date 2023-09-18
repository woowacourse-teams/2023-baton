import React, { useEffect } from 'react';
import Modal from '../common/Modal/Modal';
import Button from '../common/Button/Button';
import { styled } from 'styled-components';
import useViewport from '@/hooks/useViewport';

interface Props {
  contents: string;
  closeModal: () => void;
  handleClickConfirmButton: () => void;
  confirmText?: string;
  cancelText?: string;
}

const ConfirmModal = ({ contents, closeModal, handleClickConfirmButton, confirmText, cancelText }: Props) => {
  const { isMobile } = useViewport();

  useEffect(() => {
    const handleEscapeKeyDown = (e: KeyboardEvent) => {
      if (e.key === 'Escape') closeModal();
    };

    window.addEventListener('keydown', handleEscapeKeyDown);

    return () => {
      window.removeEventListener('keydown', handleEscapeKeyDown);
    };
  }, []);

  return (
    <Modal width={isMobile ? '120px' : '540px'} height="211px" closeModal={closeModal}>
      <S.ConfirmModalContainer>
        <S.ConfirmMessage>{contents}</S.ConfirmMessage>
        <S.ButtonContainer>
          <Button colorTheme="GRAY" width="134px" height="35px" fontSize="16px" fontWeight={700} onClick={closeModal}>
            {cancelText || '취소'}
          </Button>
          <Button
            colorTheme="WHITE"
            width="134px"
            height="35px"
            fontSize={isMobile ? '12px' : '14px'}
            fontWeight={700}
            onClick={handleClickConfirmButton}
          >
            {confirmText || '확인'}
          </Button>
        </S.ButtonContainer>
      </S.ConfirmModalContainer>
    </Modal>
  );
};

export default ConfirmModal;

const S = {
  ConfirmModalContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    width: 100%;
    height: 100%;
  `,

  ConfirmMessage: styled.p`
    margin-bottom: 40px;

    font-size: 18px;

    white-space: pre-wrap;
  `,

  ButtonContainer: styled.div`
    display: flex;
    gap: 20px;
  `,
};
