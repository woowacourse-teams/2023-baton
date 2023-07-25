import Layout from '@/layout/Layout';
import React from 'react';
import { styled } from 'styled-components';
import Button from '@/components/common/Button';
import { usePageRouter } from '@/hooks/usePageRouter';

const CreationResultPage = () => {
  const { goToMainPage } = usePageRouter();

  return (
    <Layout>
      <S.ResultMessageContainer>
        <S.ResultMessage>서포터 선택이 완료되었습니다. </S.ResultMessage>
        <S.ResultMessage>서포터가 확인 후 리뷰를 진행할 예정입니다.</S.ResultMessage>
        <Button onClick={goToMainPage} colorTheme="WHITE">
          홈으로 가기
        </Button>
      </S.ResultMessageContainer>
    </Layout>
  );
};

export default CreationResultPage;

const S = {
  ResultMessageContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: center;
    gap: 30px;

    height: 100%;

    & button {
      margin-top: 30px;
    }
  `,

  ResultMessage: styled.p`
    font-size: 28px;
    font-weight: 700;
  `,
};
