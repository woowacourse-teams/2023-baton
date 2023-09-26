import Button from '@/components/common/Button/Button';
import { usePageRouter } from '@/hooks/usePageRouter';
import Layout from '@/layout/Layout';
import React from 'react';
import styled from 'styled-components';

const ResultPage = () => {
  const { goToMyPage } = usePageRouter();

  return (
    <Layout>
      <S.ResultPageContainer>
        <S.Message>
          {'리뷰 요청글 생성이 완료되었습니다.\n'}
          <S.Bold>러너 마이페이지</S.Bold>에서 <S.Bold>서포터를 선택</S.Bold>하고 리뷰를 진행해 주세요.
        </S.Message>
        <Button colorTheme="WHITE" onClick={goToMyPage}>
          확인
        </Button>
      </S.ResultPageContainer>
    </Layout>
  );
};

export default ResultPage;

const S = {
  ResultPageContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 50px;

    margin-top: 20%;
  `,

  Message: styled.h1`
    font-size: 30px;
    line-height: 1.5;
    white-space: pre-wrap;
    text-align: center;

    @media (max-width: 768px) {
      font-size: 20px;
    }
  `,

  Bold: styled.span`
    font-weight: 700;
  `,
};
