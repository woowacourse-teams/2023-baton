import React from 'react';
import { styled } from 'styled-components';
import { GetMyPagePostResponse, MyPagePost } from '@/types/myPage';
import MyPagePostItem from '../MyPagePostItem/MyPagePostItem';

interface Props {
  filteredPostList: MyPagePost[];
  isRunner: boolean;
}

const MyPagePostList = ({ filteredPostList, isRunner }: Props) => {
  if (filteredPostList?.length === 0) return <p>게시글 정보가 없습니다.</p>;

  return (
    <S.RunnerPostWrapper>
      {filteredPostList?.map((item: MyPagePost) => (
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
