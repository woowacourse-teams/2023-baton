import { getOtherSupporterPostCount } from '@/apis/apis';
import { APIError } from '@/types/error';
import { GetOtherSupporterPostCountResponse } from '@/types/runnerPost';
import { useQuery } from '@tanstack/react-query';

export const useOtherSupporterPostCount = (supporterId: number) => {
  const queryResult = useQuery<GetOtherSupporterPostCountResponse, APIError>({
    queryKey: ['otherSupporterPostCount', supporterId],
    queryFn: () => getOtherSupporterPostCount(supporterId),
  });

  return {
    data: queryResult.data as NonNullable<typeof queryResult.data>,
  };
};
