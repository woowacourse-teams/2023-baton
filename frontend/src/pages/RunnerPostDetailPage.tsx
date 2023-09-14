import React, { useCallback, useContext, useEffect, useMemo, useState } from 'react';
import { styled } from 'styled-components';
import { useParams } from 'react-router-dom';
import { usePageRouter } from '@/hooks/usePageRouter';
import Layout from '@/layout/Layout';
import PostTagList from '@/components/PostTagList/PostTagList';
import Avatar from '@/components/common/Avatar/Avatar';
import eyeIcon from '@/assets/eye-icon.svg';
import applicantIcon from '@/assets/applicant-icon.svg';
import Button from '@/components/common/Button/Button';
import { REVIEW_STATUS_LABEL_TEXT } from '@/constants/index';
import Label from '@/components/common/Label/Label';
import { GetDetailedRunnerPostResponse } from '@/types/runnerPost';
import ConfirmModal from '@/components/ConfirmModal/ConfirmModal';
import githubIcon from '@/assets/github-icon.svg';
import { useToken } from '@/hooks/useToken';
import { deleteRequest, getRequest, postRequest } from '@/api/fetch';
import { ERROR_DESCRIPTION, ERROR_TITLE, TOAST_COMPLETION_MESSAGE, TOAST_ERROR_MESSAGE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import SendMessageModal from '@/components/SendMessageModal/SendMessageModal';
import { validateMessage } from '@/utils/validate';
import useViewport from '@/hooks/useViewport';

const RunnerPostPage = () => {
  const { goToMainPage, goBack, goToRunnerProfilePage, goToMyPage } = usePageRouter();

  const { runnerPostId } = useParams();

  const { getToken, isLogin } = useToken();

  const { isMobile } = useViewport();

  const { showErrorToast, showCompletionToast } = useContext(ToastContext);

  const [runnerPost, setRunnerPost] = useState<GetDetailedRunnerPostResponse | null>(null);
  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState<boolean>(false);
  const [isMessageModalOpen, setIsMessageModalOpen] = useState<boolean>(false);
  const [message, setMessage] = useState<string>('');

  useEffect(() => {
    getRunnerPost();
  }, []);

  const getRunnerPost = () => {
    const token = getToken();
    if (!token) return;

    getRequest(`/posts/runner/${runnerPostId}`, token ?? undefined)
      .then(async (response) => {
        const data: GetDetailedRunnerPostResponse = await response.json();
        setRunnerPost(data);
      })
      .catch((error: Error) => {
        showErrorToast({ title: ERROR_TITLE.REQUEST, description: error.message });
        goBack();
      });
  };

  const deleteRunnerPost = () => {
    const token = getToken();
    if (!token) return;

    deleteRequest(`/posts/runner/${runnerPostId}`, token)
      .then(() => {
        showCompletionToast(TOAST_COMPLETION_MESSAGE.DELETE);

        goToMainPage();
      })
      .catch((error: Error) => showErrorToast({ title: ERROR_TITLE.REQUEST, description: error.message }));
  };

  const sendMessage = () => {
    const token = getToken();
    if (!token) return;

    try {
      validateMessage(message);
    } catch (error) {
      const description = error instanceof Error ? error.message : ERROR_DESCRIPTION.UNEXPECTED;
      showErrorToast({ title: ERROR_TITLE.REQUEST, description });

      return;
    }

    const body = JSON.stringify({ message: message });

    postRequest(`/posts/runner/${runnerPostId}/application`, token, body)
      .then(() => {
        showCompletionToast(TOAST_COMPLETION_MESSAGE.SUBMISSION);

        goToMyPage();
      })
      .catch((error: Error) => showErrorToast({ title: ERROR_TITLE.REQUEST, description: error.message }));
  };

  const handleChangeMessage = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setMessage(e.target.value);
  };

  const handleClickDeleteButton = () => {
    setIsConfirmModalOpen(false);

    deleteRunnerPost();
  };

  const openConfirmModal = () => {
    setIsConfirmModalOpen(true);
  };

  const closeConfirmModal = () => {
    setIsConfirmModalOpen(false);
  };

  const openMessageModal = () => {
    setIsMessageModalOpen(true);
  };

  const closeMessageModal = () => {
    setIsMessageModalOpen(false);
  };

  const viewProfile = () => {
    if (runnerPost) {
      goToRunnerProfilePage(runnerPost?.runnerProfile.runnerId);
    }
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
                <S.EditLinkContainer
                  $isOwner={
                    runnerPost.isOwner &&
                    runnerPost.reviewStatus !== 'IN_PROGRESS' &&
                    runnerPost.reviewStatus !== 'DONE'
                  }
                >
                  <S.EditLink onClick={openConfirmModal}>삭제</S.EditLink>
                </S.EditLinkContainer>
              </S.PostDeadlineContainer>
              <S.PostTitleContainer>
                <S.PostTitle>{runnerPost.title}</S.PostTitle>
                <S.InformationContainer>
                  <S.statisticsContainer>
                    <S.statisticsImage src={eyeIcon} />
                    <S.statisticsText>{runnerPost.watchedCount}</S.statisticsText>
                    <S.statisticsImage src={applicantIcon} />
                    <S.statisticsText>{runnerPost.applicantCount}</S.statisticsText>
                  </S.statisticsContainer>
                </S.InformationContainer>
              </S.PostTitleContainer>
            </S.PostHeaderContainer>
            <S.PostBodyContainer>
              <S.Contents>{runnerPost.contents}</S.Contents>
              <S.BottomContentContainer>
                <S.LeftSideContainer>
                  <S.ProfileContainer onClick={viewProfile}>
                    <Avatar imageUrl={runnerPost.runnerProfile.imageUrl} />
                    <S.Profile>
                      <S.Name>{runnerPost.runnerProfile.name}</S.Name>
                      <S.Job>{runnerPost.runnerProfile.company}</S.Job>
                    </S.Profile>
                  </S.ProfileContainer>
                  <PostTagList tags={runnerPost.tags} />
                </S.LeftSideContainer>
                <S.RightSideContainer>
                  <Button colorTheme="BLACK" fontSize={isMobile ? '14px' : ''} fontWeight={700}>
                    <S.Anchor href={runnerPost.pullRequestUrl} target="_blank">
                      <img src={githubIcon} />
                      <S.GoToGitHub>코드 보러가기</S.GoToGitHub>
                    </S.Anchor>
                  </Button>
                  {runnerPost.isOwner || runnerPost.isApplied || runnerPost.reviewStatus !== 'NOT_STARTED' ? null : (
                    <Button
                      colorTheme="WHITE"
                      fontSize={isMobile ? '14px' : ''}
                      fontWeight={700}
                      onClick={openMessageModal}
                    >
                      리뷰 제안하기
                    </Button>
                  )}
                </S.RightSideContainer>
              </S.BottomContentContainer>
            </S.PostBodyContainer>
            <S.PostFooterContainer>
              <Button colorTheme="GRAY" fontSize={isMobile ? '14px' : ''} fontWeight={700} onClick={goBack}>
                목록
              </Button>
            </S.PostFooterContainer>
          </S.PostContainer>
        )}
      </S.RunnerPostContainer>

      {isConfirmModalOpen && (
        <ConfirmModal
          contents="정말 삭제하시겠습니까?"
          closeModal={closeConfirmModal}
          handleClickConfirmButton={handleClickDeleteButton}
        />
      )}

      {isMessageModalOpen && (
        <SendMessageModal
          messageState={message}
          handleChangeMessage={handleChangeMessage}
          placeholder="러너에게 보낼 메세지를 입력해주세요."
          closeModal={closeMessageModal}
          handleClickSendButton={sendMessage}
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

    @media (max-width: 768px) {
      margin: 10px 0 30px 0;
    }
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

    @media (max-width: 768px) {
      font-size: 28px;
    }
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

    cursor: pointer;
  `,

  Profile: styled.div`
    display: flex;
    flex-flow: column;
    gap: 5px;
  `,

  Name: styled.p`
    font-size: 18px;
    font-weight: bold;

    @media (max-width: 768px) {
      font-size: 15px;
    }
  `,

  Job: styled.p`
    font-size: 18px;

    @media (max-width: 768px) {
      font-size: 15px;
    }
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

    @media (max-width: 768px) {
      min-height: 300px;
    }
  `,

  BottomContentContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: end;

    @media (max-width: 768px) {
      gap: 8px;
    }
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

    @media (max-width: 768px) {
      font-size: 12px;
    }
  `,

  Anchor: styled.a`
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 10px;
  `,
};

export default RunnerPostPage;
