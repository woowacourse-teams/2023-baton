import React, { useEffect, useState } from 'react';
import { styled } from 'styled-components';
import { BATON_BASE_URL } from '@/constants/index';
import MyPageRunnerPostItem from '../MyPageRunnerPostItem/MyPageRunnerPostItem';
import { MyPageRunnerPost } from '@/types/myPage';

interface Props {
  filterList: () => MyPageRunnerPost[];
}

const MyPageRunnerPostList = ({ filterList }: Props) => {
  if (filterList().length === 0) return <p>게시글 정보가 없습니다.</p>;

  return (
    <S.RunnerPostWrapper>
      {filterList()?.map((item: MyPageRunnerPost) => (
        <MyPageRunnerPostItem key={item.runnerPostId} {...item} />
      ))}
    </S.RunnerPostWrapper>
  );
};

export default MyPageRunnerPostList;

const S = {
  RunnerPostWrapper: styled.ul`
    display: flex;
    flex-direction: column;
    align-items: center;

    gap: 30px;
  `,
};
