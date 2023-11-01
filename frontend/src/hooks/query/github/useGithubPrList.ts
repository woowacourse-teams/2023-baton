import { getGithubPrList } from '@/apis/githubApis';
import { ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { useQuery, useSuspenseQueries, useSuspenseQuery } from '@tanstack/react-query';
import { useContext, useEffect } from 'react';

export const useGithubPrList = (userId: string, repoName: string, enabled?: boolean) => {
  const { showErrorToast } = useContext(ToastContext);

  const queryResult = useSuspenseQuery({
    queryKey: ['githubPrList', userId, repoName, enabled],

    queryFn: async () => getGithubPrList(userId, repoName, 1, enabled ?? true),

    select: (response) => {
      if (typeof response?.data !== 'object') return { data: [], isDummy: !enabled };

      const mappedData = response.data.map((item) => {
        return { title: item.title, url: 'https://github.com' + item.link };
      });

      return { data: mappedData, isDummy: !enabled };
    },
  });

  useEffect(() => {
    if (queryResult.error) {
      showErrorToast({ title: ERROR_TITLE.NETWORK, description: 'PR 정보를 불러오지 못했어요' });
    }
  }, [queryResult.error]);

  return {
    ...queryResult,
    data: queryResult.data,
  };
};
