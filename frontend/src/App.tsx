import React from 'react';
import { styled } from 'styled-components';
import { Outlet } from 'react-router-dom';
import { useToken } from './hooks/useToken';
import ToastProvider from './contexts/ToastContext';
import ChannelService from './ChannelService';
import { CHANNEL_SERVICE_KEY } from './constants';

const App = () => {
  ChannelService.loadScript();

  if (CHANNEL_SERVICE_KEY) {
    ChannelService.boot({
      pluginKey: CHANNEL_SERVICE_KEY,
    });
  }

  const { validateToken } = useToken();

  validateToken();

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
