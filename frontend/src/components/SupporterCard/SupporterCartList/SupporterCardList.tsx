import React, { useContext, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import SupporterCardItem from '../SupporterCardItem/SupporterCardItem';
import { useParams } from 'react-router-dom';
import { ToastContext } from '@/contexts/ToastContext';
import { usePageRouter } from '@/hooks/usePageRouter';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';
import { useProposedSupporterList } from '@/hooks/query/useProposedSupporterList';
import { isLogin } from '@/apis/auth';

const SupporterCardList = () => {
  const { runnerPostId } = useParams();

  const { showErrorToast } = useContext(ToastContext);
  const { goToLoginPage } = usePageRouter();

  const { data: supporterList } = useProposedSupporterList(Number(runnerPostId));

  useEffect(() => {
    if (!isLogin()) {
      showErrorToast({ title: ERROR_TITLE.REQUEST, description: ERROR_DESCRIPTION.NO_TOKEN });
      goToLoginPage();

      return;
    }
  }, []);

  return (
    <S.SupporterCardListContainer>
      {supporterList?.map((supporter) => (
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

    @media (max-width: 768px) {
      grid-template-columns: repeat(1, 1fr);

      row-gap: 50px;
      column-gap: 0;
    }
  `,
};
