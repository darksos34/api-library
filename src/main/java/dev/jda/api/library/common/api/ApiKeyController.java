package dev.jda.api.library.common.api;

import dev.jda.api.library.common.constant.RequestPath;
import dev.jda.api.library.common.security.util.ApiKeyGenerator;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

@RestController
@RequestMapping(RequestPath.API)
public class ApiKeyController {

    private static final int DEFAULT_LENGTH = 32;
    private static final int MAX_LENGTH = 256;

    @GetMapping(RequestPath.KEY)
    public ResponseEntity<ApiKeyResponse> generateApiKey(
            @RequestParam(value = "length", required = false) Integer length,
            Authentication authentication) {

        // Authentication is populated by JwtFilter; security config should require authentication.
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Unauthorized");
        }

        int len = (length == null) ? DEFAULT_LENGTH : length;
        if (len <= 0 || len > MAX_LENGTH) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "length must be between 1 and " + MAX_LENGTH);
        }

        String apiKey = ApiKeyGenerator.generateApiKey(len);
        return ResponseEntity.ok(new ApiKeyResponse(apiKey, apiKey.length()));
    }
}
