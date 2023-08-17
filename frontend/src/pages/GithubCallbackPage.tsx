import { ACCESS_TOKEN_LOCAL_STORAGE_KEY } from '@/constants';
import { useToken } from '@/hooks/useToken';
import { usePageRouter } from '@/hooks/usePageRouter';
import React, { useEffect } from 'react';
import { useLocation } from 'react-router-dom';
import { getRequest } from '@/api/fetch';

function GithubCallbackPage() {
  const location = useLocation();

  const { goToMainPage, goToLoginPage } = usePageRouter();
  const { saveToken } = useToken();

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

  const getToken = (code: string) => {
    if (localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY) === null) {
      getRequest(`/oauth/login/github?code=${code}`)
        .then(async (response) => {
          const jwt = await response.headers.get('Authorization');

          if (!jwt) {
            return alert('토큰을 받아오지 못했습니다.');
          }

          saveToken(jwt);
          goToMainPage();
        })
        .catch((error: Error) => {
          alert(error.message);

          goToLoginPage();
        });
    }
  };

  return <div>GithubRedirect...</div>;
}

export default GithubCallbackPage;
