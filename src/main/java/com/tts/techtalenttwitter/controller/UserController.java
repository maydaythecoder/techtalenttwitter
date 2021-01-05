package com.tts.techtalenttwitter.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.tts.techtalenttwitter.model.TweetDisplay;
import com.tts.techtalenttwitter.model.User;
import com.tts.techtalenttwitter.service.TweetService;
import com.tts.techtalenttwitter.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController 
{
    @Autowired
    private UserService userService;

    @Autowired
    private TweetService tweetService;

    @GetMapping(value = "/users/{username}")
    public String getUser(@PathVariable(value = "username") String username, Model model) 
    {
        User loggedInUser = userService.getLoggedInUser();
        User user = userService.findByUsername(username);
        List<TweetDisplay> tweets = tweetService.findAllByUser(user);
        List<User> following = loggedInUser.getFollowing();
        boolean isFollowing = false;
        for (User followedUser : following) {
            if (followedUser.getUsername().equals(username)) {
                isFollowing = true;
            }
        }
        boolean isSelfPage = loggedInUser.getUsername().equals(username);
        model.addAttribute("isSelfPage", isSelfPage);
        model.addAttribute("following", isFollowing);
        model.addAttribute("tweetList", tweets);
        model.addAttribute("user", user);
        return "user";
    }

    
    @GetMapping(value = "/users")
    public String getUsers(@RequestParam(value = "filter", required = false) String filter,
                           Model model)
    {
        List<User> users = new ArrayList<User>();

        User loggedInUser = userService.getLoggedInUser();

        List<User> usersFollowing = loggedInUser.getFollowing();
        List<User> usersFollowers = loggedInUser.getFollowers();

        if(filter == null)
        {
            filter="all";
        }
        if ( filter.equalsIgnoreCase("followers"))
        {
            users = usersFollowers;
            model.addAttribute("filter", "followers");
        }
        else if( filter.equalsIgnoreCase("following"))
        {
            users = usersFollowing;
            model.addAttribute("filter", "following");
        }
        else
        {
            users = userService.findAll();
            model.addAttribute("filter", "all");
        }
        model.addAttribute("users", users);

        setTweetCounts(users, model);
        setFollowingStatus(users, usersFollowing, model);
        return "users";
    }

    private void setTweetCounts(List<User> users, Model model)
    {
        HashMap<String, Integer> tweetCounts = new HashMap<>();
        for (User user: users)
        {
            List<TweetDisplay> tweets = tweetService.findAllByUser(user);
            tweetCounts.put(user.getUsername(), tweets.size());
        }
        model.addAttribute("tweetCounts", tweetCounts);
    }

    private void setFollowingStatus(List<User> users, List<User> usersFollowing, Model model) 
    {
		HashMap<String, Boolean> followingStatus = new HashMap<>();
		String username = userService.getLoggedInUser().getUsername();
        for (User user : users)
        {
			if (usersFollowing.contains(user)) {
				followingStatus.put(user.getUsername(), true);
			} else if (!user.getUsername().equals(username)) {
				followingStatus.put(user.getUsername(), false);
			}
		}
		model.addAttribute("followingStatus", followingStatus);
	}
}
