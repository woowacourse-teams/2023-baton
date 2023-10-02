import { useContext } from 'react';
import { ToastContext } from '@/contexts/ToastContext';
import { useMutation } from '@tanstack/react-query';
import { APIError } from '@/types/error';
import { patchReviewComplete, postMissionBranchCreation } from '@/apis/apis';
import { ERROR_TITLE, TOAST_COMPLETION_MESSAGE } from '@/constants/message';
import { queryClient } from './queryClient';

export const useMissionBranchCreation = () => {
  const { showErrorToast, showCompletionToast } = useContext(ToastContext);

  const queryResult = useMutation<void, APIError, string>({
    mutationFn: async (repoName: string) => {
      postMissionBranchCreation(repoName);
    },

    onSuccess: () => {
      showCompletionToast(TOAST_COMPLETION_MESSAGE.REPO_COMPLETE);
    },

    onError: () => {
      showErrorToast({ title: ERROR_TITLE.REQUEST, description: '브랜치 생성에 실패했어요' });
    },

    retry: 1,
  });

  return queryResult;
};
