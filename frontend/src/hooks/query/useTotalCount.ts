import { useQueries } from '@tanstack/react-query';
import { getRunnerPost } from '@/apis/apis';
import { ReviewStatusFilter } from '@/types/runnerPost';
import { REVIEW_STATUS_FILTER } from '@/constants';

const useTotalCount = () => {
  const reviewStatuses = REVIEW_STATUS_FILTER;

  const queryResults = useQueries({
    queries: reviewStatuses.map((status) => ({
      queryKey: ['runnerPostTotalCount', status],

      queryFn: () => getRunnerPost({ limit: 0, reviewStatus: status }).then((res) => res.pageInfo.totalCount),
    })),
  });

  const isLoaded = queryResults.map((data) => {
    return data.isLoading;
  });

  const isAllLoaded = isLoaded.every((don) => don === false);

  const totalCounts = queryResults.reduce((acc, result, index) => {
    if (result.isSuccess) {
      acc[reviewStatuses[index]] = result.data;
    }

    return acc;
  }, {} as { [key in ReviewStatusFilter]?: number });

  return { totalCounts, isAllLoaded };
};

export default useTotalCount;
