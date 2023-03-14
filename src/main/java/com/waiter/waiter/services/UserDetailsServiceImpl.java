package com.waiter.waiter.services;

import com.waiter.waiter.auth.MyUserDetails;
import com.waiter.waiter.entities.User;
import com.waiter.waiter.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, UserService {

    @Autowired
    private UserRepository userRepo;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;


    @Override
    public void saveUser(User user) {
        String encodedPassword = bCryptPasswordEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        user.setEnabled(true);
//        user.setRole(Role.ROLE_USER);
        userRepo.save(user);
    }

    @Override
    public List<Object> isUserPresent(User user) {
        boolean userExists = false;
        String message = null;
        User u  = userRepo.getUserByUsername(user.getUsername());
        if(u != null){
            userExists = true;
            message = "Username Already Present!";
        }

        return Arrays.asList(userExists, message);

    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user  = userRepo.getUserByUsername(username);
        if (user==null) {
            throw new UsernameNotFoundException("User not found!");
        }
        return new MyUserDetails(user);
    }

    public User getLoggedUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username;
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }
        User user = userRepo.getUserByUsername(username);
        return user;
    }
    //public User getLoggedUser() {
    //    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    //
    //    if (authentication != null && authentication.getPrincipal() instanceof User) {
    //        return (User) authentication.getPrincipal();
    //    }
    //
    //    return null;
    //}


}
