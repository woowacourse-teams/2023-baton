import { patchRequest } from '@/api/fetch';
import Button from '@/components/common/Button/Button';
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
      showErrorToast({ title: '권한 오류', description: '로그인이 필요합니다.' });
      return;
    }

    patchRequest(`/posts/runner/${runnerPostId}/cancelation`, token)
      .then(() => {
        showCompletionToast({ title: '취소 완료', description: '리뷰 제안을 취소했습니다.' });
        goToMyPage();
      })
      .catch((error: Error) => {
        showErrorToast({ title: '요청 실패', description: error.message });
      });
  };

  const finishReview = () => {
    if (!token) {
      showErrorToast({ title: '권한 오류', description: '로그인이 필요합니다.' });
      return;
    }

    patchRequest(`/posts/runner/${runnerPostId}/done`, token)
      .then(() => {
        showCompletionToast({ title: '리뷰 완료', description: '리뷰가 완료되었습니다.' });
        goToMyPage();
      })
      .catch((error: Error) => {
        showErrorToast({ title: '요청 실패', description: error.message });
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
      showErrorToast({ title: '요청 실패', description: '서포터 정보가 없습니다.' });
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
    case 'IN_PROGRESS': // 코드 보러가기 기능 추가 필요
      return isRunner ? null : (
        <Button colorTheme="WHITE" fontWeight={700} width="180px" height="40px" onClick={handleClickFinishButton}>
          리뷰 완료
        </Button>
      );
    case 'DONE': // 러너 피드백 기능 추가 필요
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
