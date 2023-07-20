import React, { useEffect, useState } from 'react';
import { styled } from 'styled-components';
import RunnerPostItem from '../RunnerPostItem/RunnerPostItem';
import { RunnerPost } from '../../../types/RunnerPost';

const RunnerPostList = () => {
  const [runnerPostList, setRunnerPostList] = useState<RunnerPost | null>(null);

  const getRunnerPost = async () => {
    try {
      const response = await fetch('msw/posts/runner', {
        method: 'GET',
        headers: {
          'Content-Type': 'application/json',
        },
      });

      if (!response.ok) throw new Error(`네트워크 에러 코드: ${response.status}`);

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
