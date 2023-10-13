import { pageParamsRequest } from './api';
import { Profile } from './profile';
import { PageInfo, ReviewStatus } from './runnerPost';

export interface GetMyPageProfileResponse extends Profile {}

export interface GetMyPagePostResponse {
  pageInfo: PageInfo;
  data: MyPagePost[];
}

export interface MyPagePost {
  runnerPostId: number;
  title: string;
  deadline: string;
  tags: string[];
  watchedCount: number;
  applicantCount: number;
  reviewStatus: ReviewStatus;
  supporterId: number;
}

interface requestParams {
  reviewStatus?: ReviewStatus;
}

export interface getMyPostRequestParams extends pageParamsRequest, requestParams {}
