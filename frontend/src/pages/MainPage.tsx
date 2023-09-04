import { getRequest } from '@/api/fetch';
import RunnerPostList from '@/components/RunnerPost/RunnerPostList/RunnerPostList';
import RunnerPostSearchBox from '@/components/RunnerPost/RunnerPostSearchBox/RunnerPostSearchBox';
import Button from '@/components/common/Button/Button';
import { ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { usePageRouter } from '@/hooks/usePageRouter';
import { useToken } from '@/hooks/useToken';
import Layout from '@/layout/Layout';
import { GetRunnerPostResponse, ReviewStatus, RunnerPost } from '@/types/runnerPost';
import React, { useContext, useEffect, useState } from 'react';
import { styled } from 'styled-components';

const MainPage = () => {
  const { goToRunnerPostCreatePage, goToLoginPage } = usePageRouter();

  const { getToken } = useToken();

  const { showErrorToast } = useContext(ToastContext);

  const [runnerPostList, setRunnerPostList] = useState<RunnerPost[]>([]);
  const [isLast, setIsLast] = useState<boolean>(true);
  const [page, setPage] = useState<number>(1);
  const [reviewStatus, setReviewStatus] = useState<ReviewStatus>('NOT_STARTED');
  const [tag, setTag] = useState<string>('');
  const [searchedTags, setSearchedTags] = useState<string[]>([]);

  const handleClickMoreButton = () => {
    getRunnerPosts();
  };

  useEffect(() => {
    getRunnerPosts();
  }, []);

  const handleClickPostButton = () => {
    if (getToken()) {
      goToRunnerPostCreatePage();

      return;
    }

    goToLoginPage();
  };

  const getRunnerPosts = () => {
    const params = new URLSearchParams([
      ['size', '10'],
      ['page', page.toString()],
      ['reviewStatus', reviewStatus],
    ]);

    if (tag) {
      params.set('tag', tag);
    }

    setPage(page + 1);

    getRequest(`/posts/runner?${params.toString()}`)
      .then(async (response) => {
        const data: GetRunnerPostResponse = await response.json();
        setRunnerPostList([...runnerPostList, ...data.data]);
        setIsLast(data.pageInfo.isLast);
      })
      .catch((error: Error) => showErrorToast({ description: error.message, title: ERROR_TITLE.REQUEST }));
  };

  const searchPosts = (reviewStatus: ReviewStatus, tag?: string) => {
    const params = new URLSearchParams([
      ['size', '10'],
      ['page', '1'],
      ['reviewStatus', reviewStatus],
    ]);

    if (tag) {
      params.set('tag', tag);
      setTag(tag);
    }

    setPage(2);
    setReviewStatus(reviewStatus);

    getRequest(`/posts/runner?${params.toString()}`)
      .then(async (response) => {
        const data: GetRunnerPostResponse = await response.json();
        setRunnerPostList([...data.data]);
        setIsLast(data.pageInfo.isLast);
      })
      .catch((error: Error) => showErrorToast({ description: error.message, title: ERROR_TITLE.REQUEST }));
  };

  return (
    <Layout>
      <S.TitleWrapper>
        <S.Title>서포터를 찾고 있어요 👀</S.Title>
      </S.TitleWrapper>
      <S.ControlPanelContainer>
        <S.LeftSideContainer>
          <RunnerPostSearchBox
            tag={tag}
            setTag={setTag}
            reviewStatus={reviewStatus}
            setReviewStatus={setReviewStatus}
            searchedTags={searchedTags}
            setSearchedTags={setSearchedTags}
            searchPosts={searchPosts}
          />
        </S.LeftSideContainer>
        <Button onClick={handleClickPostButton} colorTheme="WHITE" fontSize="18px" ariaLabel="리뷰 요청 글 작성하기">
          리뷰 요청 글 작성하기
        </Button>
      </S.ControlPanelContainer>
      <S.RunnerPostContainer>
        <RunnerPostList posts={runnerPostList} />
        {!isLast && (
          <Button colorTheme="RED" width="1200px" height="55px" onClick={handleClickMoreButton}>
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

  RunnerPostContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 50px;
  `,
};
