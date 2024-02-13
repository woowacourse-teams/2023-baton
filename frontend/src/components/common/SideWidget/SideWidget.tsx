import styled, { css } from 'styled-components';
import Flex from '../Flex/Flex';
import Text from '../Text/Text';
import { JavaIcon, SpringIcon } from '@/assets/technicalLabelIcon';
import { Rank } from '@/types/rank';
import TechLabel from '@/components/TechLabel/TechLabel';
import Avatar from '../Avatar/Avatar';
import { colors } from '@/styles/colorPalette';
import Banner from '@/components/Banner/Banner';
import { usePageRouter } from '@/hooks/usePageRouter';

interface SideWidgetProps {
  children: React.ReactNode;
  title: string;
}

const SideWidget = ({ children, title }: SideWidgetProps) => {
  return (
    <Container>
      <Flex direction="column" gap={10}>
        <Text typography="t4" $bold={true}>
          {title}
        </Text>
        <ContentsContainer>{children}</ContentsContainer>
      </Flex>
    </Container>
  );
};

const Container = styled.div`
  min-width: 250px;
  margin-bottom: 20px;
`;

const ContentsContainer = styled.div`
  border-radius: 12px;
  box-shadow: 1px 2px 4px rgba(0, 0, 0, 0.2);
`;

interface SideWidgetListProps {
  data: Rank[];
}

const SideWidgetList = ({ data }: SideWidgetListProps) => {
  const { goToSupporterProfilePage } = usePageRouter();

  const handleClickRanker = (id: number) => {
    goToSupporterProfilePage(id);
  };

  return (
    <div>
      <ListContainer>
        {data?.map((supporter) => {
          return (
            <ListWrapper key={supporter.supporterId} onClick={() => handleClickRanker(supporter.supporterId)}>
              <CustomFlex justify="space-between">
                <Flex gap={10} align="center">
                  <div>{supporter.rank}</div>
                  <Avatar width="15px" height="15px" imageUrl={supporter.imageUrl} />
                  <Text>{supporter.name}</Text>
                  <Flex gap={4}>
                    {supporter.technicalTags?.map((tech) => (
                      <TechLabel key={tech} tag={tech} hideText={true} />
                    ))}
                  </Flex>
                </Flex>
                <Flex gap={5}>
                  <Text>완료한 리뷰</Text>
                  <Text $bold={true}>{supporter.reviewedCount}</Text>
                </Flex>
              </CustomFlex>
            </ListWrapper>
          );
        })}
      </ListContainer>
    </div>
  );
};

const ListContainer = styled.ul`
  padding: 8px 10px;
`;

const ListWrapper = styled.li`
  padding: 10px 15px;

  & {
    cursor: pointer;
  }

  &:hover {
    background-color: ${colors.gray100};
  }
`;

const CustomFlex = styled(Flex)`
  gap: 10px;
`;

const SideWidgetBanner = () => {
  return (
    <BannerContainer>
      <Banner />
    </BannerContainer>
  );
};

const BannerContainer = styled.div`
  padding: 10px;
`;

SideWidget.List = SideWidgetList;
SideWidget.Banner = SideWidgetBanner;

export default SideWidget;
