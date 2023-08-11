import React, { useEffect, useState } from 'react';
import { styled } from 'styled-components';
import RunnerPostItem from '../RunnerPostItem/RunnerPostItem';
import { GetRunnerPostResponse } from '@/types/runnerPost';
import { getRequest } from '@/api/fetch';

const RunnerPostList = () => {
  const [runnerPostList, setRunnerPostList] = useState<GetRunnerPostResponse | null>(null);

  useEffect(() => {
    const getRunnerPost = async () => {
      const result = await getRequest<GetRunnerPostResponse>('/posts/runner/test');

      setRunnerPostList(result);
    };

    getRunnerPost();
  }, []);

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
