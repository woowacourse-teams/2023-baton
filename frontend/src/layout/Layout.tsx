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

  ChildrenWrapper: styled.div`
    width: 1200px;

    margin-bottom: 80px;
  `,
};
