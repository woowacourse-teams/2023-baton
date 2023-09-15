import React from 'react';
import { styled } from 'styled-components';
import { Outlet } from 'react-router-dom';
import ToastProvider from './contexts/ToastContext';
import ChannelService from './ChannelService';
import { CHANNEL_SERVICE_KEY } from './constants';
import { useLogin } from './hooks/useLogin';

const App = () => {
  const { checkLoginToken } = useLogin();

  checkLoginToken();
  ChannelService.loadScript();

  if (CHANNEL_SERVICE_KEY) {
    ChannelService.boot({
      pluginKey: CHANNEL_SERVICE_KEY,
    });
  }

  return (
    <ToastProvider>
      <S.AppContainer>
        <Outlet />
      </S.AppContainer>
    </ToastProvider>
  );
};

export default App;

const S = {
  AppContainer: styled.div`
    width: 100%;
  `,
};
