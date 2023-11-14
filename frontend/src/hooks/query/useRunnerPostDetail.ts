import { getRunnerPostDetail } from '@/apis/apis';
import { ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { APIError } from '@/types/error';
import { GetDetailedRunnerPostResponse } from '@/types/runnerPost';
import { useSuspenseQuery } from '@tanstack/react-query';
import { useContext, useEffect } from 'react';

export const useRunnerPostDetail = (runnerPostId: number, isLogin: boolean) => {
  const { showErrorToast } = useContext(ToastContext);

  const queryResult = useSuspenseQuery<GetDetailedRunnerPostResponse, APIError>({
    queryKey: ['runnerPostDetail', runnerPostId],
    queryFn: async () => getRunnerPostDetail(runnerPostId, isLogin).then((res) => res),
  });

  useEffect(() => {
    if (queryResult.error) {
      showErrorToast({ title: ERROR_TITLE.NETWORK, description: '게시물 정보를 불러오지 못했어요' });
    }
  }, [queryResult.error]);

  return {
    data: queryResult.data,
  };
};
