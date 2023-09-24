package touch.baton.domain.tag.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import touch.baton.domain.runnerpost.RunnerPost;
import touch.baton.domain.tag.RunnerPostTag;

import java.util.List;

public interface RunnerPostTagRepository extends JpaRepository<RunnerPostTag, Long> {

    @Query("""
            select rpt
            from RunnerPostTag rpt
            join fetch Tag tag on rpt.tag.id = tag.id
            where rpt.runnerPost.id = :runnerPostId
            """)
    List<RunnerPostTag> joinTagByRunnerPostId(@Param("runnerPostId") final Long runnerPostId);

    @Query("""
            SELECT rpt
            FROM RunnerPostTag rpt
            JOIN FETCH Tag tag ON rpt.tag.id = tag.id
            WHERE rpt.runnerPost IN :runnerPosts
            """)
    List<RunnerPostTag> joinTagByRunnerPosts(@Param("runnerPosts") final List<RunnerPost> runnerPosts);
}
