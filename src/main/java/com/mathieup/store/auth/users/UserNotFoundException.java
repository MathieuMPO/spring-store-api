package com.mathieup.store.auth.users;

public class UserNotFoundException extends RuntimeException {
    public UserNotFoundException(String message) {
        super(message);
    }
    public UserNotFoundException(Long id) {
        super("User with id " + id.toString() + " not found");
    }
}
