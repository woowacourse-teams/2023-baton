import Avatar from '@components/common/Avatar';
import Button from '@components/common/Button';
import Layout from '@layout/Layout';
import React from 'react';
import { styled } from 'styled-components';
import eyeIcon from '@assets/eye-icon.svg';
import chattingIcon from '@assets/chatting-icon.svg';

interface Profile {
  memberId: number;
  name: string;
  company: string;
  imageUrl: string;
}

interface RunnerPost {
  runnerPostId: number;
  title: string;
  deadline: string;
  tags: string[];
  chattingCount: number;
  watchedCount: number;
  contents: string;
  isOwner: boolean;
  profile: Profile;
}

const information: RunnerPost = {
  runnerPostId: 1,
  title: '리액트 리뷰 부탁드립니다.',
  deadline: '2023-07-15T13:56',
  tags: ['개인프로젝트', '리액트', 'React'],
  chattingCount: 2,
  watchedCount: 123,
  contents:
    '자바를 배운지 3년 정도 되었는데요 제가 짠 코드가 어떤지 궁금해서 리뷰 요청 올려봅니다.\n현업에 계신 분들.. 좀 도와주세요 ㅠㅠ\nPR 메시지는 PR 링크에 작성해놨습니다 !',
  isOwner: true,
  profile: {
    memberId: 1,
    name: '감자도리',
    company: '대학교 2학년',
    imageUrl: 'https://avatars.githubusercontent.com/u/103256030?v=4',
  },
};

const RunnerPostPage = () => {
  const { title, deadline, tags, chattingCount, watchedCount, contents, isOwner, profile } = information;

  return (
    <Layout>
      <S.RunnerPostContainer>
        <S.Title> 서포터를 찾고 있어요 👀</S.Title>
        <S.PostContainer>
          <S.PostHeaderContainer>
            <S.HashTagList>
              {tags.map((tag) => (
                <S.HashTag key={tag}>#{tag}</S.HashTag>
              ))}
            </S.HashTagList>
            <S.EditLinkContainer $isOwner={isOwner}>
              <S.EditLink>수정</S.EditLink> <S.EditLink>삭제</S.EditLink>
            </S.EditLinkContainer>
            <S.PostTitle>{title}.</S.PostTitle>
            <S.PostDeadline>{deadline.replace('T', ' ')} 까지</S.PostDeadline>
          </S.PostHeaderContainer>
          <S.PostBodyContainer>
            <S.InformationContainer>
              <S.ProfileContainer>
                <Avatar imageUrl={profile.imageUrl} width={'60px'} />
                <S.Profile>
                  <S.Name>{profile.name}</S.Name>
                  <S.Job>{profile.company}</S.Job>
                </S.Profile>
              </S.ProfileContainer>
              <S.statisticsContainer>
                <S.statisticsImage src={eyeIcon} />
                <S.statisticsText>{watchedCount}</S.statisticsText>
                <S.statisticsImage src={chattingIcon} />
                <S.statisticsText>{chattingCount}</S.statisticsText>
              </S.statisticsContainer>
            </S.InformationContainer>
            <S.Contents>{contents}</S.Contents>
          </S.PostBodyContainer>
          <S.PostFooterContainer>
            <Button colorTheme="GRAY" fontWeight={700}>
              목록
            </Button>
            <S.PrimaryButtonContainer>
              <Button colorTheme="WHITE" fontWeight={700}>
                코드 보러가기
              </Button>
              <Button colorTheme="WHITE" fontWeight={700}>
                1:1 대화하기
              </Button>
            </S.PrimaryButtonContainer>
          </S.PostFooterContainer>
        </S.PostContainer>
      </S.RunnerPostContainer>
    </Layout>
  );
};

const S = {
  RunnerPostContainer: styled.div`
    width: 100%;
    height: 100%;

    background-color: white;
  `,

  Title: styled.div`
    padding: 40px 30px 40px 10px;

    font-size: 35px;
    font-weight: bold;
  `,

  PostContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 15px;

    width: 90%;
    margin: 0 auto;
  `,

  PostHeaderContainer: styled.div`
    display: grid;
    grid-template-columns: 1fr 1fr;
    grid-template-rows: 1fr 1fr;
    row-gap: 10px;

    width: 100%;
  `,

  HashTagList: styled.ul`
    list-style: none;

    li:not(:last-child) {
      margin-right: 10px;
    }
  `,

  HashTag: styled.li`
    float: left;

    font-size: 18px;
    color: var(--gray-500);
  `,

  EditLinkContainer: styled.div<{ $isOwner: boolean }>`
    visibility: ${({ $isOwner }) => ($isOwner ? 'visible' : 'hidden')};

    margin-left: auto;

    font-size: 20px;
    font-weight: bold;

    :hover {
      cursor: pointer;
      text-decoration: underline;
    }
  `,

  EditLink: styled.a`
    background-color: white;
  `,

  PostTitle: styled.div`
    font-size: 28px;
    font-weight: bold;
  `,

  PostDeadline: styled.div`
    margin-left: auto;

    font-size: 16px;
    color: var(--gray-600);
  `,

  PostBodyContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 30px;

    width: 100%;

    margin-bottom: 10px;
    padding: 20px 10px 20px 10px;
    border-top: 1px solid black;
    border-bottom: 1px solid black;
  `,

  InformationContainer: styled.div`
    display: flex;
    justify-content: space-between;
  `,

  ProfileContainer: styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 20px;
  `,

  Profile: styled.div`
    display: flex;
    flex-flow: column;
    gap: 5px;
  `,

  Name: styled.p`
    font-size: 18px;
    font-weight: bold;
  `,

  Job: styled.p`
    font-size: 18px;
  `,

  statisticsContainer: styled.div`
    display: flex;
    align-items: center;
    gap: 5px;

    margin-bottom: auto;
  `,

  statisticsImage: styled.img`
    width: 20px;
  `,

  statisticsText: styled.p`
    margin-right: 10px;

    font-size: 14px;
  `,

  Contents: styled.div`
    min-height: 400px;

    font-size: 18px;
    line-height: 200%;
    white-space: pre-wrap;
  `,

  PostFooterContainer: styled.div`
    display: flex;
    justify-content: space-between;

    font-size: 20px;
    font-weight: bold;
  `,

  PrimaryButtonContainer: styled.div`
    display: flex;
    gap: 20px;
  `,
};

export default RunnerPostPage;
