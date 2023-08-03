import React from 'react';
import { styled } from 'styled-components';
import { Outlet } from 'react-router-dom';
import { useLogin } from './hooks/useLogin';

const App = () => {
  const { validateToken } = useLogin();

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
