import { useContext } from 'react';
import { ToastContext } from '@/contexts/ToastContext';
import { useMutation } from '@tanstack/react-query';
import { APIError } from '@/types/error';
import { patchReviewComplete } from '@/apis/apis';
import { ERROR_TITLE, TOAST_COMPLETION_MESSAGE } from '@/constants/message';
import { queryClient } from './queryClient';

export const useReviewComplete = () => {
  const { showErrorToast, showCompletionToast } = useContext(ToastContext);

  const queryResult = useMutation<void, APIError, number>({
    mutationFn: async (runnerPostId: number) => {
      patchReviewComplete(runnerPostId);
    },

    onSuccess: () => {
      showCompletionToast(TOAST_COMPLETION_MESSAGE.REVIEW_COMPLETE);
      queryClient.invalidateQueries({ queryKey: ['mySupporterPost', 'IN_PROGRESS'] });
    },

    onError: () => {
      showErrorToast({ title: ERROR_TITLE.REQUEST, description: '리뷰 완료 요청이 실패했어요' });
    },

    retry: 1,
  });

  return queryResult;
};
