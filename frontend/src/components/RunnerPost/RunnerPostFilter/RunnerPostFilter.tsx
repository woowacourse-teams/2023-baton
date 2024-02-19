import React from 'react';
import { css, keyframes, styled } from 'styled-components';
import { REVIEW_STATUS_FILTER_TEXT } from '@/constants';
import Text from '@/components/common/Text/Text';
import Flex from '@/components/common/Flex/Flex';
import { PostCount, ReviewStatusFilter } from '@/types/runnerPost';
import { usePostCount } from '@/hooks/query/usePostCount';
import useViewport from '@/hooks/useViewport';

interface Props {
  reviewStatus: ReviewStatusFilter;
  handleClickRadioButton: (e: React.ChangeEvent<HTMLInputElement>) => void;
}

const RunnerPostFilter = ({ reviewStatus, handleClickRadioButton }: Props) => {
  const { data: totalCount } = usePostCount();
  const { isMobile } = useViewport();

  function transformPostCountToReviewStatusFilter(postCount: PostCount): { [key in ReviewStatusFilter]: number } {
    const all = Object.values(postCount).reduce((acc, currentValue) => acc + currentValue, 0);

    return {
      NOT_STARTED: postCount.notStarted,
      IN_PROGRESS: postCount.inProgress,
      OVERDUE: postCount.overdue,
      DONE: postCount.done,
      ALL: all,
    };
  }

  if (!totalCount) {
    return <div style={isMobile ? { height: '38px' } : { height: '46px' }}></div>;
  }

  const reviewStatusCounts = transformPostCountToReviewStatusFilter(totalCount);

  return (
    <S.FilterContainer>
      <S.LabelList>
        {Object.entries(REVIEW_STATUS_FILTER_TEXT).map(([value, text]) => {
          const isSelected = reviewStatus === value;

          return (
            <Flex align="center" key={value}>
              <S.StatusLabel>
                <S.RadioButton
                  type="radio"
                  name="reviewStatus"
                  value={value}
                  checked={isSelected}
                  onChange={handleClickRadioButton}
                />
                <S.Label $isSelected={isSelected}>
                  <Flex align="end" gap={3}>
                    <Text color={isSelected ? 'red' : 'gray600'}>{text}</Text>
                    <Text color={isSelected ? 'red' : 'gray600'} typography="t8">
                      ({reviewStatusCounts[value as ReviewStatusFilter]})
                    </Text>
                  </Flex>
                </S.Label>
              </S.StatusLabel>
            </Flex>
          );
        })}
      </S.LabelList>
    </S.FilterContainer>
  );
};

export default RunnerPostFilter;

const appear = keyframes`
 0% {
  transform: scaleX(0);
 }
 100% {
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

  transform-origin: left;

  animation: 0.2s ease-in ${appear};
`;

const S = {
  FilterContainer: styled.ul`
    width: max-content;

    @media (max-width: 768px) {
      max-width: 340px;
      overflow-x: auto;
      white-space: nowrap;
    }
  `,

  LabelList: styled.li`
    display: flex;
    gap: 20px;

    list-style: none;

    @media (max-width: 768px) {
      gap: 14px;
    }
  `,

  StatusLabel: styled.label`
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

    height: 30px;

    background-color: transparent;

    font-size: 18px;
    font-weight: 700;

    &::after {
      ${({ $isSelected }) => ($isSelected ? underLine : null)}
    }

    @media (max-width: 768px) {
      height: 22px;

      font-size: 14px;
    }
  `,
};
