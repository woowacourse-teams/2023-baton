import React from 'react';
import styled from 'styled-components';
import closeIcon from '@/assets/close-icon-red.svg';

interface Props extends React.HTMLProps<HTMLButtonElement> {
  children: React.ReactNode;
}

const Tag = ({ children, onClick }: Props) => {
  return (
    <S.TagWrapper>
      <S.Tag onClick={onClick} type="button">
        {`#${children}`}
        <S.CloseTagIcon src={closeIcon} />
      </S.Tag>
    </S.TagWrapper>
  );
};

export default Tag;

const S = {
  TagWrapper: styled.div`
    :hover {
      transition: all 0.35s ease;
      transform: scale(1.04);
    }
  `,

  Tag: styled.button`
    display: flex;
    align-items: center;

    height: 34px;
    padding: 0 15px;
    gap: 5px;

    background-color: white;
    border-radius: 16px;
    border: 1px solid var(--baton-red);

    color: var(--baton-red);
  `,

  CloseTagIcon: styled.img`
    width: 9px;
    height: 9px;
  `,
};
