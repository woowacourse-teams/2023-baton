import React from 'react';
import App from './App';
import { createBrowserRouter } from 'react-router-dom';
import MainPage from '@pages/MainPage';
import RunnerPostPage from '@pages/RunnerPostPage';
import RunnerPostCreatePage from '@pages/RunnerPostCreatePage';
import LoginPage from '@pages/LoginPage';

export const ROUTER_PATH = {
  MAIN: '/',
  RUNNER_POST: '/runner-post/:runnerPostId',
  RUNNER_POST_CREATE: '/runner-post-create/',
  LOGIN: '/login',
  NOT_FOUND: '/*',
};

export const router = createBrowserRouter(
  [
    {
      element: <App />,
      children: [
        {
          index: true,
          element: <MainPage />,
        },
        {
          path: ROUTER_PATH.RUNNER_POST,
          element: <RunnerPostPage />,
        },
        {
          path: ROUTER_PATH.RUNNER_POST_CREATE,
          element: <RunnerPostCreatePage />,
        },
        {
          path: ROUTER_PATH.LOGIN,
          element: <LoginPage />,
        },
      ],
    },
  ],
  {
    basename: '/',
  },
);
