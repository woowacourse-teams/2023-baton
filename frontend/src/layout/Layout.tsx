import React from 'react';
import Header from './Header';
import { styled } from 'styled-components';

interface Props {
  children: React.ReactNode;
}

const Layout = ({ children }: Props) => {
  return (
    <S.LayoutContainer>
      <Header />
      <S.ChildrenWrapper>{children}</S.ChildrenWrapper>
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

  ChildrenWrapper: styled.article`
    max-width: 1200px;
    width: 100%;

    padding: 0 25px;

    @media (max-width: 768px) {
      padding: 25px;
    }
  `,
};
