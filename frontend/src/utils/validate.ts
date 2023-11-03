import { isPastTime } from './date';
import { checkCorrectPullRequestUrl } from './githubUrl';

export const validateTitle = (title: string) => {
  if (!title) {
    throw new Error('제목을 입력해주세요');
  }
};

export const validateTags = (tags: string[]) => {
  if (tags.some((tag) => tag.length > 15)) {
    throw new Error('태그명은 15자 이내로 입력해주세요.');
  }

  if (tags.length > 5) {
    throw new Error('태그는 최대 5개 입력할 수 있습니다.');
  }

  const tagSet = new Set(tags);
  if (tags.length !== tagSet.size) {
    throw new Error('중복된 태그는 입력 불가합니다.');
  }
};

export const validatePullRequestUrl = (pullRequestUrl: string) => {
  if (!pullRequestUrl) {
    throw new Error('PR 주소를 입력해주세요');
  }

  if (!checkCorrectPullRequestUrl(pullRequestUrl))
    throw new Error('올바른 URL주소를 입력해주세요\nex) https://github.com/team/baton/pull/1');
};

export const validateDeadline = (deadline: string) => {
  if (!deadline) throw new Error('마감기한을 입력해주세요');

  const date = new Date(deadline);
  if (isPastTime(date)) {
    throw new Error('현재보다 1시간 이후의 시간을 입력해주세요');
  }
};

export const validateCompany = (compony: string | null) => {
  if (!compony) {
    throw new Error('소속을 입력해주세요');
  }
};

export const validateName = (name: string | null) => {
  if (!name) {
    throw new Error('이름을 입력해주세요');
  }
};

export const validateIntroduction = (introduction: string | null) => {
  if (!introduction) {
    throw new Error('자기소개를 입력해주세요');
  }
};

export const validateMessage = (message: string) => {
  if (message.length < 20) throw new Error('메세지는 20자 이상 입력해주세요');
};

export const validateImplementContents = (contents: string, maxLength: number = 20) => {
  if (contents.length < maxLength) throw new Error(`구현한 내용을 ${maxLength}자 이상 입력해주세요`);
};

export const validateCuriousContents = (contents: string, maxLength: number = 20) => {
  if (contents.length < maxLength) throw new Error(`아쉽거나 궁금한 내용을 ${maxLength}자 이상 입력해주세요`);
};
