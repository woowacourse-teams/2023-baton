export const ERROR_TITLE = {
  VALIDATION: '잘못된 입력',
  REQUEST: '요청 오류',
  EXPIRATION: '만료',
  NO_PERMISSION: '권한 없음',
  NETWORK: '네트워크 오류',
  ERROR: '오류',
} as const;

export const ERROR_DESCRIPTION = {
  TOKEN_EXPIRATION: '로그인이 만료되었어요',
  NO_TOKEN: '로그인이 필요한 기능이에요',
  UNEXPECTED: '예기치 못한 오류가 발생했어요',
  LOGIN: '로그인을 실패했어요',
  NO_SUPPORTER: '해당 서포터가 존재하지 않아요',
  NO_POST: '해당 게시물이 존재하지 않아요',
  NO_MODIFICATION: '수정사항이 존재하지 않아요',
  NO_PROFILE: '프로필 정보를 불러오지 못했어요',
} as const;

export const TOAST_ERROR_MESSAGE = {
  TOKEN_EXPIRATION: {
    title: ERROR_TITLE.EXPIRATION,
    description: ERROR_DESCRIPTION.TOKEN_EXPIRATION,
  },

  NO_TOKEN: {
    title: ERROR_TITLE.NO_PERMISSION,
    description: ERROR_DESCRIPTION.NO_TOKEN,
  },

  UNEXPECTED: {
    title: ERROR_TITLE.ERROR,
    description: ERROR_DESCRIPTION.UNEXPECTED,
  },

  LOGIN: {
    title: ERROR_TITLE.ERROR,
    description: ERROR_DESCRIPTION.LOGIN,
  },

  NO_PROFILE: {
    title: ERROR_TITLE.REQUEST,
    description: ERROR_DESCRIPTION.NO_PROFILE,
  },
} as const;

export const TOAST_COMPLETION_MESSAGE = {
  SUBMISSION: {
    title: '제출 완료',
    description: '리뷰 제안을 보냈어요',
  },

  SAVE: {
    title: '저장 완료',
    description: '저장을 완료했어요',
  },

  DELETE: {
    title: '삭제 완료',
    description: '삭제를 완료했어요',
  },

  SUPPORTER_SELECT: {
    title: '선택 완료',
    description: '서포터 선택을 완료했어요',
  },

  REVIEW_CANCEL: {
    title: '취소 완료',
    description: '취소를 완료했어요',
  },

  REVIEW_COMPETE: {
    title: '리뷰 완료',
    description: '리뷰를 완료했어요',
  },
};
