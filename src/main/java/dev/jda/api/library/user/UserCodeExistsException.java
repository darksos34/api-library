package dev.jda.api.library.user;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserCodeExistsException extends RuntimeException {
    private final String code;

    public UserCodeExistsException(String code) {
        super("User with code " + code + " already exists.");
        this.code = code;
    }
}
