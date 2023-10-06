import { getAlram } from '@/apis/apis';
import { ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { GetAlarmResponse } from '@/types/alarm';
import { APIError } from '@/types/error';
import { useSuspenseQuery } from '@tanstack/react-query';
import { useContext, useEffect } from 'react';

export const useAlarm = (isLogin: boolean) => {
  const { showErrorToast } = useContext(ToastContext);

  const queryResult = useSuspenseQuery<GetAlarmResponse, APIError>({
    queryKey: ['alarm'],
    queryFn: () => getAlram(isLogin),
  });

  useEffect(() => {
    if (queryResult.error) {
      showErrorToast({ title: ERROR_TITLE.NETWORK, description: '알람을 불러오지 못했어요' });
    }
  }, [queryResult.error]);

  return {
    data: queryResult.data as NonNullable<typeof queryResult.data>,
  };
};
