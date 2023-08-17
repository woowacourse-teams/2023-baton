import { getRequest, patchRequest } from '@/api/fetch';
import InputBox from '@/components/InputBox/InputBox';
import TechLabelButton from '@/components/TechLabelButton/TechLabelButton';
import TechTagSelectModal from '@/components/TechTagSelectModal/TechTagSelectModal';
import TextArea from '@/components/Textarea/Textarea';
import Avatar from '@/components/common/Avatar/Avatar';
import Button from '@/components/common/Button/Button';
import { ERROR_DESCRIPTION, ERROR_TITLE, TOAST_COMPLETION_MESSAGE, TOAST_ERROR_MESSAGE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import { useToken } from '@/hooks/useToken';
import Layout from '@/layout/Layout';
import {
  Profile,
  PatchRunnerProfileRequest,
  GetRunnerProfileResponse,
  PatchSupporterProfileRequest,
  GetSupporterProfileResponse,
} from '@/types/profile';
import { Technic } from '@/types/tags';
import { deepEqual } from '@/utils/object';
import { validateCompany, validateIntroduction, validateName } from '@/utils/validate';
import React, { useContext, useEffect, useState } from 'react';
import styled from 'styled-components';

const ProfileEditPage = () => {
  const { getToken } = useToken();

  const { showErrorToast, showCompletionToast } = useContext(ToastContext);

  const [isRunner, setIsRunner] = useState(true);

  const [runnerProfile, setRunnerProfile] = useState<GetRunnerProfileResponse | null>(null);
  const [supporterProfile, setSupporterProfile] = useState<GetSupporterProfileResponse | null>(null);

  const [name, setName] = useState<string | null>(null);
  const [company, setCompany] = useState<string | null>(null);
  const [introduction, setIntroduction] = useState<string | null>(null);
  const [technicalTags, setTechnicalTags] = useState<Technic[] | null>(null);

  const [isModalOpen, setIsModalOpen] = useState(false);

  useEffect(() => {
    getRunnerProfile();
    getSupporterProfile();
  }, []);

  const getRunnerProfile = () => {
    const token = getToken()?.value;
    if (!token) return;

    getRequest(`/profile/runner/me`, token).then(async (response) => {
      const data: GetRunnerProfileResponse = await response.json();
      setRunnerProfile(data);

      if (isRunner) {
        updateProfileInputValue(data);
      }
    });
  };

  const getSupporterProfile = () => {
    const token = getToken()?.value;
    if (!token) return;

    getRequest(`/profile/supporter/me`, token).then(async (response) => {
      const data: GetSupporterProfileResponse = await response.json();
      setSupporterProfile(data);

      if (!isRunner) {
        updateProfileInputValue(data);
      }
    });
  };

  const isModified = isRunner
    ? !deepEqual({ ...runnerProfile }, { ...runnerProfile, name, company, introduction, technicalTags })
    : !deepEqual({ ...supporterProfile }, { ...supporterProfile, name, company, introduction, technicalTags });

  const isLoading = isRunner ? !!runnerProfile : !!supporterProfile;

  const handleChangeName = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.value === ' ') {
      e.target.value = '';
    }

    setName(e.target.value);
  };

  const handleChangeCompany = (e: React.ChangeEvent<HTMLInputElement>) => {
    if (e.target.value === ' ') {
      e.target.value = '';
    }

    setCompany(e.target.value);
  };

  const handleChangeIntroduction = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    const newIntroduction = e.target.value.replace(`\n\n\n`, `\n\n`);
    setIntroduction(newIntroduction);
  };

  const popTag = (tag: Technic) => {
    if (!technicalTags) return;

    const newTags = technicalTags.filter((item) => tag !== item);
    setTechnicalTags(newTags);
  };

  const updateProfileInputValue = (profile: Profile) => {
    const newProfile = structuredClone(profile);

    setName(newProfile.name);
    setCompany(newProfile.company);
    setIntroduction(newProfile.introduction);
    setTechnicalTags([...newProfile.technicalTags].sort());
  };

  const handleClickSaveButton = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    isRunner ? saveRunnerProfile() : saveSupporterProfile();
  };

  const validateModification = () => {
    validateName(name);
    validateCompany(company);
    validateIntroduction(introduction);
  };

  const saveRunnerProfile = () => {
    if (!isModified) return;

    if (!runnerProfile || !technicalTags) return;

    try {
      validateModification();
    } catch (error) {
      return showErrorToast({
        description: error instanceof Error ? error.message : ERROR_DESCRIPTION.UNEXPECTED,
        title: ERROR_TITLE.VALIDATION,
      });
    }

    if (!name || !company || !introduction) return;
    const newInfo = { name: name.trim(), company: company.trim(), introduction: introduction.trim(), technicalTags };

    setRunnerProfile({ ...runnerProfile, ...newInfo });
    updateProfileInputValue({ ...runnerProfile, ...newInfo });
    patchRunnerProfile(newInfo);
  };

  const saveSupporterProfile = () => {
    if (!isModified) return;

    if (!supporterProfile || !technicalTags) return;

    try {
      validateModification();
    } catch (error) {
      return showErrorToast({
        description: error instanceof Error ? error.message : ERROR_DESCRIPTION.UNEXPECTED,
        title: ERROR_TITLE.VALIDATION,
      });
    }

    if (!name || !company || !introduction) return;
    const newInfo = { name: name.trim(), company: company.trim(), introduction: introduction.trim(), technicalTags };

    setSupporterProfile({ ...supporterProfile, ...newInfo });
    updateProfileInputValue({ ...supporterProfile, ...newInfo });
    patchSupporterProfile(newInfo);
  };

  const confirmTagSelect = (tags: Technic[]) => {
    setTechnicalTags([...tags]);
    closeModal();
  };

  const handleClickRunnerButton = () => {
    if (!runnerProfile) return showErrorToast(TOAST_ERROR_MESSAGE.NO_PROFILE);

    if (isModified) {
      if (!confirm('저장하지 않고 러너 프로필로 이동할까요?')) return;
    }

    setIsRunner(true);
    updateProfileInputValue(runnerProfile);
  };

  const handleClickSupporterButton = () => {
    if (!supporterProfile) return showErrorToast(TOAST_ERROR_MESSAGE.NO_PROFILE);

    if (isModified) {
      if (!confirm('저장하지 않고 서포터 프로필로 이동할까요?')) return;
    }

    setIsRunner(false);
    updateProfileInputValue(supporterProfile);
  };

  const openModal = () => {
    setIsModalOpen(true);
  };

  const closeModal = () => {
    setIsModalOpen(false);
  };

  const patchRunnerProfile = async (runnerProfile: PatchRunnerProfileRequest) => {
    const token = getToken()?.value;
    if (!token) return;

    const body = JSON.stringify(runnerProfile);

    patchRequest(`/profile/runner/me`, token, body)
      .then(() => showCompletionToast(TOAST_COMPLETION_MESSAGE.SAVE))
      .catch((error) => showErrorToast({ title: ERROR_TITLE.REQUEST, description: error.message }));
  };

  const patchSupporterProfile = async (supporterProfile: PatchSupporterProfileRequest) => {
    const token = getToken()?.value;
    if (!token) return;

    const body = JSON.stringify(supporterProfile);

    await patchRequest(`/profile/supporter/me`, token, body)
      .then(() => showCompletionToast(TOAST_COMPLETION_MESSAGE.SAVE))
      .catch((error: Error) => showErrorToast({ title: ERROR_TITLE.REQUEST, description: error.message }));
  };

  return (
    <Layout>
      <S.TitleWrapper>
        <S.Title>프로필 수정</S.Title>
      </S.TitleWrapper>
      <S.ProfileContainer>
        <S.ButtonContainer>
          <S.RunnerSupporterButton $isSelected={isRunner} onClick={isRunner ? undefined : handleClickRunnerButton}>
            러너
          </S.RunnerSupporterButton>
          <S.RunnerSupporterButton $isSelected={!isRunner} onClick={isRunner ? handleClickSupporterButton : undefined}>
            서포터
          </S.RunnerSupporterButton>
        </S.ButtonContainer>
        {isLoading ? (
          <S.Form>
            <S.AvatarWrapper>
              <Avatar
                width="150px"
                height="150px"
                imageUrl={runnerProfile?.imageUrl ?? 'https://via.placeholder.com/150'}
              />
            </S.AvatarWrapper>
            <S.SaveButtonWrapper>
              <Button
                width="100px"
                colorTheme={isModified ? 'WHITE' : 'GRAY'}
                onClick={handleClickSaveButton}
                fontWeight={700}
              >
                저장
              </Button>
            </S.SaveButtonWrapper>
            <S.InputContainer>
              <S.InputName>이름</S.InputName>
              <S.InputWrapper>
                <InputBox
                  value={name ?? undefined}
                  inputTextState={name ?? ''}
                  maxLength={10}
                  maxLengthFontSize="12px"
                  handleInputTextState={handleChangeName}
                  placeholder="이름을 입력하세요"
                />
              </S.InputWrapper>
            </S.InputContainer>
            <S.InputContainer>
              <S.InputName>소속</S.InputName>
              <S.InputWrapper>
                <InputBox
                  value={company ?? undefined}
                  inputTextState={company ?? ''}
                  maxLength={20}
                  maxLengthFontSize="12px"
                  handleInputTextState={handleChangeCompany}
                  placeholder="소속을 입력하세요"
                />
              </S.InputWrapper>
            </S.InputContainer>
            <S.InputContainer>
              <S.InputName>자기소개</S.InputName>
              <S.InputWrapper>
                <TextArea
                  value={introduction ?? undefined}
                  inputTextState={introduction ?? ''}
                  fontSize="18px"
                  width="400px"
                  height="200px"
                  padding="0"
                  maxLength={200}
                  handleInputTextState={handleChangeIntroduction}
                  placeholder="자기소개를 입력하세요"
                />
              </S.InputWrapper>
            </S.InputContainer>
            <S.InputContainer>
              <S.InputName>
                {isRunner ? <pre>{`학습하고 싶은 \n기술스택`}</pre> : <pre>{`자신있는 \n기술스택`}</pre>}
              </S.InputName>
              <S.TechTagsList>
                {technicalTags?.map((tag) => (
                  <S.TagButtonWrapper key={tag}>
                    <TechLabelButton tag={tag} handleClickTag={popTag} isDeleteButton={true} />
                  </S.TagButtonWrapper>
                ))}
                <S.AddTagButton type="button" onClick={openModal}>
                  +
                </S.AddTagButton>
              </S.TechTagsList>
            </S.InputContainer>
          </S.Form>
        ) : (
          'Loading'
        )}
      </S.ProfileContainer>

      {isModalOpen && (
        <TechTagSelectModal
          closeModal={closeModal}
          confirmTagSelect={confirmTagSelect}
          defaultTags={technicalTags ?? undefined}
        />
      )}
    </Layout>
  );
};

