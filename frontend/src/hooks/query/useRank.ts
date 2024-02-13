import { getSupporterRank } from '@/apis/apis';
import { ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { GetSupporterRankResponse } from '@/types/rank';
import { useQuery } from '@tanstack/react-query';
import { useContext, useEffect } from 'react';

export const useRank = () => {
  const { showErrorToast } = useContext(ToastContext);

  const queryResult = useQuery<GetSupporterRankResponse>({
    queryKey: ['supporterRank'],

    queryFn: () => getSupporterRank().then((res) => res),
  });

  useEffect(() => {
    if (queryResult.error) {
      showErrorToast({ title: ERROR_TITLE.NETWORK, description: queryResult.error.message });
    }
  }, [queryResult.error]);

  return {
    ...queryResult,
    data: queryResult.data,
  };
};
