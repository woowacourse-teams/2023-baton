import {
  JavaIconWhite,
  JavascriptIconWhite,
  ReactIconWhite,
  SpringIconWhite,
  TypescriptIconWhite,
} from '@/assets/technicalLabelIcon';
import { TECH_COLOR_MAP } from '@/constants/tags';
import { Technic } from '@/types/tags';
import React from 'react';
import { styled } from 'styled-components';

interface Props {
  tag: Technic;
  handleClickTag: (tag: Technic) => void;
  $isSelected?: boolean;
  isDeleteButton?: boolean;
}

const TECH_ICON_MAP: Record<Technic, JSX.Element> = {
  javascript: <JavascriptIconWhite />,
  typescript: <TypescriptIconWhite />,
  react: <ReactIconWhite />,
  java: <JavaIconWhite />,
  spring: <SpringIconWhite />,
};

const TECH_GARY_ICON_MAP: Record<Technic, JSX.Element> = {
  javascript: <JavascriptIconWhite color="var(--gray-500)" />,
  typescript: <TypescriptIconWhite color="var(--gray-500)" />,
  react: <ReactIconWhite color="var(--gray-500)" />,
  java: <JavaIconWhite color="var(--gray-500)" />,
  spring: <SpringIconWhite color="var(--gray-500)" />,
};

const TechLabelButton = ({ tag, $isSelected = true, handleClickTag, isDeleteButton }: Props) => {
  const handleClick = () => {
    handleClickTag(tag);
  };

  return (
    <S.TagSelectButton type="button" $isSelected={$isSelected} $color={TECH_COLOR_MAP[tag]} onClick={handleClick}>
      {$isSelected ? TECH_ICON_MAP[tag] : TECH_GARY_ICON_MAP[tag]}
      {isDeleteButton ? `${tag} X` : tag}
    </S.TagSelectButton>
  );
};

export default TechLabelButton;

const S = {
  TagSelectButton: styled.button<{ $isSelected: boolean; $color: string }>`
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 6px;

    padding: 0 15px;
    height: 32px;
    border-radius: 14px;
    border: 1px solid ${({ $isSelected, $color }) => ($isSelected ? $color : 'white')};

    background-color: ${({ $isSelected }) => ($isSelected ? 'white' : 'var(--gray-200)')};

    color: ${({ $isSelected, $color }) => ($isSelected ? $color : 'var(--gray-500)')};
    font-size: 15px;
  `,
};
