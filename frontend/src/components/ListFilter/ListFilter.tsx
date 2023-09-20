import { ListSelectOption } from '@/types/select';
import React from 'react';
import styled, { css, keyframes } from 'styled-components';

interface Props {
  options: ListSelectOption[];
  selectOption: (value: string | number) => void;
  width?: string;
  fontSize?: string;
}

const ListFilter = ({ options, selectOption, width, fontSize }: Props) => {
  const makeHandleClickOption = (value: string | number) => () => {
    if (options.filter((option) => option.value === value).length === 0) return;

    selectOption(value);
  };

  return (
    <S.FilterContainer>
      <S.FilterList $width={width}>
        {options.map((option) => (
          <S.ButtonWrapper key={option.value}>
            <S.FilterItem>
              <S.FilterButton
                onClick={makeHandleClickOption(option.value)}
                $isSelected={option.selected}
                $fontSize={fontSize}
              >
                {option.label}
              </S.FilterButton>
            </S.FilterItem>
          </S.ButtonWrapper>
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
    min-width: 320px;
    padding: 0 15px;

    @media (max-width: 468px) {
      padding: 0;
    }
  `,

  FilterItem: styled.li`
    display: flex;
    justify-content: center;

    @media (max-width: 768px) {
      flex: 1;
    }
  `,

  FilterButton: styled.button<{ $isSelected: boolean; $fontSize?: string }>`
    display: flex;
    flex-direction: column;
    align-items: center;

    background-color: transparent;

    font-size: ${({ $fontSize }) => $fontSize ?? '26px'};
    font-weight: 700;
    color: ${({ $isSelected }) => ($isSelected ? 'var(--baton-red)' : 'var(--gray-700)')};

    &::after {
      ${({ $isSelected }) => ($isSelected ? underLine : null)}
    }
  `,

  ButtonWrapper: styled.div`
    width: 150px;
  `,
};
