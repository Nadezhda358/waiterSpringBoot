package com.waiter.waiter;

import com.waiter.waiter.entities.User;
import com.waiter.waiter.enums.Role;
import com.waiter.waiter.repositories.UserRepository;
import com.waiter.waiter.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserDetailsServiceImplTest {
    @Mock
    private UserRepository userRepo;

    @Mock
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Mock
    private Authentication authentication;

    @Mock
    private UserDetails userDetails;

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

        when(bCryptPasswordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepo.save(user)).thenReturn(user);

        userDetailsService.saveUser(user);

        Mockito.verify(userRepo, Mockito.times(1)).save(user);
        assertEquals("encodedPassword", user.getPassword());
        assertTrue(user.isEnabled());
    }
    @Test
    public void testIsUserPresentWithExistingUser() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepo.getUserByUsername(user.getUsername())).thenReturn(user);

        List<Object> result = userDetailsService.isUserPresent(user);

        assertTrue((boolean) result.get(0));
        assertEquals("Username Already Present!", result.get(1));
    }

    @Test
    public void testIsUserPresentWithNonExistingUser() {
        User user = new User();
        user.setUsername("testuser");

        when(userRepo.getUserByUsername(user.getUsername())).thenReturn(null);

        List<Object> result = userDetailsService.isUserPresent(user);

        assertFalse((boolean) result.get(0));
        assertNull(result.get(1));
    }

    @Test
    public void testLoadUserByUsernameWithExistingUser() {
        User user = new User();
        user.setUsername("testuser");
        user.setPassword("testpassword");
        user.setEnabled(true);
        user.setRole(Role.WAITER);

        when(userRepo.getUserByUsername(user.getUsername())).thenReturn(user);

        UserDetails result = userDetailsService.loadUserByUsername(user.getUsername());

        assertEquals(user.getUsername(), result.getUsername());
        assertEquals(user.getPassword(), result.getPassword());
        assertTrue(result.isEnabled());
        assertEquals(user.getRole().toString(), result.getAuthorities().iterator().next().getAuthority());
    }

    @Test
    public void testLoadUserByUsernameWithNonexistentUser() {
        String username = "nonexistentuser";
        when(userRepo.getUserByUsername(username)).thenReturn(null);

        Throwable exception = assertThrows(UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername(username));
        assertEquals("User not found!", exception.getMessage());
    }
    @Test
    public void testGetLoggedUserWithUserDetailsPrincipal() {
        Authentication authentication = mock(Authentication.class);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        when(authentication.getPrincipal()).thenReturn(userDetails);
        when(userDetails.getUsername()).thenReturn("john");

        User expectedUser = new User();
        expectedUser.setUsername("john");
        when(userRepo.getUserByUsername("john")).thenReturn(expectedUser);

        User actualUser = userDetailsService.getLoggedUser();

        assertEquals(expectedUser, actualUser);
    }


}
