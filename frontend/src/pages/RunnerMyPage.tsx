import ListFilter from '@/components/ListFilter/ListFilter';
import TechLabel from '@/components/TechLabel/TechLabel';
import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import Layout from '@/layout/Layout';
import { GetMyPagePostResponse, GetMyPageProfileResponse, MyPagePost } from '@/types/myPage';
import React, { useContext, useEffect, useState } from 'react';
import styled from 'styled-components';
import MyPagePostList from '@/components/MyPage/MyPagePostList/MyPagePostList';
import { PostOptions, runnerPostOptions } from '@/utils/postOption';
import { usePageRouter } from '@/hooks/usePageRouter';
import useViewport from '@/hooks/useViewport';
import { useFetch } from '@/hooks/useFetch';
import { useLogin } from '@/hooks/useLogin';
import { ToastContext } from '@/contexts/ToastContext';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';

const RunnerMyPage = () => {
  const [myPageProfile, setMyPageProfile] = useState<GetMyPageProfileResponse | null>(null);
  const [myPagePostList, setMyPagePostList] = useState<MyPagePost[]>([]);
  const [postOptions, setPostOptions] = useState<PostOptions>(runnerPostOptions);
  const [page, setPage] = useState<number>(1);
  const [isLast, setIsLast] = useState<boolean>(true);

  const { isLogin } = useLogin();
  const { getRequestWithAuth } = useFetch();
  const { goToProfileEditPage, goToLoginPage } = usePageRouter();
  const { isMobile } = useViewport();
  const { showErrorToast } = useContext(ToastContext);

  useEffect(() => {
    if (!isLogin) {
      showErrorToast({ title: ERROR_TITLE.NO_PERMISSION, description: ERROR_DESCRIPTION.NO_TOKEN });
      goToLoginPage();

      return;
    }

    const fetchMyPageData = async () => {
      setIsLast(() => true);
      setPage(1);

      getProfile();
      getPostList(1);
    };

    fetchMyPageData();
  }, [postOptions]);

  const getProfile = () => {
    getRequestWithAuth(`/profile/runner/me`, async (response) => {
      const data: GetMyPageProfileResponse = await response.json();

      setMyPageProfile(data);
    });
  };

  const getPostList = (currentPage: number) => {
    const selectedPostOption = postOptions.filter((option) => option.selected)[0].value;

    getRequestWithAuth(
      `/posts/runner/me/runner?size=10&page=${currentPage}&reviewStatus=${selectedPostOption}`,
      async (response) => {
        const data: GetMyPagePostResponse = await response.json();

        setMyPagePostList(data.pageInfo.currentPage === 1 ? data.data : (current) => [...current, ...data.data]);
        setPage(() => data.pageInfo.currentPage);
        setIsLast(data.pageInfo.isLast);
      },
    );
  };

  const selectOptions = (value: string | number) => {
    const selectedOptionIndex = postOptions.findIndex((option) => option.value === value);
    if (selectedOptionIndex === -1) return;

    const newOptions = postOptions.map((option, index) => {
      if (index === selectedOptionIndex) return { ...option, selected: true };
      return { ...option, selected: false };
    });

    setPostOptions(newOptions);
  };

  const handleClickMoreButton = () => {
    setPage(() => page + 1);
    getPostList(page + 1);
  };

  const handleDeletePost = (runnerPostId: number) => {
    const deletedPostList = myPagePostList.filter((post) => post.runnerPostId !== runnerPostId);

    setMyPagePostList(deletedPostList);
  };

  return (
    <Layout>
      <S.TitleWrapper>
        <S.Title>러너 마이페이지</S.Title>
        <Button
          width={isMobile ? '60px' : '95px'}
          height={isMobile ? '30px' : '38px'}
          colorTheme="WHITE"
          fontSize={isMobile ? '12px' : '16px'}
          fontWeight={400}
          onClick={goToProfileEditPage}
        >
          수정하기
        </Button>
      </S.TitleWrapper>
      <S.MyPageWrapper>
        <S.MyPageContainer>
          <S.ProfileContainer>
            <S.InfoContainer>
              <Avatar
                imageUrl={myPageProfile?.imageUrl || 'https://via.placeholder.com/150'}
                width={isMobile ? '70px' : '100px'}
                height={isMobile ? '70px' : '100px'}
              />
              <S.InfoDetailContainer>
                <S.Name>{myPageProfile?.name}</S.Name>
                <S.Company>{myPageProfile?.company}</S.Company>
                <S.TechLabel>
                  {myPageProfile?.technicalTags.map((tag) => (
                    <TechLabel key={tag} tag={tag} />
                  ))}
                </S.TechLabel>
              </S.InfoDetailContainer>
            </S.InfoContainer>
          </S.ProfileContainer>

          <S.IntroductionContainer>
            <S.Introduction>{myPageProfile?.introduction}</S.Introduction>
          </S.IntroductionContainer>

          <S.PostsContainer>
            <S.FilterWrapper>
              <ListFilter
                options={postOptions}
                selectOption={selectOptions}
                width="100%"
                fontSize={isMobile ? '16px' : '26px'}
              />
            </S.FilterWrapper>
            <MyPagePostList handleDeletePost={handleDeletePost} filteredPostList={myPagePostList} isRunner={true} />
            <S.MoreButtonWrapper>
              {!isLast && (
                <Button
                  colorTheme="RED"
                  width={isMobile ? '100%' : '1200px'}
                  fontSize={isMobile ? '14px' : '18px'}
                  height="55px"
                  onClick={handleClickMoreButton}
                >
                  더보기
                </Button>
              )}
            </S.MoreButtonWrapper>
          </S.PostsContainer>
        </S.MyPageContainer>
      </S.MyPageWrapper>
    </Layout>
  );
};

