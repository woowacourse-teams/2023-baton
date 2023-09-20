import Button from '@/components/common/Button/Button';
import { ERROR_DESCRIPTION, ERROR_TITLE, TOAST_COMPLETION_MESSAGE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { useFetch } from '@/hooks/useFetch';
import { usePageRouter } from '@/hooks/usePageRouter';
import useViewport from '@/hooks/useViewport';

import { ReviewStatus } from '@/types/runnerPost';
import React, { useContext } from 'react';
import styled from 'styled-components';

interface Props {
  runnerPostId: number;
  reviewStatus: ReviewStatus;
  isRunner: boolean;
  supporterId?: number;
}

const MyPagePostButton = ({ runnerPostId, reviewStatus, isRunner, supporterId }: Props) => {
  const { goToSupportSelectPage, goToSupporterFeedbackPage } = usePageRouter();

  const { isMobile } = useViewport();

  const { patchRequestWithAuth } = useFetch();
  const { showCompletionToast, showErrorToast } = useContext(ToastContext);

  const cancelReview = () => {
    patchRequestWithAuth(`/posts/runner/${runnerPostId}/cancelation`, async (response) => {
      showCompletionToast(TOAST_COMPLETION_MESSAGE.REVIEW_CANCEL);
    });
  };

  const finishReview = () => {
    patchRequestWithAuth(`/posts/runner/${runnerPostId}/done`, async (response) => {
      showCompletionToast(TOAST_COMPLETION_MESSAGE.REVIEW_COMPLETE);
    });
  };

  const handleClickCancelReviewButton = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();

    cancelReview();
  };

  const handleClickFinishButton = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();

    finishReview();
  };

  const handleClickSupportSelectButton = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();
    goToSupportSelectPage(runnerPostId);
  };

  const handleClickSupportFeedbackButton = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.stopPropagation();
    if (!supporterId) {
      showErrorToast({ title: ERROR_TITLE.ERROR, description: ERROR_DESCRIPTION.NO_SUPPORTER });
      return;
    }

    goToSupporterFeedbackPage(runnerPostId, supporterId);
  };

  switch (reviewStatus) {
    case 'NOT_STARTED':
      return (
        <S.PostButtonWrapper>
          <Button
            fontSize={isMobile ? '14px' : ''}
            colorTheme="WHITE"
            fontWeight={700}
            width={isMobile ? '100%' : '180px'}
            height="40px"
            onClick={isRunner ? handleClickSupportSelectButton : handleClickCancelReviewButton}
          >
            {isRunner ? '서포터 선택하기' : '리뷰 제안 취소'}
          </Button>
        </S.PostButtonWrapper>
      );
    case 'IN_PROGRESS':
      return isRunner ? null : (
        <S.PostButtonWrapper>
          <Button
            colorTheme="WHITE"
            fontWeight={700}
            fontSize={isMobile ? '14px' : ''}
            width={isMobile ? '100%' : '180px'}
            height="40px"
            onClick={handleClickFinishButton}
          >
            리뷰 완료
          </Button>
        </S.PostButtonWrapper>
      );
    case 'DONE':
      return isRunner ? (
        <S.PostButtonWrapper>
          <Button
            colorTheme="WHITE"
            fontWeight={700}
            fontSize={isMobile ? '14px' : ''}
            width={isMobile ? '100%' : '180px'}
            height="40px"
            onClick={handleClickSupportFeedbackButton}
          >
            후기 작성
          </Button>
        </S.PostButtonWrapper>
      ) : null;
    default:
      return null;
  }
};

export default MyPagePostButton;

const S = {
  PostButtonWrapper: styled.div`
    width: 180px;

    @media (max-width: 768px) {
      width: 100%;
      margin-top: 18px;
    }
  `,
};
