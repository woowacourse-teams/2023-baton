import { ReviewStatus } from './runnerPost';

export interface GetRunnerProfileResponse {
  profile: Profile;
  runnerPosts: ProfileRunnerPost[];
}

export interface ProfileRunnerPost {
  runnerPostId: number;
  title: string;
  deadline: string;
  tags: string[];
  reviewStatus: ReviewStatus;
}

export interface Profile {
  name: string;
  imageUrl: string;
  githubUrl: string;
  introduction: string;
}