export default RunnerMyPage;

const S = {
  TitleWrapper: styled.header`
    display: flex;
    justify-content: space-between;

    margin-top: 72px;

    @media (max-width: 768px) {
      margin: 40px 0 40px 0;
    }
  `,

  MyPageWrapper: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
  `,

  MyPageContainer: styled.div`
    width: 1200px;

    @media (max-width: 1200px) {
      width: calc(85% + 40px);
    }
  `,

  Title: styled.h1`
    font-size: 36px;
    font-weight: 700;

    @media (max-width: 768px) {
      font-size: 28px;
    }
  `,

  ProfileContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: flex-start;

    margin-top: 70px;
    margin-bottom: 50px;

    @media (max-width: 768px) {
      margin-top: 10px;
      margin-bottom: 30px;
    }
  `,

  InfoContainer: styled.div`
    display: flex;
    align-items: center;

    gap: 20px;
  `,

  InfoDetailContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 10px;
  `,

  Name: styled.div`
    font-size: 26px;
    font-weight: 700;

    @media (max-width: 768px) {
      font-size: 22px;
    }
  `,

  Company: styled.div`
    font-size: 18px;
    @media (max-width: 768px) {
      font-size: 16px;
    }
  `,

  TechLabel: styled.div`
    display: flex;
    flex-wrap: wrap;
    gap: 8px;
  `,

  IntroductionContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: end;

    margin-bottom: 20px;
  `,

  Introduction: styled.div`
    position: relative;
    display: flex;
    flex-wrap: wrap;
    gap: 9px 7px;

    margin-left: 40px;
    width: 75%;

    font-size: 18px;
    line-height: 1.8;

    white-space: pre-line;

    &::before {
      position: absolute;
      content: '';

      left: -30px;
      height: 100%;
      width: 4.5px;
      border-radius: 2px;

      background-color: var(--gray-400);

      @media (max-width: 768px) {
        width: 3.5px;
      }
    }

    @media (max-width: 768px) {
      width: 85%;

      font-size: 14px;
    }
  `,

  PostsContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 50px;

    width: 100%;
  `,

  FilterWrapper: styled.div`
    width: 920px;
    padding: 80px 20px;

    @media (max-width: 1200px) {
      width: 100%;
    }

    @media (max-width: 768px) {
      width: 100%;
      padding: 0;
      margin-top: 20px;
    }
  `,

  MoreButtonWrapper: styled.div`
    max-width: 1200px;
    min-width: 340px;
    width: 100%;
    margin-bottom: 20px;
  `,
};
