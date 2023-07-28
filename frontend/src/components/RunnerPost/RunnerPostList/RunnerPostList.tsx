import React, { useEffect, useState } from 'react';
import { styled } from 'styled-components';
import RunnerPostItem from '../RunnerPostItem/RunnerPostItem';
import { BATON_BASE_URL } from '@/constants/index';
import { GetRunnerPostResponse } from '@/types/runnerPost';

const RunnerPostList = () => {
  const [runnerPostList, setRunnerPostList] = useState<GetRunnerPostResponse | null>(null);

  const getRunnerPost = async () => {
    try {
      const response = await fetch(`${BATON_BASE_URL}/posts/runner/test`, {
        method: 'GET',
      });

      if (!response.ok) {
        throw new Error(`네트워크 에러 코드: ${response.status}`);
      }

      const runnerPostList = await response.json();

      return runnerPostList;
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    const fetchRunnerPost = async () => {
      const result = await getRunnerPost();
      setRunnerPostList(result);
    };

    fetchRunnerPost();
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
