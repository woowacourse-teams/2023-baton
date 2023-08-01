import React from 'react';
import Modal from './common/Modal';
import { styled } from 'styled-components';
import SupporterSelectList from './SupporterSelect/SupporterSelectList/SupporterSelectList';
import { SupporterCard } from '@/types/supporterCard';

interface Props {
  closeModal: () => void;
  handleSelectButton: (selectedSupporter: SupporterCard) => void;
}

const SelectSupportModal = ({ closeModal, handleSelectButton }: Props) => {
  return (
    <Modal width="800px" height="95%" closeModal={closeModal}>
      <S.TitleContainer>
        <S.Title>서포터를 선택해 주세요 ✅</S.Title>
        <S.TitleDescription>선택한 서포터가 확인 후 리뷰를 진행합니다.</S.TitleDescription>
      </S.TitleContainer>
      <S.SelectSupportContainer>
        <SupporterSelectList handleSelectButton={handleSelectButton} />
      </S.SelectSupportContainer>
    </Modal>
  );
};

export default SelectSupportModal;

const S = {
  ModalContainer: styled.div``,
  SelectSupportContainer: styled.div`
    width: 696px;
    max-height: 75%;
    margin: 0 auto;

    overflow-y: scroll;
  `,

  ButtonContainer: styled.div`
    display: flex;
    gap: 20px;
  `,

  TitleContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 15px;

    margin: 20px 0 53px 20px;
  `,

  Title: styled.p`
    font-size: 36px;
    font-weight: 700;
  `,

  TitleDescription: styled.div`
    margin-left: 5px;

    font-size: 18px;
    font-weight: 500;
    color: var(--gray-500);
  `,
};
