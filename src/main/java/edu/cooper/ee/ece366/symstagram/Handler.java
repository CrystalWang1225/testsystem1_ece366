package edu.cooper.ee.ece366.symstagram;

import edu.cooper.ee.ece366.symstagram.model.Post;
import edu.cooper.ee.ece366.symstagram.model.User;
import edu.cooper.ee.ece366.symstagram.Service;
import spark.Request;
import spark.Response;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Handler {
    private final Service service;

    HashMap<String, User> userSet = new HashMap<String, User>();
    //  HashMap<String,Post> postSet = new HashMap<String, Post>();

    public Handler(Service service){
        this.service = service;
    }

    public static void UpdateResponse(Response response, Integer code, String message) {
        response.status(code);
        response.body(message);
        System.out.println(message);
    }

    public User Register(Request request, Response response) {
        User user = service.createUser(
                request.queryParams("name"),
                request.queryParams("password"),
                request.queryParams("phone"),
                request.queryParams("email"));
        //userSet.put(user.getEmail(), user);


        UpdateResponse(response,200,String.valueOf(user));

        return user;
    }

    public Boolean Login(Request request, Response response) {
        String password = request.queryParams("password");
        String email = request.queryParams("email");

        if(userSet.containsKey(email)) {
            if (userSet.get(email).getPassword().equals(password)) {
                UpdateResponse(response,200, "Login successful for "+userSet.get(email).getName());
                return true;
            }
            else {
                UpdateResponse(response,401, "Wrong password");
                return false;
            }
        }
        else {
            UpdateResponse(response,401, "User with that email does not exist");
            return false;
        }
    }

//    public boolean sendPost(Request request, Response response){
//        User user = service.getUser(request.queryParams("email"));
//        User friend = service .getUser(request.queryParams("friendemail"));
//
//        Post post = service.sendPost( request.queryParams("postText"),user, friend);
//
//
//        UpdateResponse(response, 200, post.getPostText());
//        return true;
//    }
//
    public Boolean SendFriendRequest(Request request, Response response) {
        User user = service.getUser(request.queryParams("email"));
        User friend = service.getUser(request.queryParams("friendemail"));
        service.sendFriendRequest(user, friend);
        UpdateResponse(response, 200, String.valueOf(user));
        return true;
    }
//
//    public String GetUsers(Request request, Response response) {
//        Object[] users = userSet.entrySet().toArray();
//        UpdateResponse(response, 200, "List of users successfully retrieved");
//        return Arrays.toString(users);
//    }
//
    public List<Long> GetFriendRequests (Request request, Response response) {
        User user = service.getUser(request.queryParams("email"));
        UpdateResponse(response, 200, "List of pending friend requests successfully retrieved");
        return service.getFriendRequests(user);
    }

    public Boolean AcceptFriendRequest(Request request, Response response) {
        User user = service.getUser(request.queryParams("email"));
        User friend = service.getUser(request.queryParams("friendemail"));
        List<Long> friends = service.getFriendRequests(user);
        if(friends.contains(friend.getID())) {
            service.acceptFriendRequest(user,friend);
            return true;
        }
        else
            return false;
    }

    public List<Long> GetFriends (Request request, Response response) {
        User user = service.getUser(request.queryParams("email"));
        UpdateResponse(response, 200, "List of friends successfully retrieved");
        return service.getFriends(user);
    }
//    public User editInfo(Request request, Response response){
//        User user = userSet.get(request.queryParams("email"));
//        service.updateUser(
//                user,
//                request.queryParams("newName"),
//                request.queryParams("newPassword"),
//                request.queryParams("newPhone"));
//        userSet.put(user.getEmail(), user);
//
//        UpdateResponse(response,200,String.valueOf(user));
//
//        return user;
//    }

}
