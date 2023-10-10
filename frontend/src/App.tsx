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
import ReactGA from 'react-ga';
import { createBrowserHistory } from 'history';

const gaTrackingId = process.env.REACT_APP_GA_TRACKING_ID;
if (gaTrackingId) {
  ReactGA.initialize(gaTrackingId);
}

const history = createBrowserHistory();
history.listen((response: { location: { pathname: string } }) => {
  console.log(response.location.pathname);
  ReactGA.set({ page: response.location.pathname });
  ReactGA.pageview(response.location.pathname);
});

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
