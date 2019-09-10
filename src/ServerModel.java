import java.util.*;


/**
 * The {@code ServerModel} is the class responsible for tracking the state of the server, including
 * its current users and the channels they are in.
 * This class is used by subclasses of {@link Command} to:
 *     1. handle commands from clients, and
 *     2. handle commands from {@link ServerBackend} to coordinate client connection/disconnection.
 */
public final class ServerModel implements ServerModelApi {
    
    private Map<Integer, String> users;
    private Set<Channel> channels;

    /**
     * Constructs a {@code ServerModel} and initializes any collections needed for modeling the
     * server state.
     */
    public ServerModel() {  
        /* List of Users: within a server/channel/etc will be instantiated using a TreeMap
         * because it will allow rapid retrieval of userID when given a nickname and vice versa
         * as compared to either TreeSet or LinkedList where one has to walk through the "list".
         * 
         * List of Channels: instantiated using a TreeMap to allow rapid addition to channels
         */ 
        this.users = new TreeMap<Integer, String>();
        this.channels = new TreeSet<Channel>();
    }
    
    //Helper function returning a Set<String> of all users in the channel a current user is in
    
    public Set<String> channelsWithUser (Integer userId) {
        Set<String> usersChannels = new TreeSet<>();
        for (Channel thisChannel : channels) {
            if (thisChannel.getUsers().contains(userId)) {
                for (Integer thisUserId : thisChannel.getUsers()) {
                    usersChannels.add(users.get(thisUserId));
                }
            }
        }
        return usersChannels;
    }
    
    public Channel getChannelFromName (String channelName) {
        for (Channel thisChannel : channels) {
            if (thisChannel.getName().equals(channelName)) {
                return thisChannel;
            }
        }
        return null;
    }


    //==========================================================================
    // Client connection handlers
    //==========================================================================

    /**
     * Informs the model that a client has connected to the server with the given user ID. The model
     * should update its state so that it can identify this user during later interactions. The
     * newly connected user will not yet have had the chance to set a nickname, and so the model
     * should provide a default nickname for the user.
     * Any user who is registered with the server (without being later deregistered) should appear
     * in the output of {@link #getRegisteredUsers()}.
     *
     * @param userId The unique ID created by the backend to represent this user
     * @return A {@link Broadcast} to the user with their new nickname
     */
    public Broadcast registerUser(int userId) {
        String nickname = generateUniqueNickname();
        users.put(userId, nickname);
        return Broadcast.connected(nickname);
    }

    /**
     * Generates a unique nickname of the form "UserX", where X is the
     * smallest non-negative integer that yields a unique nickname for a user.
     * @return the generated nickname
     */
    private String generateUniqueNickname() {
        int suffix = 0;
        String nickname;
        Collection<String> existingUsers = getRegisteredUsers();
        do {
            nickname = "User" + suffix++;
        } while (existingUsers != null && existingUsers.contains(nickname));
        return nickname;
    }

    /**
     * Determines if a given nickname is valid or invalid (contains at least
     * one alphanumeric character, and no non-alphanumeric characters).
     * @param name The channel or nickname string to validate
     * @return true if the string is a valid name
     */
    public static boolean isValidName(String name) {
        if (name == null || name.isEmpty()) {
            return false;
        }
        for (char c : name.toCharArray()) {
            if (!Character.isLetterOrDigit(c)) {
                return false;
            }
        }
        return true;
    }

    /**
     * Informs the model that the client with the given user ID has disconnected from the server.
     * After a user ID is deregistered, the server backend is free to reassign this user ID to an
     * entirely different client; as such, the model should remove all state of the user associated
     * with the deregistered user ID. The behavior of this method if the given user ID is not
     * registered with the model is undefined.
     * Any user who is deregistered (without later being registered) should not appear in the output
     * of {@link #getRegisteredUsers()}.
     *
     * @param userId The unique ID of the user to deregister
     * @return A {@link Broadcast} instructing clients to remove the user from all channels
     */
    public Broadcast deregisterUser(int userId) {
        TreeSet<String> usersChannels = new TreeSet<String>();
        Iterator<Channel> allChannelsIterator = channels.iterator();
        while(allChannelsIterator.hasNext()) {
            Channel currentChannel = allChannelsIterator.next();
            if (currentChannel.getUsers().contains(userId)) {
                usersChannels.add(currentChannel.getName());
                currentChannel.removeUser(userId);
            }
        }
        return Broadcast.disconnected(users.get(userId), usersChannels);
    }

    

