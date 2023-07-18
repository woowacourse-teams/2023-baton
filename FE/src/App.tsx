import React from 'react';
import { styled } from 'styled-components';
import Layout from './layout/Layout';
import Avatar from '@components/common/Avatar';
import Button from '@components/common/Button';
import Tag from '@components/common/Tag';
import Modal from '@components/common/Modal';
import { Outlet } from 'react-router-dom';

const App = () => {
  return (
    <S.AppContainer>
      <Outlet />
    </S.AppContainer>
  );
};

export default App;

const S = {
  AppContainer: styled.div`
    width: 100%;
  `,
};
