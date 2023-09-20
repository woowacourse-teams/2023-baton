import React, { useContext, useEffect, useState } from 'react';
import { styled } from 'styled-components';
import { usePageRouter } from '@/hooks/usePageRouter';
import Layout from '@/layout/Layout';
import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import Label from '@/components/common/Label/Label';
import ConfirmModal from '@/components/ConfirmModal/ConfirmModal';
import githubIcon from '@/assets/github-icon.svg';
import { useLogin } from '@/hooks/useLogin';
import { ToastContext } from '@/contexts/ToastContext';
import useViewport from '@/hooks/useViewport';
import EventImage from '@/assets/banner/event_banner_post.png';
import { JavaIconWhite, JavascriptIcon } from '@/assets/technicalLabelIcon';
import { GetHeaderProfileResponse } from '@/types/profile';
import { ERROR_DESCRIPTION, ERROR_TITLE, TOAST_COMPLETION_MESSAGE } from '@/constants/message';
import { useFetch } from '@/hooks/useFetch';

const NoticePage = () => {
  const { goBack } = usePageRouter();
  const { isLogin } = useLogin();
  const { getRequestWithAuth, postRequestWithAuth } = useFetch();
  const { isMobile } = useViewport();
  const { showErrorToast, showCompletionToast } = useContext(ToastContext);

  const [isConfirmModalOpen, setIsConfirmModalOpen] = useState<boolean>(false);
  const [profileName, setProfileName] = useState<string>();
  const [selectedLanguage, setSelectedLanguage] = useState<string>('');

  useEffect(() => {
    if (isLogin) {
      getProfile();
    }
  }, []);

  const getProfile = () => {
    getRequestWithAuth(`/profile/me`, async (response) => {
      const data: GetHeaderProfileResponse = await response.json();

      setProfileName(data.name);
    });
  };

  const openConfirmModal = (e: React.MouseEvent<HTMLButtonElement>) => {
    if (!isLogin) {
      showErrorToast({
        title: ERROR_TITLE.NO_PERMISSION,
        description: ERROR_DESCRIPTION.NO_TOKEN,
      });

      return;
    }

    const targetText = e.currentTarget.getAttribute('data-type');

    if (targetText) {
      setSelectedLanguage(targetText);
    }

    setIsConfirmModalOpen(true);
  };

  const closeConfirmModal = () => {
    setIsConfirmModalOpen(false);
  };

  const postRepository = async (selectedLanguage: string) => {
    const repositoryName = selectedLanguage === 'java' ? 'java-boss-monster' : 'javascript-boss-monster';

    await postRequestWithAuth(
      '/branch',
      () => {
        showCompletionToast(TOAST_COMPLETION_MESSAGE.REPO_COMPLETE);
      },
      JSON.stringify({ repoName: repositoryName }),
    );
  };

  const handleClickStartButton = () => {
    postRepository(selectedLanguage);
    closeConfirmModal();
  };

  return (
    <Layout>
      <S.RunnerPostContainer>
        <S.PostContainer>
          <S.PostHeaderContainer>
            <Label width="131px" height="33px" fontSize="16px" colorTheme="WHITE">
              공지사항
            </Label>
            <S.PostTitleContainer>
              <S.PostTitle>📣 보스 몬스터 잡기 미션</S.PostTitle>
            </S.PostTitleContainer>
          </S.PostHeaderContainer>
          <S.PostBodyContainer>
            <S.Contents>
              <S.EventImageWrapper>
                <S.EventImage src={EventImage}></S.EventImage>
              </S.EventImageWrapper>
              <S.EventMessage>🥲 코드 리뷰를 받아보고 싶은데 스스로 구현해 본 프로젝트는 없는데.. </S.EventMessage>
              <S.EventMessage>🤔 내가 짠 코드는 어떨까?</S.EventMessage>
              <br />
              <S.EventMessage>
                라는 고민을 가진 분들을 위해 바톤에서 작은 미션을 준비했습니다. 보스 몬스터 잡기 미션을 직접 구현해보고
              </S.EventMessage>
              <S.EventMessage>
                <S.EventMessageBold>
                  바톤에 리뷰 요청 글을 작성하신 분들에 한해 바톤에서 직접 코드 리뷰를 해드릴 예정
                </S.EventMessageBold>
                입니다.
              </S.EventMessage>
              <S.EventMessage>
                <br />
                <S.EventMessageBold>사용 언어는 java, javascript 두 가지이며</S.EventMessageBold>
                <S.EventMessageBold>아래 미션 시작하기 버튼을 통해 시작</S.EventMessageBold>하실 수 있습니다.
              </S.EventMessage>
              <S.EventMessage>
                공부하면서 궁금했던 점들을 미션 구현을 통해 정리해보고 바톤에서
                <S.EventMessageBold>코드 리뷰 받아보세요 !</S.EventMessageBold>
              </S.EventMessage>

              <S.EventMessageTitle>🔎 미션 보러가기</S.EventMessageTitle>
              <S.ButtonContainer>
                <Button colorTheme="BLACK" width="100%">
                  <S.Anchor href="https://github.com/baton-mission/java-boss-monster" target="_blank">
                    <img src={githubIcon} />
                    <S.GoToGitHub>java</S.GoToGitHub>
                  </S.Anchor>
                </Button>
                <Button colorTheme="BLACK" width="100%">
                  <S.Anchor href="https://github.com/baton-mission/javascript-boss-monster" target="_blank">
                    <img src={githubIcon} />
                    <S.GoToGitHub>javascript</S.GoToGitHub>
                  </S.Anchor>
                </Button>
              </S.ButtonContainer>

              <S.EventMessageTitleContainer>
                <S.EventMessageTitle>🧑‍💻 보스 몬스터 잡기 미션 시작하기</S.EventMessageTitle>
                <S.EventGuide href="https://github.com/baton-mission/docs/blob/main/mission-guide.md" target="_blank">
                  ❓ 미션은 어떻게 진행하나요?
                </S.EventGuide>
              </S.EventMessageTitleContainer>
              <S.ButtonContainer>
                <Button dataType="java" colorTheme="RED" onClick={openConfirmModal} width="100%">
                  <S.Anchor>
                    <JavaIconWhite color="#000000" />
                    <S.Language>java</S.Language>
                  </S.Anchor>
                </Button>
                <Button dataType="javascript" colorTheme="RED" onClick={openConfirmModal} width="100%">
                  <S.Anchor>
                    <JavascriptIcon />
                    <S.Language>javascript</S.Language>
                  </S.Anchor>
                </Button>
              </S.ButtonContainer>
            </S.Contents>

            <S.ProfileContainer href="https://github.com/baton-mission" target="_blank">
              <Avatar imageUrl="https://avatars.githubusercontent.com/u/144600586?s=200&v=4" />
              <S.Profile>
                <S.Name>바톤</S.Name>
                <S.Job>너도 좋고 나도 좋은 코드리뷰 매칭 플랫폼</S.Job>
              </S.Profile>
            </S.ProfileContainer>
          </S.PostBodyContainer>
          <S.PostFooterContainer>
            <Button colorTheme="GRAY" fontSize={isMobile ? '14px' : ''} fontWeight={700} onClick={goBack}>
              목록
            </Button>
          </S.PostFooterContainer>
        </S.PostContainer>
      </S.RunnerPostContainer>

      {isConfirmModalOpen && (
        <ConfirmModal
          contents={
            <>
              <p>
                <S.highlightSpan>{selectedLanguage} 미션을 선택</S.highlightSpan>하셨습니다.
              </p>
              <p>
                확인을 누르시면 <S.highlightSpan>{profileName}님의 깃허브 아이디로 브랜치가 생성</S.highlightSpan>
                됩니다.
              </p>
            </>
          }
          closeModal={closeConfirmModal}
          handleClickConfirmButton={handleClickStartButton}
        />
      )}
    </Layout>
  );
};

