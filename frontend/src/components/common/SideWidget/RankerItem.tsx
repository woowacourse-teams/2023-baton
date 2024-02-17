import styled, { keyframes } from 'styled-components';
import Avatar from '../Avatar/Avatar';
import Flex from '../Flex/Flex';
import Text from '../Text/Text';
import TechLabel from '@/components/TechLabel/TechLabel';
import { Rank } from '@/types/rank';
import { colors } from '@/styles/colorPalette';

interface RankerItemProps {
  supporter: Rank;
  onClick: () => void;
}

const RankerItem = ({ supporter, onClick }: RankerItemProps) => {
  return (
    <ListWrapper key={supporter.supporterId} onClick={onClick}>
      <Flex justify="space-between" gap={10}>
        <Flex gap={10} align="center">
          <Text typography="t7">{supporter.rank}</Text>
          <Avatar width="35px" height="35px" imageUrl={supporter.imageUrl} />
          <Flex direction="column" align="start" gap={8}>
            <Flex direction="column" align="start">
              <Text>{supporter.name}</Text>
              <Text typography="t8" color="gray500">
                @{supporter.company}
              </Text>
            </Flex>
            <Flex gap={4}>
              {supporter.technicalTags?.map((tech) => (
                <TechLabel key={tech} tag={tech} hideText={true} />
              ))}
            </Flex>
          </Flex>
        </Flex>
        <CustomFlex gap={5} align="end" justify="end">
          <Text>완료한 리뷰</Text>
          <Text $bold={true}>{supporter.reviewedCount}</Text>
        </CustomFlex>
      </Flex>
    </ListWrapper>
  );
};

const fadeIn = keyframes`
  from {
    opacity: 0;
    transform: translateY(20px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
`;

const ListWrapper = styled.li`
  padding: 10px 15px;

  & {
    cursor: pointer;
  }

  &:hover {
    background-color: ${colors.gray100};
  }

  @media (max-width: 768px) {
    animation: ${fadeIn} 0.5s ease-out forwards;
  }
`;

const CustomFlex = styled(Flex)`
  min-width: 100px;
`;

export default RankerItem;
