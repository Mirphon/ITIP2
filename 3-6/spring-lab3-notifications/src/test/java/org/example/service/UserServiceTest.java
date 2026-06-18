package org.example.service;

import org.example.model.dto.UserDto;
import org.example.model.entity.User;
import org.example.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock
    private UserRepository userRepository;
    @InjectMocks
    private UserService userService;
    @Test
    void shouldCallSaveOnRepository() {
        UserDto dto = UserDto.builder()
                .name("Иван")
                .email("ivan@example.com")
                .build();
        when(userRepository.save(any(User.class))).thenReturn(new User());
        userService.createUser(dto);
        verify(userRepository).save(any(User.class));
    }

    @Test
    void shouldSaveUserCorrectly() {
        UserDto dto = UserDto.builder().name("Иван").email("ivan@example.com").build();
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        userService.createUser(dto);

        verify(userRepository).save(userCaptor.capture());
        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getName()).isEqualTo("Иван");
        assertThat(savedUser.getEmail()).isEqualTo("ivan@example.com");
    }

    @Test
    void shouldDeleteUserWhenUserExists() {
        Long userId = 1L;
        User mockUser = new User(); // Создаем пустой объект-заглушку

        when(userRepository.findById(userId)).thenReturn(Optional.of(mockUser));

        userService.deleteUser(userId);

        verify(userRepository, times(1)).delete(mockUser);
    }
}