import { getAlram } from '@/apis/apis';
import { Alarm, GetAlarmResponse } from '@/types/alarm';
import { APIError } from '@/types/error';
import { useSuspenseQuery } from '@tanstack/react-query';

export const useAlarm = (isLogin: boolean) => {
  const queryResult = useSuspenseQuery<GetAlarmResponse, APIError>({
    queryKey: ['alarm'],
    queryFn: () => getAlram(isLogin),
  });

  return {
    data: queryResult.data as NonNullable<typeof queryResult.data>,
  };
};
