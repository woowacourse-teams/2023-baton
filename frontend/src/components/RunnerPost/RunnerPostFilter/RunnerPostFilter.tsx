import React from 'react';
import FilterIcon from '@/assets/filter-icon.svg';
import { styled } from 'styled-components';
import Label from '@/components/common/Label/Label';
import { REVIEW_STATUS_LABEL_TEXT } from '@/constants';

interface Props {
  reviewStatus: string;
  handleClickRadioButton: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const RunnerPostFilter = ({ reviewStatus, handleClickRadioButton }: Props) => {
  return (
    <S.FilterContainer>
      <S.TitleContainer>
        <S.Icon src={FilterIcon} />
        <S.Title>Filter</S.Title>
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
            <Label colorTheme={reviewStatus === value ? 'RED' : 'WHITE'} width="100px" height="30px">
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
