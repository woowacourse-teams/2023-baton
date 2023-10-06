import { request } from './fetch';
import {
  CreateRunnerPostRequest,
  GetDetailedRunnerPostResponse,
  GetRunnerPostResponse,
  ReviewStatus,
} from '@/types/runnerPost';
import { GetSearchTagResponse } from '@/types/tags';
import {
  GetHeaderProfileResponse,
  GetRunnerProfileResponse,
  GetSupporterProfileResponse,
  PatchRunnerProfileRequest,
  PatchSupporterProfileRequest,
} from '@/types/profile';
import { GetMyPagePostResponse } from '@/types/myPage';
import { PostFeedbackRequest } from '@/types/feedback';
import { GetSupporterCandidateResponse } from '@/types/supporterCandidate';
import { ACCESS_TOKEN_LOCAL_STORAGE_KEY, BATON_BASE_URL } from '@/constants';

export const getRunnerPost = (limit: number, reviewStatus?: ReviewStatus, cursor?: number, tagName?: string) => {
  const params = new URLSearchParams({
    limit: limit.toString(),
    ...(cursor && { cursor: cursor.toString() }),
    ...(reviewStatus && { reviewStatus }),
    ...(tagName && { tagName }),
  });

  return request.get<GetRunnerPostResponse>(`/posts/runner?${params.toString()}`, false);
};

export const getMyRunnerPost = (size: number, page: number, reviewStatus?: ReviewStatus) => {
  const params = new URLSearchParams({
    size: size.toString(),
    ...(page && { page: page.toString() }),
    ...(reviewStatus && { reviewStatus }),
  });

  return request.get<GetMyPagePostResponse>(`/posts/runner/me/runner?${params.toString()}`, true);
};

export const getMySupporterPost = (size: number, page: number, reviewStatus?: ReviewStatus) => {
  const params = new URLSearchParams({
    size: size.toString(),
    ...(page && { page: page.toString() }),
    ...(reviewStatus && { reviewStatus }),
  });

  return request.get<GetMyPagePostResponse>(`/posts/runner/me/supporter?${params.toString()}`, true);
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

export const getProposedSupporterList = (runnerPostId: number) => {
  return request.get<GetSupporterCandidateResponse>(`/posts/runner/${runnerPostId}/supporters`, true);
};

export const getRunnerPostDetail = (runnerPostId: number, isLogin: boolean) => {
  return request.get<GetDetailedRunnerPostResponse>(`/posts/runner/${runnerPostId}`, isLogin);
};

export const getOtherRunnerProfile = (userId: number) => {
  return request.get<GetRunnerProfileResponse>(`/profile/runner/${userId}`, false);
};

export const getOtherSupporterProfile = (userId: number) => {
  return request.get<GetRunnerProfileResponse>(`/profile/supporter/${userId}`, false);
};

export const getOtherSupporterPost = (supporterId: number) => {
  const params = new URLSearchParams([
    ['supporterId', supporterId.toString()],
    ['reviewStatus', 'DONE'],
  ]);

  return request.get<GetRunnerPostResponse>(`/posts/runner/search?${params.toString()}`, false);
};

export const getLoginToken = (authCode: string) => {
  return fetch(`${BATON_BASE_URL}/oauth/login/github?code=${authCode}`, {
    method: 'GET',
    headers: {
      credentials: 'include',
    },
  });
};

export const postRunnerPostCreation = (formData: CreateRunnerPostRequest) => {
  const body = JSON.stringify(formData);
  return request.post<void>(`/posts/runner`, body);
};

export const postReviewSuggestionWithMessage = (runnerPostId: number, message: string) => {
  const body = JSON.stringify({ message });
  return request.post<void>(`/posts/runner/${runnerPostId}/application`, body);
};

export const postFeedbackToSupporter = (formData: PostFeedbackRequest) => {
  const body = JSON.stringify(formData);
  return request.post<void>(`/feedback/supporter`, body);
};

export const postMissionBranchCreation = (repoName: string) => {
  const body = JSON.stringify({ repoName });
  return request.post<void>(`/branch`, body);
};

export const postRefreshToken = () => {
  return fetch(`${BATON_BASE_URL}/oauth/refresh`, {
    method: 'POST',
    headers: {
      Authorization: `Bearer ${localStorage.getItem(ACCESS_TOKEN_LOCAL_STORAGE_KEY)}`,
      credentials: 'include',
    },
  });
};

export const patchReviewCancelation = (runnerPostId: number) => {
  return request.patch<void>(`/posts/runner/${runnerPostId}/cancelation`);
};

export const patchReviewComplete = (runnerPostId: number) => {
  return request.patch<void>(`/posts/runner/${runnerPostId}/done`);
};

export const patchMyRunnerProfile = (formData: PatchRunnerProfileRequest) => {
  const body = JSON.stringify(formData);
  return request.patch<void>(`/profile/runner/me`, body);
};

export const patchMySupporterProfile = (formData: PatchSupporterProfileRequest) => {
  const body = JSON.stringify(formData);
  return request.patch<void>(`/profile/runner/me`, body);
};

export const patchProposedSupporterSelection = (runnerPostId: number, supporterId: number) => {
  const body = JSON.stringify({ supporterId });
  return request.patch<void>(`/posts/runner/${runnerPostId}/supporters`, body);
};

export const deleteRunnerPost = (runnerPostId: number) => {
  return request.delete<void>(`/posts/runner/${runnerPostId}`);
};
