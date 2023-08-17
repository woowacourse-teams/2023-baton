import { Candidate, GetSupporterCandidateResponse } from '@/types/supporterCandidate';
import React, { useContext, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import SupporterCardItem from '../SupporterCardItem/SupporterCardItem';
import { useParams } from 'react-router-dom';
import { useToken } from '@/hooks/useToken';
import { getRequest } from '@/api/fetch';
import { ToastContext } from '@/contexts/ToastContext';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';

const SupporterCardList = () => {
  const { runnerPostId } = useParams();

  const { getToken } = useToken();

  const { showErrorToast } = useContext(ToastContext);

  const [supporterList, setSupporterList] = useState<Candidate[]>([]);

  useEffect(() => {
    getSupporterList();
  }, []);

  const getSupporterList = async () => {
    const token = getToken()?.value;
    if (!token) return;

    getRequest(`/posts/runner/${runnerPostId}/supporters`, `Bearer ${token}`)
      .then(async (response) => {
        const data = await response.json();
        setSupporterList(data.data);
      })
      .catch((error: Error) => {
        const description = error instanceof Error ? error.message : ERROR_DESCRIPTION.UNEXPECTED;
        showErrorToast({ title: ERROR_TITLE.REQUEST, description });
      });
  };

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
