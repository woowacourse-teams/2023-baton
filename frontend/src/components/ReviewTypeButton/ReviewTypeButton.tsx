import React, { useRef } from 'react';
import GreatFeedbackIcon from '../../assets/feedback-icon/great-feedback-icon.svg';
import GoodFeedbackIcon from '../../assets/feedback-icon/good-feedback-icon.svg';
import BadFeedbackIcon from '../../assets/feedback-icon/bad-feedback-icon.svg';
import GreatFeedbackGrayIcon from '../../assets/feedback-icon/great-feedback-gray-icon.svg';
import GoodFeedbackGrayIcon from '../../assets/feedback-icon/good-feedback-gray-icon.svg';
import BadFeedbackGrayIcon from '../../assets/feedback-icon/bad-feedback-gray-icon.svg';
import { ReviewType } from '@/types/feedback';
import { styled } from 'styled-components';

interface Props extends React.HTMLProps<HTMLImageElement> {
  isSelected: boolean;
  value: ReviewType;
  label?: string;
  handelClick?: () => void;
}

const ReviewTypeButton = ({ isSelected, value, label, handelClick, ...rest }: Props) => {
  const iconRef = useRef<HTMLImageElement>(null);

  const handleMouseHover = (e: React.PointerEvent<HTMLButtonElement>) => {
    if (!iconRef.current) return;

    switch (value) {
      case 'BAD':
        iconRef.current.src = BadFeedbackIcon;
        break;
      case 'GOOD':
        iconRef.current.src = GoodFeedbackIcon;
        break;
      case 'GREAT':
        iconRef.current.src = GreatFeedbackIcon;
    }
  };

  const handleMouseLeave = (e: React.PointerEvent<HTMLButtonElement>) => {
    if (!iconRef.current) return;
    switch (value) {
      case 'BAD':
        iconRef.current.src = BadFeedbackGrayIcon;
        break;
      case 'GOOD':
        iconRef.current.src = GoodFeedbackGrayIcon;
        break;
      case 'GREAT':
        iconRef.current.src = GreatFeedbackGrayIcon;
    }
  };

  if (isSelected) {
    switch (value) {
      case 'BAD':
        return (
          <S.ReviewTypeItem>
            <S.Button>
              <img src={BadFeedbackIcon} {...rest} />
              {label && <S.ReviewTypeLabel $isSelect={isSelected}>{label}</S.ReviewTypeLabel>}
            </S.Button>
          </S.ReviewTypeItem>
        );
      case 'GOOD':
        return (
          <S.ReviewTypeItem>
            <S.Button>
              <img src={GoodFeedbackIcon} {...rest} />
              {label && <S.ReviewTypeLabel $isSelect={isSelected}>{label}</S.ReviewTypeLabel>}
            </S.Button>
          </S.ReviewTypeItem>
        );
      case 'GREAT':
        return (
          <S.ReviewTypeItem>
            <S.Button>
              <img src={GreatFeedbackIcon} {...rest} />
              {label && <S.ReviewTypeLabel $isSelect={isSelected}>{label}</S.ReviewTypeLabel>}
            </S.Button>
          </S.ReviewTypeItem>
        );
      default:
        return null;
    }
  }

  switch (value) {
    case 'BAD':
      return (
        <S.ReviewTypeItem>
          <S.Button onMouseOver={handleMouseHover} onMouseLeave={handleMouseLeave} onClick={handelClick}>
            <img src={BadFeedbackGrayIcon} {...rest} ref={iconRef} />
            {label && <S.ReviewTypeLabel $isSelect={isSelected}>{label}</S.ReviewTypeLabel>}
          </S.Button>
        </S.ReviewTypeItem>
      );
    case 'GOOD':
      return (
        <S.ReviewTypeItem>
          <S.Button onMouseOver={handleMouseHover} onMouseLeave={handleMouseLeave} onClick={handelClick}>
            <img src={GoodFeedbackGrayIcon} {...rest} ref={iconRef} />
            {label && <S.ReviewTypeLabel $isSelect={isSelected}>{label}</S.ReviewTypeLabel>}
          </S.Button>
        </S.ReviewTypeItem>
      );
    case 'GREAT':
      return (
        <S.ReviewTypeItem>
          <S.Button onMouseOver={handleMouseHover} onMouseLeave={handleMouseLeave} onClick={handelClick}>
            <img src={GreatFeedbackGrayIcon} {...rest} ref={iconRef} />
            {label && <S.ReviewTypeLabel $isSelect={isSelected}>{label}</S.ReviewTypeLabel>}
          </S.Button>
        </S.ReviewTypeItem>
      );
    default:
      return null;
  }
};

export default ReviewTypeButton;

const S = {
  ReviewTypeItem: styled.li``,

  Button: styled.button`
    display: flex;
    flex-direction: column;
    align-items: center;
    gap: 15px;

    background-color: transparent;

    &:hover {
      & > div {
        color: var(--gray-800);
      }
    }
  `,

  ReviewTypeLabel: styled.div<{ $isSelect: boolean }>`
    font-size: 18px;
    font-weight: 600;
    color: ${({ $isSelect }) => ($isSelect ? 'var(--gray-800)' : 'var(--gray-400)')};
  `,
};
