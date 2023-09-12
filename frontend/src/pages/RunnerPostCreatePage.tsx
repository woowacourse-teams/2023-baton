import InputBox from '@/components/InputBox/InputBox';
import TagInput from '@/components/TagInput/TagInput';
import Button from '@/components/common/Button/Button';
import { usePageRouter } from '@/hooks/usePageRouter';
import Layout from '@/layout/Layout';
import React, { useContext, useState } from 'react';
import { styled } from 'styled-components';
import { CreateRunnerPostRequest } from '@/types/runnerPost';
import { useToken } from '@/hooks/useToken';
import { addDays, addHours, getDatetime, getDayLastTime } from '@/utils/date';
import { postRequest } from '@/api/fetch';
import {
  validateCuriousContents,
  validateDeadline,
  validateImplementContents,
  validatePullRequestUrl,
  validateTags,
  validateTitle,
} from '@/utils/validate';
import { ERROR_DESCRIPTION, ERROR_TITLE } from '@/constants/message';
import { ToastContext } from '@/contexts/ToastContext';
import useViewport from '@/hooks/useViewport';
import GuideTextarea from '@/components/GuideTextarea/GuideTextarea';

const RunnerPostCreatePage = () => {
  const nowDate = new Date();

  const { goBack, goToMainPage } = usePageRouter();
  const { getToken } = useToken();
  const { showErrorToast } = useContext(ToastContext);

  const { isMobile } = useViewport();

  const [tags, setTags] = useState<string[]>([]);
  const [title, setTitle] = useState<string>('');
  const [pullRequestUrl, setPullRequestUrl] = useState<string>('');
  const [deadline, setDeadline] = useState<string>(getDayLastTime(addDays(nowDate, 1)));
  const [implementedContents, setImplementedContents] = useState<string>('');
  const [curiousContents, setCuriousContents] = useState<string>('');
  const [postscriptContents, setPostscriptContents] = useState<string>('');

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

  const changeImplementedContents = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setImplementedContents(e.target.value);
  };

  const changeCuriousContents = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setCuriousContents(e.target.value);
  };

  const changePostscriptContents = (e: React.ChangeEvent<HTMLTextAreaElement>) => {
    setPostscriptContents(e.target.value);
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
    validateImplementContents(implementedContents);
    validateCuriousContents(curiousContents);
  };

  const postRunnerForm = (data: CreateRunnerPostRequest) => {
    const token = getToken()?.value;
    if (!token) return;

    const body = JSON.stringify(data);

    postRequest(`/posts/runner`, token, body)
      .then(async () => {
        goToMainPage();
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
      implementedContents,
      curiousContents,
      postscriptContents,
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
              width={isMobile ? '300px' : '800px'}
              height="40px"
              fontSize={isMobile ? '20px' : '38px'}
              maxLengthFontSize={isMobile ? '14px' : '18px'}
              fontWeight="700"
              placeholder="제목을 입력해주세요"
              autoFocus={true}
            />
          </S.InputContainer>
          <S.InputContainer>
            <TagInput
              tags={tags}
              pushTag={pushTag}
              popTag={removeTag}
              width="100%"
              fontSize={isMobile ? '14px' : '18px'}
            />
          </S.InputContainer>

          <S.InputContainer>
            <InputBox
              inputTextState={pullRequestUrl}
              handleInputTextState={changePullRequestUrl}
              width={isMobile ? '300px' : '500px'}
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
          <GuideTextarea
            title="무엇을 구현하였나요?"
            inputTextState={implementedContents}
            maxLength={200}
            guideTexts={[
              '로또 추첨 어플리케이션을 객체지향적으로 구현했습니다.',
              '바톤 미션중에 플레이어가 몬스터와 대결하는 콘솔 애플리케이션을 구현했습니다.',
              '개인 프로젝트로 CRUD 기능이 있는 게시판을 만들었습니다.',
            ]}
            handleInputTextState={changeImplementedContents}
            placeholder="구현 기능에 대한 설명을 해주세요"
          />
          <GuideTextarea
            title="아쉬운 점이나 궁금한 점이 있나요?"
            inputTextState={curiousContents}
            maxLength={200}
            guideTexts={[
              '설계 위주로 리뷰받고 싶어요. 어떻게 더 좋은 설계로 바꿀 수 있나요?',
              '현재 코드에서 어떻게 수정해야 객체지향에 가까워 질 수 있나요?',
              '컴포넌트를 어떤 기준으로 나누면 좋을까요?',
              '커밋 단위 조절하는 것이 어려웠어요. 잘게 커밋하려면 어떻게 해야할까요?',
              '코드의 가독성을 개선하는 팁을 알려주세요.',
              '서비스가 모든 걸 처리하다보니 하는 역할이 많아진것 같아요. 어떻게 개선할 수 있을까요?',
            ]}
            handleInputTextState={changeCuriousContents}
            placeholder="궁금한 점을 적어주세요"
          />
          <GuideTextarea
            title="서포터에게 하고싶은 말이 있나요?"
            inputTextState={postscriptContents}
            maxLength={200}
            guideTexts={[
              '3년차 이상 개발자께서 리뷰해주시면 좋을 것 같아요.',
              '최대한 빨리 리뷰해주세요.',
              'Best Practice나 공부할 키워드를 많이 알려주시면 좋을 것 같아요.',
            ]}
            handleInputTextState={changePostscriptContents}
            placeholder="서포터에게 하고 싶은 말을 적어주세요"
            isOptional={true}
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

    @media (max-width: 768px) {
      padding: 0;
    }
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
