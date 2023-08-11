import React, { useState } from 'react';
import styled from 'styled-components';
import Modal from '../common/Modal/Modal';
import { TECHNICS } from '@/constants/tags';
import { TechnicsType } from '@/types/tags';
import Button from '../common/Button/Button';
import TechLabelButton from '../TechLabelButton/TechLabelButton';

interface Props {
  defaultTags?: TechnicsType[];
  closeModal: () => void;
  confirmTagSelect: (tags: TechnicsType[]) => void;
}

const TechTagSelectModal = ({ defaultTags, closeModal, confirmTagSelect }: Props) => {
  const [modalSelectTags, setModalSelectedTags] = useState<TechnicsType[]>([...(defaultTags ?? [])]);

  const pushModalTag = (tag: TechnicsType) => {
    if (!modalSelectTags) return;

    const newTags = [...modalSelectTags, tag].sort((a, b) => {
      if (a > b) return 1;
      return -1;
    });

    setModalSelectedTags(newTags);
  };

  const popModalTag = (tag: TechnicsType) => {
    if (!modalSelectTags) return;

    const newTags = modalSelectTags.filter((item) => tag !== item);
    setModalSelectedTags(newTags);
  };

  const handleClickModalTag = (tag: TechnicsType) => {
    if (!modalSelectTags) return;
    if (modalSelectTags?.includes(tag)) {
      popModalTag(tag);

      return;
    }

    pushModalTag(tag);
  };

  const handleClickConfirmButton = () => {
    confirmTagSelect(modalSelectTags ?? []);
  };

  return (
    <Modal width="410px" closeModal={closeModal} height="100">
      <S.ModalContents>
        <S.ModalTitle>기술스택</S.ModalTitle>
        <S.TagContainer>
          {TECHNICS?.map((tag) => (
            <S.TagButtonWrapper key={tag}>
              <TechLabelButton
                tag={tag}
                $isSelected={modalSelectTags?.includes(tag) ?? false}
                handleClickTag={handleClickModalTag}
              />
            </S.TagButtonWrapper>
          ))}
        </S.TagContainer>
        <S.ButtonContainer>
          <Button type="button" colorTheme="GRAY" onClick={closeModal} fontWeight={700} width="160px">
            취소
          </Button>
          <Button type="button" colorTheme="WHITE" onClick={handleClickConfirmButton} fontWeight={700} width="160px">
            선택
          </Button>
        </S.ButtonContainer>
      </S.ModalContents>
    </Modal>
  );
};

export default TechTagSelectModal;
const S = {
  ModalContents: styled.div`
    display: flex;
    flex-direction: column;
    justify-content: space-between;
    gap: 20px;

    padding: 15px 0;
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
