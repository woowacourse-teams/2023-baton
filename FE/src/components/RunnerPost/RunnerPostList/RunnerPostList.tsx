import React from 'react';
import { styled } from 'styled-components';
import mockData from '../../../mocks/data/RunnerPostMock.json';
import RunnerPostItem from '../RunnerPostItem/RunnerPostItem';

const RunnerPostList = () => {
  const runnerPostMock = mockData;

  return (
    <S.RunnerPostWrapper>
      {runnerPostMock?.map((item) => (
        <RunnerPostItem key={item.runnerPostId} postItem={item} />
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
