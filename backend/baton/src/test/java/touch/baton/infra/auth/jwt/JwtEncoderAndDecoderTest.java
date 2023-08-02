package touch.baton.infra.auth.jwt;

import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class JwtEncoderAndDecoderTest {

    private final Logger log = LoggerFactory.getLogger(this.getClass());

    private JwtDecoder jwtDecoder;

    private JwtEncoder jwtEncoder;

    private JwtConfig jwtConfig;

    @BeforeEach
    void setUp() {
        this.jwtConfig = new JwtConfig("hyenahyenahyenahyenahyenahyenahyenahyenahyenahyenahyenahyena", "hyena");
        this.jwtDecoder = new JwtDecoder(this.jwtConfig);
        this.jwtEncoder = new JwtEncoder(this.jwtConfig);
    }

    @DisplayName("Claim 으로 email 을 넣어 인코딩한 JWT 를 디코드했을 때 email 을 구할 수 있다..")
    @Test
    void encode_and_decode() {
        // given
        final String encodedJwt = jwtEncoder.jwtToken(Map.of("email", "test@test.com"));

        // when
        final Claims claims = jwtDecoder.parseJwtToken("Bearer " + encodedJwt);
        final String email = claims.get("email", String.class);

        // then
        assertThat(email).isEqualTo("test@test.com");
    }

    @DisplayName("인코드할 때 사용한 secretKey 가 디코드할 때 사용할 secretKey 와 다를 경우 예외가 발생한다.")
    @Test
    void fail_decode_with_wrong_secretKey() {
        // given
        final String encodedJwt = jwtEncoder.jwtToken(Map.of("email", "test@test.com"));

        // when
        final JwtConfig wrongJwtConfig = new JwtConfig("wrongSecretKeywrongSecretKeywrongSecretKey", "hyena");
        final JwtDecoder wrongJwtDecoder = new JwtDecoder(wrongJwtConfig);

        // then
        assertThatThrownBy(() -> wrongJwtDecoder.parseJwtToken(encodedJwt))
                .isInstanceOf(IllegalStateException.class);
    }
}
