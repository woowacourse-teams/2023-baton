import React from 'react';
import App from './App';
import { createBrowserRouter } from 'react-router-dom';
import MainPage from './pages/MainPage';
import NotFoundPage from './pages/NotFoundPage';
const RunnerPostPage = React.lazy(() => import('./pages/RunnerPostDetailPage'));
const RunnerPostCreatePage = React.lazy(() => import('./pages/RunnerPostCreatePage'));
const LoginPage = React.lazy(() => import('./pages/LoginPage'));
const RunnerMyPage = React.lazy(() => import('./pages/RunnerMyPage'));
const SupporterMyPage = React.lazy(() => import('./pages/SupporterMyPage'));
const GithubCallbackPage = React.lazy(() => import('./pages/GithubCallbackPage'));
const ProfileEditPage = React.lazy(() => import('./pages/ProfileEditPage'));
const SupporterSelectPage = React.lazy(() => import('./pages/SupporterSelectPage'));
const SupporterFeedbackPage = React.lazy(() => import('./pages/SupporterFeedbackPage'));
const SupporterProfilePage = React.lazy(() => import('./pages/SupporterProfilePage'));
const RunnerProfilePage = React.lazy(() => import('./pages/RunnerProfilePage'));
const NoticePage = React.lazy(() => import('./pages/NoticePage'));
const ResultPage = React.lazy(() => import('./pages/ResultPage'));

export const ROUTER_PATH = {
  MAIN: '/',
  RUNNER_POST: '/runner-post/:runnerPostId',
  RUNNER_POST_CREATE: '/runner-post-create/',
  SUPPORTER_SELECT: '/supporter-select/:runnerPostId',
  RUNNER_MYPAGE: '/my-page-runner',
  SUPPORTER_MYPAGE: '/my-page-supporter',
  LOGIN: '/login',
  NOT_FOUND: '/*',
  RUNNER_PROFILE: '/runner-profile/:runnerId',
  SUPPORTER_PROFILE: '/supporter-profile/:supporterId',
  RUNNER_PROFILE_EDIT: '/runner-profile-edit',
  SUPPORTER_PROFILE_EDIT: '/supporter-profile-edit',
  SUPPORTER_FEEDBACK: '/supporter-feedback/:runnerPostId/:supporterId',
  GITHUB_CALLBACK: '/oauth/github/callback',
  NOTICE: '/notice',
  RESULT: '/result',
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
          path: ROUTER_PATH.RUNNER_MYPAGE,
          element: <RunnerMyPage />,
        },
        {
          path: ROUTER_PATH.SUPPORTER_MYPAGE,
          element: <SupporterMyPage />,
        },
        {
          path: ROUTER_PATH.RUNNER_PROFILE_EDIT,
          element: <ProfileEditPage isRunner={true} />,
        },
        {
          path: ROUTER_PATH.SUPPORTER_PROFILE_EDIT,
          element: <ProfileEditPage isRunner={false} />,
        },
        { path: ROUTER_PATH.GITHUB_CALLBACK, element: <GithubCallbackPage /> },
        {
          path: ROUTER_PATH.SUPPORTER_SELECT,
          element: <SupporterSelectPage />,
        },
        {
          path: ROUTER_PATH.SUPPORTER_FEEDBACK,
          element: <SupporterFeedbackPage />,
        },
        {
          path: ROUTER_PATH.RUNNER_PROFILE,
          element: <RunnerProfilePage />,
        },
        {
          path: ROUTER_PATH.SUPPORTER_PROFILE,
          element: <SupporterProfilePage />,
        },
        {
          path: ROUTER_PATH.NOTICE,
          element: <NoticePage />,
        },
        { path: ROUTER_PATH.RESULT, element: <ResultPage /> },
        { path: ROUTER_PATH.NOT_FOUND, element: <NotFoundPage /> },
      ],
    },
  ],
  {
    basename: '/',
  },
);
