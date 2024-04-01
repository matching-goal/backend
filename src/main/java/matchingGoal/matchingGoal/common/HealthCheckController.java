package matchingGoal.matchingGoal.common;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthCheckController {
  @GetMapping("/api/healthcheck")
  public ResponseEntity<String> healthCheck() {
    return ResponseEntity.ok("Check");
  }


}
