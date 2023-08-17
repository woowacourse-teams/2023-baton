import InputBox from '@/components/InputBox/InputBox';
import TagInput from '@/components/TagInput/TagInput';
import TextArea from '@/components/Textarea/Textarea';
import Button from '@/components/common/Button/Button';
import { usePageRouter } from '@/hooks/usePageRouter';
import Layout from '@/layout/Layout';
import React, { useContext, useState } from 'react';
import { styled } from 'styled-components';
import { CreateRunnerPostRequest } from '@/types/runnerPost';
import { useToken } from '@/hooks/useToken';
import { addDays, addHours, getDatetime, getDayLastTime, isPastTime } from '@/utils/date';
import { postRequest } from '@/api/fetch';
import { validateDeadline, validatePullRequestUrl, validateTags, validateTitle } from '@/utils/validate';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';

const RunnerPostCreatePage = () => {
  const nowDate = new Date();

  const { goBack, goToCreationResultPage } = usePageRouter();
  const { getToken } = useToken();

  const { showErrorToast } = useContext(ToastContext);

  const [tags, setTags] = useState<string[]>([]);
  const [title, setTitle] = useState<string>('');
  const [pullRequestUrl, setPullRequestUrl] = useState<string>('');
  const [deadline, setDeadline] = useState<string>(getDayLastTime(addDays(nowDate, 1)));
  const [contents, setContents] = useState<string>('');

  const pushTag = (newTag: string) => {
    const newTags = [...tags, newTag];

    try {
      validateTags(newTags);
    } catch (error) {
      const description = error instanceof Error ? error.message : ERROR_DESCRIPTION.UNEXPECTED;
      showErrorToast({ title: ERROR_TITLE.VALIDATION, description });

      return;
    }

    setTags(newTags);
  };

  const removeTag = (tag?: string) => {
    if (!tag) {
      setTags((current) => current.slice(0, -1));
      return;
    }

    const newTags = tags.filter((item) => item !== tag);
    setTags(newTags);
  };

  const changeTitle = (e: React.ChangeEvent<HTMLInputElement>) => {
    setTitle(e.target.value.trim());
  };

  const changePullRequestUrl = (e: React.ChangeEvent<HTMLInputElement>) => {
    setPullRequestUrl(e.target.value);
  };

  const changeDeadline = (e: React.ChangeEvent<HTMLInputElement>) => {
    try {
      validateDeadline(e.target.value);
    } catch (error) {
      const newDeadline = getDatetime(addHours(nowDate, 1));
      setDeadline(newDeadline);

      const description = error instanceof Error ? error.message : ERROR_DESCRIPTION.UNEXPECTED;
      return showErrorToast({ title: ERROR_TITLE.VALIDATION, description });
    }
    setDeadline(e.target.value);
  };

  const changeContents = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setContents(e.target.value);
  };

  const cancelPostWrite = (e: React.MouseEvent<HTMLButtonElement>) => {
    e.preventDefault();

    goBack();
  };

  const goToNextForm = () => {
    try {
      validateInputs();
    } catch (error) {
      const description = error instanceof Error ? error.message : ERROR_DESCRIPTION.UNEXPECTED;
      return showErrorToast({ title: ERROR_TITLE.VALIDATION, description });
    }

    submitForm();
  };

  const validateInputs = () => {
    validateTitle(title);
    validateTags(tags);
    validatePullRequestUrl(pullRequestUrl);
    validateDeadline(deadline);
  };

  const postRunnerForm = (data: CreateRunnerPostRequest) => {
    const token = getToken()?.value;
    if (!token) return;

    const body = JSON.stringify(data);

    postRequest(`/posts/runner`, token, body)
      .then(async () => {
        goToCreationResultPage();
      })
      .catch((error: Error) => {
        const description = error instanceof Error ? error.message : ERROR_DESCRIPTION.UNEXPECTED;
        showErrorToast({ title: ERROR_TITLE.REQUEST, description });
      });
  };

  const submitForm = async () => {
    const postData: CreateRunnerPostRequest = {
      tags,
      title,
      pullRequestUrl,
      deadline,
      contents,
    };

    await postRunnerForm(postData);
  };

  return (
    <Layout>
      <S.FormContainer>
        <S.Form>
          <S.InputContainer>
            <InputBox
              inputTextState={title}
              handleInputTextState={changeTitle}
              maxLength={30}
              width="800px"
              fontSize="38px"
              fontWeight="700"
              placeholder="제목을 입력해주세요"
              autoFocus={true}
            />
          </S.InputContainer>
          <S.InputContainer>
            <TagInput tags={tags} pushTag={pushTag} popTag={removeTag} width="100%" />
          </S.InputContainer>

          <S.InputContainer>
            <InputBox
              inputTextState={pullRequestUrl}
              handleInputTextState={changePullRequestUrl}
              width="500px"
              placeholder="코드 리뷰받을 PR 주소를 입력해주세요"
            />
          </S.InputContainer>

          <S.InputContainer>
            <S.InputName>마감기한</S.InputName>
            <S.Deadline
              type="datetime-local"
              value={deadline}
              max={getDatetime(addDays(nowDate, 365))}
              min={getDatetime(nowDate)}
              onChange={changeDeadline}
            />
          </S.InputContainer>
          <TextArea
            inputTextState={contents}
            width="1200px"
            height="340px"
            maxLength={500}
            handleInputTextState={changeContents}
            placeholder="> 리뷰어가 작성된 코드의 의미를 파악할 수 있도록 내용을 작성해주시면 더 나은 리뷰가 될 수 있어요 :)"
          />

          <S.ButtonContainer>
            <Button type="button" onClick={cancelPostWrite} colorTheme="GRAY" fontWeight={700}>
              취소
            </Button>
            <Button type="button" colorTheme="WHITE" fontWeight={700} onClick={goToNextForm}>
              리뷰요청 글 생성
            </Button>
          </S.ButtonContainer>
        </S.Form>
      </S.FormContainer>
    </Layout>
  );
};

export default RunnerPostCreatePage;

const S = {
  Title: styled.div`
    margin: 40px 0 50px 0;

    color: var(--gray-800);
    font-size: 32px;
    font-weight: 700;
  `,

  FormContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 20px;

    padding: 0 20px;
  `,

  InputContainer: styled.div`
    display: flex;
    justify-content: start;
  `,

  Form: styled.form`
    display: flex;
    flex-direction: column;
    gap: 30px;

    &:first-child {
      margin-top: 60px;
    }
  `,

  InputName: styled.div`
    display: flex;
    align-items: center;

    height: 36px;
    margin-right: 20px;

    color: var(--gray-800);
    font-size: 18px;
    font-weight: 500px;
  `,

  Deadline: styled.input`
    gap: 10px;

    &:focus {
      outline: 0;
    }
  `,

  ButtonContainer: styled.div`
    display: flex;
    justify-content: center;
    gap: 20px;

    margin: 50px 0;
  `,

  SelectedSupporter: styled.div`
    display: flex;
    align-items: end;
    gap: 30px;
  `,
};
