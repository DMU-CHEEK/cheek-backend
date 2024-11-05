package dmu.cheek.oauth.apple.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import dmu.cheek.global.error.ErrorCode;
import dmu.cheek.global.error.exception.AuthenticationException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.UnsupportedJwtException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.security.PublicKey;
import java.util.Base64;
import java.util.Map;

@RequiredArgsConstructor
@Component
public class AppleTokenParser {

    private static final String IDENTITY_TOKEN_VALUE_DELIMITER = "\\.";
    private static final int HEADER_INDEX = 0;

    private final ObjectMapper objectMapper;

    public Map parseHeader(final String appleToken) {
        try {
            final String encodedHeader = appleToken.split(IDENTITY_TOKEN_VALUE_DELIMITER)[HEADER_INDEX];
            final String decodedHeader = decodeFromUrlString(encodedHeader);

            return objectMapper.readValue(decodedHeader, Map.class);
        } catch (JsonMappingException e) {
            throw new AuthenticationException(ErrorCode.JSON_MAPPING_ERROR);
        } catch (JsonProcessingException e) {
            throw new AuthenticationException(ErrorCode.JSON_PROCESSING_ERROR);
        }
    }

    public String decodeFromUrlString(String encodedHeader) {
        byte[] decodedBytes = Base64.getUrlDecoder().decode(encodedHeader);
        return new String(decodedBytes);
    }

    public Claims extractClaims(final String appleToken, final PublicKey publicKey) {
        try {
            return Jwts.parser()
                    .setSigningKey(publicKey)
                    .build()
                    .parseSignedClaims(appleToken)
                    .getPayload();

        } catch (UnsupportedJwtException e) {
            throw new AuthenticationException(ErrorCode.UNSUPPORTED_TOKEN);
        } catch (JwtException e) {
            throw new AuthenticationException(ErrorCode.INVALID_TOKEN);
        }
    }
}
