import React, { Suspense } from 'react';
import { styled } from 'styled-components';
import { Outlet } from 'react-router-dom';
import ToastProvider from './contexts/ToastContext';
import ChannelService from './ChannelService';
import { CHANNEL_SERVICE_KEY } from './constants';
import LoadingPage from './pages/LoadingPage';
import ModalProvider from './contexts/ModalContext';
import { QueryClientProvider } from '@tanstack/react-query';
import { queryClient } from './hooks/query/queryClient';

const App = () => {
  ChannelService.loadScript();

  if (CHANNEL_SERVICE_KEY) {
    ChannelService.boot({
      pluginKey: CHANNEL_SERVICE_KEY,
    });
  }

  return (
    <ToastProvider>
      <ModalProvider>
        <S.AppContainer>
          <QueryClientProvider client={queryClient}>
            <Suspense fallback={<LoadingPage />}>
              <Outlet />
            </Suspense>
          </QueryClientProvider>
        </S.AppContainer>
      </ModalProvider>
    </ToastProvider>
  );
};

export default App;

const S = {
  AppContainer: styled.div`
    width: 100%;
  `,
};
