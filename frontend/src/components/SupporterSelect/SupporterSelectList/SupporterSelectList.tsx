import { BATON_BASE_URL } from '@/constants';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import SupporterSelectItem from '../SupporterSelectItem/SupporterSelectItem';
import { GetSupporterCardResponse, SupporterCard } from '@/types/supporterCard';

interface Props {
  handleSelectButton: (selectedSupporter: SupporterCard) => void;
  selectedTechField: string | null;
}

const SupporterSelectList = ({ handleSelectButton, selectedTechField }: Props) => {
  const [supporterCardList, setSupporterCardList] = useState<GetSupporterCardResponse | null>(null);
  const [filteredSupporter, setFilteredSupporter] = useState<SupporterCard[] | null>(null);

  const getSupporterCardList = async () => {
    try {
      const response = await fetch(`${BATON_BASE_URL}/supporters/test`, {
        method: 'GET',
      });

      if (!response.ok) {
        throw new Error(`Error: ${response.status}`);
      }

      const supporterCardList = await response.json();

      return supporterCardList;
    } catch (error) {
      console.error(error);
    }
  };

  useEffect(() => {
    const fetchRunnerPost = async () => {
      const result = await getSupporterCardList();
      setSupporterCardList(result);
    };

    fetchRunnerPost();
  }, []);

  useEffect(() => {
    if (selectedTechField === '프론트엔드') {
      const frontend = supporterCardList?.data.filter((supporter: SupporterCard) => {
        const frontendCrew = ['가람', '에이든', '도리'];

        return frontendCrew.includes(supporter.name);
      });
      if (frontend) setFilteredSupporter(frontend);
    }

    if (selectedTechField === '백엔드') {
      const backend = supporterCardList?.data.filter((supporter: SupporterCard) => {
        const backendCrew = ['헤나', '주디', '박정훈', '김석호'];

        return backendCrew.includes(supporter.name);
      });
      if (backend) setFilteredSupporter(backend);
    }
  }, [supporterCardList, selectedTechField]);

  const selectedSupporter = (selectedSupporter: SupporterCard) => {
    handleSelectButton(selectedSupporter);
  };

  return (
    <S.SupporterSelectListContainer>
      {filteredSupporter?.map((card) => (
        <SupporterSelectItem key={card.supporterId} {...card} selectedSupporter={selectedSupporter} />
      ))}
    </S.SupporterSelectListContainer>
  );
};

export default SupporterSelectList;

const S = {
  SupporterSelectListContainer: styled.ul`
    display: grid;
    grid-template-columns: repeat(1, 1fr);
    column-gap: 27px;
    row-gap: 20px;

    padding: 0 0 20px 0;
  `,
};
