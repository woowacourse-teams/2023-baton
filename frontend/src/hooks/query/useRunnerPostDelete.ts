import { useContext } from 'react';
import { ToastContext } from '@/contexts/ToastContext';
import { useMutation } from '@tanstack/react-query';
import { APIError } from '@/types/error';
import { deleteRunnerPost } from '@/apis/apis';
import { ERROR_TITLE, TOAST_COMPLETION_MESSAGE } from '@/constants/message';
import { queryClient } from './queryClient';
import { usePageRouter } from '../usePageRouter';

export const useRunnerPostDelete = () => {
  const { goToMainPage } = usePageRouter();
  const { showErrorToast, showCompletionToast } = useContext(ToastContext);

  const queryResult = useMutation<void, APIError, number>({
    mutationFn: (runnerPostId: number) => deleteRunnerPost(runnerPostId),

    onSuccess: () => {
      showCompletionToast(TOAST_COMPLETION_MESSAGE.DELETE);
      queryClient.invalidateQueries({ queryKey: ['runnerPost'] });
      goToMainPage();
    },

    onError: () => {
      showErrorToast({ title: ERROR_TITLE.REQUEST, description: '게시물 삭제 요청이 실패했어요' });
    },
  });

  return queryResult;
};
