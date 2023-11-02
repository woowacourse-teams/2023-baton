import React from 'react';
import styled from 'styled-components';

interface Props {
  title: string;
  url: string;
  isPointed: boolean;
  selectItem: (url: string) => void;
  pointItem: (url: string) => void;
}

const AutoCompleteItem = ({ title, url, isPointed, selectItem }: Props) => {
  const handleClickItem = (e: React.MouseEvent<HTMLLIElement>) => {
    e.preventDefault();

    selectItem(url);
  };

  return (
    <S.ListItem $isPointed={isPointed} onMouseDown={handleClickItem}>
      <S.ListText>{title}</S.ListText>
    </S.ListItem>
  );
};

export default AutoCompleteItem;

const S = {
  ListItem: styled.li<{ $isPointed: boolean }>`
    display: flex;
    align-items: center;

    height: 45px;
    width: 100%;
    padding: 0 30px;

    background-color: ${({ $isPointed }) => $isPointed && 'var(--gray-100)'};

    cursor: pointer;

    &:hover {
      background-color: var(--gray-100);
    }

    &:focus {
      background-color: var(--gray-100);
      outline: none;
    }
  `,

  ListText: styled.div`
    width: 100%;

    font-size: 14px;

    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  `,
};
