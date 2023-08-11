import { ReviewStatus } from './runnerPost';

export interface GetMyPageProfileResponse {
  name: string;
  company: string;
  imageUrl: string;
  githubUrl: string;
  introduction: string;
  technicalTags: string[];
}

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
