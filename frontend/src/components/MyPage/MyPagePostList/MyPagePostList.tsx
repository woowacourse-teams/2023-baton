import React from 'react';
import { styled } from 'styled-components';
import MyPageRunnerPostItem from '../MyPagePostItem/MyPagePostItem';
import { MyPagePost } from '@/types/myPage';

interface Props {
  filterList: () => MyPagePost[];
}

const MyPagePostList = ({ filterList }: Props) => {
  if (filterList().length === 0) return <p>게시글 정보가 없습니다.</p>;

  return (
    <S.RunnerPostWrapper>
      {filterList()?.map((item: MyPagePost) => (
        <MyPageRunnerPostItem key={item.runnerPostId} {...item} />
      ))}
    </S.RunnerPostWrapper>
  );
};

export default MyPagePostList;

const S = {
  RunnerPostWrapper: styled.ul`
    display: flex;
    flex-direction: column;
    align-items: center;

    gap: 30px;
  `,
};
