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
import java.util.Optional;


public class Handler {
    HashMap<String, User> userSet = new HashMap<String, User>();
    HashMap<String,Post> postSet = new HashMap<String, Post>();

    public Handler(){}
    Service service = new Service();

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
        userSet.put(user.getEmail(), user);

        UpdateResponse(response,200,String.valueOf(user));

        return user;
    }

    public Post createPost(Request request, Response response){
        Post post = service.createPost(
                request.queryParams("postText"),
                userSet.get(request.queryParams("email"))
                );
        postSet.put(post.getId(),post);
        UpdateResponse(response, 200, String.valueOf(post));
        return post;
    }

    public Boolean SendFriendRequest(Request request, Response response) {
        User user = userSet.get(request.queryParams("email"));
        User friend = userSet.get(request.queryParams("friendemail"));
        if(service.sendFriendRequest(user, friend)) {
            UpdateResponse(response, 200, String.valueOf(user));
            return true;
        }
        else {
            UpdateResponse(response, 400, "user(s) do not exist");
            return false;
        }

    }

    public String GetUsers(Request request, Response response) {
        Object[] users = userSet.entrySet().toArray();
        UpdateResponse(response, 200, "List of users successfully retrieved");
        return Arrays.toString(users);
    }

    public ArrayList<String> GetFriendRequests (Request request, Response response) {
        User user = userSet.get(request.queryParams("email"));
        UpdateResponse(response, 200, "List of pending friend requests successfully retrieved");
        return service.getFriendRequests(user);
    }

    public Boolean AcceptFriendRequest(Request request, Response response) {
        User user = userSet.get(request.queryParams("email"));
        User friend = userSet.get(request.queryParams("friendemail"));
        if(service.acceptFriendRequest(user, friend)) {
            UpdateResponse(response, 200, String.valueOf(user));
            return true;
        }
        else {
            UpdateResponse(response, 400, "user(s) do not exist");
            return false;
        }
    }

    public ArrayList<String> GetFriends (Request request, Response response) {
        User user = userSet.get(request.queryParams("email"));
        UpdateResponse(response, 200, "List of friends successfully retrieved");
        return service.getFriends(user);
    }
    public User editInfo(Request request, Response response){
        User user = userSet.get(request.queryParams("email"));
        service.updateUser(
                user,
                request.queryParams("newName"),
                request.queryParams("newPassword"),
                request.queryParams("newPhone"));
        userSet.put(user.getEmail(), user);

        UpdateResponse(response,200,String.valueOf(user));

        return user;
    }

}