import { rest } from 'msw';
import runnerPostList from './data/runnerPostList.json';
import runnerPostDetails from './data/runnerPostDetails.json';
import supporterCardList from './data/supporterCardList.json';
import myPageRunnerProfile from './data/myPageRunnerProfile.json';
import myPageSupporterProfile from './data/myPageSupporterProfile.json';
import myPageRunnerPost from './data/myPageRunnerPost.json';
import myPageSupporterPost from './data/myPageSupporterPost.json';
import runnerProfile from './data/runnerProfile.json';
import runnerProfileInfo from './data/runnerProfileInfo.json';
import supporterProfileInfo from './data/supporterProfileInfo.json';
import supporterCandidate from './data/supporterCandidate.json';

export const handlers = [
  rest.post('*/posts/runner/test', async (req, res, ctx) => {
    const { title, tags, pullRequestUrl, deadline, contents } = await req.json();

    return res(ctx.delay(300), ctx.status(201), ctx.set('Content-Type', 'application/json'));
  }),

  rest.get('*/posts/runner/test', async (_, res, ctx) => {
    return res(ctx.delay(300), ctx.status(200), ctx.json(runnerPostList));
  }),

  rest.put('*/posts/runner/:runnerPostId/test', async (req, res, ctx) => {
    const { title, tags, pullRequestUrl, deadline, contents } = await req.json();

    return res(ctx.delay(300), ctx.status(201), ctx.set('Content-Type', 'application/json'));
  }),

  rest.delete('*/posts/runner/:runnerPostId/test', async (req, res, ctx) => {
    return res(ctx.delay(300), ctx.status(204), ctx.set('Content-Type', 'application/json'));
  }),

  rest.get('*/posts/runner/:runnerPostId/test', async (req, res, ctx) => {
    return res(
      ctx.delay(300),
      ctx.status(200),
      ctx.set('Content-Type', 'application/json'),
      ctx.json(runnerPostDetails),
    );
  }),

  rest.get('*/supporters/test', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(supporterCardList));
  }),

  rest.post('*/posts/runner/test', async (req, res, ctx) => {
    const { title, tags, pullRequestUrl, deadline, contents } = await req.json();

    return res(ctx.delay(300), ctx.status(201), ctx.set('Content-Type', 'application/json'));
  }),

  rest.get('*/profile/runner/me', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(myPageRunnerProfile));
  }),

  rest.get('*/profile/supporter/me', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(myPageSupporterProfile));
  }),

  rest.get('*/posts/runner/me/runner', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(myPageRunnerPost));
  }),

  rest.get('*/posts/runner/me/supporter', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(myPageSupporterPost));
  }),

  rest.get('*/profile/runner/me', async (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.delay(500),
      ctx.set('Content-Type', 'application/json'),
      ctx.json(runnerProfileInfo),
    );
  }),

  rest.get('*/profile/supporter/me', async (req, res, ctx) => {
    return res(
      ctx.status(200),
      ctx.delay(500),
      ctx.set('Content-Type', 'application/json'),
      ctx.json(supporterProfileInfo),
    );
  }),

  rest.patch('*/profile/runner/me', async (req, res, ctx) => {
    const { name, company, introduction, technicalTags } = await req.json();

    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(runnerProfile));
  }),

  rest.patch('*/profile/supporter/me', async (req, res, ctx) => {
    const { name, company, introduction, technicalTags } = await req.json();

    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(runnerProfile));
  }),

  rest.get('*/posts/runner/:runnerPostId/supporters', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(supporterCandidate));
  }),
];
