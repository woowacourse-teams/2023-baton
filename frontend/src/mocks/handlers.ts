import { rest } from 'msw';
import runnerPostList from './data/runnerPostList.json';
import runnerPostDetails from './data/runnerPostDetails.json';
import supporterCardList from './data/supporterCardList.json';
import runnerProfile from './data/runnerProfile.json';

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
    const { title, tags, pullRequestUrl, deadline, contents, supporterId } = await req.json();

    return res(ctx.delay(300), ctx.status(201), ctx.set('Content-Type', 'application/json'));
  }),

  rest.get('*/profile/runner', async (req, res, ctx) => {
    return res(ctx.status(200), ctx.set('Content-Type', 'application/json'), ctx.json(runnerProfile));
  }),
];
