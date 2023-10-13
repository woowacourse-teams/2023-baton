import { getProposedSupporterList } from '@/apis/apis';
import { ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { APIError } from '@/types/error';
import { Candidate } from '@/types/supporterCandidate';
import { useQuery } from '@tanstack/react-query';
import { useContext, useEffect } from 'react';

export const useProposedSupporterList = (runnerPostId: number) => {
  const { showErrorToast } = useContext(ToastContext);

  const queryResult = useQuery<Candidate[], APIError, Candidate[]>({
    queryKey: ['proposedSupporterList', runnerPostId],
    queryFn: () => getProposedSupporterList(runnerPostId).then((res) => res.data),
  });

  useEffect(() => {
    if (queryResult.error) {
      showErrorToast({ title: ERROR_TITLE.NETWORK, description: '제안한 서포터 목록을 불러오지 못했어요' });
    }
  }, [queryResult.error]);

  return {
    data: queryResult.data as NonNullable<typeof queryResult.data>,
  };
};
