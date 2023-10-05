import React from 'react';
import { styled } from 'styled-components';
import Button from '../common/Button/Button';
import TextArea from '../Textarea/Textarea';
import useViewport from '@/hooks/useViewport';

interface Props {
  messageState: string;
  handleChangeMessage: (e: React.ChangeEvent<HTMLTextAreaElement>) => void;
  placeholder: string;
  closeModal: () => void;
  handleClickSendButton: () => void;
}

const SendMessageModal = ({
  messageState,
  handleChangeMessage,
  placeholder,
  closeModal,
  handleClickSendButton,
}: Props) => {
  const { isMobile } = useViewport();

  return (
    <S.SendMessageModalContainer>
      <TextArea
        width="100%"
        height="100%"
        fontSize={isMobile ? '18px' : '28px'}
        lineHeight={1.2}
        maxLength={500}
        padding="0"
        placeholder={placeholder}
        handleInputTextState={handleChangeMessage}
        inputTextState={messageState}
      />
      <S.ButtonContainer>
        <Button
          width={isMobile ? '90px' : '180px'}
          colorTheme="GRAY"
          fontSize={isMobile ? '16px' : '18px'}
          fontWeight={700}
          onClick={closeModal}
        >
          취소
        </Button>
        <Button
          width={isMobile ? '90px' : '180px'}
          colorTheme="WHITE"
          fontSize={isMobile ? '16px' : '18px'}
          fontWeight={700}
          onClick={handleClickSendButton}
        >
          전송
        </Button>
      </S.ButtonContainer>
    </S.SendMessageModalContainer>
  );
};

export default SendMessageModal;

const S = {
  SendMessageModalContainer: styled.div`
    display: flex;
    flex-direction: column;

    width: 720px;
    height: 500px;
    padding: 25px 20px;

    @media (max-width: 768px) {
      width: 90vw;
      height: 400px;
    }
  `,

  ButtonContainer: styled.div`
    display: flex;
    justify-content: center;
    gap: 20px;

    margin-top: auto;
  `,
};
