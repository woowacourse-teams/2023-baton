import { getOtherSupporterPost } from '@/apis/apis';
import { ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { APIError } from '@/types/error';
import { GetRunnerPostResponse } from '@/types/runnerPost';
import { useQuery } from '@tanstack/react-query';
import { useContext, useEffect } from 'react';

export const useOtherSupporterPost = (userId: number) => {
  const { showErrorToast } = useContext(ToastContext);

  const queryResult = useQuery<GetRunnerPostResponse, APIError>({
    queryKey: ['otherSupporterPost', userId],
    queryFn: () => getOtherSupporterPost(userId).then((res) => res),
  });

  useEffect(() => {
    if (queryResult.error) {
      showErrorToast({ title: ERROR_TITLE.NETWORK, description: '해당 유저의 게시물 정보를 불러오지 못했어요' });
    }
  }, [queryResult.error]);

  return {
    data: queryResult.data as NonNullable<typeof queryResult.data>,
  };
};
