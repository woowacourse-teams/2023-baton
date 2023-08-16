import React, { useCallback, useEffect, useMemo, useState } from 'react';
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
import SendMessageModal from '@/components/SendMessageModal/SendMessageModal';

const RunnerPostPage = () => {
  const { runnerPostId } = useParams();

  const { goToMainPage, goToMyPage, goBack } = usePageRouter();
  const { getToken } = useToken();

  const [runnerPost, setRunnerPost] = useState<GetDetailedRunnerPostResponse | null>(null);
  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState<boolean>(false);
  const [isMessageModalOpen, setIsMessageModalOpen] = useState<boolean>(false);
  const [message, setMessage] = useState<string>('');

  const token = useMemo(() => getToken()?.value, [getToken]);

  useEffect(() => {
    getRunnerPost();
  }, []);

  const getRunnerPost = () => {
    getRequest(`/posts/runner/${runnerPostId}/test`, token)
      .then(async (response) => {
        const data: GetDetailedRunnerPostResponse = await response.json();
        setRunnerPost(data);
      })
      .catch((error: Error) => {
        alert(error.message || '게시글 정보를 불러오지 못했습니다.');

        goBack();
      });
  };

  const deleteRunnerPost = useCallback(() => {
    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    deleteRequest(`/posts/runner/${runnerPostId}`, token)
      .then(() => {
        alert('리뷰 요청글을 삭제했습니다.');

        goToMainPage();
      })
      .catch((error: Error) => {
        alert(error.message || '게시글을 삭제하지 못했습니다.');
      });
  }, [token]);

  const sendMessage = () => {
    if (!token) {
      alert('로그인이 필요합니다.');
      return;
    }

    if (message.length < 20) {
      alert('20자 이상 입력해주세요!');
      return;
    }

    const body = JSON.stringify({ message: message });

    postRequest(`/posts/runner/${runnerPostId}/application`, token!, body)
      .then(() => {
        alert('러너에게 리뷰 제안을 보냈습니다.');

        goToMyPage();
      })
      .catch((error: Error) => {
        alert(error.message || '리뷰 제안을 보내지 못했습니다.');
      });
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
                  {runnerPost.isOwner || runnerPost.isApplied || runnerPost.reviewStatus !== 'NOT_STARTED' ? null : (
                    <Button colorTheme="WHITE" fontWeight={700} onClick={openMessageModal}>
                      리뷰 제안하기
                    </Button>
                  )}
                </S.RightSideContainer>
              </S.BottomContentContainer>
            </S.PostBodyContainer>
            <S.PostFooterContainer>
              <Button colorTheme="GRAY" fontWeight={700} onClick={goBack}>
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
    align-items: end;
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
