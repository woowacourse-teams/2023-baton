import React from 'react';
import { styled } from 'styled-components';
import { MyPagePost } from '@/types/myPage';
import MyPagePostItem from '../MyPagePostItem/MyPagePostItem';

interface Props {
  filterList: () => MyPagePost[];
  isRunner: boolean;
}

const MyPagePostList = ({ filterList, isRunner }: Props) => {
  if (filterList().length === 0) return <p>게시글 정보가 없습니다.</p>;

  return (
    <S.RunnerPostWrapper>
      {filterList()?.map((item: MyPagePost) => (
        <MyPagePostItem key={item.runnerPostId} {...item} isRunner={isRunner} />
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
