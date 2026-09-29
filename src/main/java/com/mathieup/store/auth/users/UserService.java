package com.mathieup.store.auth.users;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private UserMapper userMapper;
    private PasswordEncoder passwordEncoder;

    public List<UserDto> getAllUsers(String sort) {
        if (!Set.of("name","email").contains(sort)) sort = "name";
        return userRepository.findAll(Sort.by(sort))
                .stream()
                .map(userMapper::toDto)
                .toList();
    }

    public UserDto getUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        return userMapper.toDto(user);
    }

    public UserDto registerUser(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyRegisteredException();
        }

        var user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRole(Role.USER);
        userRepository.save(user);

        return userMapper.toDto(user);
    }

    public UserDto updateUser(Long id, UpdateUserRequest request) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userMapper.update(request, user);
        userRepository.save(user);
        return userMapper.toDto(user);
    }

    public void  deleteUser(Long id) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));
        userRepository.delete(user);
    }


    public void updatePassword(Long id, ChangePasswordRequest request) {
        var user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException(id));

        if (!user.getPassword().equals(request.getOldPassword())) {
            throw new BadCredentialsException("Old password doesn't match");
        }

        user.setPassword(request.getNewPassword());
        userRepository.save(user);
    }
}
