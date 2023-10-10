import Button from '@/components/common/Button/Button';
import { usePageRouter } from '@/hooks/usePageRouter';
import React, { useContext, useEffect } from 'react';
import styled from 'styled-components';
import notFound from '@/assets/not-found.jpg';
import { logout } from '@/apis/auth';
import { CustomApiError } from '@/types/error';
import ToastProvider, { ToastContext } from '@/contexts/ToastContext';

interface Props {
  reset?: () => void;
  error?: Error | CustomApiError;
}

const NotFoundPage = ({ reset, error }: Props) => {
  const { goToMainPage } = usePageRouter();
  const { showErrorToast } = useContext(ToastContext);

  useEffect(() => {
    if (error) {
      console.log(error);
      showErrorToast({ title: '요청 오류', description: error.message });
    }
  }, []);

  const handleClickHomeButton = () => {
    if (reset) {
      logout();
      reset();
    }

    window.location.href = '/';
  };

  return (
    <S.NotFoundPageContainer>
      <S.NotFoundImage src={notFound} />
      <S.Message>
        <S.Bold>페이지를 찾을 수 없습니다 😢</S.Bold>
      </S.Message>
      <Button colorTheme="WHITE" onClick={handleClickHomeButton}>
        홈으로 가기
      </Button>
    </S.NotFoundPageContainer>
  );
};

export default NotFoundPage;

const S = {
  NotFoundPageContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 50px;

    margin-top: 10vh;
  `,

  NotFoundImage: styled.img`
    height: 40vh;
  `,

  Message: styled.h1`
    font-size: 30px;
    line-height: 1.5;
    white-space: pre-wrap;
    text-align: center;

    @media (max-width: 768px) {
      font-size: 24px;
    }
  `,

  Bold: styled.span`
    font-weight: 700;
  `,
};