const S = {
  RunnerPostContainer: styled.div`
    width: 100%;
    height: 100%;

    margin: 72px 0 53px 0;

    background-color: white;

    @media (max-width: 768px) {
      margin: 10px 0 30px 0;
    }
  `,

  Title: styled.div`
    font-size: 36px;
    font-weight: bold;
  `,

  PostContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 15px;

    width: 90%;
    margin: 0 auto;
  `,

  PostHeaderContainer: styled.div`
    width: 100%;
  `,

  PostTitleContainer: styled.div`
    display: flex;
    align-items: center;
    justify-content: space-between;
    gap: 10px;

    margin-top: 30px;
  `,

  PostTitle: styled.h1`
    font-size: 38px;
    font-weight: 700;

    @media (max-width: 768px) {
      font-size: 28px;
    }
  `,

  PostDeadline: styled.div`
    display: flex;
    align-items: center;
    font-size: 16px;
    color: var(--gray-600);
  `,

  PostBodyContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 100px;

    width: 100%;

    margin-bottom: 20px;
    padding: 20px 0;
    border-bottom: 1px solid #9d9d9d;
  `,

  ProfileContainer: styled.a`
    display: flex;
    align-items: center;
    gap: 20px;

    width: 450px;

    cursor: pointer;

    @media (max-width: 768px) {
      width: 300px;
    }
  `,

  Profile: styled.div`
    display: flex;
    flex-flow: column;
    gap: 5px;
  `,

  Name: styled.p`
    font-size: 18px;
    font-weight: bold;

    @media (max-width: 768px) {
      font-size: 15px;
    }
  `,

  Job: styled.p`
    font-size: 18px;

    @media (max-width: 768px) {
      font-size: 12px;
    }
  `,

  Contents: styled.div`
    min-height: 400px;

    white-space: pre-wrap;

    @media (max-width: 768px) {
      min-height: 300px;
    }
  `,

  EventImageWrapper: styled.div`
    display: flex;
    margin: 10px 0 50px 0;
  `,

  EventImage: styled.img`
    width: 754px;
    height: 265px;

    margin: 0 auto;

    @media (max-width: 768px) {
      width: 320px;
      height: 112px;
    }
  `,

  EventMessage: styled.p`
    line-height: 200%;
  `,

  EventMessageBold: styled.span`
    font-weight: 700;
  `,

  EventMessageTitleContainer: styled.div`
    display: flex;
    align-items: end;
    gap: 20px;

    @media (max-width: 768px) {
      flex-direction: column;
      align-items: start;
      gap: 0;
    }
  `,

  EventMessageTitle: styled.p`
    margin: 60px 0 30px 0;

    font-size: 28px;
    font-weight: 700;

    @media (max-width: 768px) {
      font-size: 20px;
      margin: 60px 0 20px 0;
    }
  `,

  EventGuide: styled.a`
    margin: 40px 0 30px 0;

    text-decoration: underline;
    text-underline-position: under;

    @media (max-width: 768px) {
      font-size: 14px;
      margin: 0 0 30px 0;
    }
  `,

  ButtonContainer: styled.div`
    display: flex;
    gap: 15px;
  `,

  GoToGitHub: styled.span``,

  Language: styled.span`
    color: var(--white);
  `,

  Anchor: styled.a`
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 10px;
  `,

  LeftSideContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 24px;
  `,

  PostFooterContainer: styled.div`
    display: flex;

    @media (max-width: 768px) {
      font-size: 12px;
    }
  `,

  highlightSpan: styled.span`
    font-weight: 700;
  `,
};

export default NoticePage;
