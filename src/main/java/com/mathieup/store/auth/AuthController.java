package com.mathieup.store.auth;

import com.mathieup.store.common.ErrorDto;
import com.mathieup.store.auth.users.UserDto;
import com.mathieup.store.auth.users.UserNotFoundException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final AuthService authService;

    @PostMapping("/login")
    public JwtResponse login(
            @Valid @RequestBody UserLoginRequest request,
            HttpServletResponse response
    ) {
        return authService.handleLogin(request,response);
    }

    @PostMapping("/refresh")
    public JwtResponse refreshToken(
            @CookieValue(value = "refreshToken") String cookieValue
    ) {
        return jwtService.refreshToken(cookieValue);
    }

    @GetMapping("/me")
    public ResponseEntity<UserDto> me(){
        return ResponseEntity.ok(authService.getCurrentUserDto());
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorDto> handleBadCredentialException(){
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorDto("Invalid username or password"));
    }

    @ExceptionHandler(JwtException.class)
    public ResponseEntity<ErrorDto> handleJwtException(JwtException e){
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ErrorDto(e.getMessage()));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorDto> handleException(UserNotFoundException e){
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(new ErrorDto(e.getMessage()));
    }

}
