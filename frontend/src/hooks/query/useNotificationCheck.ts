import { useMutation } from '@tanstack/react-query';
import { APIError } from '@/types/error';
import { patchNotificationCheck } from '@/apis/apis';
import { queryClient } from './queryClient';

export const useNotificationCheck = () => {
  const queryResult = useMutation<void, APIError, number>({
    mutationFn: (notificationId: number) => patchNotificationCheck(notificationId),

    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['notification'] });
    },

    retry: 1,
  });

  return queryResult;
};
