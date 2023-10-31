import { getGithubRepoList } from '@/apis/githubApis';
import { ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { useQuery } from '@tanstack/react-query';
import { useContext, useEffect } from 'react';

export const useGithubRepoList = (userId: string, typingRepoName?: string) => {
  const { showErrorToast } = useContext(ToastContext);

  const queryResult = useQuery({
    queryKey: ['githubRepoList', userId],

    queryFn: async () => getGithubRepoList(userId),

    select: (response) => {
      // fork된 저장소만을 골라 해당 저장소의 원본 저장소 정보를 반환
      const mappedData = response.data
        .filter((item) => item.isForked)
        .map((item) => {
          return { title: item.title, url: `https://github.com/${item.originUser}/${item.originRepoName}` };
        });

      if (!typingRepoName) return mappedData;

      const searchedData = mappedData.filter((item) => item.url.includes(typingRepoName));

      return searchedData;
    },

    enabled: !!userId,
  });

  useEffect(() => {
    if (queryResult.error) {
      showErrorToast({ title: ERROR_TITLE.NETWORK, description: '저장소 정보를 불러오지 못했어요' });
    }
  }, [queryResult.error]);

  return {
    data: queryResult.data,
  };
};
