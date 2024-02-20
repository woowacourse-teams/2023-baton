import { request } from './fetch';
import {
  CreateRunnerPostRequest,
  GetDetailedRunnerPostResponse,
  GetOtherSupporterPostCountResponse,
  GetRunnerPostResponse,
  getRunnerPostRequestParams,
  PostCount,
} from '@/types/runnerPost';
import { GetSearchTagResponse } from '@/types/tags';
import {
  GetHeaderProfileResponse,
  GetRunnerProfileResponse,
  GetSupporterProfileResponse,
  PatchRunnerProfileRequest,
  PatchSupporterProfileRequest,
  getSupporterPostRequestParams,
} from '@/types/profile';
import { GetMyPagePostResponse, getMyPostRequestParams } from '@/types/myPage';
import { PostFeedbackRequest } from '@/types/feedback';
import { GetSupporterCandidateResponse } from '@/types/supporterCandidate';
import { GetNotificationResponse } from '@/types/notification';
import { GetSupporterRankResponse, Rank } from '@/types/rank';

export const getRunnerPost = ({ limit, reviewStatus, cursor, tagName }: getRunnerPostRequestParams) => {
  const params = new URLSearchParams({
    limit: limit.toString(),
    ...(cursor && { cursor: cursor.toString() }),
    ...(reviewStatus && { reviewStatus }),
    ...(tagName && { tagName }),
  });

  return request.get<GetRunnerPostResponse>(`/posts/runner?${params.toString()}`, false);
};

export const getMyRunnerPost = ({ limit, cursor, reviewStatus }: getMyPostRequestParams) => {
  const params = new URLSearchParams({
    limit: limit.toString(),
    ...(cursor && { cursor: cursor.toString() }),
    ...(reviewStatus && { reviewStatus }),
  });

  return request.get<GetMyPagePostResponse>(`/posts/runner/me/runner?${params.toString()}`, true);
};

export const getMySupporterPost = ({ limit, cursor, reviewStatus }: getMyPostRequestParams) => {
  const params = new URLSearchParams({
    limit: limit.toString(),
    ...(cursor && { cursor: cursor.toString() }),
    ...(reviewStatus && { reviewStatus }),
  });

  return request.get<GetMyPagePostResponse>(`/posts/runner/me/supporter?${params.toString()}`, true);
};

export const getOtherSupporterPost = ({ limit, cursor, supporterId }: getSupporterPostRequestParams) => {
  const params = new URLSearchParams({
    limit: limit.toString(),
    supporterId: supporterId.toString(),
    reviewStatus: 'DONE',
    ...(cursor && { cursor: cursor.toString() }),
  });

  return request.get<GetRunnerPostResponse>(`/posts/runner/search?${params.toString()}`, false);
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

export const getNotification = () => {
  return request.get<GetNotificationResponse>(`/notifications`, true);
};

export const getOtherRunnerProfile = (userId: number) => {
  return request.get<GetRunnerProfileResponse>(`/profile/runner/${userId}`, false);
};

export const getOtherSupporterProfile = (userId: number) => {
  return request.get<GetRunnerProfileResponse>(`/profile/supporter/${userId}`, false);
};

export const getOtherSupporterPostCount = (userId: number) => {
  return request.get<GetOtherSupporterPostCountResponse>(`/posts/runner/search/count?supporterId=${userId}`, false);
};

export const getSupporterRank = () => {
  return request.get<GetSupporterRankResponse>('/rank/supporter', false);
};

export const getPostCount = () => {
  return request.get<PostCount>('/posts/runner/count', false);
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

export const patchNotificationCheck = (notificationId: number) => {
  return request.patch<void>(`/notifications/${notificationId}`, undefined);
};

export const deleteRunnerPost = (runnerPostId: number) => {
  return request.delete<void>(`/posts/runner/${runnerPostId}`);
};

export const deleteNotification = (notificationsId: number) => {
  return request.delete<void>(`/notifications/${notificationsId}`);
};
