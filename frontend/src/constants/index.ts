import { ReviewStatus } from '@/types/runnerPost';

export const BATON_BASE_URL =
  process.env.NODE_ENV === 'development' ? 'http://15.164.179.87:8080/api/v1' : process.env.BASE_URL;

export const REVIEW_STATUS_LABEL_TEXT: { [key in ReviewStatus]: string } = {
  NOT_STARTED: '리뷰 대기중',
  IN_PROGRESS: '리뷰 진행중',
  DONE: '리뷰 완료',
};

export const ACCESS_TOKEN_LOCAL_STORAGE_KEY = 'ACCESS_TOKEN';
