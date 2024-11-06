package dmu.cheek.health.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/health")
@Tag(name = "Health Check API", description = "헬스 체크 API")
public class HealthController {

    @GetMapping()
    @Operation(summary = "헬스 체크", description = "헬스 체크 API")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("ok");
    }
}
