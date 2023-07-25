export type ReviewStatus = 'NOT_STARTED' | 'IN_PROGRESS' | 'DONE';

export interface RunnerPostData {
  runnerPostId: number;
  title: string;
  deadline: string;
  tags: string[];
  name: string;
  imageUrl: string;
  watchedCount: number;
  chattingCount: number;
  reviewStatus: ReviewStatus;
}

export interface RunnerPost {
  runnerPosts: RunnerPostData[];
}
