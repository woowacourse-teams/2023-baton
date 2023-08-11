import { Candidate, GetSupporterCandidateResponse } from '@/types/supporterCandidate';
import React, { useEffect, useState } from 'react';
import { styled } from 'styled-components';
import SupporterCardItem from '../SupporterCardItem/SupporterCardItem';
import { useParams } from 'react-router-dom';
import { useToken } from '@/hooks/useToken';
import { getRequest } from '@/api/fetch';

const SupporterCardList = () => {
  const { runnerPostId } = useParams();

  const { getToken } = useToken();

  const [supporterList, setSupporterList] = useState<Candidate[]>([]);

  useEffect(() => {
    const getSupporterList = async () => {
      const token = getToken()?.value;
      if (!token) throw new Error('토큰이 존재하지 않습니다');

      const result = await getRequest<GetSupporterCandidateResponse>(
        `/posts/runner/${runnerPostId}/supporters`,
        `Bearer ${token}`,
      );

      setSupporterList(result.data);
    };

    getSupporterList();
  }, []);

  return (
    <S.SupporterCardListContainer>
      {supporterList.map((supporter) => (
        <SupporterCardItem key={supporter.supporterId} supporter={supporter} />
      ))}
    </S.SupporterCardListContainer>
  );
};

export default SupporterCardList;

const S = {
  SupporterCardListContainer: styled.ul`
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    row-gap: 80px;
    column-gap: 40px;

    width: 100%;
  `,
};
