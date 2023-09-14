import React from 'react';
import Header from './Header';
import { styled } from 'styled-components';

interface Props {
  children: React.ReactNode;
  maxWidth?: string;
}

const Layout = ({ children, maxWidth }: Props) => {
  return (
    <S.LayoutContainer>
      <Header />
      <S.ChildrenWrapper $maxWidth={maxWidth}>{children}</S.ChildrenWrapper>
    </S.LayoutContainer>
  );
};

export default Layout;

const S = {
  LayoutContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
  `,

  ChildrenWrapper: styled.article<{ $maxWidth?: string }>`
    max-width: ${({ $maxWidth }) => $maxWidth || '1200px'};
    width: 100%;

    @media (max-width: 768px) {
      padding: 25px;
    }
  `,
};
