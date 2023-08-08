import React from 'react';
import Modal from '../common/Modal/Modal';
import Button from '../common/Button/Button';
import { styled } from 'styled-components';

interface Props {
  contents: string;
  closeModal: () => void;
  handleClickConfirmButton: () => void;
}

const ConfirmModal = ({ contents, closeModal, handleClickConfirmButton }: Props) => {
  return (
    <Modal width="495px" height="211px" closeModal={closeModal}>
      <S.ConfirmModalContainer>
        <S.ConfirmMessage>{contents}</S.ConfirmMessage>
        <S.ButtonContainer>
          <Button colorTheme="GRAY" width="134px" height="35px" fontSize="16px" fontWeight={700} onClick={closeModal}>
            취소
          </Button>
          <Button
            colorTheme="WHITE"
            width="134px"
            height="35px"
            fontSize="16px"
            fontWeight={700}
            onClick={handleClickConfirmButton}
          >
            삭제
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
  `,

  ButtonContainer: styled.div`
    display: flex;
    gap: 20px;
  `,
};
