import ListFilter from '@/components/ListFilter/ListFilter';
import TechLabel from '@/components/TechLabel/TechLabel';
import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import { useToken } from '@/hooks/useToken';
import Layout from '@/layout/Layout';
import { GetMyPagePostResponse, GetMyPageProfileResponse, MyPagePost } from '@/types/myPage';
import React, { useContext, useEffect, useState } from 'react';
import styled from 'styled-components';
import githubIcon from '@/assets/github-icon.svg';
import MyPagePostList from '@/components/MyPage/MyPagePostList/MyPagePostList';
import { getRequest } from '@/api/fetch';
import { PostOptions, runnerPostOptions, supporterPostOptions } from '@/utils/postOption';
import { ToastContext } from '@/contexts/ToastContext';
import { ERROR_TITLE } from '@/constants/message';
import { usePageRouter } from '@/hooks/usePageRouter';

const MyPage = () => {
  const [myPageProfile, setMyPageProfile] = useState<GetMyPageProfileResponse | null>(null);
  const [myPagePostList, setMyPagePostList] = useState<MyPagePost[]>([]);

  const [postOptions, setPostOptions] = useState<PostOptions>(runnerPostOptions);

  const [isRunner, setIsRunner] = useState<boolean>(true);

  const [page, setPage] = useState<number>(1);
  const [isLast, setIsLast] = useState<boolean>(true);

  const { getToken } = useToken();
  const { goToProfileEditPage } = usePageRouter();
  const { showErrorToast } = useContext(ToastContext);

  useEffect(() => {
    const fetchMyPageData = async (role: 'runner' | 'supporter') => {
      setIsLast(() => true);
      setPage(1);

      getProfile(role);
      getPostList(role);
    };

    fetchMyPageData(isRunner ? 'runner' : 'supporter');
  }, [isRunner, postOptions]);

  const getProfile = (role: 'runner' | 'supporter') => {
    const token = getToken()?.value;
    if (!token) return;

    getRequest(`/profile/${role}/me`, token)
      .then(async (response) => {
        const data: GetMyPageProfileResponse = await response.json();

        setMyPageProfile(data);
      })
      .catch((error: Error) => showErrorToast({ title: ERROR_TITLE.REQUEST, description: error.message }));
  };

  const getPostList = (role: 'runner' | 'supporter') => {
    const token = getToken()?.value;
    if (!token) return;

    const selectedPostOption = postOptions.filter((option) => option.selected)[0].value;

    const rolePath =
      role === 'runner'
        ? `runner/me/runner?size=10&page=${page}&reviewStatus=${selectedPostOption}`
        : `runner/me/supporter?size=10&page=${page}&reviewStatus=${selectedPostOption}`;

    getRequest(`/posts/${rolePath}`, token)
      .then(async (response) => {
        const data: GetMyPagePostResponse = await response.json();

        setMyPagePostList(data.pageInfo.currentPage === 1 ? data.data : (current) => [...current, ...data.data]);
        setPage(() => data.pageInfo.currentPage);
        setIsLast(data.pageInfo.isLast);
      })
      .catch((error: Error) => showErrorToast({ title: ERROR_TITLE.REQUEST, description: error.message }));
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
    getPostList(role);
  };

  return (
    <Layout>
      <S.ProfileContainer>
        <S.InfoContainer>
          <Avatar
            imageUrl={myPageProfile?.imageUrl || 'https://via.placeholder.com/150'}
            width="100px"
            height="100px"
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
        <S.ButtonContainer>
          <S.RunnerSupporterButton $isSelected={isRunner} onClick={handleClickSupporterButton}>
            러너
          </S.RunnerSupporterButton>
          <S.RunnerSupporterButton $isSelected={!isRunner} onClick={handleClickSupporterButton}>
            서포터
          </S.RunnerSupporterButton>
        </S.ButtonContainer>
      </S.ProfileContainer>

      <S.IntroductionContainer>
        <S.Introduction>{myPageProfile?.introduction}</S.Introduction>
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
      </S.IntroductionContainer>

      <S.PostsContainer>
        <S.FilterWrapper>
          <ListFilter options={postOptions} selectOption={selectOptions} />
        </S.FilterWrapper>
        <MyPagePostList filteredPostList={myPagePostList} isRunner={isRunner} />
        <S.MoreButtonWrapper>
          {!isLast && (
            <Button colorTheme="RED" width="1200px" height="55px" onClick={handleClickMoreButton}>
              더보기
            </Button>
          )}
        </S.MoreButtonWrapper>
      </S.PostsContainer>
    </Layout>
  );
};

export default MyPage;

const S = {
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
  `,

  Company: styled.div`
    font-size: 18px;
  `,

  TechLabel: styled.div`
    display: flex;
    gap: 8px;
  `,

  ButtonContainer: styled.div`
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 10px;

    margin-top: 30px;
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
  `,

  IntroductionContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: end;

    margin-bottom: 50px;
  `,

  Introduction: styled.div`
    position: relative;
    display: flex;
    flex-wrap: wrap;
    gap: 9px 7px;

    margin-left: 40px;
    width: 75%;

    &::before {
      position: absolute;
      content: '';

      left: -30px;
      height: 100%;
      width: 4.5px;
      border-radius: 2px;

      background-color: var(--gray-400);
    }

    font-size: 18px;
    line-height: 1.8;

    white-space: pre-line;
  `,

  PostsContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
  `,

  FilterWrapper: styled.div`
    padding: 80px 20px;
  `,

  MoreButtonWrapper: styled.div`
    margin-top: 50px;
  `,
};
