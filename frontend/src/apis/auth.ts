import { ACCESS_TOKEN_LOCAL_STORAGE_KEY, BATON_BASE_URL } from '@/constants';
import { getRestMinute } from '@/utils/jwt';

export const getAccessToken = () => localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);

export const saveAccessToken = (response: Response) => {
  const jwt = response.headers.get('Authorization');

  if (jwt) {
    localStorage.setItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY, jwt);

    return;
  }

  localStorage.removeItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);
};

export const issueLoginToken = async (authCode: string) => {
  const response = await fetch(`${BATON_BASE_URL}/oauth/login/github?code=${authCode}`, {
    method: 'GET',
    headers: {
      credentials: 'include',
    },
  });

  saveAccessToken(response);
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
};

export const checkLoginToken = async () => {
  const jwt = getAccessToken();

  if (!jwt || getRestMinute(jwt) > 2) return;

  return await postRefreshToken();
};