    //==========================================================================
    // Model update functions
    //==========================================================================
    // TODO: Add functions that update your model

    public Broadcast newNickname(int userId, String newNickname, Command command) {
        if (users.containsValue(newNickname)) {
            return Broadcast.error(command, ServerError.NAME_ALREADY_IN_USE);
        }
        return Broadcast.okay(command, channelsWithUser(userId));
    }
    
    public Broadcast createChannel(String channelName, int senderId, String sender, 
                                   Command command) {
        if(channels.contains(channelName)) {
            return Broadcast.error(command, ServerError.CHANNEL_ALREADY_EXISTS);
        }
        channels.add(new Channel(senderId, sender, false));
        
        Set<String> senderSet = new TreeSet<String>();
        senderSet.add(sender);
        return Broadcast.okay(command, senderSet);
    }
    
    public Broadcast joinChannel(String channelName, int senderId, String sender, 
                                 Command command) {
        TreeSet<String> currentChannelUsersNicknames = new TreeSet<String>();
        Iterator<Channel> allChannelsIterator = channels.iterator();
        if(channels.contains(getChannelFromName(channelName))) {
        while(allChannelsIterator.hasNext()) {
            Channel currentChannel = allChannelsIterator.next();
            if (currentChannel.getName().equals(channelName)) {
                if(currentChannel.isPrivate()) {
                    return Broadcast.error(command, ServerError.JOIN_PRIVATE_CHANNEL);
                }
                currentChannel.addUser(senderId);
                Iterator<Integer> currentChannelIds = currentChannel.getUsers().iterator();
                while(allChannelsIterator.hasNext()) {
                    Channel currentId = allChannelsIterator.next();
                    currentChannelUsersNicknames.add(users.get(currentId));
                }
                return Broadcast.names(command, currentChannelUsersNicknames, 
                                       users.get(currentChannel.getOwner()));
            }
        }
        }
        return Broadcast.error(command, ServerError.NO_SUCH_CHANNEL);
    }
    
    public Broadcast message(String channelName, String sender, Command command) {
        Channel currentChannel = getChannelFromName(channelName);
        if(!channels.contains(channelName)) {
            return Broadcast.error(command, ServerError.NO_SUCH_CHANNEL);
        }
        if(!currentChannel.getUsers().contains(sender)) {
            return Broadcast.error(command, ServerError.USER_NOT_IN_CHANNEL);
        }
        Set<String> usersInChannel = (Set<String>) getUsers(channelName);
        
        return Broadcast.okay(command, usersInChannel);
    }
    
    public Broadcast leave(String channelName, int senderId, String sender, 
                           Command command) {
        Channel currentChannel = getChannelFromName(channelName);
        if (currentChannel == null) {
            return Broadcast.error(command, ServerError.NO_SUCH_CHANNEL);
        }
        if(!currentChannel.getUsers().contains(sender)) {
            return Broadcast.error(command, ServerError.USER_NOT_IN_CHANNEL);
        }
        
        Set<String> previousUsersInChannel = (Set<String>) getUsers(channelName);
        
        if(currentChannel.getOwner() == senderId) {
            channels.remove(channelName);
        }
        else {
            currentChannel.removeUser(senderId);
        }
        return Broadcast.okay(command, previousUsersInChannel);
        
    }
    
    public Broadcast inviteChannel(String channelName, String sender, Command command, 
                                   String userToInvite) {
        Channel currentChannel = getChannelFromName(channelName);
        if(currentChannel == null) {
            return Broadcast.error(command, ServerError.NO_SUCH_CHANNEL);
        }
        if(!users.containsValue(userToInvite)) {
            return Broadcast.error(command, ServerError.NO_SUCH_USER);
        }
        if(this.getOwner(channelName) != sender) {
            return Broadcast.error(command, ServerError.USER_NOT_OWNER);
        }
        if(!currentChannel.isPrivate()) {
            return Broadcast.error(command, ServerError.INVITE_TO_PUBLIC_CHANNEL);
        }
        currentChannel.addUser(getUserId(userToInvite));
        return Broadcast.names(command, (Set<String>) getUsers(channelName), 
                               users.get(currentChannel.getOwner()));
    }
    
