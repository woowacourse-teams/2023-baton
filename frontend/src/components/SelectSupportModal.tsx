import React, { useState } from 'react';
import Modal from './common/Modal';
import { styled } from 'styled-components';
import SupporterSelectList from './SupporterSelect/SupporterSelectList/SupporterSelectList';
import { SupporterCard } from '@/types/supporterCard';

interface Props {
  closeModal: () => void;
  handleSelectButton: (selectedSupporter: SupporterCard) => void;
}

const SelectSupportModal = ({ closeModal, handleSelectButton }: Props) => {
  const [selectedTechField, setSelectedTechField] = useState<string | null>('프론트엔드');

  const clickedFilter = (event: React.MouseEvent<HTMLParagraphElement>) => {
    setSelectedTechField(event.currentTarget.textContent || null);
  };

  return (
    <Modal width="850px" height="95%" closeModal={closeModal}>
      <S.ModalContainer>
        <S.TitleContainer>
          <S.Title>서포터를 선택해 주세요 ✅</S.Title>
          <S.TitleDescription>선택한 서포터가 확인 후 리뷰를 진행합니다.</S.TitleDescription>
        </S.TitleContainer>
        <S.FilterContainer>
          <S.FilterTitle onClick={clickedFilter} selected={selectedTechField === '프론트엔드'}>
            프론트엔드
          </S.FilterTitle>
          <S.FilterTitle onClick={clickedFilter} selected={selectedTechField === '백엔드'}>
            백엔드
          </S.FilterTitle>
        </S.FilterContainer>
        <S.SelectSupportContainer>
          <SupporterSelectList selectedTechField={selectedTechField} handleSelectButton={handleSelectButton} />
        </S.SelectSupportContainer>
      </S.ModalContainer>
    </Modal>
  );
};

export default SelectSupportModal;

const S = {
  ModalContainer: styled.div`
    display: flex;
    flex-direction: column;
    justify-content: center;
    height: 100%;
  `,

  ButtonContainer: styled.div`
    display: flex;
    gap: 20px;
  `,

  TitleContainer: styled.header`
    display: flex;
    flex-direction: column;
    gap: 15px;

    margin: 20px 0 53px 20px;
  `,

  Title: styled.h1`
    font-size: 36px;
    font-weight: 700;
  `,

  TitleDescription: styled.h2`
    margin-left: 5px;

    font-size: 18px;
    font-weight: 500;
    color: var(--gray-500);
  `,

  FilterContainer: styled.div`
    display: flex;
    gap: 30px;

    margin: 0 0 30px 25px;

    font-size: 18px;
    font-weight: 700;
    color: var(--gray-500);
  `,
  FilterTitle: styled.p<{ selected: boolean }>`
    height: 25px;

    cursor: pointer;

    color: ${({ selected }) => (selected ? 'red' : 'var(--gray-500)')};
    border-bottom: ${({ selected }) => (selected ? '2px solid var(--baton-red)' : '')};

    &:hover {
      color: var(--baton-red);
      border-bottom: 2px solid var(--baton-red);
    }
  `,

  SelectSupportContainer: styled.div`
    flex-grow: 1;
    width: 696px;
    max-height: 75%;
    margin: 0 auto;

    overflow-y: scroll;

    &::-webkit-scrollbar {
      display: none;
    }
  `,
};
