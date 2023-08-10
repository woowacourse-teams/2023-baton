import { ReviewStatus } from './runnerPost';

export interface GetRunnerMyPageResponse {
  name: string;
  company: string;
  imageUrl: string;
  githubUrl: string;
  introduction: string;
  technicalLabels: string[];
  runnerPosts: MyPageRunnerPost[];
}

export interface MyPageRunnerPost {
  runnerPostId: number;
  title: string;
  deadline: string;
  tags: string[];
  watchedCount: number;
  applicantCount: number;
  reviewStatus: ReviewStatus;
  // 러너 마이페이지 게시글 api 나오면 수정 요망
}
