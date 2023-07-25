import React from 'react';
import Modal from './common/Modal';
import Button from './common/Button';
import { styled } from 'styled-components';

interface Props {
  name: string;
}

const ConfirmModal = ({ name }: Props) => {
  return (
    <Modal width="495px" height="211px" closeModal={() => {}}>
      <S.ConfirmModalContainer>
        <S.ConfirmMessage>{name}님에게 리뷰를 요청하시겠습니까?</S.ConfirmMessage>
        <S.ButtonContainer>
          <Button colorTheme="GRAY" width="134px" height="35px" fontSize="16px" fontWeight={700}>
            취소
          </Button>
          <Button colorTheme="WHITE" width="134px" height="35px" fontSize="16px" fontWeight={700}>
            리뷰 요청하기
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
