<<<<<<< HEAD
import React, { useState } from 'react';
=======
import React, { Suspense, useState } from 'react';
>>>>>>> dev/FE
import { styled } from 'styled-components';
import { Outlet } from 'react-router-dom';
import ToastProvider from './contexts/ToastContext';
import ChannelService from './ChannelService';
import { CHANNEL_SERVICE_KEY } from './constants';
import { useLogin } from './hooks/useLogin';
import LoadingPage from './pages/LoadingPage';

const App = () => {
  const { checkLoginToken } = useLogin();
  const [isLoading, setIsLoading] = useState<boolean>(true);

  checkLoginToken().finally(() => {
    setIsLoading(false);
  });

  ChannelService.loadScript();

  if (CHANNEL_SERVICE_KEY) {
    ChannelService.boot({
      pluginKey: CHANNEL_SERVICE_KEY,
    });
  }

  return isLoading ? (
    <LoadingPage />
  ) : (
    <ToastProvider>
      <S.AppContainer>
<<<<<<< HEAD
        <Outlet />
=======
        <Suspense fallback={<div></div>}>
          <Outlet />
        </Suspense>
>>>>>>> dev/FE
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
