import React from 'react';
import Modal from './common/Modal';
import { styled } from 'styled-components';
import SupporterSelectList from './SupporterSelect/SupporterSelectList/SupporterSelectList';
import { SupporterCard } from '@/types/SupporterSelect';

interface Props {
  closeModal: () => void;
  handleSelectButton: (selectedSupporter: SupporterCard) => void;
}

const SelectSupportModal = ({ closeModal, handleSelectButton }: Props) => {
  return (
    <Modal width="1200px" height="95%" closeModal={closeModal}>
      <S.SelectSupportModalWrapper>
        <S.SelectSupportModalContainer>
          <S.TitleContainer>
            <S.Title>서포터를 선택해 주세요 ✅</S.Title>
            <S.TitleDescription>선택한 서포터가 확인 후 리뷰를 진행합니다.</S.TitleDescription>
          </S.TitleContainer>
          <SupporterSelectList handleSelectButton={handleSelectButton} />
        </S.SelectSupportModalContainer>
      </S.SelectSupportModalWrapper>
    </Modal>
  );
};

export default SelectSupportModal;

const S = {
  SelectSupportModalWrapper: styled.div`
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100%;
  `,

  SelectSupportModalContainer: styled.div`
    display: flex;
    flex-direction: column;

    width: 1200px;
    max-height: 800px;
    padding: 0 40px;

    overflow-y: scroll;
  `,

  ButtonContainer: styled.div`
    display: flex;
    gap: 20px;
  `,

  TitleContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 30px;

    margin: 20px 0 53px 0;
  `,

  Title: styled.p`
    font-size: 36px;
    font-weight: 700;
  `,

  TitleDescription: styled.div`
    font-size: 18px;
    font-weight: 500;
  `,
};
