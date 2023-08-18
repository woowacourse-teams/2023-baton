import React from 'react';
import ReactDOM from 'react-dom/client';
import { RouterProvider } from 'react-router-dom';
import { router } from './router';
import { GlobalStyle } from './styles/GlobalStyles';
import { worker } from './mocks/worker';

const root = ReactDOM.createRoot(document.getElementById('root') as HTMLElement);

if (process.env.NODE_ENV === 'development') {
  worker.start();
}

root.render(
  <React.StrictMode>
    <GlobalStyle />
    <RouterProvider router={router} />
  </React.StrictMode>,
);
