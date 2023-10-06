import React, { useContext } from 'react';
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
import ConfirmModal from '@/components/ConfirmModal/ConfirmModal';
import githubIcon from '@/assets/github-icon.svg';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import SendMessageModal from '@/components/SendMessageModal/SendMessageModal';
import { validateMessage } from '@/utils/validate';
import useViewport from '@/hooks/useViewport';
import { useLogin } from '@/hooks/useLogin';
import GuideContents from '@/components/GuideContents/GuideContents';
import { useRunnerPostDetail } from '@/hooks/query/useRunnerPostDetail';
import { useRunnerPostDelete } from '@/hooks/query/useRunnerPostDelete';
import { useReviewSuggestion } from '@/hooks/query/useReviewSuggestion';
import { ModalContext } from '@/contexts/ModalContext';

const RunnerPostPage = () => {
  const { goBack, goToRunnerProfilePage, goToLoginPage } = usePageRouter();

  const { runnerPostId: paramRunnerPostId } = useParams();
  const runnerPostId = Number(paramRunnerPostId);

  const { isLogin } = useLogin();

  const { isMobile } = useViewport();

  const { showErrorToast } = useContext(ToastContext);
  const { openModal, closeModal } = useContext(ModalContext);

  const { data: runnerPostDetail } = useRunnerPostDetail(runnerPostId, isLogin);
  const { mutate: deleteRunnerPost } = useRunnerPostDelete();
  const { mutate: suggestReview, isPending: isPendingSuggestion } = useReviewSuggestion();

  const sendMessage = (message: string) => {
    try {
      validateMessage(message);
    } catch (error) {
      const description = error instanceof Error ? error.message : ERROR_DESCRIPTION.UNEXPECTED;
      showErrorToast({ title: ERROR_TITLE.REQUEST, description });

      return;
    }

    suggestReview({ runnerPostId, message });
  };

  const handleClickDeleteButton = () => {
    closeModal();

    deleteRunnerPost(runnerPostId);
  };

  const openConfirmModal = () => {
    openModal(
      <ConfirmModal
        contents="정말 삭제하시겠습니까?"
        closeModal={closeModal}
        handleClickConfirmButton={handleClickDeleteButton}
      />,
    );
  };

  const openMessageModal = () => {
    if (!isLogin) {
      showErrorToast({ title: ERROR_TITLE.NO_PERMISSION, description: ERROR_DESCRIPTION.NO_TOKEN });
      goToLoginPage();

      return;
    }

    openModal(
      <SendMessageModal
        placeholder="러너에게 보낼 메세지를 입력해주세요."
        closeModal={closeModal}
        sendMessage={sendMessage}
      />,
    );
  };

  const viewProfile = () => {
    const runnerId = runnerPostDetail.runnerProfile.runnerId;
    goToRunnerProfilePage(runnerId);
  };

  return (
    <Layout>
      <S.RunnerPostContainer>
        <S.PostContainer>
          <S.PostHeaderContainer>
            <Label
              width="131px"
              height="33px"
              fontSize="16px"
              colorTheme={
                runnerPostDetail.reviewStatus === 'DONE'
                  ? 'GRAY'
                  : runnerPostDetail.reviewStatus === 'IN_PROGRESS'
                  ? 'RED'
                  : 'WHITE'
              }
            >
              {REVIEW_STATUS_LABEL_TEXT[runnerPostDetail.reviewStatus]}
            </Label>
            <S.PostDeadlineContainer>
              <S.PostDeadline>{runnerPostDetail.deadline.replace('T', ' ')} 까지</S.PostDeadline>
              <S.EditLinkContainer
                $isOwner={
                  runnerPostDetail.isOwner &&
                  runnerPostDetail.reviewStatus !== 'IN_PROGRESS' &&
                  runnerPostDetail.reviewStatus !== 'DONE'
                }
              >
                <S.EditLink onClick={openConfirmModal}>삭제</S.EditLink>
              </S.EditLinkContainer>
            </S.PostDeadlineContainer>
            <S.PostTitleContainer>
              <S.PostTitle>{runnerPostDetail.title}</S.PostTitle>
              <S.InformationContainer>
                <S.statisticsContainer>
                  <S.statisticsImage src={eyeIcon} />
                  <S.statisticsText>{runnerPostDetail.watchedCount}</S.statisticsText>
                  <S.statisticsImage src={applicantIcon} />
                  <S.statisticsText>{runnerPostDetail.applicantCount}</S.statisticsText>
                </S.statisticsContainer>
              </S.InformationContainer>
            </S.PostTitleContainer>
          </S.PostHeaderContainer>
          <S.PostBodyContainer>
            <S.GuideContentsContainer>
              <GuideContents title="무엇을 구현하였나요?" contents={runnerPostDetail.implementedContents} />
              <GuideContents title="아쉬운 점이나 궁금한 점이 있나요?" contents={runnerPostDetail.curiousContents} />
              {runnerPostDetail.postscriptContents && (
                <GuideContents
                  title="서포터에게 하고싶은 말이 있나요?"
                  contents={runnerPostDetail.postscriptContents}
                />
              )}
            </S.GuideContentsContainer>
            <S.BottomContentContainer>
              <S.LeftSideContainer>
                <S.ProfileContainer onClick={viewProfile}>
                  <Avatar
                    width={isMobile ? '40px' : '60px'}
                    height={isMobile ? '40px' : '60px'}
                    imageUrl={runnerPostDetail.runnerProfile.imageUrl}
                  />
                  <S.Profile>
                    <S.Name>{runnerPostDetail.runnerProfile.name}</S.Name>
                    <S.Job>{runnerPostDetail.runnerProfile.company}</S.Job>
                  </S.Profile>
                </S.ProfileContainer>
                <PostTagList tags={runnerPostDetail.tags} />
              </S.LeftSideContainer>
              <S.RightSideContainer>
                <Button
                  colorTheme="BLACK"
                  width={isMobile ? '130px' : '150px'}
                  fontSize={isMobile ? '14px' : ''}
                  fontWeight={700}
                >
                  <S.Anchor href={runnerPostDetail.pullRequestUrl} target="_blank">
                    <img src={githubIcon} />
                    <S.GoToGitHub>코드 보러가기</S.GoToGitHub>
                  </S.Anchor>
                </Button>
                {runnerPostDetail.isOwner ||
                runnerPostDetail.isApplied ||
                runnerPostDetail.reviewStatus !== 'NOT_STARTED' ? null : (
                  <Button
                    colorTheme="WHITE"
                    width={isMobile ? '130px' : '150px'}
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
            <Button
              width={isMobile ? '60px' : '180px'}
              colorTheme="GRAY"
              fontSize={isMobile ? '14px' : ''}
              fontWeight={700}
              onClick={goBack}
            >
              목록
            </Button>
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

    margin: 72px 0 53px 0;

    background-color: white;

    @media (max-width: 768px) {
      margin: 40px 0 30px 0;
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

    @media (max-width: 768px) {
      display: flex;
      flex-direction: column;
      align-items: flex-start;
      gap: 10px;

      & > div {
        margin-left: auto;
      }
    }
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

  GuideContentsContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 70px;

    margin-bottom: 50px;
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

    @media (max-width: 768px) {
      gap: 15px;
    }
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

    margin-bottom: 10px;

    @media (max-width: 768px) {
      display: flex;
      flex-direction: column;
      align-items: start;

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

    @media (max-width: 768px) {
      display: flex;
      flex-direction: row;
      flex: 1;
      gap: 12px;

      margin-top: 20px;
    }
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
