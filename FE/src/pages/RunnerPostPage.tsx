import Avatar from '@components/common/Avatar';
import Button from '@components/common/Button';
import Layout from '@layout/Layout';
import React, { useEffect, useState } from 'react';
import { styled } from 'styled-components';
import eyeIcon from '@assets/eye-icon.svg';
import chattingIcon from '@assets/chatting-icon.svg';
import { useNavigate } from 'react-router-dom';
import { ROUTER_PATH } from '../router';

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

const getRunnerPost = async (): Promise<RunnerPost> => {
  const response = await fetch(`msw/posts/runner/1`, {
    method: 'GET',
  });

  if (!response.ok) throw new Error('게시글을 불러오지 못했습니다.');

  const data = await response.json().catch(() => {
    throw new Error('게시글을 불러오지 못했습니다.');
  });

  return data;
};

const RunnerPostPage = () => {
  const [runnerPost, setRunnerPost] = useState<RunnerPost | null>(null);

  useEffect(() => {
    getRunnerPost()
      .then((data) => {
        setRunnerPost(data);
      })
      .catch((error) => {
        setRunnerPost(null);
        console.log(error);
      });
  }, [runnerPost]);

  const navigate = useNavigate();

  const goToMainPage = () => {
    navigate(ROUTER_PATH.MAIN);
  };

  return (
    <Layout>
      <S.RunnerPostContainer>
        <S.Title> 서포터를 찾고 있어요 👀</S.Title>
        {runnerPost && (
          <S.PostContainer>
            <S.PostHeaderContainer>
              <S.HashTagList>
                {runnerPost.tags.map((tag) => (
                  <S.HashTag key={tag}>#{tag}</S.HashTag>
                ))}
              </S.HashTagList>
              <S.EditLinkContainer $isOwner={runnerPost.isOwner}>
                <S.EditLink>수정</S.EditLink> <S.EditLink>삭제</S.EditLink>
              </S.EditLinkContainer>
              <S.PostTitle>{runnerPost.title}.</S.PostTitle>
              <S.PostDeadline>{runnerPost.deadline.replace('T', ' ')} 까지</S.PostDeadline>
            </S.PostHeaderContainer>
            <S.PostBodyContainer>
              <S.InformationContainer>
                <S.ProfileContainer>
                  <Avatar imageUrl={runnerPost.profile.imageUrl} width={'60px'} />
                  <S.Profile>
                    <S.Name>{runnerPost.profile.name}</S.Name>
                    <S.Job>{runnerPost.profile.company}</S.Job>
                  </S.Profile>
                </S.ProfileContainer>
                <S.statisticsContainer>
                  <S.statisticsImage src={eyeIcon} />
                  <S.statisticsText>{runnerPost.watchedCount}</S.statisticsText>
                  <S.statisticsImage src={chattingIcon} />
                  <S.statisticsText>{runnerPost.chattingCount}</S.statisticsText>
                </S.statisticsContainer>
              </S.InformationContainer>
              <S.Contents>{runnerPost.contents}</S.Contents>
            </S.PostBodyContainer>
            <S.PostFooterContainer>
              <Button colorTheme="GRAY" fontWeight={700} onClick={goToMainPage}>
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
        )}
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

    font-size: 36px;
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