export default ProfileEditPage;

const S = {
  TitleWrapper: styled.div`
    margin: 72px 0 53px 0;
  `,

  Title: styled.h1`
    font-size: 36px;
    font-weight: 700;
  `,

  ProfileContainer: styled.div`
    width: 900px;

    margin: 0 auto;
  `,

  InputContainer: styled.div`
    display: flex;
    justify-content: start;
    gap: 50px;
  `,

  Form: styled.form`
    display: flex;
    flex-direction: column;
    align-items: center;

    gap: 30px;
  `,

  AvatarWrapper: styled.div``,

  SaveButtonWrapper: styled.div`
    display: flex;
    justify-content: end;

    width: 600px;
    padding-bottom: 30px;
  `,

  InputName: styled.div`
    display: flex;
    align-items: start;

    width: 100px;
    padding: 5px 0;

    color: var(--gray-800);
    font-size: 20px;
    font-weight: 500;
    line-height: 130%;
  `,

  DeadlineContainer: styled.div`
    display: flex;

    gap: 20px;
  `,

  TechTagsList: styled.ul`
    display: flex;
    flex-wrap: wrap;
    align-content: flex-start;
    gap: 10px 8px;

    width: 400px;
    height: 100px;
    padding: 5px 0;
  `,

  ButtonContainer: styled.div`
    display: flex;
    justify-content: center;
    gap: 15px;

    margin: 50px;
  `,

  InputWrapper: styled.div`
    border-bottom: 1px solid var(--gray-200);
  `,

  RunnerSupporterButton: styled.button<{ $isSelected: boolean }>`
    display: flex;
    justify-content: center;
    align-items: center;

    width: 90px;
    height: 38px;
    border-radius: 18px;
    border: 1px solid ${({ $isSelected }) => ($isSelected ? 'white' : 'var(--baton-red)')};

    background-color: ${({ $isSelected }) => ($isSelected ? 'var(--baton-red)' : 'white')};

    color: ${({ $isSelected }) => ($isSelected ? 'white' : 'var(--baton-red)')};
  `,

  TagButtonWrapper: styled.li`
    height: 32px;
  `,

  AddTagButton: styled.button`
    display: flex;
    justify-content: center;
    align-items: center;

    width: 100px;
    height: 32px;
    border-radius: 14px;

    background: var(--gray-400);

    color: white;
    font-size: 23px;
    font-weight: 300;
  `,
};
