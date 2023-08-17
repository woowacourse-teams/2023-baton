import React, { useContext, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import RunnerPostItem from '../RunnerPostItem/RunnerPostItem';
import { GetRunnerPostResponse } from '@/types/runnerPost';
import { getRequest } from '@/api/fetch';
import { ToastContext } from '@/contexts/ToastContext';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';

const RunnerPostList = () => {
  const { showErrorToast } = useContext(ToastContext);

  const [runnerPostList, setRunnerPostList] = useState<GetRunnerPostResponse | null>(null);

  useEffect(() => {
    getRunnerPost();
  }, []);

  const getRunnerPost = () => {
    getRequest('/posts/runner/test')
      .then(async (response) => {
        const data: GetRunnerPostResponse = await response.json();

        setRunnerPostList(data);
      })
      .catch((error: Error) => showErrorToast({ description: error.message, title: ERROR_TITLE.ERROR }));
  };

  return (
    <S.RunnerPostWrapper>
      {runnerPostList?.data.map((runnerPostData) => (
        <RunnerPostItem key={runnerPostData.runnerPostId} runnerPostData={runnerPostData} />
      ))}
    </S.RunnerPostWrapper>
  );
};

export default RunnerPostList;

const S = {
  RunnerPostWrapper: styled.ul`
    display: flex;
    flex-direction: column;
    align-items: center;

    gap: 30px;
  `,
};
