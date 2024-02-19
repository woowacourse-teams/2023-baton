import { getPostCount } from '@/apis/apis';
import { PostCount } from '@/types/runnerPost';
import { useQuery } from '@tanstack/react-query';

export const usePostCount = () => {
  const queryResult = useQuery<PostCount>({
    queryKey: ['runnerPostCount'],

    queryFn: () => getPostCount().then((res) => res.data),
  });

  return {
    ...queryResult,
    data: queryResult.data,
  };
};
