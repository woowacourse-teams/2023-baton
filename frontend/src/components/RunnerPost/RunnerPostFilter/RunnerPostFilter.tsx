import React from 'react';
import { css, keyframes, styled } from 'styled-components';
import { REVIEW_STATUS_LABEL_TEXT } from '@/constants';

interface Props {
  reviewStatus: string;
  handleClickRadioButton: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const RunnerPostFilter = ({ reviewStatus, handleClickRadioButton }: Props) => {
  return (
    <S.FilterContainer>
      <S.LabelList>
        {Object.entries(REVIEW_STATUS_LABEL_TEXT).map(([value, text]) => (
          <S.StatusLabel key={value}>
            <S.RadioButton
              type="radio"
              name="reviewStatus"
              value={value}
              checked={reviewStatus === value}
              onChange={handleClickRadioButton}
            />
            <S.Label $isSelected={reviewStatus === value}>{text}</S.Label>
          </S.StatusLabel>
        ))}
      </S.LabelList>
    </S.FilterContainer>
  );
};

export default RunnerPostFilter;

const appear = keyframes`
 0% {
  transform-origin:left;
  transform: scaleX(0);
 }
 100% {
  transform-origin:left;
  transform: scaleX(1);
 }
`;

const underLine = css`
  content: '';
  margin-top: 5px;
  height: 3px;
  width: calc(100% + 4px);
  border-radius: 1px;

  background-color: var(--baton-red);

  animation: 0.2s ease-in ${appear};
`;

const S = {
  FilterContainer: styled.div``,

  TitleContainer: styled.div`
    display: flex;
    align-items: flex-end;
    gap: 5px;

    width: 70px;

    @media (max-width: 768px) {
      width: fit-content;
    }
  `,

  Icon: styled.img`
    width: 22px;
  `,

  Title: styled.h3`
    font-size: 20px;
  `,

  LabelList: styled.li`
    display: flex;
    gap: 20px;

    list-style: none;

    @media (max-width: 768px) {
      gap: 12px;
    }
  `,

  StatusLabel: styled.label`
    display: flex;

    :hover {
      cursor: pointer;
    }
  `,

  RadioButton: styled.input`
    appearance: none;
  `,

  Label: styled.div<{ $isSelected: boolean }>`
    display: flex;
    flex-direction: column;
    align-items: center;

    width: fit-content;
    height: 30px;

    background-color: transparent;

    font-size: 18px;
    font-weight: 700;
    color: ${({ $isSelected }) => ($isSelected ? 'var(--baton-red)' : 'var(--gray-600)')};

    &::after {
      ${({ $isSelected }) => ($isSelected ? underLine : null)}
    }

    @media (max-width: 768px) {
      height: 22px;

      font-size: 14px;
    }
  `,
};
