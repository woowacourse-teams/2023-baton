import { getPostCount } from '@/apis/apis';
import { PostCount } from '@/types/runnerPost';
import { useQuery, useSuspenseQuery } from '@tanstack/react-query';

export const usePostCount = () => {
  const queryResult = useSuspenseQuery<PostCount>({
    queryKey: ['runnerPostCount'],

    queryFn: () => getPostCount().then((res) => res),
  });

  return {
    ...queryResult,
    data: queryResult.data,
  };
};
