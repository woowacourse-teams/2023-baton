import { ACCESS_TOKEN_LOCAL_STORAGE_KEY } from '@/constants';

export interface LoginToken {
  value: string;
  expirationDate: Date;
}

export const useLogin = () => {
  const getToken = (): LoginToken | null => {
    try {
      const item = localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);

      if (!item) {
        throw new Error('토큰이 존재하지 않습니다!');
      }

      return JSON.parse(item);
    } catch {
      return null;
    }
  };

  const saveToken = (token: string) => {
    const date = new Date();
    date.setDate(date.getDate() + 30);

    localStorage.setItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY, JSON.stringify({ value: token, expirationDate: date }));
  };

  const removeToken = () => {
    localStorage.removeItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);
  };

  const validateToken = () => {
    const item = localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY);
    if (!item) return;

    const token: LoginToken = JSON.parse(item);
    const time = new Date(token.expirationDate);

    if (Number(time) - Date.now() < 0) {
      removeToken();

      alert('로그인 토큰 유효기간이 지났습니다. 다시 로그인해 주세요');
    }
  };

  return { getToken, removeToken, saveToken, validateToken };
};
