import { patchProposedSupporterSelection } from '@/apis/apis';
import { ERROR_TITLE, TOAST_COMPLETION_MESSAGE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { APIError } from '@/types/error';
import { useMutation } from '@tanstack/react-query';
import { useContext } from 'react';
import { queryClient } from './queryClient';
import { usePageRouter } from '../usePageRouter';

interface MutationArgs {
  runnerPostId: number;
  supporterId: number;
}

export const useSelectionSupporter = () => {
  const { showErrorToast, showCompletionToast } = useContext(ToastContext);
  const { goToMyPage } = usePageRouter();

  const queryResult = useMutation<void, APIError, MutationArgs>({
    mutationFn: async ({ runnerPostId, supporterId }) => patchProposedSupporterSelection(runnerPostId, supporterId),

    onSuccess: () => {
      showCompletionToast(TOAST_COMPLETION_MESSAGE.SUPPORTER_SELECT);
      queryClient.invalidateQueries({ queryKey: ['myRunnerPost', 'IN_PROGRESS'] });
      goToMyPage();
    },

    onError: () => {
      showErrorToast({ title: ERROR_TITLE.REQUEST, description: '서포터 선택에 실패했어요' });
    },

    retry: 1,
  });

  return queryResult;
};
