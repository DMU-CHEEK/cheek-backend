package dmu.cheek.global.token.dto;

import java.time.LocalDateTime;

public record TokenDetail(String token, LocalDateTime expired) {
}
