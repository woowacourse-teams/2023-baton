import SupporterCardList from '@/components/SupporterCard/SupporterCartList/SupporterCardList';
import Layout from '@/layout/Layout';
import React from 'react';
import { styled } from 'styled-components';

const SupporterSelectPage = () => {
  return (
    <Layout>
      <S.TitleWrapper>
        <S.Title>서포터를 선택해 주세요 ✅</S.Title>
        <S.SubTitle>선택한 서포터가 확인 후 리뷰를 진행합니다.</S.SubTitle>
      </S.TitleWrapper>
      <S.SupporterListContainer>
        <SupporterCardList />
      </S.SupporterListContainer>
    </Layout>
  );
};

const S = {
  TitleWrapper: styled.header`
    display: flex;
    flex-direction: column;
    gap: 20px;

    margin: 72px 0 53px 0;
  `,

  Title: styled.h1`
    font-size: 36px;
    font-weight: 700;
  `,

  SubTitle: styled.h2`
    font-size: 18px;
    color: var(--gray-500);
  `,

  SupporterListContainer: styled.div``,
};

export default SupporterSelectPage;
