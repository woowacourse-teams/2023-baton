import letterIcon from '../assets/letter-icon.svg';
import { postRequest } from '@/api/fetch';
import CheckBox from '@/components/CheckBox/CheckBox';
import ReviewTypeButton from '@/components/ReviewTypeButton/ReviewTypeButton';
import Button from '@/components/common/Button/Button';
import { DESCRIPTION_OPTIONS_BAD, DESCRIPTION_OPTIONS_GOOD, REVIEW_TYPE_OPTIONS } from '@/constants/feedback';
import { usePageRouter } from '@/hooks/usePageRouter';
import { useToken } from '@/hooks/useToken';
import Layout from '@/layout/Layout';
import { DescriptionOptions, PostFeedbackRequest, ReviewType, ReviewTypeOptions } from '@/types/feedback';
import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { styled } from 'styled-components';

const SupporterFeedbackPage = () => {
  const { runnerPostId, supporterId } = useParams();

  const { getToken } = useToken();

  const { goToMyPage } = usePageRouter();

  const [reviewTypeOptions, setReviewTypeOptions] = useState<ReviewTypeOptions>(structuredClone(REVIEW_TYPE_OPTIONS));
  const [descriptionOptions, setDescriptionOptions] = useState<DescriptionOptions>(
    structuredClone(DESCRIPTION_OPTIONS_GOOD),
  );

  const reviewType = React.useMemo(
    () => reviewTypeOptions.filter((option) => option.selected)[0]?.value,
    [reviewTypeOptions],
  );

  const descriptions = React.useMemo(
    () => descriptionOptions.filter((option) => option.selected).map((option) => option.value),
    [descriptionOptions],
  );

  const selectReviewType = (value: ReviewType) => {
    const optionIndex = reviewTypeOptions.findIndex((option) => option.value === value);
    if (optionIndex === -1) return;

    const newOptions = reviewTypeOptions.map((option, index) => {
      if (index === optionIndex) return { ...option, selected: true };
      return { ...option, selected: false };
    });

    setReviewTypeOptions(newOptions);

    switchDescriptionOption(value);
  };

  const switchDescriptionOption = (value: ReviewType) => {
    switch (value) {
      case 'BAD':
        setDescriptionOptions(structuredClone(DESCRIPTION_OPTIONS_BAD));
        break;
      case 'GOOD':
      case 'GREAT':
      default:
        setDescriptionOptions(structuredClone(DESCRIPTION_OPTIONS_GOOD));
    }
  };

  const selectDescription = (description: string) => {
    const optionIndex = descriptionOptions.findIndex((option) => option.value === description);
    if (optionIndex === -1) return;

    const newOptions = structuredClone(descriptionOptions);
    newOptions[optionIndex].selected = !newOptions[optionIndex].selected;

    setDescriptionOptions(newOptions);
  };

  const postSupporterProfile = async (feedback: PostFeedbackRequest) => {
    const token = getToken()?.value;
    if (!token) {
      return alert('토큰이 존재하지 않습니다');
    }

    const body = JSON.stringify(feedback);
    const authorization = `Bearer ${token}`;
    postRequest(`/feedback/supporter`, authorization, body).catch(() => alert('제출 과정에서 오류가 발생했습니다'));
  };

  const handleClickSubmit = async () => {
    if (!reviewType) return;
    if (!Number(supporterId)) return alert('해당 서포터가 존재하지 않습니다');
    if (!Number(runnerPostId)) return alert('해당 게시물이 존재하지 않습니다');

    await postSupporterProfile({
      reviewType,
      descriptions,
      supporterId: Number(supporterId),
      runnerPostId: Number(runnerPostId),
    });

    goToMyPage();
  };

  const descriptionSubTitle = `코드 리뷰를 받으며 ${reviewType === 'BAD' ? '아쉬웠던' : '좋았던'} 점을 체크해주세요.`;

  return (
    <Layout>
      <S.TitleWrapper>
        <S.Title>리뷰 후기 남기기 💌</S.Title>
      </S.TitleWrapper>
      <S.FeedbackContainer>
        <S.ReviewTypeContainer>
          <S.SubTitle>리뷰에 대한 후기를 남겨주세요</S.SubTitle>
          <S.ReviewTypeList>
            {reviewTypeOptions.map((option) => (
              <ReviewTypeButton
                key={option.value}
                type="button"
                value={option.value}
                isSelected={option.selected}
                width="100px"
                height="70px"
                label={option.label}
                selectReviewType={selectReviewType}
              />
            ))}
          </S.ReviewTypeList>
        </S.ReviewTypeContainer>
        <S.UnderLine />
        {reviewType && (
          <S.DescriptionContainer>
            <S.SubTitle>{descriptionSubTitle}</S.SubTitle>
            <CheckBox options={descriptionOptions ?? []} selectOption={selectDescription} />
            <S.ButtonWrapper>
              <Button colorTheme="WHITE" width="470px" height="47px" onClick={handleClickSubmit}>
                제출
              </Button>
            </S.ButtonWrapper>
          </S.DescriptionContainer>
        )}
        <S.LetterRight src={letterIcon} />
      </S.FeedbackContainer>
    </Layout>
  );
};

export default SupporterFeedbackPage;

const S = {
  TitleWrapper: styled.div`
    margin: 72px 0 53px 0;
  `,

  Title: styled.h1`
    font-size: 36px;
    font-weight: 700;
  `,

  FeedbackContainer: styled.div`
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 50px;

    width: 600px;
    height: 780px;

    padding: 60px 30px;
    margin: 0 auto;

    box-shadow: 0 10px 28px rgba(0, 0, 0, 0.25), 0 6px 10px rgba(0, 0, 0, 0.22);
  `,

  ReviewTypeContainer: styled.div`
    position: relative;
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 50px;
  `,

  ReviewTypeList: styled.ul`
    display: flex;
    justify-content: space-between;

    width: 400px;
  `,

  SubTitle: styled.div`
    font-size: 20px;
    font-weight: 700;

    text-align: center;
  `,

  UnderLine: styled.hr`
    width: 100%;
    border: 0.5px solid var(--gray-200);
  `,

  DescriptionContainer: styled.div``,

  DescriptionList: styled.ul`
    display: flex;
    flex-direction: column;

    width: 400px;
  `,

  DescriptionItem: styled.button`
    display: flex;
    flex-direction: column;

    background-color: transparent;
  `,

  ButtonWrapper: styled.div`
    margin: 30px 0;
  `,

  LetterRight: styled.img`
    position: absolute;
    top: 500px;
    left: 500px;
  `,
};
