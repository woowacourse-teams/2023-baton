import { pageParamsRequest } from './api';
import { Technic } from './tags';

export interface GetRunnerProfileResponse extends Profile {}

export interface GetSupporterProfileResponse extends Profile {}

export interface PatchRunnerProfileRequest extends ProfileRequest {}

export interface PatchSupporterProfileRequest extends ProfileRequest {}

export type ProfileRequest = Omit<Profile, 'githubUrl' | 'imageUrl'>;

export type GetHeaderProfileResponse = Pick<Profile, 'name' | 'imageUrl'>;

export interface Profile {
  name: string;
  company: string;
  imageUrl: string;
  githubUrl: string;
  introduction: string;
  technicalTags: Technic[];
}

interface requestParams {
  supporterId: number;
}

export interface getSupporterPostRequestParams extends pageParamsRequest, requestParams {}
