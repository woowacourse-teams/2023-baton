import Banner from '@/components/Banner/Banner';
import RunnerPostList from '@/components/RunnerPost/RunnerPostList/RunnerPostList';
import RunnerPostSearchBox from '@/components/RunnerPost/RunnerPostSearchBox/RunnerPostSearchBox';
import Button from '@/components/common/Button/Button';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { useFetch } from '@/hooks/useFetch';
import { useLogin } from '@/hooks/useLogin';
import { usePageRouter } from '@/hooks/usePageRouter';
import useViewport from '@/hooks/useViewport';
import Layout from '@/layout/Layout';
import { GetRunnerPostResponse, ReviewStatus, RunnerPost } from '@/types/runnerPost';
import { Tag } from '@/types/tags';
import React, { useContext, useEffect, useState } from 'react';
import { styled } from 'styled-components';

const MainPage = () => {
  const { goToRunnerPostCreatePage, goToLoginPage } = usePageRouter();

  const { isLogin } = useLogin();
  const { getRequest } = useFetch();
  const { showErrorToast } = useContext(ToastContext);
  const { isMobile } = useViewport();

  const [runnerPostList, setRunnerPostList] = useState<RunnerPost[]>([]);
  const [isLast, setIsLast] = useState<boolean>(true);
  const [page, setPage] = useState<number>(1);
  const [reviewStatus, setReviewStatus] = useState<ReviewStatus>('NOT_STARTED');
  const [tag, setTag] = useState<string>('');
  const [searchedTags, setSearchedTags] = useState<Tag[]>([]);

  const handleClickMoreButton = () => {
    getNextPage();
  };

  useEffect(() => {
    searchPosts(reviewStatus);
  }, []);

  const handleClickPostButton = () => {
    if (!isLogin) {
      showErrorToast({ title: ERROR_TITLE.NO_PERMISSION, description: ERROR_DESCRIPTION.NO_TOKEN });
      goToLoginPage();

      return;
    }

    goToRunnerPostCreatePage();
  };

  const getNextPage = () => {
    const params = new URLSearchParams([
      ['size', '10'],
      ['page', (page + 1).toString()],
      ['reviewStatus', reviewStatus],
    ]);

    if (tag) {
      params.set('tagName', tag);
    }

    setIsLast(true);

    getRequest(`/posts/runner/tags/search?${params.toString()}`, async (response) => {
      const data: GetRunnerPostResponse = await response.json();

      setRunnerPostList([...runnerPostList, ...data.data]);
      setPage(data.pageInfo.currentPage);
      setIsLast(data.pageInfo.isLast);
    });
  };

  const searchPosts = (reviewStatus: ReviewStatus, tag?: string) => {
    const params = new URLSearchParams([
      ['size', '10'],
      ['page', '1'],
      ['reviewStatus', reviewStatus],
    ]);

    if (tag) {
      params.set('tagName', tag);
      setTag(tag);
    }

    setIsLast(true);
    setRunnerPostList([]);

    getRequest(`/posts/runner/tags/search?${params.toString()}`, async (response) => {
      const data: GetRunnerPostResponse = await response.json();

      setRunnerPostList(() => [...data.data]);
      setPage(data.pageInfo.currentPage);
      setIsLast(data.pageInfo.isLast);
    });
  };

  return (
    <Layout maxWidth="none">
      <Banner />
      <S.MainContainer>
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
          <S.RightSideContainer>
            <Button
              width="100%"
              onClick={handleClickPostButton}
              colorTheme="WHITE"
              fontSize={isMobile ? '14px' : '18px'}
              ariaLabel="리뷰 요청 글 작성하기"
            >
              리뷰 요청 글 작성하기
            </Button>
          </S.RightSideContainer>
        </S.ControlPanelContainer>
        <S.RunnerPostContainer>
          <RunnerPostList posts={runnerPostList} />
          <S.MoreButtonWrapper>
            {!isLast && (
              <Button
                colorTheme="RED"
                width={isMobile ? '100%' : '1150px'}
                height="55px"
                onClick={handleClickMoreButton}
              >
                더보기
              </Button>
            )}
          </S.MoreButtonWrapper>
        </S.RunnerPostContainer>
      </S.MainContainer>
    </Layout>
  );
};

export default MainPage;

const S = {
  MainContainer: styled.div`
    max-width: 1200px;
    padding: 0 20px;
    margin: 0 auto;

    @media (max-width: 768px) {
      padding: 0;
    }
  `,

  TitleWrapper: styled.header`
    margin: 72px 0 53px 0;

    @media (max-width: 768px) {
      margin: 40px 0 40px 0;
    }
  `,

  Title: styled.h1`
    font-size: 36px;
    font-weight: 700;

    @media (max-width: 768px) {
      font-size: 28px;
    }
  `,

  ControlPanelContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: end;

    margin-bottom: 36px;

    @media (max-width: 768px) {
      align-items: start;
      flex-direction: column;

      gap: 18px;
    }

    @media (max-height: 768px) {
      margin-bottom: 24px;
    }
  `,

  LeftSideContainer: styled.div`
    display: flex;
    align-items: end;
    gap: 20px;
  `,

  RightSideContainer: styled.div`
    align-self: flex-end;
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

  MoreButtonWrapper: styled.div`
    max-width: 1200px;
    width: 100%;
    margin-bottom: 20px;

    @media (max-width: 768px) {
      min-width: 340px;
    }
  `,

  RunnerPostContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 50px;
  `,
};
