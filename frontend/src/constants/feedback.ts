import { DescriptionOptions, ReviewTypeOptions } from '@/types/feedback';

export const REVIEW_TYPE = ['GREAT', 'GOOD', 'BAD'] as const;

export const REVIEW_TYPE_OPTIONS: ReviewTypeOptions = [
  {
    value: 'BAD',
    label: '별로에요',
    selected: false,
  },
  {
    value: 'GOOD',
    label: '좋았어요',
    selected: false,
  },

  {
    value: 'GREAT',
    label: '최고에요',
    selected: false,
  },
];

export const DESCRIPTION_OPTIONS_GOOD: DescriptionOptions = [
  {
    value: '제가 생각하지 못한 부분까지 꼼꼼하게 리뷰해주셨어요',
    label: '제가 생각하지 못한 부분까지 꼼꼼하게 리뷰해주셨어요',
    selected: false,
  },
  {
    value: '리뷰까지 걸리는 시간이 짧았어요',
    label: '리뷰까지 걸리는 시간이 짧았어요',
    selected: false,
  },
  {
    value: '시간 약속을 잘 지켜요',
    label: '시간 약속을 잘 지켜요',
    selected: false,
  },
  {
    value: '응답이 빨라요',
    label: '응답이 빨라요',
    selected: false,
  },
];

export const DESCRIPTION_OPTIONS_BAD: DescriptionOptions = [
  {
    value: '리뷰가 꼼꼼하지 못해요',
    label: '리뷰가 꼼꼼하지 못해요',
    selected: false,
  },
  {
    value: '리뷰까지 걸리는 시간이 길었어요',
    label: '리뷰까지 걸리는 시간이 길었어요',
    selected: false,
  },
  {
    value: '약속을 잘 지키지 못해요',
    label: '약속을 잘 지키지 못해요',
    selected: false,
  },
  {
    value: '응답이 느려요',
    label: '응답이 느려요',
    selected: false,
  },
];
