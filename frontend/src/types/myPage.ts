import { Profile } from './profile';
import { ReviewStatus } from './runnerPost';

export interface GetMyPageProfileResponse extends Profile {}

export interface GetMyPagePost {
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
}
