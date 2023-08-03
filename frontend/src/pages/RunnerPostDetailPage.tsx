import React, { useEffect, useState } from 'react';
import { styled } from 'styled-components';
import { useParams } from 'react-router-dom';
import { usePageRouter } from '@/hooks/usePageRouter';
import Layout from '@/layout/Layout';
import PostTagList from '@/components/PostTagList';
import Avatar from '@/components/common/Avatar';
import eyeIcon from '@/assets/eye-icon.svg';
import chattingIcon from '@/assets/chatting-icon.svg';
import Button from '@/components/common/Button';
import { BATON_BASE_URL, REVIEW_STATUS_LABEL_TEXT } from '@/constants/index';
import Label from '@/components/common/Label';
import { GetDetailedRunnerPostResponse } from '@/types/runnerPost';
import ConfirmModal from '@/components/ConfirmModal';
import githubIcon from '@/assets/github-icon.svg';

const RunnerPostPage = () => {
  const [runnerPost, setRunnerPost] = useState<GetDetailedRunnerPostResponse | null>(null);
  const [isModalOpen, setIsModalOpen] = useState<boolean>(false);

  const { goToMainPage } = usePageRouter();
  const { runnerPostId } = useParams();

  useEffect(() => {
    getRunnerPost()
      .then((data) => {
        setRunnerPost(data);
      })
      .catch((error) => {
        setRunnerPost(null);
      });
  }, []);

  const getRunnerPost = async (): Promise<GetDetailedRunnerPostResponse> => {
    const response = await fetch(`${BATON_BASE_URL}/posts/runner/${runnerPostId}/test`, {
      method: 'GET',
    });

    if (!response.ok) throw new Error('게시글을 불러오지 못했습니다.');

    const data = await response.json().catch(() => {
      throw new Error('게시글을 불러오지 못했습니다.');
    });

    return data;
  };

  const deleteRunnerPost = async () => {
    const response = await fetch(`${BATON_BASE_URL}/posts/runner/${runnerPostId}/test`, {
      method: 'DELETE',
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!response.ok) throw new Error('게시글을 삭제하지 못했습니다.');
  };

  const handleClickDeleteButton = () => {
    setIsModalOpen(false);

    deleteRunnerPost();
    goToMainPage();
  };

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  return (
    <Layout>
      <S.RunnerPostContainer>
        {runnerPost && (
          <S.PostContainer>
            <S.PostHeaderContainer>
              <Label
                width="131px"
                height="33px"
                fontSize="16px"
                colorTheme={
                  runnerPost.reviewStatus === 'DONE'
                    ? 'GRAY'
                    : runnerPost.reviewStatus === 'IN_PROGRESS'
                    ? 'RED'
                    : 'WHITE'
                }
              >
                {REVIEW_STATUS_LABEL_TEXT[runnerPost.reviewStatus]}
              </Label>
              <S.PostDeadlineContainer>
                <S.PostDeadline>{runnerPost.deadline.replace('T', ' ')} 까지</S.PostDeadline>
                <S.EditLinkContainer $isOwner={runnerPost.isOwner}>
                  <S.EditLink>{/*수정 기능 구현 필요*/}</S.EditLink> <S.EditLink onClick={openModal}>삭제</S.EditLink>
                </S.EditLinkContainer>
              </S.PostDeadlineContainer>
              <S.PostTitleContainer>
                <S.PostTitle>{runnerPost.title}.</S.PostTitle>
                <S.InformationContainer>
                  <S.statisticsContainer>
                    <S.statisticsImage src={eyeIcon} />
                    <S.statisticsText>{runnerPost.watchedCount}</S.statisticsText>
                    <S.statisticsImage src={chattingIcon} />
                    <S.statisticsText>{runnerPost.chattingCount}</S.statisticsText>
                  </S.statisticsContainer>
                </S.InformationContainer>
              </S.PostTitleContainer>
            </S.PostHeaderContainer>
            <S.PostBodyContainer>
              <S.Contents>{runnerPost.contents}</S.Contents>
              <S.BottomContentContainer>
                <S.LeftSideContainer>
                  <S.ProfileContainer>
                    <Avatar imageUrl={runnerPost.runnerProfile.imageUrl} />
                    <S.Profile>
                      <S.Name>{runnerPost.runnerProfile.name}</S.Name>
                      <S.Job>{runnerPost.runnerProfile.company}</S.Job>
                    </S.Profile>
                  </S.ProfileContainer>
                  <PostTagList tags={runnerPost.tags} />
                </S.LeftSideContainer>
                <S.RightSideContainer>
                  <Button colorTheme="BLACK" fontWeight={700}>
                    <S.Anchor href={runnerPost.pullRequestUrl} target="_blank">
                      <img src={githubIcon} />
                      <S.GoToGitHub>코드 보러가기</S.GoToGitHub>
                    </S.Anchor>
                  </Button>
                  {/* <Button colorTheme="WHITE" fontWeight={700}>
                    1:1 대화하기
                  </Button> */}
                </S.RightSideContainer>
              </S.BottomContentContainer>
            </S.PostBodyContainer>
            <S.PostFooterContainer>
              <Button colorTheme="GRAY" fontWeight={700} onClick={goToMainPage}>
                목록
              </Button>
            </S.PostFooterContainer>
          </S.PostContainer>
        )}
      </S.RunnerPostContainer>
      {isModalOpen && (
        <ConfirmModal
          contents="정말 삭제하시겠습니까?"
          closeModal={closeModal}
          handleClickConfirmButton={handleClickDeleteButton}
        />
      )}
    </Layout>
  );
};

const S = {
  RunnerPostContainer: styled.div`
    width: 100%;
    height: 100%;

    margin: 72px 0 53px 0;

    background-color: white;
  `,

  Title: styled.div`
    font-size: 36px;
    font-weight: bold;
  `,
  PostDeadlineContainer: styled.div`
    display: flex;
    justify-content: space-between;

    margin: 20px 0;
  `,

  PostContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 15px;

    width: 90%;
    margin: 0 auto;
  `,

  PostHeaderContainer: styled.div`
    width: 100%;
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
    font-size: 16px;
    color: var(--gray-500);
  `,

  PostTitleContainer: styled.div`
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 10px;
  `,

  PostTitle: styled.h1`
    font-size: 38px;
    font-weight: 700;
  `,

  PostDeadline: styled.div`
    display: flex;
    align-items: center;
    font-size: 16px;
    color: var(--gray-600);
  `,

  PostBodyContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 30px;

    width: 100%;

    margin-bottom: 20px;
    padding: 40px 10px 40px 0;
    border-bottom: 1px solid #9d9d9d;
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

    & > p {
      color: #a4a4a4;
    }
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

  BottomContentContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: end; // 1:1 대화버튼 추가시 center로 수정
  `,

  LeftSideContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 24px;
  `,

  RightSideContainer: styled.div`
    display: flex;
    flex-direction: column;

    gap: 24px;
  `,

  GoToGitHub: styled.p``,

  PostFooterContainer: styled.div`
    display: flex;
    justify-content: space-between;

    font-size: 20px;
    font-weight: bold;
  `,

  Anchor: styled.a`
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 10px;
  `,
};

export default RunnerPostPage;
