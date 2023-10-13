import React from 'react';
import { SelectOption } from '@/types/select';
import { CheckBoxIcon } from '@/assets/checkboxIcon';
import { styled } from 'styled-components';

interface Props {
  options: SelectOption<string>[];
  selectOption: (value: string) => void;
}

const CheckBox = ({ options, selectOption }: Props) => {
  const makeHandleClickOption = (value: string) => () => {
    if (options.filter((option) => option.value === value).length === 0) return;

    selectOption(value);
  };

  return (
    <S.CheckBoxContainer>
      <S.CheckBoxList>
        {options.map((option) => (
          <S.CheckBoxItem key={option.value}>
            <S.CheckBoxButton onClick={makeHandleClickOption(option.value)}>
              <CheckBoxIcon isSelected={option.selected} />
              {option.label}
            </S.CheckBoxButton>
          </S.CheckBoxItem>
        ))}
      </S.CheckBoxList>
      <S.SurveyAnchor href="https://forms.gle/8szmPaaJue7tCumLA" target="_blank">
        ▶︎ 바톤을 이용하면서 불편했던 부분이 있었나요?
      </S.SurveyAnchor>
    </S.CheckBoxContainer>
  );
};

export default CheckBox;

const S = {
  CheckBoxContainer: styled.div`
    display: flex;
    flex-direction: column;
    gap: 40px;

    padding: 35px 20px;
  `,

  CheckBoxList: styled.ul`
    display: flex;
    flex-direction: column;
    gap: 15px;
  `,

  CheckBoxButton: styled.button`
    display: flex;
    align-items: center;
    gap: 20px;

    background-color: transparent;
  `,

  CheckBoxItem: styled.div`
    font-size: 18px;
    color: var(--gray-800);
  `,

  SurveyAnchor: styled.a`
    font-size: 20px;
    font-weight: 700;
    text-align: center;

    &:hover {
      text-decoration: underline;
    }
  `,
};
