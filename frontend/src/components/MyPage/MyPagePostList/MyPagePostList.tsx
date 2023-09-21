import React from 'react';
import { styled } from 'styled-components';
import { GetMyPagePostResponse, MyPagePost } from '@/types/myPage';
import MyPagePostItem from '../MyPagePostItem/MyPagePostItem';

interface Props {
  filteredPostList: MyPagePost[];
  isRunner: boolean;
  handleDeletePost: (handleDeletePost: number) => void;
}

const MyPagePostList = ({ filteredPostList, isRunner, handleDeletePost }: Props) => {
  if (filteredPostList?.length === 0) return <p>게시글 정보가 없습니다.</p>;

  return (
    <S.RunnerPostWrapper aria-label="게시글 목록">
      {filteredPostList?.map((item: MyPagePost) => (
        <MyPagePostItem handleDeletePost={handleDeletePost} key={item.runnerPostId} {...item} isRunner={isRunner} />
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

    width: 100%;

    @media (max-width: 768px) {
      gap: 20px;
    }
  `,
};
