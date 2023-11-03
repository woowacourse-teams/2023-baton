import { getMyRunnerProfile } from '@/apis/apis';
import { ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { APIError } from '@/types/error';
import { GetRunnerProfileResponse } from '@/types/profile';
import { useQuery } from '@tanstack/react-query';
import { useContext, useEffect } from 'react';

interface GithubInfo {
  githubId: string;
  githubUrl: string;
}

export const useMyGithubInfo = () => {
  const { showErrorToast } = useContext(ToastContext);

  /* githubUrl은 수정할 수 없으므로 staleTime을 Infinity로 설정 */
  const queryResult = useQuery<GetRunnerProfileResponse, APIError, GithubInfo>({
    queryKey: ['myGithubInfo'],

    queryFn: getMyRunnerProfile,

    select: (data) => {
      return {
        githubUrl: data.githubUrl,
        githubId: data.githubUrl.split('/').slice(-1)[0],
      };
    },

    staleTime: Infinity,
  });

  useEffect(() => {
    if (queryResult.error) {
      showErrorToast({ title: ERROR_TITLE.NETWORK, description: '내 깃허브 정보를 불러오지 못했어요' });
    }
  }, [queryResult.error]);

  return {
    ...queryResult,
    data: queryResult.data,
  };
};
