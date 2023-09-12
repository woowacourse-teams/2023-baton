package touch.baton.assure.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import touch.baton.domain.oauth.token.ExpireDate;
import touch.baton.domain.oauth.token.RefreshToken;
import touch.baton.domain.oauth.token.Token;

public interface TestRefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

    @Modifying
    @Transactional
    @Query("""
            update RefreshToken  rt
            set rt.expireDate = :expireDate
            where rt.token = :token
            """)
    void changeExpireDateByToken(@Param("token") final Token token, @Param("expireDate") final ExpireDate expireDate);
}
