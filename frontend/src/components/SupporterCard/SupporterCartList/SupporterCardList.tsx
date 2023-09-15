import { Candidate } from '@/types/supporterCandidate';
import React, { useContext, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import SupporterCardItem from '../SupporterCardItem/SupporterCardItem';
import { useParams } from 'react-router-dom';
import { ToastContext } from '@/contexts/ToastContext';
import { useFetch } from '@/hooks/useFetch';
import { useLogin } from '@/hooks/useLogin';
import { usePageRouter } from '@/hooks/usePageRouter';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';

const SupporterCardList = () => {
  const { runnerPostId } = useParams();

  const { isLogin } = useLogin();
  const { getRequestWithAuth } = useFetch();
  const { showErrorToast } = useContext(ToastContext);
  const { goToLoginPage } = usePageRouter();

  const [supporterList, setSupporterList] = useState<Candidate[]>([]);

  useEffect(() => {
    if (!isLogin) {
      showErrorToast({ title: ERROR_TITLE.REQUEST, description: ERROR_DESCRIPTION.NO_TOKEN });
      goToLoginPage();

      return;
    }
    getSupporterList();
  }, []);

  const getSupporterList = async () => {
    getRequestWithAuth(`/posts/runner/${runnerPostId}/supporters`, async (response) => {
      const data = await response.json();
      setSupporterList(data.data);
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
