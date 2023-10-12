import React, { Suspense, useEffect } from 'react';
import { styled } from 'styled-components';
import { Outlet } from 'react-router-dom';
import ToastProvider from './contexts/ToastContext';
import ChannelService from './ChannelService';
import { CHANNEL_SERVICE_KEY } from './constants';
import LoadingPage from './pages/LoadingPage';
import ModalProvider from './contexts/ModalContext';
import { QueryClientProvider, QueryErrorResetBoundary } from '@tanstack/react-query';
import { queryClient } from './hooks/query/queryClient';
import { createBrowserHistory } from 'history';
import ErrorBoundary from './components/ErrorBoundary/ErrorBoundary';
import ReactGA from 'react-ga4';

const App = () => {
  useEffect(() => {
    const gaTrackingId = process.env.REACT_APP_GA_TRACKING_ID;
    if (gaTrackingId) {
      ReactGA.initialize(gaTrackingId);
    }

    const history = createBrowserHistory();
    history.listen((response) => {
      ReactGA.set({ page: response.location.pathname });
      ReactGA.send(response.location.pathname);
    });
  }, []);

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
            <QueryErrorResetBoundary>
              {({ reset }) => (
                <ErrorBoundary onReset={reset}>
                  <Suspense fallback={<LoadingPage />}>
                    <Outlet />
                  </Suspense>
                </ErrorBoundary>
              )}
            </QueryErrorResetBoundary>
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
