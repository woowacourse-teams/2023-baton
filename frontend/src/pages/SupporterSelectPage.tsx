import SupporterCardList from '@/components/SupporterCard/SupporterCartList/SupporterCardList';
import { BATON_BASE_URL } from '@/constants';
import { usePageRouter } from '@/hooks/usePageRouter';
import { useToken } from '@/hooks/useToken';
import Layout from '@/layout/Layout';
import { GetSupporterCandidateResponse, Candidate } from '@/types/supporterCandidate';
import React, { useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { styled } from 'styled-components';

const SupporterSelectPage = () => {
  const { runnerPostId } = useParams();

  const { getToken } = useToken();
  const { goToMyPage } = usePageRouter();

  const [supporterList, setSupporterList] = useState<Candidate[]>([]);

  const getSupporterList = async () => {
    try {
      const token = getToken()?.value;
      if (!token) throw new Error('토큰이 존재하지 않습니다');

      const response = await fetch(`${BATON_BASE_URL}/posts/runner/${runnerPostId}/supporters`, {
        method: 'GET',
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }

      const supporterList: GetSupporterCandidateResponse = await response.json();

      return supporterList.data;
    } catch (error) {
      console.error(error);

      return [];
    }
  };

  useEffect(() => {
    getSupporterList()
      .then((result) => {
        setSupporterList(result);
      })
      .catch(() => {
        alert('서포터 목록을 불러오지 못했습니다.');

        goToMyPage();
      });
  }, []);

  return (
    <Layout>
      <S.TitleWrapper>
        <S.Title>서포터를 선택해 주세요 ✅</S.Title>
        <S.SubTitle>선택한 서포터가 확인 후 리뷰를 진행합니다.</S.SubTitle>
      </S.TitleWrapper>
      <S.SupporterListContainer>
        <SupporterCardList supporterList={supporterList} />
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
