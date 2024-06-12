package dmu.cheek.global.token;

import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.BusinessException;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class JwtTokenProvider {

    public boolean validateAccessToken(String accessToken) {
        try {
            Jwts.parser()
                    .parseClaimsJws(accessToken)
                    .getBody();
            return true;
        } catch (MalformedJwtException e) {
            log.info("형식에 맞지 않는 token");
            throw new BusinessException(ErrorCode.MALFORMED_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("만료된 token");
            throw new BusinessException(ErrorCode.EXPIRED_TOKEN);
       } catch (Exception e) {
            log.info("유효하지 않는 token");
            throw new BusinessException(ErrorCode.INVALID_TOKEN);
        }
    }
}