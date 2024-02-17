import { DefaultBodyType, PathParams, ResponseComposition, RestContext, RestRequest, rest } from 'msw';
import runnerPostList from './data/runnerPostList.json';
import runnerPostDetails from './data/runnerPostDetails.json';
import myPageRunnerProfile from './data/myPageRunnerProfile.json';
import myPageSupporterProfile from './data/myPageSupporterProfile.json';
import runnerProfileInfo from './data/runnerProfileInfo.json';
import supporterProfileInfo from './data/supporterProfileInfo.json';
import supporterCandidate from './data/supporterCandidate.json';
import headerProfile from './data/headerProfile.json';
import supporterProfilePost from './data/supporterProfilePost.json';
import myPagePostList from './data/myPagePost/myPagePostList.json';
import tagList from './data/tagList.json';
import notificationList from './data/notification.json';
import rank from './data/rank.json';
import { BATON_BASE_URL } from '@/constants';
import { getRestMinute } from '@/utils/jwt';

export const handlers = [
  rest.get(`${BATON_BASE_URL}/posts/runner`, async (req, res, ctx) => {
    const name = req.url.searchParams.get('tagName');
    const reviewStatus = req.url.searchParams.get('reviewStatus');
    const limit = req.url.searchParams.get('limit');
    const cursor = req.url.searchParams.get('cursor');

    if (!reviewStatus) {
      return res(
        ctx.delay(300),
        ctx.status(200),
        ctx.set('Content-Type', 'application/json'),
        ctx.json(runnerPostList),
      );
    }

    if (!limit)
      return res(
        ctx.status(400),
        ctx.set('Content-Type', 'application/json'),
        ctx.json({ errorCode: 'FE001', message: '잘못된 요청' }),
      );

    const postList = structuredClone(runnerPostList);
    postList.pageInfo.nextCursor = Number(cursor) + 1;
    if (Number(cursor) === 4) postList.pageInfo.isLast = true;

    if (!name || name.trim() === '') {
      postList.data.forEach((post, idx) => {
        post.runnerPostId = Date.now() + idx;
        post.title = `${post.title} (${cursor})`;
        if (reviewStatus) post.reviewStatus = reviewStatus;
      });

      return res(ctx.delay(300), ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(postList));
    }

    postList.data.forEach((post, idx) => {
      post.runnerPostId = Date.now() + idx;
      post.title = `검색된 목록입니다 (${cursor})`;
      post.tags = [name];
      post.reviewStatus = reviewStatus!;
    });

    return res(ctx.delay(300), ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(postList));
  }),

  rest.post(`${BATON_BASE_URL}/posts/runner`, async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(201));
  }),

  rest.get(`${BATON_BASE_URL}/profile/me`, async (req, res, ctx) => {
    const jwt = req.headers.get('Authorization');

    return jwt && getRestMinute(jwt) > 0
      ? res(ctx.delay(300), ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(headerProfile))
      : res(
          ctx.delay(300),
          ctx.status(401),
          ctx.set('Content-Type', 'application/json'),
          ctx.json({ errorCode: 'OA003', message: 'Authorization 값을 입력해주세요.' }),
        );
  }),

  rest.put(`${BATON_BASE_URL}/posts/runner/:runnerPostId`, async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(201));
  }),

  rest.delete(`${BATON_BASE_URL}/posts/runner/:runnerPostId`, async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(204));
  }),

  rest.get(`${BATON_BASE_URL}/profile/runner/me`, async (req, res, ctx) => {
    return res(
      ctx.delay(1000),
      ctx.status(200),
      ctx.set('Content-Type', 'application/json'),
      ctx.json(myPageRunnerProfile),
    );
  }),

  rest.get(`${BATON_BASE_URL}/profile/supporter/me`, async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(myPageSupporterProfile));
  }),

  rest.get(`${BATON_BASE_URL}/posts/runner/me/runner`, async (req, res, ctx) => {
    return handleRequest(req, res, ctx);
  }),

  rest.get(`${BATON_BASE_URL}/posts/runner/me/supporter`, async (req, res, ctx) => {
    return handleRequest(req, res, ctx);
  }),

  rest.get(`${BATON_BASE_URL}/profile/runner/me`, async (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.delay(500),
      ctx.set('Content-Type', 'application/json'),
      ctx.json(runnerProfileInfo),
    );
  }),

  rest.get(`${BATON_BASE_URL}/profile/supporter/me`, async (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.delay(500),
      ctx.set('Content-Type', 'application/json'),
      ctx.json(supporterProfileInfo),
    );
  }),

  rest.patch(`${BATON_BASE_URL}/profile/runner/me`, async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(runnerProfileInfo));
  }),

  rest.patch(`${BATON_BASE_URL}/profile/supporter/me`, async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(runnerProfileInfo));
  }),

  rest.get(`${BATON_BASE_URL}/posts/runner/:runnerPostId/supporters`, async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(supporterCandidate));
  }),

  rest.patch(`${BATON_BASE_URL}/posts/runner/:runnerPostId/supporters`, async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(supporterCandidate));
  }),

  rest.post(`${BATON_BASE_URL}/feedback/supporter`, async (req, res, ctx) => {
    const { reviewType, descriptions, supporterId, runnerPostId } = await req.json();

    return res(ctx.delay(300), ctx.status(201), ctx.set('Content-Type', 'application/json'), ctx.json({}));
  }),

  rest.get(`${BATON_BASE_URL}/profile/runner/:runnerId`, async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(runnerProfileInfo));
  }),

  rest.get(`${BATON_BASE_URL}/profile/supporter/:supporterId`, async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(supporterProfileInfo));
  }),

  rest.get(`${BATON_BASE_URL}/posts/runner/search`, async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(supporterProfilePost));
  }),

  rest.post(`${BATON_BASE_URL}/posts/runner/:runnerPostId/application`, async (req, res, ctx) => {
    return res(ctx.status(201));
  }),

  rest.patch(`${BATON_BASE_URL}/posts/runner/:runnerPostId/cancelation`, async (req, res, ctx) => {
    return res(ctx.status(201));
  }),

  rest.get(`${BATON_BASE_URL}/tags/search`, async (req, res, ctx) => {
    const name = req.url.searchParams.get('tagName');

    const searchedTags = name ? tagList.data.filter((tag) => tag.tagName.includes(name)) : [];

    return res(ctx.status(200), ctx.json({ data: searchedTags }));
  }),

  rest.post(`${BATON_BASE_URL}/branch`, async (req, res, ctx) => {
    const { repoName } = await req.json();

    return res(ctx.status(201));
  }),

  rest.get(`${BATON_BASE_URL}/posts/runner/:runnerPostId`, async (req, res, ctx) => {
    return res(
      ctx.delay(300),
      ctx.status(200),
      ctx.set('Content-Type', 'application/json'),
      ctx.json(runnerPostDetails),
    );
  }),

  rest.patch(`${BATON_BASE_URL}/posts/runner/:runnerPostId/done`, async (req, res, ctx) => {
    return res(ctx.status(201));
  }),

  rest.get(`${BATON_BASE_URL}/notifications`, async (req, res, ctx) => {
    return res(
      ctx.delay(300),
      ctx.status(200),
      ctx.set('Content-Type', 'application/json'),
      ctx.json(notificationList),
    );
  }),

  rest.delete(`${BATON_BASE_URL}/notifications/:notificationId`, async (req, res, ctx) => {
    const { notificationId } = req.params;
    const { data: mockData } = notificationList;

    const deletedNotificationList = mockData.filter(
      (notification) => notification.notificationId !== Number(notificationId),
    );

    return res(ctx.delay(200), ctx.status(204));
  }),

  rest.patch(`${BATON_BASE_URL}/notifications/:notificationId`, async (req, res, ctx) => {
    return res(ctx.status(201));
  }),

  rest.get(`${BATON_BASE_URL}/oauth/login/github`, async (req, res, ctx) => {
    return res(
      ctx.delay(300),
      ctx.status(200),
      ctx.set({
        Authorization:
          'eyJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJiYXRvbiIsImlhdCI6MTY5NTI2MDcxNSwiZXhwIjozMDk1MjYyNTE1LCJzb2NpYWxJZCI6Imd1cmlkYWVrIn0.TelcG0n8a7IxDU0-bOttjE9NZ4KtDgwaq5UhRkg1y9s',
      }),
    );
  }),

  rest.patch(`${BATON_BASE_URL}/oauth/logout`, async (req, res, ctx) => {
    return res(ctx.status(204));
  }),

  rest.get(`${BATON_BASE_URL}/posts/runner/search/count`, async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json({ count: 99 }));
  }),

  rest.get(`${BATON_BASE_URL}/rank/supporter`, async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(rank));
  }),
];

const handleRequest = (
  req: RestRequest<never, PathParams<string>>,
  res: ResponseComposition<DefaultBodyType>,
  ctx: RestContext,
) => {
  const reviewStatus = req.url.searchParams.get('reviewStatus');
  const limit = req.url.searchParams.get('limit');
  const cursor = req.url.searchParams.get('cursor');

  if (!reviewStatus || !limit)
    return res(
      ctx.status(400),
      ctx.set('Content-Type', 'application/json'),
      ctx.json({ errorCode: 'FE001', message: '잘못된 요청' }),
    );

  const CopiedMyPagePostList = structuredClone(myPagePostList);

  if (reviewStatus) {
    CopiedMyPagePostList.data.forEach((post, idx) => {
      post.runnerPostId = Date.now() + idx;
      post.title = `${post.title} (${cursor})`;
      post.reviewStatus = reviewStatus;
    });

    CopiedMyPagePostList.pageInfo.nextCursor = Number(cursor) + 1;
    if (Number(cursor) === 3) CopiedMyPagePostList.pageInfo.isLast = true;

    return res(
      ctx.delay(300),
      ctx.status(200),
      ctx.set('Content-Type', 'application/json'),
      ctx.json(CopiedMyPagePostList),
    );
  }
};
