import React from 'react';
import { styled } from 'styled-components';
import RunnerPostItem from '../RunnerPostItem/RunnerPostItem';
import { RunnerPost } from '@/types/runnerPost';

interface Props {
  posts: RunnerPost[];
}

const RunnerPostList = ({ posts }: Props) => {
  return (
<<<<<<< HEAD
    <S.RunnerPostWrapper>
=======
    <S.RunnerPostWrapper aria-label="게시글 목록">
>>>>>>> dev/FE
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

    width: 100%;

    @media (max-width: 768px) {
      gap: 20px;
    }
  `,
};
