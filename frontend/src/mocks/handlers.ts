import { rest } from 'msw';
import runnerPostList from './data/runnerPostList.json';
import runnerPostDetails from './data/runnerPostDetails.json';
import myPageRunnerProfile from './data/myPageRunnerProfile.json';
import myPageSupporterProfile from './data/myPageSupporterProfile.json';
import runnerProfileInfo from './data/runnerProfileInfo.json';
import supporterProfileInfo from './data/supporterProfileInfo.json';
import supporterCandidate from './data/supporterCandidate.json';
import headerProfile from './data/headerProfile.json';
import supporterProfilePost from './data/supporterProfilePost.json';
import notStarted from './data/myPagePost/notStarted.json';
import inProgress from './data/myPagePost/inProgress.json';
import done from './data/myPagePost/done.json';
import tagList from './data/tagList.json';
import { BATON_BASE_URL } from '@/constants';

export const handlers = [
  rest.post(`${BATON_BASE_URL}/posts/runner`, async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(201));
  }),

  rest.get(`${BATON_BASE_URL}/profile/me`, async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(headerProfile));
  }),

  rest.put(`${BATON_BASE_URL}/posts/runner/:runnerPostId)`, async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(201));
  }),

  rest.delete(`${BATON_BASE_URL}/posts/runner/:runnerPostId`, async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(204));
  }),

  rest.get(`${BATON_BASE_URL}/profile/runner/me`, async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(myPageRunnerProfile));
  }),

  rest.get(`${BATON_BASE_URL}/profile/supporter/me`, async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(myPageSupporterProfile));
  }),

  rest.get(`${BATON_BASE_URL}/posts/runner/me/runner?size&page&reviewStatus`, async (req, res, ctx) => {
    const reviewStatus = req.url.searchParams.get('reviewStatus');

    switch (reviewStatus) {
      case 'NOT_STARTED':
        return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(notStarted));

      case 'IN_PROGRESS':
        return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(inProgress));

      case 'DONE':
        return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(done));

      default:
        return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json({}));
    }
  }),

  rest.get(`${BATON_BASE_URL}/posts/runner/me/supporter?size&page&reviewStatus`, async (req, res, ctx) => {
    const reviewStatus = req.url.searchParams.get('reviewStatus');

    switch (reviewStatus) {
      case 'NOT_STARTED':
        return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(notStarted));

      case 'IN_PROGRESS':
        return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(inProgress));

      case 'DONE':
        return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(done));

      default:
        return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json({}));
    }
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

  rest.get(`${BATON_BASE_URL}/posts/runner/tags/search`, async (req, res, ctx) => {
    const name = req.url.searchParams.get('tagName');
    const reviewStatus = req.url.searchParams.get('reviewStatus');
    const page = req.url.searchParams.get('page');

    if (!reviewStatus || !page)
      return res(
        ctx.status(400),
        ctx.set('Content-Type', 'application/json'),
        ctx.json({ errorCode: 'FE001', message: '잘못된 요청' }),
      );

    const postList = structuredClone(runnerPostList);

    if (!name || name.trim() === '') {
      postList.data.forEach((post, idx) => {
        post.runnerPostId = Date.now() + idx;
        post.title = `${post.title} (${page})`;
        post.reviewStatus = reviewStatus;
      });

      postList.pageInfo.currentPage = Number(page);

      return res(ctx.delay(300), ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(postList));
    }

    postList.data.forEach((post, idx) => {
      post.runnerPostId = Date.now() + idx;
      post.title = `검색된 목록입니다 (${page})`;
      post.tags = [name];
      post.reviewStatus = reviewStatus!;
    });

    return res(ctx.delay(300), ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(postList));
  }),

  rest.get(`${BATON_BASE_URL}/posts/runner/:runnerPostId`, async (req, res, ctx) => {
    return res(
      ctx.delay(300),
      ctx.status(200),
      ctx.set('Content-Type', 'application/json'),
      ctx.json(runnerPostDetails),
    );
  }),
];
