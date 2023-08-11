import { ReviewStatus } from './runnerPost';
import { TechnicsType } from './tags';

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

export interface RunnerProfileResponse extends Profile {}

export interface SupporterProfileResponse extends Profile {}

export interface RunnerProfileRequest extends ProfileRequest {}

export interface SupporterProfileRequest extends ProfileRequest {}

export type ProfileRequest = Omit<Profile, 'githubUrl' | 'imageUrl'>;

export interface Profile {
  name: string;
  company: string;
  imageUrl: string;
  githubUrl: string;
  introduction: string;
  technicalTags: TechnicsType[];
}
