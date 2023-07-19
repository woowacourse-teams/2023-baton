import Avatar from '@components/common/Avatar';
import React from 'react';
import { styled } from 'styled-components';

interface Props {
  runnerPostId: number;
  title: string;
  deadline: string;
  tags: string[];
  profile: { name: string; imageUrl: string };
}

const RunnerPostItem = ({ postItem: { title, deadline, tags, profile } }: { postItem: Props }) => {
  return (
    <S.RunnerPostItemContainer>
      <S.LeftSideContainer>
        <S.PostTitle>{title}</S.PostTitle>
        <S.DeadLile>{deadline} 까지</S.DeadLile>
        <S.TagContainer>
          {tags.map((tag, index) => (
            <span key={index}>#{tag}</span>
          ))}
        </S.TagContainer>
      </S.LeftSideContainer>
      <S.RightSideContainer>
        <S.ProfileContainer>
          <Avatar width="60px" height="60px" imageUrl={profile.imageUrl} />
          <S.ProfileName>{profile.name}</S.ProfileName>
        </S.ProfileContainer>
        <S.ChatViewContainer>
          <div>조회수</div>
          <div>채팅수</div>
        </S.ChatViewContainer>
      </S.RightSideContainer>
    </S.RunnerPostItemContainer>
  );
};

export default RunnerPostItem;

const S = {
  RunnerPostItemContainer: styled.li`
    display: flex;
    justify-content: space-between;

    width: 1200px;
    height: 206px;
    padding: 35px 40px;

    border: 0.5px solid var(--gray-500);
    border-radius: 12px;
    box-shadow: 1px 2px 3px rgba(0, 0, 0, 0.2);

    cursor: pointer;

    &:hover {
      transition: all 0.3s ease;
      transform: scale(1.015);
      outline: 1.5px solid var(--baton-red);
    }
  `,

  PostTitle: styled.p`
    margin-bottom: 15px;

    font-size: 28px;
    font-weight: 700;
  `,

  DeadLile: styled.p`
    margin-bottom: 60px;

    color: var(--gray-600);
  `,

  TagContainer: styled.div`
    & span {
      margin-right: 10px;

      font-size: 14px;
      color: var(--gray-600);
    }
  `,

  LeftSideContainer: styled.div``,
  RightSideContainer: styled.div``,

  ProfileContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;

    margin-bottom: 30px;
    gap: 10px;
  `,

  ProfileName: styled.p`
    font-size: 14px;
  `,

  ChatViewContainer: styled.div`
    display: flex;
  `,
};
