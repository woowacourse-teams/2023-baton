import { getGithubRepoList } from '@/apis/githubApis';
import { ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { useQuery, useSuspenseQuery } from '@tanstack/react-query';
import { useContext, useEffect } from 'react';

export const useGithubRepoList = (userId: string, typingRepoName?: string, enabled?: boolean) => {
  const { showErrorToast } = useContext(ToastContext);

  const queryResult = useSuspenseQuery({
    queryKey: ['githubRepoList', userId, enabled],

    queryFn: async () => getGithubRepoList(userId, 1, enabled ?? true),

    select: (response) => {
      // fork된 저장소일 경우 해당 저장소의 원본 저장소 정보를 반환
      const mappedData = response.data.map((item) => {
        return {
          title: item.title,
          url: `https://github.com/${item.originUser ?? userId}/${item.originRepoName ?? item.title}`,
        };
      });

      return { data: mappedData, isDummy: !enabled };
    },
  });

  useEffect(() => {
    if (queryResult.error) {
      showErrorToast({ title: ERROR_TITLE.NETWORK, description: '저장소 정보를 불러오지 못했어요' });
    }
  }, [queryResult.error]);

  return {
    ...queryResult,
    data: queryResult.data,
  };
};
