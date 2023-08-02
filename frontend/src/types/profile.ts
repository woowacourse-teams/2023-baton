import { ReviewStatus } from './runnerPost';

export interface RunnerProfileResponse {
  profile: {
    name: string;
    imageUrl: string;
    githubUrl: string;
    introduction: string;
  };
  runnerPosts: ProfileRunnerPost[];
}

export interface ProfileRunnerPost {
  runnerPostId: number;
  title: string;
  deadline: string;
  tags: string[];
  reviewStatus: ReviewStatus;
}
