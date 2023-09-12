import { ReviewStatus } from '@/types/runnerPost';

export const BATON_BASE_URL =
  process.env.NODE_ENV === 'development' ? 'http://15.164.179.87/api/v1' : process.env.REACT_APP_BASE_URL;

export const REVIEW_STATUS_LABEL_TEXT: { [key in ReviewStatus]: string } = {
  NOT_STARTED: '리뷰 대기중',
  IN_PROGRESS: '리뷰 진행중',
  DONE: '리뷰 완료',
  OVERDUE: '기간 만료',
};

export const REVIEW_STATUS: ReviewStatus[] = Object.keys(REVIEW_STATUS_LABEL_TEXT) as ReviewStatus[];
export const RUNNER_POST_OPTIONS = ['대기중인 리뷰', '진행중인 리뷰', '완료된 리뷰'];
export const SUPPORTER_POST_OPTIONS = ['신청한 리뷰', '진행중인 리뷰', '완료된 리뷰'];

export const ACCESS_TOKEN_LOCAL_STORAGE_KEY = 'ACCESS_TOKEN';

export const CHANNEL_SERVICE_KEY = process.env.REACT_APP_CHANNELTALK_KEY;
