import { CreateRunnerPostRequest, GetRunnerPostResponse, ReviewStatus } from '@/types/runnerPost';
import { request } from './fetch';
import { GetSearchTagResponse } from '@/types/tags';
import {
  GetHeaderProfileResponse,
  GetRunnerProfileResponse,
  GetSupporterProfileResponse,
  PatchRunnerProfileRequest,
  PatchSupporterProfileRequest,
} from '@/types/profile';

export const getRunnerPost = (limit: number, reviewStatus?: ReviewStatus, cursor?: any, tagName?: string) => {
  const params = new URLSearchParams({
    limit: limit.toString(),
    ...(cursor && { cursor: cursor.toString() }),
    ...(reviewStatus && { reviewStatus }),
    ...(tagName && { tagName }),
  });

  return request.get<GetRunnerPostResponse>(`/posts/runner?${params.toString()}`, false);
};

export const getSearchTag = (keyword: string) => {
  return request.get<GetSearchTagResponse>(`/tags/search?tagName=${keyword}`, false);
};

export const getHeaderProfile = () => {
  return request.get<GetHeaderProfileResponse>('/profile/me', true);
};

export const getMyRunnerProfile = () => {
  return request.get<GetRunnerProfileResponse>('/profile/runner/me', true);
};

export const getMySupporterProfile = () => {
  return request.get<GetSupporterProfileResponse>('/profile/supporter/me', true);
};

export const getProposedSupporterList = async (runnerPostId: number) => {
  return request.get<GetSupporterCandidateResponse>(`/posts/runner/${runnerPostId}/supporters`, true);
};

export const postRunnerPostCreation = async (formData: CreateRunnerPostRequest) => {
  const body = JSON.stringify(formData);
  return request.post<void>(`/posts/runner`, body);
};

export const patchReviewCancelation = (runnerPostId: number) => {
  return request.patch<void>(`/posts/runner/${runnerPostId}/cancelation`);
};

export const patchReviewComplete = (runnerPostId: number) => {
  return request.patch<void>(`/posts/runner/${runnerPostId}/done`);
};

export const patchMyRunnerProfile = async (formData: PatchRunnerProfileRequest) => {
  const body = JSON.stringify(formData);
  return request.patch<void>(`/profile/runner/me`, body);
};

export const patchMySupporterProfile = async (formData: PatchSupporterProfileRequest) => {
  const body = JSON.stringify(formData);
  return request.patch<void>(`/profile/runner/me`, body);
};

export const patchProposedSupporterSelection = async (runnerPostId: number, supporterId: number) => {
  const body = JSON.stringify({ supporterId });
  return request.patch<void>(`/posts/runner/${runnerPostId}/supporters`, body);
};

