import { ACCESS_TOKEN_LOCAL_STORAGE_KEY, BATON_BASE_URL } from '@/constants';
import { queryClient } from '@/hooks/query/queryClient';
import { getRestMinute } from '@/utils/jwt';

export const getAccessToken = () => localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);

const removeAccessToken = () => {
  localStorage.removeItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);
};

export const isLogin = () => Boolean(getAccessToken());

export const logout = () => {
  deleteRefreshToken();

  removeAccessToken();

  queryClient.removeQueries({ queryKey: ['headerProfile'] });
};

const saveAccessToken = (response: Response) => {
  if (!response.ok) {
    removeAccessToken();
    queryClient.resetQueries({ queryKey: ['headerProfile'] });

    return;
  }

  const jwt = response.headers.get('Authorization');

  if (jwt) {
    localStorage.setItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY, jwt);
    queryClient.resetQueries({ queryKey: ['headerProfile'] });
  }
};

export const issueLoginToken = async (authCode: string) => {
  const response = await fetch(`${BATON_BASE_URL}/oauth/login/github?code=${authCode}`, {
    method: 'GET',
    headers: {
      credentials: 'include',
    },
  });

  saveAccessToken(response);

  return response;
};

export const postRefreshToken = async () => {
  const response = await fetch(`${BATON_BASE_URL}/oauth/refresh`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
      credentials: 'include',
    },
  });

  saveAccessToken(response);

  return response;
};

export const deleteRefreshToken = async () => {
  const response = await fetch(`${BATON_BASE_URL}/oauth/logout`, {
    method: 'DELETE',
    headers: {
      Authorization: `Bearer ${getAccessToken()}`,
    },
  });

  return response;
};

export const checkLoginToken = async () => {
  const jwt = getAccessToken();

  if (!jwt || getRestMinute(jwt) > 2) return;

  return await postRefreshToken();
};
