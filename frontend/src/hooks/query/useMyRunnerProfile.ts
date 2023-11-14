import { getMyRunnerProfile } from '@/apis/apis';
import { ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { APIError } from '@/types/error';
import { GetRunnerProfileResponse } from '@/types/profile';
import { useSuspenseQuery } from '@tanstack/react-query';
import { useContext, useEffect } from 'react';

export const useMyRunnerProfile = () => {
  const { showErrorToast } = useContext(ToastContext);

  const queryResult = useSuspenseQuery<GetRunnerProfileResponse, APIError>({
    queryKey: ['myRunnerProfile'],
    queryFn: getMyRunnerProfile,
  });

  useEffect(() => {
    if (queryResult.error) {
      showErrorToast({ title: ERROR_TITLE.NETWORK, description: '프로필 정보를 불러오지 못했어요' });
    }
  }, [queryResult.error]);

  return {
    data: queryResult.data,
  };
};
