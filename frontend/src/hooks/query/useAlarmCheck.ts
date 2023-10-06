import { useContext } from 'react';
import { ToastContext } from '@/contexts/ToastContext';
import { useMutation } from '@tanstack/react-query';
import { APIError } from '@/types/error';
import { patchAlarmCheck } from '@/apis/apis';
import { queryClient } from './queryClient';

export const useAlarmCheck = () => {
  const queryResult = useMutation<void, APIError, number>({
    mutationFn: (alarmId: number) => patchAlarmCheck(alarmId),

    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['alarm'] });
    },

    retry: 1,
  });

  return queryResult;
};
