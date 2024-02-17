import styled, { css, keyframes } from 'styled-components';
import Flex from '../Flex/Flex';
import Text from '../Text/Text';
import { Rank } from '@/types/rank';
import Banner from '@/components/Banner/Banner';
import { usePageRouter } from '@/hooks/usePageRouter';
import useViewport from '@/hooks/useViewport';
import { useEffect, useState } from 'react';
import RankerItem from './RankerItem';
import { DownArrowIcon, UpArrowIcon } from '@/assets/arrow-icon';

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
  min-width: 280px;
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
  const { isMobile } = useViewport();
  const [currentIndex, setCurrentIndex] = useState(0);
  // const [showMore, setShowMore] = useState(false);

  useEffect(() => {
    const interval = setInterval(() => {
      setCurrentIndex((prevIndex) => (prevIndex + 1) % data.length);
    }, 2500);

    return () => clearInterval(interval);
  }, [data.length, currentIndex]);

  const handleClickRanker = (id: number) => {
    goToSupporterProfilePage(id);
  };

  // const handleShowMore = () => {
  //   setShowMore((prevShowMore) => !prevShowMore);
  // };

  return (
    <ListWrapper>
      <ListContainer>
        {isMobile ? (
          <RankerItem
            supporter={data[currentIndex]}
            onClick={() => handleClickRanker(data[currentIndex].supporterId)}
          />
        ) : (
          data.map((supporter) => (
            <RankerItem supporter={supporter} onClick={() => handleClickRanker(supporter.supporterId)} />
          ))
        )}
      </ListContainer>
      {/* {isMobile && (
        <IconContainer justify="center" onClick={handleShowMore}>
          {showMore ? <UpArrowIcon /> : <DownArrowIcon />}
        </IconContainer>
      )} */}
    </ListWrapper>
  );
};
const ListWrapper = styled.div``;

const ListContainer = styled.ul`
  padding: 8px 10px;
`;

// const IconContainer = styled(Flex)`
//   padding: 5px;
// `;

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
