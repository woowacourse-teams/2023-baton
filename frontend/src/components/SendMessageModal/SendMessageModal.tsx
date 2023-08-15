import React, { useState } from 'react';
import { styled } from 'styled-components';
import Modal from '../common/Modal/Modal';
import Button from '../common/Button/Button';
import TextArea from '../Textarea/Textarea';

interface Props {
  placeholder: string;
  closeModal: () => void;
  handleClickSendButton: () => void;
}

const SendMessageModal = ({ placeholder, closeModal, handleClickSendButton }: Props) => {
  const [message, setMessage] = useState<string>('');

  const handleChangeMessage = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setMessage(e.target.value);
  };

  return (
    <Modal width="900px" height="500px" closeModal={closeModal}>
      <S.SendMessageModalContainer>
        <TextArea
          width="100%"
          height="100%"
          fontSize="28px"
          lineHeight={1.2}
          maxLength={500}
          padding="0"
          placeholder={placeholder}
          handleInputTextState={handleChangeMessage}
          inputTextState={message}
        />
        <S.ButtonContainer>
          <Button colorTheme="GRAY" fontWeight={700} onClick={closeModal}>
            취소
          </Button>
          <Button colorTheme="WHITE" fontWeight={700} onClick={handleClickSendButton}>
            전송
          </Button>
        </S.ButtonContainer>
      </S.SendMessageModalContainer>
    </Modal>
  );
};

export default SendMessageModal;

const S = {
  SendMessageModalContainer: styled.div`
    display: flex;
    flex-direction: column;

    padding: 12px;

    width: 100%;
    height: 100%;
  `,

  ButtonContainer: styled.div`
    display: flex;
    justify-content: center;
    gap: 20px;

    margin-top: auto;
  `,
};
