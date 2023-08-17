import { getRequest } from '@/api/fetch';
import TechLabel from '@/components/TechLabel/TechLabel';
import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import Layout from '@/layout/Layout';
import { GetRunnerProfileResponse } from '@/types/profile';
import React, { useContext, useEffect, useState } from 'react';
import { useParams } from 'react-router-dom';
import { styled } from 'styled-components';
import githubIcon from '@/assets/github-icon.svg';
import { ToastContext } from '@/contexts/ToastContext';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';

const RunnerProfilePage = () => {
  const [runnerProfile, setRunnerProfile] = useState<GetRunnerProfileResponse | null>(null);

  const { runnerId } = useParams();

  const { showErrorToast } = useContext(ToastContext);

  useEffect(() => {
    getProfile();
  }, [runnerId]);

  const getProfile = async () => {
    getRequest(`/profile/runner/${runnerId}`)
      .then(async (response) => {
        const data: GetRunnerProfileResponse = await response.json();

        setRunnerProfile(data);
      })
      .catch((error: Error) =>
        showErrorToast({
          description: error instanceof Error ? error.message : ERROR_DESCRIPTION.UNEXPECTED,
          title: ERROR_TITLE.REQUEST,
        }),
      );
  };

  return (
    <Layout>
      <S.ProfileContainer>
        <S.InfoContainer>
          <Avatar
            imageUrl={runnerProfile?.imageUrl || 'https://via.placeholder.com/150'}
            width="100px"
            height="100px"
          />
          <S.InfoDetailContainer>
            <S.Name>{runnerProfile?.name}</S.Name>
            <S.Company>{runnerProfile?.company}</S.Company>
            <S.TechLabel>
              {runnerProfile?.technicalTags.map((tag) => (
                <TechLabel key={tag} tag={tag} />
              ))}
            </S.TechLabel>
          </S.InfoDetailContainer>
        </S.InfoContainer>
      </S.ProfileContainer>

      <S.IntroductionContainer>
        <S.Introduction>{runnerProfile?.introduction}</S.Introduction>
        <Button width="127px" height="43px" colorTheme="BLACK" fontWeight={700}>
          <S.Anchor href={runnerProfile?.githubUrl} target="_blank">
            <img src={githubIcon} />
            <S.GoToGitHub>Github</S.GoToGitHub>
          </S.Anchor>
        </Button>
      </S.IntroductionContainer>
    </Layout>
  );
};

export default RunnerProfilePage;

const S = {
  ProfileContainer: styled.div`
    display: flex;
    justify-content: space-between;
    align-items: flex-start;

    padding: 50px 0;
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
    gap: 10px;
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

    padding: 0 10px;
    margin-bottom: 50px;

    border-left: 3px solid var(--gray-600);
  `,

  Introduction: styled.div`
    width: 700px;

    font-size: 18px;

    white-space: no-wrap;
  `,

  Anchor: styled.a`
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 10px;
  `,

  GoToGitHub: styled.p``,
};
