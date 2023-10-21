import { getHeaderProfile } from '@/apis/apis';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { useQuery } from '@tanstack/react-query';
import { useContext, useEffect } from 'react';
import { usePageRouter } from '../usePageRouter';
import { isLogin } from '@/apis/auth';
import { queryClient } from './queryClient';

export const useHeaderProfile = () => {
  const { showErrorToast } = useContext(ToastContext);
  const { goToLoginPage } = usePageRouter();

  const queryResult = useQuery({
    queryKey: ['headerProfile'],
    queryFn: getHeaderProfile,
    enabled: isLogin(),
  });

  useEffect(() => {
    if (queryResult.error) {
      showErrorToast({ title: ERROR_TITLE.EXPIRATION, description: ERROR_DESCRIPTION.TOKEN_EXPIRATION });
      goToLoginPage();

      queryClient.removeQueries({ queryKey: ['headerProfile'] });
    }
  }, [queryResult.error]);

  return {
    data: queryResult.data,
  };
};
