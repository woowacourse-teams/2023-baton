import { JavaIcon, JavascriptIcon, ReactIcon, SpringIcon, TypescriptIcon } from '@/assets/technicalLabelIcon';
import React from 'react';
import { styled } from 'styled-components';

interface TechData {
  icon: JSX.Element;
  labelColor: string;
}

interface Props {
  tag: string;
  hideText?: boolean;
}

const techMapping: Record<string, TechData> = {
  javascript: { icon: <JavascriptIcon />, labelColor: '#ffedcc' },
  typescript: { icon: <TypescriptIcon />, labelColor: '#dceeff' },
  react: { icon: <ReactIcon />, labelColor: '#cef2fc' },
  java: { icon: <JavaIcon />, labelColor: '#b0d8dd' },
  spring: { icon: <SpringIcon />, labelColor: '#c5eea9' },
};

const TechLabel = ({ tag, hideText }: Props) => {
  const techData = techMapping[tag];

  return (
    <S.TagContainer $labelColor={techData.labelColor} $hideText={hideText}>
      {techData.icon}
      {hideText ? null : <p>{tag}</p>}
    </S.TagContainer>
  );
};

export default TechLabel;

const S = {
  TagContainer: styled.div<{ $labelColor: string; $hideText: boolean | undefined }>`
    display: flex;
    align-items: center;
    gap: 4px;

    padding: ${({ $hideText }) => ($hideText ? 0 : '1px 8px')};

    font-size: ${({ $hideText }) => ($hideText ? '14px' : '12px')};
    line-height: 18px;

    border-radius: 2em;

    background-color: ${({ $labelColor }) => $labelColor};
  `,
};
