package com.tts.techtalenttwitter.service;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import com.tts.techtalenttwitter.model.Role;
import com.tts.techtalenttwitter.model.User;
import com.tts.techtalenttwitter.repository.RoleRepository;
import com.tts.techtalenttwitter.repository.UserRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService 
{ 
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserService(UserRepository userRepository,
                       RoleRepository roleRepository,
                       BCryptPasswordEncoder bCryptPasswordEncoder)
    {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    public User findByUsername(String username)
    {
        return userRepository.findByUsername(username);        
    }

    public List<User> findAll()
    {
        return (List<User>) userRepository.findAll();
    }

    public void save(User user)
    {
        userRepository.save(user);
    }

    public User saveNewUser(User user)
    {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setActive(1);
        Role userRole = roleRepository.findByRole("USER");
        user.setRoles(new HashSet<Role>(Arrays.asList(userRole)));
        return userRepository.save(user);        
    }

    public User getLoggedInUser()
    {
        String loggedInUsername = SecurityContextHolder.
            getContext().getAuthentication().getName();
        return findByUsername(loggedInUsername);            
    }
}
