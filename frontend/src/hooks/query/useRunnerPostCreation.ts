import { useContext } from 'react';
import { ToastContext } from '@/contexts/ToastContext';
import { useMutation } from '@tanstack/react-query';
import { APIError } from '@/types/error';
import { CreateRunnerPostRequest } from '@/types/runnerPost';
import { postRunnerPostCreation } from '@/apis/apis';
import { ERROR_TITLE, TOAST_COMPLETION_MESSAGE } from '@/constants/message';
import { usePageRouter } from '../usePageRouter';
import { queryClient } from './queryClient';

export const useRunnerPostCreation = () => {
  const { showErrorToast, showCompletionToast } = useContext(ToastContext);
  const { goToResultPage } = usePageRouter();

  const queryResult = useMutation<void, APIError, CreateRunnerPostRequest>({
    mutationFn: async (formData: CreateRunnerPostRequest) => {
      postRunnerPostCreation(formData);
    },

    onSuccess: () => {
      showCompletionToast(TOAST_COMPLETION_MESSAGE.CREATE_POST);
      queryClient.invalidateQueries({ queryKey: ['runnerPost'] });
      queryClient.invalidateQueries({ queryKey: ['myRunnerPost', 'NOT_STARTED'] });
      goToResultPage();
    },

    onError: () => {
      showErrorToast({ title: ERROR_TITLE.REQUEST, description: '게시물 등록에 실패했어요' });
    },

    retry: 1,
  });

  return queryResult;
};
