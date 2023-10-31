import { getGithubPrList } from '@/apis/githubApis';
import { ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { useQuery } from '@tanstack/react-query';
import { useContext, useEffect } from 'react';

export const useGithubPrList = (userId: string, repoName: string) => {
  const { showErrorToast } = useContext(ToastContext);

  const queryResult = useQuery({
    queryKey: ['githubPrList', userId, repoName],
    queryFn: async () => getGithubPrList(userId, repoName),
    enabled: !!userId && !!repoName,
  });

  useEffect(() => {
    if (queryResult.error) {
      showErrorToast({ title: ERROR_TITLE.NETWORK, description: 'PR 정보를 불러오지 못했어요' });
    }
  }, [queryResult.error]);

  return {
    data: queryResult.data,
  };
};
