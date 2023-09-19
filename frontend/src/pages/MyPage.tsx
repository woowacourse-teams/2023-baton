import ListFilter from '@/components/ListFilter/ListFilter';
import TechLabel from '@/components/TechLabel/TechLabel';
import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import Layout from '@/layout/Layout';
import { GetMyPagePostResponse, GetMyPageProfileResponse, MyPagePost } from '@/types/myPage';
import React, { useContext, useEffect, useState } from 'react';
import styled from 'styled-components';
import MyPagePostList from '@/components/MyPage/MyPagePostList/MyPagePostList';
import { PostOptions, runnerPostOptions, supporterPostOptions } from '@/utils/postOption';
import { usePageRouter } from '@/hooks/usePageRouter';
import useViewport from '@/hooks/useViewport';
import { useFetch } from '@/hooks/useFetch';
import { useLogin } from '@/hooks/useLogin';
import { ToastContext } from '@/contexts/ToastContext';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';

const MyPage = () => {
  const [myPageProfile, setMyPageProfile] = useState<GetMyPageProfileResponse | null>(null);
  const [myPagePostList, setMyPagePostList] = useState<MyPagePost[]>([]);

  const [postOptions, setPostOptions] = useState<PostOptions>(runnerPostOptions);

  const [isRunner, setIsRunner] = useState<boolean>(true);

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

    const fetchMyPageData = async (role: 'runner' | 'supporter') => {
      setIsLast(() => true);
      setPage(1);

      getProfile(role);
      getPostList(role, 1);
    };

    fetchMyPageData(isRunner ? 'runner' : 'supporter');
  }, [isRunner, postOptions]);

  const getProfile = (role: 'runner' | 'supporter') => {
    getRequestWithAuth(`/profile/${role}/me`, async (response) => {
      const data: GetMyPageProfileResponse = await response.json();

      setMyPageProfile(data);
    });
  };

  const getPostList = (role: 'runner' | 'supporter', currentPage: number) => {
    const selectedPostOption = postOptions.filter((option) => option.selected)[0].value;

    const rolePath =
      role === 'runner'
        ? `runner/me/runner?size=10&page=${currentPage}&reviewStatus=${selectedPostOption}`
        : `runner/me/supporter?size=10&page=${currentPage}&reviewStatus=${selectedPostOption}`;

    getRequestWithAuth(`/posts/${rolePath}`, async (response) => {
      const data: GetMyPagePostResponse = await response.json();

      setMyPagePostList(data.pageInfo.currentPage === 1 ? data.data : (current) => [...current, ...data.data]);
      setPage(() => data.pageInfo.currentPage);
      setIsLast(data.pageInfo.isLast);
    });
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

  const handleClickSupporterButton = () => {
    setPostOptions(isRunner ? runnerPostOptions : supporterPostOptions);
    setIsRunner(!isRunner);
  };

  const handleClickMoreButton = () => {
    const role = isRunner ? 'runner' : 'supporter';

    setPage(() => page + 1);
    getPostList(role, page + 1);
  };

  return (
    <Layout>
      <S.MyPageWrapper>
        <S.MyPageContainer>
          {isMobile && (
            <S.ButtonContainer>
              <S.RunnerSupporterButton $isSelected={isRunner} onClick={handleClickSupporterButton}>
                러너
              </S.RunnerSupporterButton>
              <S.RunnerSupporterButton $isSelected={!isRunner} onClick={handleClickSupporterButton}>
                서포터
              </S.RunnerSupporterButton>
            </S.ButtonContainer>
          )}
          <S.ProfileContainer>
            <S.InfoContainer>
              <Avatar
                imageUrl={myPageProfile?.imageUrl || 'https://via.placeholder.com/150'}
                width={isMobile ? '70px' : '100px'}
                height={isMobile ? '70px' : '100px'}
              />
              <S.InfoDetailContainer>
                {myPageProfile?.name &&
                  (isRunner ? (
                    <S.Name>{'러너 - ' + myPageProfile?.name}</S.Name>
                  ) : (
                    <S.Name>{'서포터 - ' + myPageProfile?.name}</S.Name>
                  ))}
                <S.Company>{myPageProfile?.company}</S.Company>
                <S.TechLabel>
                  {myPageProfile?.technicalTags.map((tag) => (
                    <TechLabel key={tag} tag={tag} />
                  ))}
                </S.TechLabel>
              </S.InfoDetailContainer>
            </S.InfoContainer>
            {!isMobile && (
              <S.ButtonContainer>
                <S.RunnerSupporterButton $isSelected={isRunner} onClick={handleClickSupporterButton}>
                  러너
                </S.RunnerSupporterButton>
                <S.RunnerSupporterButton $isSelected={!isRunner} onClick={handleClickSupporterButton}>
                  서포터
                </S.RunnerSupporterButton>
              </S.ButtonContainer>
            )}
          </S.ProfileContainer>
          <S.IntroductionContainer>
            <S.Introduction>{myPageProfile?.introduction}</S.Introduction>
            {!isMobile && (
              <Button
                width="95px"
                height="38px"
                colorTheme="WHITE"
                fontSize="16px"
                fontWeight={400}
                onClick={goToProfileEditPage}
              >
                수정하기
              </Button>
            )}
          </S.IntroductionContainer>
          {isMobile && (
            <S.MobileButtonWrapper>
              <Button
                width="75px"
                height="30px"
                colorTheme="WHITE"
                fontSize="14px"
                fontWeight={400}
                onClick={goToProfileEditPage}
              >
                수정하기
              </Button>
            </S.MobileButtonWrapper>
          )}
          <S.PostsContainer>
            <S.FilterWrapper>
              <ListFilter
                options={postOptions}
                selectOption={selectOptions}
                width="100%"
                fontSize={isMobile ? '16px' : '26px'}
              />
            </S.FilterWrapper>
            <MyPagePostList filteredPostList={myPagePostList} isRunner={isRunner} />
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

export default MyPage;

const S = {
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

  TitleContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: end;
  `,

  Title: styled.h1`
    font-size: 36px;
    font-weight: 700;
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

    margin: 30px 0;
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

  ButtonContainer: styled.div`
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 10px;

    margin-top: 30px;

    @media (max-width: 768px) {
      display: flex;
      justify-content: end;

      width: calc(85% + 40px);
      margin-top: 50px;
      margin-left: 10px;
    }
  `,

  MobileButtonWrapper: styled.div`
    display: flex;
    justify-content: end;

    width: calc(85% + 40px);
    margin-bottom: 70px;
  `,

  RunnerSupporterButton: styled.button<{ $isSelected: boolean }>`
    display: flex;
    justify-content: center;
    align-items: center;

    width: 95px;
    height: 38px;
    border-radius: 18px;
    border: 1px solid ${({ $isSelected }) => ($isSelected ? 'white' : 'var(--baton-red)')};

    background-color: ${({ $isSelected }) => ($isSelected ? 'var(--baton-red)' : 'white')};

    color: ${({ $isSelected }) => ($isSelected ? 'white' : 'var(--baton-red)')};

    @media (max-width: 768px) {
      width: 70px;
      height: 32px;

      font-size: 14px;
    }
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
