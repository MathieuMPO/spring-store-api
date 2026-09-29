package com.mathieup.store.auth.users;

import com.mathieup.store.common.ErrorDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;

@RestController
@AllArgsConstructor
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    @GetMapping
    public List<UserDto> getAllUsers(
            //@RequestHeader(name = "x-auth-token") String authToken,
            @RequestParam(required = false,name = "sort", defaultValue = "") String sort
    ) {
        return userService.getAllUsers(sort);
    }

    @GetMapping("/{id}")
    public UserDto getUserById(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public ResponseEntity<UserDto> registerUser(
            @Valid @RequestBody RegisterUserRequest request,
            UriComponentsBuilder uriBuilder
            ) {
        var userDto = userService.registerUser(request);

        var uri = uriBuilder.path("/users/{id}").buildAndExpand(userDto.getId()).toUri();

        return ResponseEntity.created(uri).body(userDto);
    }

    @PutMapping("/{id}")
    public UserDto updateUser(
            @PathVariable(name = "id") Long id,
            @RequestBody UpdateUserRequest request
    ) {
        return userService.updateUser(id,request);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/update-password")
    public ResponseEntity<Void> updatePassword(
            @PathVariable Long id,
            @RequestBody ChangePasswordRequest request
    ){
        userService.updatePassword(id,request);
        return ResponseEntity.noContent().build();
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleUserNotFoundException(UserNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(EmailAlreadyRegisteredException.class)
    public ResponseEntity<Map<String,String>> handleDuplicateEmailRegistration(){
        return ResponseEntity.badRequest().body(
                Map.of("email", "Email is already registered")
        );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialsException(BadCredentialsException e){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDto(e.getMessage()));
    }

}
