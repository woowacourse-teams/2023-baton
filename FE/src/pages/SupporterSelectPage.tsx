import SupporterSelectList from '@/components/SupporterSelect/SupporterSelectList/SupporterSelectList';
import Layout from '@/layout/Layout';
import React from 'react';
import styled from 'styled-components';

const SupporterSelectPage = () => {
  return (
    <Layout>
      <S.TitleContainer>
        <S.Title>서포터를 선택해 주세요 ✅</S.Title>
        <S.TitleDescription>선택한 서포터가 확인 후 리뷰를 진행합니다.</S.TitleDescription>
      </S.TitleContainer>
      <SupporterSelectList />
    </Layout>
  );
};

export default SupporterSelectPage;

const S = {
  TitleContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 30px;

    margin: 72px 0 53px 0;
  `,

  Title: styled.p`
    font-size: 36px;
    font-weight: 700;
  `,

  TitleDescription: styled.div`
    font-size: 18px;
    font-weight: 500;
  `,

  ModalChildrenContainer: styled.div`
    display: flex;
    flex-direction: column;

    height: 100%;
    padding: 10px;
    gap: 20px;
  `,

  ModalTitle: styled.div`
    display: flex;
    justify-content: center;
  `,

  DisClaimMessage: styled.div`
    line-height: 1.5;
    color: var(--gray-500);

    margin-bottom: 20px;
  `,

  ButtonContainer: styled.div`
    display: flex;
    justify-content: center;

    gap: 20px;
  `,
};