    public Broadcast kick(String channelName, int senderId, String sender, Command command,
                         String userToKick) {
        Channel currentChannel = getChannelFromName(channelName);
        if(!channels.contains(channelName)) {
            return Broadcast.error(command, ServerError.NO_SUCH_CHANNEL);
        }
        if(!users.containsValue(userToKick)) {
            return Broadcast.error(command, ServerError.NO_SUCH_USER);
        }
        if(this.getOwner(channelName) != sender) {
            return Broadcast.error(command, ServerError.USER_NOT_OWNER);
        }
        if(!currentChannel.getUsers().contains(userToKick)) {
            return Broadcast.error(command, ServerError.USER_NOT_IN_CHANNEL);
        }
        
        Set<String> previousUsersInChannel = (Set<String>) getUsers(channelName);
        
        if(currentChannel.getOwner() == senderId) {
            channels.remove(channelName);
        }
        else {
            currentChannel.removeUser(getUserId(userToKick));
        }
        return Broadcast.okay(command, previousUsersInChannel);
    }
    
    
    //==========================================================================
    // Server model queries
    // These functions provide helpful ways to test the state of your model.
    // You may also use them in your implementation.
    //==========================================================================

    /**
     * Gets the user ID currently associated with the given nickname. The returned ID is -1 if the
     * nickname is not currently in use
     *
     * @param nickname The nickname for which to get the associated user ID
     * @return The user ID of the user with the argued nickname if such a user exists, otherwise -1
     */
    public int getUserId(String nickname) {
        for (Map.Entry<Integer, String> currentUser : users.entrySet()) {
            if (currentUser.getValue().equals(nickname)) {
                return currentUser.getKey();
            }
        }
        return -1;
    }

    /**
     * Gets the nickname currently associated with the given user ID. The returned nickname is
     * null if the user ID is not currently in use.
     *
     * @param userId The user ID for which to get the associated nickname
     * @return The nickname of the user with the argued user ID if such a user exists, otherwise
     *          null
     */
    public String getNickname(int userId) {
        if(users.containsKey(userId)) {
            return users.get(userId); 
        }
        return null;
    }

    /**
     * Gets a collection of the nicknames of all users who are registered with the server. Changes
     * to the returned collection should not affect the server state.
     * 
     * This method is provided for testing.
     *
     * @return The collection of registered user nicknames
     */
    public Collection<String> getRegisteredUsers() {
        return users.values();
    }

    /**
     * Gets a collection of the names of all the channels that are present on the server. Changes to
     * the returned collection should not affect the server state.
     * 
     * This method is provided for testing.
     *
     * @return The collection of channel names
     */
    public Collection<String> getChannels() {
        Collection<String> channelNames = new TreeSet<>();
        for (Channel c : channels) {
            channelNames.add(c.getName());
        }
        return channelNames;
    }

    /**
     * Gets a collection of the nicknames of all the users in a given channel. The collection is
     * empty if no channel with the given name exists. Modifications to the returned collection
     * should not affect the server state.
     *
     * This method is provided for testing.
     *
     * @param channelName The channel for which to get member nicknames
     * @return The collection of user nicknames in the argued channel
     */
    public Collection<String> getUsers(String channelName) {
        Channel thatChannel = getChannelFromName(channelName);
        Set<String> nicknames = new TreeSet<>();
        for(Integer thatUser : thatChannel.getUsers()) {
            nicknames.add(users.get(thatUser));
        }
        return nicknames;
    }

    /**
     * Gets the nickname of the owner of the given channel. The result is {@code null} if no
     * channel with the given name exists.
     *
     * This method is provided for testing.
     *
     * @param channelName The channel for which to get the owner nickname
     * @return The nickname of the channel owner if such a channel exists, othewise null
     */
    public String getOwner(String channelName) {
        return users.get(getChannelFromName(channelName).getOwner());
    }

}
