import React from 'react';
import App from './App';
import { createBrowserRouter } from 'react-router-dom';
import MainPage from './pages/MainPage';
import RunnerPostPage from './pages/RunnerPostDetailPage';
import RunnerPostCreatePage from './pages/RunnerPostCreatePage';
import LoginPage from './pages/LoginPage';
import CreationResultPage from './pages/CreationResultPage';
import MyPage from './pages/MyPage';
import GithubCallbackPage from './pages/GithubCallbackPage';
import SupporterSelectPage from './pages/SupporterSelectPage';

export const ROUTER_PATH = {
  MAIN: '/',
  RUNNER_POST: '/runner-post/:runnerPostId',
  RUNNER_POST_CREATE: '/runner-post-create/',
  SUPPORTER_SELECT: '/supporter-select/:runnerPostId',
  MY_PAGE: '/my-page',
  LOGIN: '/login',
  NOT_FOUND: '/*',
  RESULT: '/result',
  GITHUB_CALLBACK: '/oauth/github/callback',
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
        {
          path: ROUTER_PATH.RESULT,
          element: <CreationResultPage />,
        },
        {
          path: ROUTER_PATH.MY_PAGE,
          element: <MyPage />,
        },
        { path: ROUTER_PATH.GITHUB_CALLBACK, element: <GithubCallbackPage /> },
        {
          path: ROUTER_PATH.SUPPORTER_SELECT,
          element: <SupporterSelectPage />,
        },
      ],
    },
  ],
  {
    basename: '/',
  },
);
