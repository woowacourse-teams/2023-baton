package touch.baton.domain.member.query.controller.response;

import touch.baton.domain.runnerpost.query.controller.response.RunnerPostResponse;

import java.util.List;

public record RunnerMyProfileResponse(RunnerResponse.Mine profile,
                                      List<RunnerPostResponse.Mine> runnerPosts
) {
}
