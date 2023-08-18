import { ListSelectOption } from '@/types/select';
import React from 'react';
import styled, { css, keyframes } from 'styled-components';

interface Props {
  options: ListSelectOption[];
  selectOption: (value: string | number) => void;
  width?: string;
}

const ListFilter = ({ options, selectOption, width }: Props) => {
  const makeHandleClickOption = (value: string | number) => () => {
    if (options.filter((option) => option.value === value).length === 0) return;

    selectOption(value);
  };

  return (
    <S.FilterContainer>
      <S.FilterList $width={width}>
        {options.map((option) => (
          <S.FilterItem>
            <S.FilterButton
              key={option.value}
              onClick={makeHandleClickOption(option.value)}
              $isSelected={option.selected}
            >
              {option.label}
            </S.FilterButton>
          </S.FilterItem>
        ))}
      </S.FilterList>
    </S.FilterContainer>
  );
};

export default ListFilter;

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
  width: calc(100% + 10px);
  border-radius: 1px;

  background-color: var(--baton-red);

  animation: 0.3s ease-in ${appear};
`;

const S = {
  FilterContainer: styled.div``,

  FilterList: styled.ul<{ $width?: string }>`
    display: flex;
    justify-content: space-between;

    width: ${({ $width }) => $width ?? '920px'};
  `,

  FilterItem: styled.li`
    width: 150px;
  `,

  FilterButton: styled.button<{ $isSelected: boolean }>`
    display: flex;
    flex-direction: column;
    align-items: center;

    background-color: transparent;

    font-size: 26px;
    font-weight: 700;
    color: ${({ $isSelected }) => ($isSelected ? 'var(--baton-red)' : 'var(--gray-700)')};

    &::after {
      ${({ $isSelected }) => ($isSelected ? underLine : null)}
    }
  `,
};
