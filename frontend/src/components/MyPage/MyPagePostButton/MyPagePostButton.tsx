import { patchRequest } from '@/api/fetch';
import Button from '@/components/common/Button/Button';
import { ERROR_DESCRIPTION, ERROR_TITLE, TOAST_COMPLETION_MESSAGE, TOAST_ERROR_MESSAGE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { usePageRouter } from '@/hooks/usePageRouter';
import { useToken } from '@/hooks/useToken';
import { ReviewStatus } from '@/types/runnerPost';
import React, { useContext, useMemo } from 'react';

interface Props {
  runnerPostId: number;
  reviewStatus: ReviewStatus;
  isRunner: boolean;
  supporterId?: number;
}

const MyPagePostButton = ({ runnerPostId, reviewStatus, isRunner, supporterId }: Props) => {
  const { goToMyPage, goToSupportSelectPage, goToSupporterFeedbackPage } = usePageRouter();
  const { getToken } = useToken();
  const { showCompletionToast, showErrorToast } = useContext(ToastContext);

  const token = useMemo(() => getToken()?.value, [getToken]);

  const cancelReview = () => {
    if (!token) {
      showErrorToast(TOAST_ERROR_MESSAGE.NO_TOKEN);
      return;
    }

    patchRequest(`/posts/runner/${runnerPostId}/cancelation`, token)
      .then(() => {
        showCompletionToast(TOAST_COMPLETION_MESSAGE.REVIEW_CANCEL);

        setTimeout(window.location.reload, 2000);
      })
      .catch((error: Error) => showErrorToast({ description: error.message, title: ERROR_TITLE.REQUEST }));
  };

  const finishReview = () => {
    if (!token) {
      showErrorToast(TOAST_ERROR_MESSAGE.NO_TOKEN);
      return;
    }

    patchRequest(`/posts/runner/${runnerPostId}/done`, token)
      .then(() => {
        showCompletionToast(TOAST_COMPLETION_MESSAGE.REVIEW_COMPETE);
        setTimeout(window.location.reload, 2000);
      })
      .catch((error: Error) => showErrorToast({ description: error.message, title: ERROR_TITLE.REQUEST }));
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
        <Button
          colorTheme="WHITE"
          fontWeight={700}
          width="180px"
          height="40px"
          onClick={isRunner ? handleClickSupportSelectButton : handleClickCancelReviewButton}
        >
          {isRunner ? '서포터 선택하기' : '리뷰 제안 취소'}
        </Button>
      );
    case 'IN_PROGRESS':
      return isRunner ? null : (
        <Button colorTheme="WHITE" fontWeight={700} width="180px" height="40px" onClick={handleClickFinishButton}>
          리뷰 완료
        </Button>
      );
    case 'DONE':
      return isRunner ? (
        <Button
          colorTheme="WHITE"
          fontWeight={700}
          width="180px"
          height="40px"
          onClick={handleClickSupportFeedbackButton}
        >
          후기 작성
        </Button>
      ) : null;
    default:
      return null;
  }
};

export default MyPagePostButton;
