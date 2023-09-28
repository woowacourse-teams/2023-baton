import React, { useEffect } from 'react';
import Button from '../common/Button/Button';
import { styled } from 'styled-components';
import useViewport from '@/hooks/useViewport';

interface Props {
  contents: React.ReactNode;
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
    <S.ConfirmModalContainer>
      <S.ConfirmMessage>{contents}</S.ConfirmMessage>
      <S.ButtonContainer>
        <Button
          colorTheme="GRAY"
          width={isMobile ? '120px' : '134px'}
          height="35px"
          fontSize={isMobile ? '14px' : '16px'}
          fontWeight={700}
          onClick={closeModal}
        >
          {cancelText || '취소'}
        </Button>
        <Button
          colorTheme="WHITE"
          width={isMobile ? '120px' : '134px'}
          height="35px"
          fontSize={isMobile ? '14px' : '16px'}
          fontWeight={700}
          onClick={handleClickConfirmButton}
        >
          {confirmText || '확인'}
        </Button>
      </S.ButtonContainer>
    </S.ConfirmModalContainer>
  );
};

export default ConfirmModal;

const S = {
  ConfirmModalContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;

    width: 540px;
    height: 250px;
    padding: 20px;

    @media (max-width: 768px) {
      width: 90vw;
    }
  `,

  ConfirmMessage: styled.p`
    margin-bottom: 40px;

    white-space: pre-wrap;
    line-height: 1.5;
  `,

  ButtonContainer: styled.div`
    display: flex;
    gap: 20px;
  `,
};
