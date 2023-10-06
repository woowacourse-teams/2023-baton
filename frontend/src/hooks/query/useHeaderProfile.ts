import { getHeaderProfile } from '@/apis/apis';
import { useQuery } from '@tanstack/react-query';

export const useHeaderProfile = (isLogin: boolean) => {
  const queryResult = useQuery({
    queryKey: ['headerProfile'],
    queryFn: getHeaderProfile,
    enabled: isLogin,
  });

  return {
    data: queryResult.data as NonNullable<typeof queryResult.data>,
  };
};
