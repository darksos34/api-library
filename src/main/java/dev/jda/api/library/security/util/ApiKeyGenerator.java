package dev.jda.api.library.security.util;

import java.security.SecureRandom;
import java.util.Base64;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class ApiKeyGenerator {

    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Logger logger = LoggerFactory.getLogger(ApiKeyGenerator.class);

    private ApiKeyGenerator() {
        throw new AssertionError("No instances of ApiKeyGenerator allowed");
    }

    /**
     * Generates a URL-safe Base64 API key with the exact requested number of characters.
     *
     * @param  length number of characters in the resulting string (must be > 0)
     * @return API key string of length {@code length}
     */
    public static String generateApiKey(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("length must be > 0");
        }

        int bytesNeeded = (int) Math.ceil(length * 3.0 / 4.0);
        StringBuilder sb = new StringBuilder();

        while (sb.length() < length) {
            byte[] bytes = new byte[bytesNeeded];
            secureRandom.nextBytes(bytes);
            sb.append(Base64.getUrlEncoder().withoutPadding().encodeToString(bytes));
        }

        return sb.substring(0, length);
    }

    static void main(String[] args) {
        int length = 32;
        if (args.length > 0) {
            try {
                length = Integer.parseInt(args[0]);
            } catch (NumberFormatException _) {
                logger.warn("Invalid length argument '{}', using default {}", args[0], length);
            }
        }

        String apiKey = generateApiKey(length);
        logger.info("Generated API key of length {}", apiKey.length());
        logger.debug("API KEY: {}", apiKey);
    }
}
