import { postFeedbackToSupporter } from '@/apis/apis';
import { ERROR_TITLE, TOAST_COMPLETION_MESSAGE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { APIError } from '@/types/error';
import { PostFeedbackRequest } from '@/types/feedback';
import { useContext } from 'react';
import { usePageRouter } from '../usePageRouter';
import { useMutation } from '@tanstack/react-query';
import { queryClient } from './queryClient';

export const useFeedbackToSupporter = () => {
  const { showErrorToast, showCompletionToast } = useContext(ToastContext);
  const { goToRunnerMyPage } = usePageRouter();

  const queryResult = useMutation<void, APIError, PostFeedbackRequest>({
    mutationFn: (formData: PostFeedbackRequest) => postFeedbackToSupporter(formData),

    onSuccess: () => {
      showCompletionToast(TOAST_COMPLETION_MESSAGE.SUBMIT_FEEDBACK);
      queryClient.invalidateQueries({ queryKey: ['myRunnerProfile', 'DONE'] });
      goToRunnerMyPage();
    },

    onError: () => {
      showErrorToast({ title: ERROR_TITLE.REQUEST, description: '피드백 작성에 실패했어요' });
    },
  });

  return queryResult;
};
