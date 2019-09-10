import java.util.*;

/*
 * The channel class is responsible for keeping track of the users
 * within the channel and the messages of each user within the channel.
 * It has a name and knows the names of all the users within its channel,
 * as well as associated messages. It has functions that allow users to
 * be added to the channel and be removed. It also knows if it is invite
 * only and who the owner of the channel is.
 */

public class Channel implements Comparable<Channel>{
    private Set<Integer> users;
    private final Integer owner;
    private final boolean channelPrivacy;
    private final String name;
    
    public Channel(Integer owner, String name, boolean privateChannel) {
        this.users = new TreeSet<Integer>();
        this.owner = owner;
        users.add(owner);
        this.channelPrivacy = privateChannel;
        this.name = name;
    }
    
    public int getOwner() {
        return this.owner;
    }
    
    public String getName() {
        return this.name;
    }
    
    public Set<Integer> getUsers() {
        Set<Integer> newSet = new TreeSet<>();
        newSet.addAll(this.users);
        return newSet;
    }
    
    public int size() {
        return users.size();
    }
    
    public void removeUser(Integer userId) {
        users.remove(userId);
    }
    
    public void addUser(int userId) {
        users.add(userId);
    }
    
    public boolean isPrivate() {
        return channelPrivacy;
    }
    
    public int compareTo(Channel thatChannel) {
        return this.name.compareTo(thatChannel.name);
    }
}