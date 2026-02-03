package dev.jda.api.library.common.api;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiKeyResponse {
    private final String apiKey;
    private final int length;

    public ApiKeyResponse(String apiKey, int length) {
        this.apiKey = apiKey;
        this.length = length;
    }

}
