import { getNotification } from '@/apis/apis';
import { GetNotificationResponse } from '@/types/notification';
import { APIError } from '@/types/error';
import { useSuspenseQuery } from '@tanstack/react-query';

export const useNotification = () => {
  const queryResult = useSuspenseQuery<GetNotificationResponse, APIError>({
    queryKey: ['notification'],
    queryFn: getNotification,
    refetchInterval: 60 * 1000,
  });

  return {
    data: queryResult.data,
  };
};
