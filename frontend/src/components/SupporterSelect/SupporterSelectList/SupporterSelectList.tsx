import { BATON_BASE_URL } from '@/constants';
import React, { useEffect, useState } from 'react';
import styled from 'styled-components';
import SupporterSelectItem from '../SupporterSelectItem/SupporterSelectItem';
import { GetSupporterCardResponse, SupporterCard } from '@/types/supporterCard';

interface Props {
  handleSelectButton: (selectedSupporter: SupporterCard) => void;
}

const SupporterSelectList = ({ handleSelectButton }: Props) => {
  const [supporterCardList, setSupporterCardList] = useState<GetSupporterCardResponse | null>(null);

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

  const selectedSupporter = (selectedSupporter: SupporterCard) => {
    handleSelectButton(selectedSupporter);
  };

  return (
    <S.SupporterSelectListContainer>
      {supporterCardList?.data.map((card) => (
        <SupporterSelectItem key={card.supporterId} {...card} selectedSupporter={selectedSupporter} />
      ))}
    </S.SupporterSelectListContainer>
  );
};

export default SupporterSelectList;

const S = {
  SupporterSelectListContainer: styled.ul`
    display: grid;
    grid-template-columns: repeat(2, 1fr);
    column-gap: 27px;
    row-gap: 20px;

    padding: 0 0 20px 0;
  `,
};
