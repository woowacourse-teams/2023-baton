import React, { useState } from 'react';
import styled from 'styled-components';
import { TECHNICS } from '@/constants/tags';
import { Technic } from '@/types/tags';
import Button from '../common/Button/Button';
import TechLabelButton from '../TechLabelButton/TechLabelButton';
import useViewport from '@/hooks/useViewport';

interface Props {
  defaultTags?: Technic[];
  closeModal: () => void;
  confirmTagSelect: (tags: Technic[]) => void;
}

const TechTagSelectModal = ({ defaultTags, closeModal, confirmTagSelect }: Props) => {
  const [SelectedTags, setSelectedTags] = useState<Technic[]>([...(defaultTags ?? [])]);
  const { isMobile } = useViewport();

  const pushTag = (tag: Technic) => {
    if (!SelectedTags) return;

    const newTags = [...SelectedTags, tag].sort();

    setSelectedTags(newTags);
  };

  const popModalTag = (tag: Technic) => {
    if (!SelectedTags) return;

    const newTags = SelectedTags.filter((item) => tag !== item);
    setSelectedTags(newTags);
  };

  const handleClickModalTag = (tag: Technic) => {
    if (!SelectedTags) return;
    if (SelectedTags?.includes(tag)) {
      popModalTag(tag);

      return;
    }

    pushTag(tag);
  };

  const handleClickConfirmButton = () => {
    confirmTagSelect(SelectedTags ?? []);
  };

  return (
    <S.TechTagSelectModalContainer>
      <S.ModalTitle>기술스택</S.ModalTitle>
      <S.TagContainer>
        {TECHNICS?.map((tag) => (
          <S.TagButtonWrapper key={tag}>
            <TechLabelButton
              tag={tag}
              isSelected={SelectedTags?.includes(tag) ?? false}
              handleClickTag={handleClickModalTag}
            />
          </S.TagButtonWrapper>
        ))}
      </S.TagContainer>
      <S.ButtonContainer>
        <Button
          type="button"
          colorTheme="GRAY"
          onClick={closeModal}
          fontWeight={700}
          width={isMobile ? '120px' : '160px'}
        >
          취소
        </Button>
        <Button
          type="button"
          colorTheme="WHITE"
          onClick={handleClickConfirmButton}
          fontWeight={700}
          width={isMobile ? '120px' : '160px'}
        >
          선택
        </Button>
      </S.ButtonContainer>
    </S.TechTagSelectModalContainer>
  );
};

export default TechTagSelectModal;
const S = {
  TechTagSelectModalContainer: styled.div`
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    gap: 20px;

    width: 420px;
    height: 280px;
    padding: 20px;

    @media (max-width: 768px) {
      width: 90vw;
      padding: 20px 10px;
    }
  `,

  ModalTitle: styled.div`
    color: var(--gray-800);
    font-size: 17px;
    font-weight: 700;
  `,

  ButtonContainer: styled.div`
    display: flex;
    justify-content: center;
    gap: 15px;

    margin-top: 15px;
  `,

  TagContainer: styled.ul`
    position: relative;
    display: flex;
    flex-wrap: wrap;
    gap: 9px 7px;

    width: 325px;
    margin-left: 30px;

    &::before {
      position: absolute;
      content: '';

      left: -25px;
      height: 100%;
      width: 5px;
      border-radius: 2px;

      background-color: var(--gray-400);
    }
  `,

  TagButtonWrapper: styled.li``,
};
