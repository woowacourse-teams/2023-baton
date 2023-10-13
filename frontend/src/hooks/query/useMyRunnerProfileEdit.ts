import { useContext } from 'react';
import { ToastContext } from '@/contexts/ToastContext';
import { useMutation } from '@tanstack/react-query';
import { APIError } from '@/types/error';
import { patchMyRunnerProfile } from '@/apis/apis';
import { ERROR_TITLE, TOAST_COMPLETION_MESSAGE } from '@/constants/message';
import { queryClient } from './queryClient';
import { PatchRunnerProfileRequest } from '@/types/profile';

export const useRunnerProfileEdit = () => {
  const { showErrorToast, showCompletionToast } = useContext(ToastContext);

  const queryResult = useMutation<void, APIError, PatchRunnerProfileRequest>({
    mutationFn: (formData: PatchRunnerProfileRequest) => patchMyRunnerProfile(formData),

    onSuccess: () => {
      showCompletionToast(TOAST_COMPLETION_MESSAGE.SAVE);
      queryClient.invalidateQueries({ queryKey: ['myRunnerProfile'] });
    },

    onError: () => {
      showErrorToast({ title: ERROR_TITLE.REQUEST, description: '프로필 수정에 실패했어요' });
    },
  });

  return queryResult;
};
