import React from 'react';
import { styled } from 'styled-components';
import { Outlet } from 'react-router-dom';
import { useToken } from './hooks/useToken';

const App = () => {
  const { validateToken } = useToken();

  validateToken();

  return (
    <S.AppContainer>
      <Outlet />
    </S.AppContainer>
  );
};

export default App;

const S = {
  AppContainer: styled.div`
    width: 100%;
  `,
};
