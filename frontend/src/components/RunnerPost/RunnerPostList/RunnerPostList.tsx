import React, { useEffect, useState } from 'react';
import { styled } from 'styled-components';
import RunnerPostItem from '../RunnerPostItem/RunnerPostItem';
import { GetRunnerPostResponse } from '@/types/runnerPost';
import { getRequest } from '@/api/fetch';

const RunnerPostList = () => {
  const [runnerPostList, setRunnerPostList] = useState<GetRunnerPostResponse | null>(null);

  useEffect(() => {
    getRunnerPost();
  }, []);

  const getRunnerPost = () => {
    getRequest('/posts/runner/test')
      .then(async (response) => {
        const data = await response.json();
        setRunnerPostList(data);
      })
      .catch((error: Error) => {
        alert('게시글 목록을 불러오지 못했습니다.' + error.message);
      });
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
