import { getRequest } from '@/api/fetch';
import RunnerPostList from '@/components/RunnerPost/RunnerPostList/RunnerPostList';
import Button from '@/components/common/Button/Button';
import { usePageRouter } from '@/hooks/usePageRouter';
import { useToken } from '@/hooks/useToken';
import Layout from '@/layout/Layout';
import { GetRunnerPostResponse, RunnerPost } from '@/types/runnerPost';
import React, { useEffect, useState } from 'react';
import { styled } from 'styled-components';

const MainPage = () => {
  const { goToRunnerPostCreatePage, goToLoginPage } = usePageRouter();
  const { getToken } = useToken();
  const [runnerPostList, setRunnerPostList] = useState<RunnerPost[]>([]);
  const [isLast, setIsLast] = useState<boolean>(true);

  useEffect(() => {
    getRunnerPost();
  }, []);

  const handleClickPostButton = () => {
    const isLogin = !!getToken();

    if (!isLogin) {
      alert('로그인이 필요한 서비스입니다.🥺');

      goToLoginPage();

      return;
    }

    goToRunnerPostCreatePage();
  };

  const getRunnerPost = () => {
    getRequest('/posts/runner/test')
      .then(async (response) => {
        const data: GetRunnerPostResponse = await response.json();
        setRunnerPostList([...runnerPostList, ...data.data]);
        setIsLast(data.pageInfo.isLast);
      })
      .catch((error: Error) => {
        alert(error.message);
      });
  };

  return (
    <Layout>
      <S.TitleWrapper>
        <S.Title>서포터를 찾고 있어요 👀</S.Title>
      </S.TitleWrapper>
      <S.ControlPanelContainer>
        <S.LeftSideContainer>
          {/* <S.FormContainer>
            <S.SearchLabel htmlFor="search-tag">#tags</S.SearchLabel>
            <S.SearchInput id="search-tag" type="text" placeholder="태그명 검색 (최대 5개 설정 가능)" />
          </S.FormContainer>
          <S.TagContainer>
            <Tag>자바</Tag>
            <Tag>javascript</Tag>
            <Tag>react</Tag>
          </S.TagContainer> */}
        </S.LeftSideContainer>

        <Button onClick={handleClickPostButton} colorTheme="WHITE" fontSize="18px" ariaLabel="리뷰 요청 글 작성하기">
          리뷰 요청 글 작성하기
        </Button>
      </S.ControlPanelContainer>
      <S.RunnerPostContainer>
        <RunnerPostList posts={runnerPostList} />
        {!isLast && (
          <Button colorTheme="RED" width="720px" onClick={getRunnerPost}>
            더보기
          </Button>
        )}
      </S.RunnerPostContainer>
    </Layout>
  );
};

export default MainPage;

const S = {
  TitleWrapper: styled.header`
    margin: 72px 0 53px 0;
  `,
  Title: styled.h1`
    font-size: 36px;
    font-weight: 700;
  `,
  ControlPanelContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: end;

    margin-bottom: 36px;
  `,

  LeftSideContainer: styled.div`
    display: flex;
    align-items: end;
    gap: 20px;
  `,

  FormContainer: styled.form`
    display: flex;
    flex-direction: column;
    align-items: start;
  `,

  SearchInput: styled.input`
    width: 263px;
    height: 40px;
    padding: 10px 10px;

    font-size: 14px;

    border-radius: 6px;
    border: 1px solid var(--gray-500);
  `,

  SearchLabel: styled.label`
    margin-bottom: 12px;

    font-size: 18px;
  `,

  TagContainer: styled.div`
    display: flex;

    margin-bottom: 3px;
    gap: 10px;
  `,

  RunnerPostContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 50px;
  `,
};
