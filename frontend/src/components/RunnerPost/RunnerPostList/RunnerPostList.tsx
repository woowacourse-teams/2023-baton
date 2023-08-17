import React from 'react';
import { styled } from 'styled-components';
import RunnerPostItem from '../RunnerPostItem/RunnerPostItem';
import { RunnerPost } from '@/types/runnerPost';

interface Props {
  posts: RunnerPost[];
}

const RunnerPostList = ({ posts }: Props) => {
  return (
    <S.RunnerPostWrapper>
      {posts.map((runnerPostData) => (
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
