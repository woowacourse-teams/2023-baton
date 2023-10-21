import { getSearchTag } from '@/apis/apis';
import { useSuspenseQuery } from '@tanstack/react-query';

export const useSearchTag = (keyword: string) => {
  const queryResult = useSuspenseQuery({
    queryKey: ['searchTag', keyword],
    queryFn: async () => (await getSearchTag(keyword)).data,
  });

  return {
    data: queryResult.data,
  };
};
