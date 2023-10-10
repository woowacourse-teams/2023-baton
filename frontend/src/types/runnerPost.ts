import { pageParamsRequest } from './api';

export type ReviewStatus = 'NOT_STARTED' | 'IN_PROGRESS' | 'DONE' | 'OVERDUE';
export type ReviewStatusFilter = ReviewStatus | 'ALL';

export interface GetRunnerPostResponse {
  data: RunnerPost[];
  pageInfo: PageInfo;
}

export interface RunnerPost {
  runnerPostId: number;
  title: string;
  deadline: string;
  tags: string[];
  runnerProfile?: RunnerProfile;
  watchedCount: number;
  applicantCount: number;
  reviewStatus: ReviewStatus;
}

export interface RunnerProfile {
  name: string;
  imageUrl: string;
}

export interface GetDetailedRunnerPostResponse extends RunnerPost {
  implementedContents: string;
  curiousContents: string;
  postscriptContents: string;
  isOwner: boolean;
  isApplied: boolean;
  pullRequestUrl: string;
  runnerProfile: DetailedRunnerProfile;
  supporter: Supporter;
}

export interface DetailedRunnerProfile extends RunnerProfile {
  runnerId: number;
  company: string;
}

export interface Supporter {
  supporterId: number;
  name: string;
}

export interface CreateRunnerPostRequest {
  title: string;
  tags: string[];
  pullRequestUrl: string;
  deadline: string;
  implementedContents: string;
  curiousContents: string;
  postscriptContents: string;
}

export interface PageInfo {
  isLast: boolean;
  nextCursor: number;
}

interface requestParams {
  tagName?: string;
  reviewStatus: ReviewStatus | null;
}

export interface getRunnerPostRequestParams extends pageParamsRequest, requestParams {}
