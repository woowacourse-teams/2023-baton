import React from 'react';
import { styled } from 'styled-components';
import { Outlet } from 'react-router-dom';
import ToastProvider from './contexts/ToastContext';
import ChannelService from './ChannelService';
import { CHANNEL_SERVICE_KEY } from './constants';
import { useLogin } from './hooks/useLogin';
import LoginErrorBoundary from './components/ErrorBoundary/LoginErrorBoundary';

const App = () => {
  const { checkLoginToken } = useLogin();

  ChannelService.loadScript();
  checkLoginToken();

  if (CHANNEL_SERVICE_KEY) {
    ChannelService.boot({
      pluginKey: CHANNEL_SERVICE_KEY,
    });
  }

  return (
    <ToastProvider>
      <LoginErrorBoundary>
        <S.AppContainer>
          <Outlet />
        </S.AppContainer>
      </LoginErrorBoundary>
    </ToastProvider>
  );
};

export default App;

const S = {
  AppContainer: styled.div`
    width: 100%;
  `,
};
