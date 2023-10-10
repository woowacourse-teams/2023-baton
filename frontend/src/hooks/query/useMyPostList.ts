import { getMyRunnerPost, getMySupporterPost } from '@/apis/apis';
import { ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { APIError } from '@/types/error';
import { GetMyPagePostResponse, MyPagePost } from '@/types/myPage';
import { ReviewStatus } from '@/types/runnerPost';
import { useInfiniteQuery, useSuspenseInfiniteQuery } from '@tanstack/react-query';
import { useContext, useEffect } from 'react';

const PAGE_SIZE = 10;

/*
 * 러너, 서포터 정보 통합 시 query key값 통합 필요 (ex myRunnerPost => myPost)
 */
export const useMyPostList = (isRunner: boolean, reviewStatus?: ReviewStatus) => {
  const key = isRunner ? 'myRunnerPost' : 'mySupporterPost';

  const { showErrorToast } = useContext(ToastContext);

  const queryResult = useInfiniteQuery<
    GetMyPagePostResponse,
    APIError,
    MyPagePost[],
    [string, typeof reviewStatus],
    number
  >({
    queryKey: [key, reviewStatus],

    queryFn: ({ pageParam }) => {
      return isRunner
        ? getMyRunnerPost({ limit: PAGE_SIZE, cursor: pageParam, reviewStatus })
        : getMySupporterPost({ limit: PAGE_SIZE, cursor: pageParam, reviewStatus });
    },

    initialPageParam: 0,

    getNextPageParam: (nextPage) => {
      if (nextPage.pageInfo.isLast) return undefined;

      return nextPage.pageInfo.nextCursor;
    },

    select: ({ pages }) => pages.reduce<MyPagePost[]>((acc, { data }) => acc.concat(data), []),
  });

  useEffect(() => {
    if (queryResult.error) {
      showErrorToast({ title: ERROR_TITLE.NETWORK, description: '내 게시물 정보를 불러오지 못했어요' });
    }
  }, [queryResult.error]);

  return {
    ...queryResult,
    data: queryResult.data as NonNullable<typeof queryResult.data>,
  };
};
