import { getOtherSupporterPost } from '@/apis/apis';
import { ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { APIError } from '@/types/error';
import { GetRunnerPostResponse, RunnerPost } from '@/types/runnerPost';
import { useInfiniteQuery, useQuery } from '@tanstack/react-query';
import { useContext, useEffect } from 'react';

const PAGE_LIMIT = 10;

export const useOtherSupporterPost = (userId: number) => {
  const { showErrorToast } = useContext(ToastContext);

  const queryResult = useInfiniteQuery<GetRunnerPostResponse, APIError, RunnerPost[], [string, typeof userId], number>({
    queryKey: ['otherSupporterPost', userId],

    queryFn: ({ pageParam }) => getOtherSupporterPost({ limit: PAGE_LIMIT, supporterId: userId, cursor: pageParam }),

    initialPageParam: 0,

    getNextPageParam: (nextPage) => {
      if (nextPage.pageInfo.isLast) return undefined;

      return nextPage.pageInfo.nextCursor;
    },

    select: ({ pages }) => pages.reduce<RunnerPost[]>((acc, { data }) => acc.concat(data), []),
  });

  useEffect(() => {
    if (queryResult.error) {
      showErrorToast({ title: ERROR_TITLE.NETWORK, description: '해당 유저의 게시물 정보를 불러오지 못했어요' });
    }
  }, [queryResult.error]);

  return {
    ...queryResult,
    data: queryResult.data,
  };
};
