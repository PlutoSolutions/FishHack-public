package cat.cattyn.fishhack.client.managers;

import java.util.ArrayList;
import java.util.List;

public class FriendManager {

    private final List<String> friends;

    public FriendManager() {
        friends = new ArrayList<>();
    }

    public boolean isFriend(String name) {
        return friends.stream().anyMatch(f -> f.equalsIgnoreCase(name));
    }

    public List<String> getFriends() {
        return friends;
    }
}