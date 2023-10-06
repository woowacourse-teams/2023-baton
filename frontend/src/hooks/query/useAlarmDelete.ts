import { useContext } from 'react';
import { ToastContext } from '@/contexts/ToastContext';
import { useMutation } from '@tanstack/react-query';
import { APIError } from '@/types/error';
import { deleteAlarm } from '@/apis/apis';
import { ERROR_TITLE } from '@/constants/message';
import { queryClient } from './queryClient';

export const useAlarmDelete = () => {
  const { showErrorToast } = useContext(ToastContext);

  const queryResult = useMutation<void, APIError, number>({
    mutationFn: (alarmId: number) => deleteAlarm(alarmId),

    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['alarm'] });
    },

    onError: () => {
      showErrorToast({ title: ERROR_TITLE.REQUEST, description: '알람을 삭제하지 못했어요' });
    },

    retry: 1,
  });

  return queryResult;
};
