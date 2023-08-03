import { ACCESS_TOKEN_LOCAL_STORAGE_KEY, BATON_BASE_URL } from '@/constants';
import { useToken } from '@/hooks/useToken';
import { usePageRouter } from '@/hooks/usePageRouter';
import React, { useEffect } from 'react';
import { useLocation } from 'react-router-dom';

function GithubCallbackPage() {
  const location = useLocation();

  const { goToMainPage, goToLoginPage } = usePageRouter();
  const { saveToken } = useToken();
  const getToken = async (code: string) => {
    if (localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY) === null) {
      try {
        const response = await fetch(`${BATON_BASE_URL}/oauth/login/github?code=${code}`, {
          method: 'GET',
        });

        const jwt = response.headers.get('Authorization');
        if (!jwt) {
          throw new Error('토큰을 받아오지 못했습니다.');
        }

        saveToken(jwt);

        goToMainPage();
      } catch (error) {
        goToLoginPage();
      }
    }
  };

  useEffect(() => {
    const searchParams = new URLSearchParams(location.search);
    const code = searchParams.get('code');
    const error = searchParams.get('error');

    if (error) {
      alert('로그인 실패!');
      goToLoginPage();
    }

    if (code) {
      getToken(code);
    }
  }, [location]);

  return <div>GithubRedirect...</div>;
}

export default GithubCallbackPage;
