import { patchProposedSupporterSelection, postReviewSuggestionWithMessage } from '@/apis/apis';
import { ERROR_TITLE, TOAST_COMPLETION_MESSAGE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { APIError } from '@/types/error';
import { useMutation } from '@tanstack/react-query';
import { useContext } from 'react';
import { queryClient } from './queryClient';
import { usePageRouter } from '../usePageRouter';

interface MutationArgs {
  runnerPostId: number;
  message: string;
}

export const useReviewSuggestion = () => {
  const { showErrorToast, showCompletionToast } = useContext(ToastContext);
  const { goToMyPage } = usePageRouter();

  const queryResult = useMutation<void, APIError, MutationArgs>({
    mutationFn: async ({ runnerPostId, message }) => {
      postReviewSuggestionWithMessage(runnerPostId, message);
    },

    onSuccess: () => {
      showCompletionToast(TOAST_COMPLETION_MESSAGE.SUBMISSION);
      queryClient.invalidateQueries({ queryKey: ['mySupporterPost', 'NOT_STARTED'] });
      goToMyPage();
    },

    onError: () => {
      showErrorToast({ title: ERROR_TITLE.REQUEST, description: '리뷰 제안 요청이 실패했어요' });
    },

    retry: 1,
  });

  return queryResult;
};
