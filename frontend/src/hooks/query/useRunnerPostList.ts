import { getRunnerPost } from '@/apis/apis';
import { ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { APIError } from '@/types/error';
import { GetRunnerPostResponse, ReviewStatus, RunnerPost } from '@/types/runnerPost';
import { useInfiniteQuery } from '@tanstack/react-query';
import { useContext, useEffect } from 'react';

const PAGE_LIMIT = 10;
const DEFAULT_REVIEW_STATUS = 'ALL';

export const useRunnerPostList = (reviewStatus: ReviewStatus | null, tagName?: string) => {
  const { showErrorToast } = useContext(ToastContext);

  const queryResult = useInfiniteQuery<
    GetRunnerPostResponse,
    APIError,
    RunnerPost[],
    [string, string, typeof tagName],
    number
  >({
    queryKey: ['runnerPost', reviewStatus ?? DEFAULT_REVIEW_STATUS, tagName],

    queryFn: ({ pageParam }) =>
      getRunnerPost({ limit: PAGE_LIMIT, reviewStatus, cursor: pageParam, tagName }).then((res) => res),

    initialPageParam: 0,

    getNextPageParam: (nextPage) => {
      if (nextPage.pageInfo.isLast) return undefined;

      return nextPage.pageInfo.nextCursor;
    },

    select: ({ pages }) => pages.reduce<RunnerPost[]>((acc, { data }) => acc.concat(data), []),
  });

  useEffect(() => {
    if (queryResult.error) {
      showErrorToast({ title: ERROR_TITLE.NETWORK, description: queryResult.error.message });
    }
  }, [queryResult.error]);

  return {
    ...queryResult,
    data: queryResult.data ?? [],
  };
};
