import React from 'react';
import { styled } from 'styled-components';
import Layout from './layout/Layout';

const App = () => {
  return (
    <S.AppContainer>
      <Layout>
        <div></div>
      </Layout>
    </S.AppContainer>
  );
};

export default App;

const S = {
  AppContainer: styled.div`
    width: 100%;
  `,
};
