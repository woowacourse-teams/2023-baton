import ListFilter from '@/components/ListFilter';
import ProfileRunnerPostItem from '@/components/Profile/ProfileRunnerPostItem/ProfileRunnerPostItem';
import Avatar from '@/components/common/Avatar';
import { BATON_BASE_URL } from '@/constants';
import Layout from '@/layout/Layout';
import { RunnerProfileResponse } from '@/types/profile';
import { ReviewStatus } from '@/types/runnerPost';
import { SelectOption } from '@/types/select';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';

type ReviewPostOptions = SelectOption<ReviewStatus>[];

const reviewPostOptions: ReviewPostOptions = [
  {
    value: 'NOT_STARTED',
    label: '대기중인 리뷰',
    selected: true,
  },
  {
    value: 'IN_PROGRESS',
    label: '진행중인 리뷰',
    selected: false,
  },
  {
    value: 'DONE',
    label: '완료된 리뷰',
    selected: false,
  },
];

const MyPage = () => {
  const [runnerProfile, setRunnerProfile] = useState<RunnerProfileResponse | null>(null);

  const [postOptions, setPostOptions] = useState<ReviewPostOptions>(reviewPostOptions);

  const [isRunner, setIsRunner] = useState(true);

  const getRunnerProfile = async () => {
    try {
      const response = await fetch(`${BATON_BASE_URL}/profile/runner`, {
        method: 'GET',
      });

      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }

      const supporterCardList = await response.json();

      return supporterCardList;
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    const fetchRunnerPost = async () => {
      const result = await getRunnerProfile();
      setRunnerProfile(result);
    };

    fetchRunnerPost();
  }, []);

  const selectOptions = (value: string | number) => {
    const selectedOptionIndex = postOptions.findIndex((option) => option.value === value);
    if (selectedOptionIndex === -1) return;

    const newOptions = postOptions.map((option, index) => {
      if (index === selectedOptionIndex) return { ...option, selected: true };
      return { ...option, selected: false };
    });

    setPostOptions(newOptions);
  };

  const filterList = () => {
    const posts = runnerProfile?.runnerPosts;
    if (!posts) return;

    const selectedOption = postOptions.filter((option) => option.selected)[0];
    if (!selectedOption) return;

    const filteredPosts = posts.filter((post) => post.reviewStatus === selectedOption.value);
    return filteredPosts;
  };

  const handleClickSupporterButton = () => {
    alert('준비중인 기능입니다');
  };

  return (
    <Layout>
      <S.ProfileContainer>
        <S.InfoContainer>
          <Avatar
            imageUrl={runnerProfile?.profile.imageUrl ?? 'https://via.placeholder.com/150'}
            width={'100px'}
            height={'100px'}
          />
          <S.IntroduceContainer>
            <S.Name>{runnerProfile?.profile.name}</S.Name>
            <S.Introduce>{runnerProfile?.profile.introduction}</S.Introduce>
          </S.IntroduceContainer>
        </S.InfoContainer>
        <S.ButtonContainer>
          <S.RunnerSupporterButton isSelected={isRunner}>러너</S.RunnerSupporterButton>
          <S.RunnerSupporterButton isSelected={!isRunner} onClick={handleClickSupporterButton}>
            서포터
          </S.RunnerSupporterButton>
        </S.ButtonContainer>
      </S.ProfileContainer>
      <S.PostsContainer>
        <S.FilterWrapper>
          <ListFilter options={postOptions} selectOption={selectOptions} />
        </S.FilterWrapper>
        <S.ListContainer>
          {filterList()?.map((item) => (
            <ProfileRunnerPostItem {...item} />
          ))}
        </S.ListContainer>
      </S.PostsContainer>
    </Layout>
  );
};

export default MyPage;

const S = {
  ProfileContainer: styled.div`
    padding: 50px;
    border-bottom: 1px solid var(--gray-200);
  `,

  InfoContainer: styled.div`
    display: flex;
    align-items: center;
    gap: 20px;

    height: 175px;
  `,

  IntroduceContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 10px;

    width: 300px;
  `,

  Name: styled.div`
    font-size: 26px;
    font-weight: 700;
  `,

  Introduce: styled.div`
    font-size: 20px;

    white-space: no-wrap;
    overflow: hidden;
    text-overflow: ellipsis;
  `,

  ButtonContainer: styled.div`
    display: flex;
    gap: 20px;
  `,

  RunnerSupporterButton: styled.button<{ isSelected: boolean }>`
    display: flex;
    justify-content: center;
    align-items: center;

    width: 220px;
    height: 38px;
    border-radius: 18px;
    border: 1px solid ${({ isSelected }) => (isSelected ? 'white' : 'var(--baton-red)')};

    background-color: ${({ isSelected }) => (isSelected ? 'var(--baton-red)' : 'white')};

    color: ${({ isSelected }) => (isSelected ? 'white' : 'var(--baton-red)')};
  `,

  PostsContainer: styled.div`
    display: flex;
    flex-direction: column;
    align-items: center;
  `,

  ListContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 20px;
  `,

  FilterWrapper: styled.div`
    padding: 70px 20px;
  `,
};
