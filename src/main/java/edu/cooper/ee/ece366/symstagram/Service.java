package edu.cooper.ee.ece366.symstagram;

import edu.cooper.ee.ece366.symstagram.model.User;
import edu.cooper.ee.ece366.symstagram.model.Post;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import edu.cooper.ee.ece366.symstagram.store.PlatformStore;
import edu.cooper.ee.ece366.symstagram.store.PlatformStoreImpl;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.core.result.ResultIterable;
import org.jdbi.v3.core.result.ResultIterator;
import org.jdbi.v3.core.statement.Query;

public class Service {

    private Jdbi jdbi;
    private PlatformStore platformStore;

    Service(PlatformStore platformStore){
        this.platformStore = platformStore;
    }

    public User createUser(String name, String password, String phone, String email) {
        User user;
        user = new User(name, password, phone, email);
        return platformStore.createUser(user);
    }

    public User updateUser(User user, String name, String password, String phone){
        user.setName(name);
        user.setPassword(password);
        user.setPhone(phone);
        return user;
    }


//    public Post sendPost(String postText, User user, User receiver){
//        Post post;
//        post = new Post(postText,user);
//        for (int i = 0; i < friends.size(); i++){
//            if (receiver.getID() == friends.get(i)){
//                return platformStore.createPost(post,user, receiver);
//            }
//        }
//        System.out.println("The receiver is not your friend!!");
//        return new Post("Nothing", null);
//    }
//
//
//
//
//   public Post sendPost(User user, User friend, String postText){
//        Post post = createPost(postText, user);
//        if (user != null && friend != null){
//            friend.addToPostLists(post);
//        }
//       return post;
//    }



    public void sendFriendRequest(User user, User friend) {
        LocalDateTime time = LocalDateTime.now();
        platformStore.sendFriendRequest(user.getID(), friend.getID(), time);
    }
    public List<Long> getFriendRequests(User user) {
        return platformStore.getFriendRequests(user.getID());
    }

    public Boolean acceptFriendRequest(User user, User friend) {
        return platformStore.acceptFriendRequest(user.getID(), friend.getID());
    }

    public List<Long> getFriends(User user) {
        return platformStore.getFriends(user.getID());
    }

    public User getUser(String userEmail){
        return platformStore.getUser(userEmail);
    }
}