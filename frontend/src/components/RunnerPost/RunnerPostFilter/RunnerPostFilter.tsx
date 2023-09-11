import React from 'react';
import FilterIcon from '@/assets/filter-icon.svg';
import { styled } from 'styled-components';
import Label from '@/components/common/Label/Label';
import { REVIEW_STATUS_LABEL_TEXT } from '@/constants';
import useViewport from '@/hooks/useViewport';

interface Props {
  reviewStatus: string;
  handleClickRadioButton: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const RunnerPostFilter = ({ reviewStatus, handleClickRadioButton }: Props) => {
  const { isMobile } = useViewport();

  return (
    <S.FilterContainer>
      <S.TitleContainer>
        <S.Icon src={FilterIcon} />
        {isMobile ? null : <S.Title>Filter</S.Title>}
      </S.TitleContainer>
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
            <Label
              colorTheme={reviewStatus === value ? 'RED' : 'WHITE'}
              width={isMobile ? '72px' : ''}
              height={isMobile ? '22px' : '30px'}
              fontSize={isMobile ? '12px' : '18px'}
            >
              {text}
            </Label>
          </S.StatusLabel>
        ))}
      </S.LabelList>
    </S.FilterContainer>
  );
};

export default RunnerPostFilter;

const S = {
  FilterContainer: styled.div`
    display: flex;
    flex-direction: row;
    align-items: center;
    gap: 10px;
  `,

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
    gap: 10px;

    list-style: none;

    @media (max-width: 768px) {
      gap: 5px;
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
};
