import { getNotification } from '@/apis/apis';
import { GetNotificationResponse } from '@/types/notification';
import { APIError } from '@/types/error';
import { useSuspenseQuery } from '@tanstack/react-query';

export const useNotification = () => {
  const queryResult = useSuspenseQuery<GetNotificationResponse, APIError>({
    queryKey: ['notification'],
    queryFn: getNotification,
  });

  return {
    data: queryResult.data as NonNullable<typeof queryResult.data>,
  };
};
