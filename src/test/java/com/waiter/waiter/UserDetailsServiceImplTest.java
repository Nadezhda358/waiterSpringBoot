package com.waiter.waiter;

import com.waiter.waiter.entities.User;
import com.waiter.waiter.repositories.UserRepository;
import com.waiter.waiter.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepo;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;
    @BeforeEach
    public void setUp(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testSaveUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");

        Mockito.when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        Mockito.when(userRepo.save(user)).thenReturn(user);

        userDetailsService.saveUser(user);

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        assertEquals("encodedPassword", user.getPassword());
        assertTrue(user.isEnabled());
    }
}
