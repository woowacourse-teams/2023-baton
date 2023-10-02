import { getOtherSupporterProfile } from '@/apis/apis';
import { ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { APIError } from '@/types/error';
import { GetSupporterProfileResponse } from '@/types/profile';
import { useQuery } from '@tanstack/react-query';
import { useContext, useEffect } from 'react';

export const useOtherSupporterProfile = (userId: number) => {
  const { showErrorToast } = useContext(ToastContext);

  const queryResult = useQuery<GetSupporterProfileResponse, APIError>({
    queryKey: ['otherSupporterProfile', userId],
    queryFn: async () => getOtherSupporterProfile(userId).then((res) => res),
  });

  useEffect(() => {
    if (queryResult.error) {
      showErrorToast({ title: ERROR_TITLE.NETWORK, description: '프로필 정보를 불러오지 못했어요' });
    }
  }, [queryResult.error]);

  return {
    data: queryResult.data as NonNullable<typeof queryResult.data>,
  };
};
