import React from 'react';
import Header from './Header';
import { styled } from 'styled-components';

interface Props {
  children: React.ReactNode;
  maxWidth?: string;
}

const HomeLayout = ({ children, maxWidth }: Props) => {
  return (
    <S.LayoutContainer>
      <Header />
      <S.ChildrenWrapper $maxWidth={maxWidth}>{children}</S.ChildrenWrapper>
    </S.LayoutContainer>
  );
};

export default HomeLayout;

const S = {
  LayoutContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
  `,

  ChildrenWrapper: styled.article<{ $maxWidth?: string }>`
    display: grid;
    grid-template-columns: 2.5fr 1fr;
    gap: 50px;
    max-width: ${({ $maxWidth }) => $maxWidth || '1280px'};
    width: 100%;
    padding: 8px 16px;

    @media (max-width: 768px) {
      display: flex;
      padding: 15px;
    }
  `,
};
