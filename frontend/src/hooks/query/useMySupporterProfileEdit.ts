import { useContext } from 'react';
import { ToastContext } from '@/contexts/ToastContext';
import { useMutation } from '@tanstack/react-query';
import { APIError } from '@/types/error';
import { patchMyRunnerProfile } from '@/apis/apis';
import { ERROR_TITLE, TOAST_COMPLETION_MESSAGE } from '@/constants/message';
import { usePageRouter } from '../usePageRouter';
import { queryClient } from './queryClient';
import { PatchSupporterProfileRequest } from '@/types/profile';

export const useRunnerProfileEdit = () => {
  const { showErrorToast, showCompletionToast } = useContext(ToastContext);
  const { goToMainPage } = usePageRouter();

  const queryResult = useMutation<void, APIError, PatchSupporterProfileRequest>({
    mutationFn: async (formData: PatchSupporterProfileRequest) => {
      patchMyRunnerProfile(formData);
    },

    onSuccess: () => {
      showCompletionToast(TOAST_COMPLETION_MESSAGE.SAVE);
      queryClient.invalidateQueries({ queryKey: ['mySupporterProfile'] });
      goToMainPage();
    },

    onError: () => {
      showErrorToast({ title: ERROR_TITLE.REQUEST, description: '프로필 수정에 실패했어요' });
    },

    retry: 1,
  });

  return queryResult;
};
