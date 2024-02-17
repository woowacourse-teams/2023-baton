import RunnerPostList from '@/components/RunnerPost/RunnerPostList/RunnerPostList';
import RunnerPostSearchBox from '@/components/RunnerPost/RunnerPostSearchBox/RunnerPostSearchBox';
import Button from '@/components/common/Button/Button';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { usePageRouter } from '@/hooks/usePageRouter';
import { useRunnerPostList } from '@/hooks/query/useRunnerPostList';
import useViewport from '@/hooks/useViewport';
import { ReviewStatus } from '@/types/runnerPost';
import { useContext, useState } from 'react';
import { styled } from 'styled-components';
import { isLogin } from '@/apis/auth';
import SideWidget from '@/components/common/SideWidget/SideWidget';
import { useRank } from '@/hooks/query/useRank';
import HomeLayout from '@/layout/HomeLayout';
import Text from '@/components/common/Text/Text';
import { Link } from 'react-router-dom';

const MainPage = () => {
  const { goToRunnerPostCreatePage, goToLoginPage } = usePageRouter();

  const { showErrorToast } = useContext(ToastContext);
  const { isMobile } = useViewport();

  const [enteredTag, setEnteredTag] = useState<string>('');
  const [reviewStatus, setReviewStatus] = useState<ReviewStatus | null>(null);

  const { data: runnerPostList, hasNextPage, fetchNextPage } = useRunnerPostList(reviewStatus, enteredTag);
  const { data: rankList } = useRank();

  const handleClickMoreButton = () => {
    fetchNextPage();
  };

  const handleClickPostButton = () => {
    if (!isLogin()) {
      showErrorToast({ title: ERROR_TITLE.NO_PERMISSION, description: ERROR_DESCRIPTION.NO_TOKEN });
      goToLoginPage();

      return;
    }

    goToRunnerPostCreatePage();
  };

  if (!rankList) {
    return null;
  }

  return (
    <HomeLayout>
      {isMobile ? (
        <S.SideWidgetContainer>
          <S.SideWidgetWrapper>
            <SideWidget title="🥇 코드 리뷰 랭킹">
              <SideWidget.List data={rankList.data}></SideWidget.List>
            </SideWidget>
          </S.SideWidgetWrapper>

          <Text as="p" typography="t7" textAlign="end" color="gray600" textDecoration="underline">
            <Link to="/notice">코드 리뷰 받을 프로젝트가 없다면?</Link>
          </Text>
        </S.SideWidgetContainer>
      ) : null}

      <S.MainContainer>
        <S.TitleWrapper>
          <S.Title>서포터를 찾고 있어요 👀</S.Title>
        </S.TitleWrapper>
        <S.ControlPanelContainer>
          <S.LeftSideContainer>
            <RunnerPostSearchBox
              reviewStatus={reviewStatus}
              setReviewStatus={setReviewStatus}
              setEnteredTag={setEnteredTag}
            />
          </S.LeftSideContainer>
          <S.RightSideContainer>
            <Button
              width={isMobile ? '160px' : '190px'}
              onClick={handleClickPostButton}
              colorTheme="WHITE"
              fontSize={isMobile ? '14px' : '18px'}
              ariaLabel="리뷰 요청 글 작성"
            >
              리뷰 요청 글 작성하기
            </Button>
          </S.RightSideContainer>
        </S.ControlPanelContainer>

        <S.RunnerPostContainer>
          <RunnerPostList posts={runnerPostList} />
          <S.MoreButtonWrapper>
            {hasNextPage && (
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

      {!isMobile && (
        <S.SideWidgetContainer>
          <S.SideWidgetWrapper>
            <SideWidget title="💻 구현 미션">
              <SideWidget.Banner></SideWidget.Banner>
            </SideWidget>

            <SideWidget title="🥇 코드 리뷰 랭킹">
              <SideWidget.List data={rankList.data}></SideWidget.List>
            </SideWidget>
          </S.SideWidgetWrapper>
        </S.SideWidgetContainer>
      )}
    </HomeLayout>
  );
};

export default MainPage;

const S = {
  MainContainer: styled.div`
    min-width: 480px;

    @media (max-width: 768px) {
      padding: 0;
      min-width: auto;
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
    flex-wrap: wrap;
    gap: 30px;

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
    max-width: 1280px;
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

  SideWidgetContainer: styled.div`
    position: relative;
  `,

  SideWidgetWrapper: styled.div`
    display: block;
    position: sticky;
    top: 88px;

    @media (max-width: 768px) {
      min-width: 340px;
      position: initial;
    }
  `,
};
