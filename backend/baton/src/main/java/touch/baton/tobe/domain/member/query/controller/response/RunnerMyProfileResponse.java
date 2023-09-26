package touch.baton.tobe.domain.member.query.controller.response;

import touch.baton.domain.runnerpost.controller.response.RunnerPostResponse;

import java.util.List;

public record RunnerMyProfileResponse(RunnerResponse.Mine profile,
                                      List<RunnerPostResponse.Mine> runnerPosts
) {
}
