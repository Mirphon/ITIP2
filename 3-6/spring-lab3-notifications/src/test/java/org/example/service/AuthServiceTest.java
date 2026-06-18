package org.example.service;

import org.example.mapper.UserMapper;
import org.example.model.dto.RegisterRequest;
import org.example.model.entity.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoder;
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private AuthService authService;

    @Test
    void shouldRegisterUserAndCallSaveOnce() {
        RegisterRequest request = new RegisterRequest("Ivan", "ivan@test.com", "pass123", null);
        when(passwordEncoder.encode(    "pass123")).thenReturn("encodedPassword");

        authService.register(request);


        verify(userRepository, times(1)).save(any(User.class));
    }

    @Test
    void shouldRegisterAdminAndCallSaveOnce() {
        RegisterRequest request = new RegisterRequest("Admin", "admin@test.com", "adminPass", "123456");
        when(passwordEncoder.encode("adminPass")).thenReturn("encodedPassword");

        authService.registerAdmin(request);


        verify(userRepository, times(1)).save(any(User.class));
    }
}