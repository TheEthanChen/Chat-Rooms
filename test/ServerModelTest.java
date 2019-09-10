import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Collection;

import java.util.*;


public class ServerModelTest {
    private ServerModel model;

    @Before
    public void setUp() {
        // We initialize a fresh ServerModel for each test
        model = new ServerModel();
    }

    /* Here is an example test that checks the functionality of your changeNickname error handling.
     * Each line has commentary directly above it which you can use as a framework for the remainder
     * of your tests.
     */
    @Test
    public void testInvalidNickname() {
        // A user must be registered before their nickname can be changed, so we first register a
        // user with an arbitrarily chosen id of 0.
        model.registerUser(0);

        // We manually create a Command that appropriately tests the case we are checking.
        // In this case, we create a NicknameCommand whose new Nickname is invalid.
        Command command = new NicknameCommand(0, "User0", "!nv@l!d!");

        // We manually create the expected Broadcast using the Broadcast factory methods.
        // In this case, we create an error Broadcast with our command and an INVALID_NAME error.
        Broadcast expected = Broadcast.error(command, ServerError.INVALID_NAME);

        // We then get the actual Broadcast returned by the method we are trying to test.
        // In this case, we use the updateServerModel method of the NicknameCommand.
        Broadcast actual = command.updateServerModel(model);

        // The first assertEquals call tests whether the method returns the appropriate broacast.
        assertEquals("Broadcast", expected, actual);

        // We also want to test whether the state has been correctly changed.
        // In this case, the state that would be affected is the user's Collection.
        Collection<String> users = model.getRegisteredUsers();

        // We now check to see if our command updated the state appropriately.
        // In this case, we first ensure that no additional users have been added.
        assertEquals("Number of registered users", 1, users.size());

        // We then check if the username was updated to an invalid value(it should not have been).
        assertTrue("Old nickname still registered", users.contains("User0"));

        // Finally, we check that the id 0 is still associated with the old, unchanged nickname.
        assertEquals("User with id 0 nickname unchanged", "User0", model.getNickname(0));
    }

    /*
     * Your TAs will be manually grading the tests you write in this file.
     * Don't forget to test both the public methods you have added to your
     * ServerModel class as well as the behavior of the server in different
     * scenarios.
     * You might find it helpful to take a look at the tests we have already
     * provided you with in ChannelsMessagesTest, ConnectionNicknamesTest,
     * and InviteOnlyTest.
     */

    @Test
    public void testValidNickname() {
        // A user must be registered before their nickname can be changed, so we first register a
        // user with an arbitrarily chosen id of 0.
        model.registerUser(0);

        // We manually create a Command that appropriately tests the case we are checking.
        // In this case, we create a NicknameCommand whose new Nickname is valid.
        Command command = new NicknameCommand(0, "User0", "Val1d");

        // We manually create the expected Broadcast using the Broadcast factory methods.
        // In this case, we create a Broadcast with our new Nickname.
        Broadcast expected = Broadcast.okay(command, (Set<String>) model.getUsers("User0"));

        // We then get the actual Broadcast returned by the method we are trying to test.
        // In this case, we use the updateServerModel method of the NicknameCommand.
        Broadcast actual = command.updateServerModel(model);

        // The first assertEquals call tests whether the method returns the appropriate broacast.
        assertEquals("Broadcast", expected, actual);

        // We also want to test whether the state has been correctly changed.
        // In this case, the state that would be affected is the user's Collection.
        Collection<String> users = model.getRegisteredUsers();

        // We now check to see if our command updated the state appropriately.
        // In this case, we first ensure that no additional users have been added.
        assertEquals("Number of registered users", 1, users.size());

        // We then check if the username was updated to an invalid value(it should not have been).
        assertTrue("Old nickname still registered", users.contains("Val1d"));

        // Finally, we check that the id 0 is still associated with the old, unchanged nickname.
        assertEquals("User with id 0 nickname changed", "Val1d", model.getNickname(0));
    }
    
    
    @Test
    public void testExistingUserId() {
        // A user must be registered before their nickname can be changed, so we first register a
        // user with an arbitrarily chosen id of 0.
        model.registerUser(0);
        model.registerUser(1);
        model.registerUser(2);

        // We manually create a Command that appropriately tests the case we are checking.
        // In this case, we create a NicknameCommand whose new Nickname is valid.
        Command command = new NicknameCommand(2, "User2", "Val1d");

        // We manually create the expected Broadcast using the Broadcast factory methods.
        // In this case, we create a Broadcast with our new Nickname.
        Broadcast expected = Broadcast.okay(command, (Set<String>) model.getUsers("User2"));

        // We then get the actual Broadcast returned by the method we are trying to test.
        // In this case, we use the updateServerModel method of the NicknameCommand.
        Broadcast actual = command.updateServerModel(model);

        // The first assertEquals call tests whether the method returns the appropriate broacast.
        assertEquals("Broadcast", expected, actual);

        // We also want to test whether the state has been correctly changed.
        // In this case, the state that would be affected is the user's Collection.
        Collection<String> users = model.getRegisteredUsers();

        // We now check to see if our command updated the state appropriately.
        // In this case, we first ensure that no additional users have been added.
        assertEquals("Number of registered users", 3, users.size());

        // We then check if the username was updated to an invalid value(it should not have been).
        assertTrue("Old nickname still registered", users.contains("Val1d"));

        // Finally, we check that the id 0 is still associated with the old, unchanged nickname.
        assertEquals("Nickname associated with User2", 2, model.getUserId("Val1d"));
    }
    
    @Test
    public void testNonexistentUserId() {
        // A user must be registered before their nickname can be changed, so we first register a
        // user with an arbitrarily chosen id of 0.
        model.registerUser(0);
        model.registerUser(1);
        model.registerUser(2);

        // We manually create a Command that appropriately tests the case we are checking.
        // In this case, we create a NicknameCommand whose new Nickname is valid.
        Command command = new NicknameCommand(0, "User2", "Val1d");

        // We manually create the expected Broadcast using the Broadcast factory methods.
        // In this case, we create a Broadcast with our new Nickname.
        Broadcast expected = Broadcast.okay(command, (Set<String>) model.getUsers("User2"));

        // We then get the actual Broadcast returned by the method we are trying to test.
        // In this case, we use the updateServerModel method of the NicknameCommand.
        Broadcast actual = command.updateServerModel(model);

        // The first assertEquals call tests whether the method returns the appropriate broacast.
        assertEquals("Broadcast", expected, actual);

        // We also want to test whether the state has been correctly changed.
        // In this case, the state that would be affected is the user's Collection.
        Collection<String> users = model.getRegisteredUsers();

        // We now check to see if our command updated the state appropriately.
        // In this case, we first ensure that no additional users have been added.
        assertEquals("Number of registered users", 3, users.size());

        // We then check if the username was updated to an invalid value(it should not have been).
        assertTrue("Old nickname still registered", users.contains("Val1d"));

        // Finally, we check that the id 0 is still associated with the old, unchanged nickname.
        assertEquals("Nickname not associated with users in model", -1, model.getUserId("Vlad"));
    }
    
    @Test
    public void testGetRegisteredUsers() {
        // A user must be registered before their nickname can be changed, so we first register a
        // user with an arbitrarily chosen id of 0.
        model.registerUser(0);
        model.registerUser(1);
        model.registerUser(2);
        TreeMap<Integer, String> allUsersMap = new TreeMap<Integer, String>();
        allUsersMap.put(0, "User0");
        allUsersMap.put(1, "User1");
        allUsersMap.put(2, "Val1d");

        // We manually create a Command that appropriately tests the case we are checking.
        // In this case, we create a NicknameCommand whose new Nickname is valid.
        Command command = new NicknameCommand(2, "User2", "Val1d");

        // We manually create the expected Broadcast using the Broadcast factory methods.
        // In this case, we create a Broadcast with our new Nickname.
        Broadcast expected = Broadcast.okay(command, (Set<String>) model.getUsers("User2"));

        // We then get the actual Broadcast returned by the method we are trying to test.
        // In this case, we use the updateServerModel method of the NicknameCommand.
        Broadcast actual = command.updateServerModel(model);

        // The first assertEquals call tests whether the method returns the appropriate broacast.
        assertEquals("Broadcast", expected, actual);

        // We also want to test whether the state has been correctly changed.
        // In this case, the state that would be affected is the user's Collection.
        Collection<String> users = model.getRegisteredUsers();

        // We now check to see if our command updated the state appropriately.
        // In this case, we first ensure that no additional users have been added.
        assertEquals("Number of registered users", 3, users.size());

        // We then check if the username was updated to an invalid value(it should not have been).
        assertTrue("Old nickname still registered", users.contains("Val1d"));
        
        assertEquals("Nicknames in the Server", allUsersMap.values(), model.getRegisteredUsers());
    }
    
    @Test
    public void testEmptyGetRegisteredUsers() {
        // A user must be registered before their nickname can be changed, so we first register a
        // user with an arbitrarily chosen id of 0.
        model.registerUser(0);
        model.deregisterUser(0);

        // We also want to test whether the state has been correctly changed.
        // In this case, the state that would be affected is the user's Collection.
        Collection<String> users = model.getRegisteredUsers();

        // We now check to see if our command updated the state appropriately.
        // In this case, we first ensure that no additional users have been added.
        assertEquals("Number of registered users", 0, users.size());

        // Finally, we check that the id 0 is still associated with the old, unchanged nickname.
        assertEquals("Call getRegisteredUsers when empty", null, model.getRegisteredUsers());
    }
    
    @Test
    public void testGetChannels() {
        // A user must be registered before their nickname can be changed, so we first register a
        // user with an arbitrarily chosen id of 0.
        model.registerUser(0);
        
        Command command = new CreateCommand(0, "User0", "Channel0", false);
        
        Set<String> userSet = new TreeSet<String>();
        userSet.add("User0");
        Set<String>channelSet = new TreeSet<String>();
        channelSet.add("Channel0");

        // We manually create the expected Broadcast using the Broadcast factory methods.
        // In this case, we create a Broadcast with our new Nickname.
        Broadcast expected = Broadcast.okay(command, userSet);

        // We then get the actual Broadcast returned by the method we are trying to test.
        // In this case, we use the updateServerModel method of the NicknameCommand.
        Broadcast actual = command.updateServerModel(model);

        // The first assertEquals call tests whether the method returns the appropriate broacast.
        assertEquals("Broadcast", expected, actual);

        // We also want to test whether the state has been correctly changed.
        // In this case, the state that would be affected is the user's Collection.
        Collection<String> channels = model.getChannels();

        // We now check to see if our command updated the state appropriately.
        // In this case, we first ensure that no additional users have been added.
        assertEquals("Number of channels", 1, channels.size());

        // Finally, we check that the id 0 is still associated with the old, unchanged nickname.
        assertEquals("Get the correct channel", channelSet, model.getChannels());
    }
    
    @Test
    public void testEmptyGetChannels() {
        // A user must be registered before their nickname can be changed, so we first register a
        // user with an arbitrarily chosen id of 0.
        model.registerUser(0);
        
        Command commandCreate = new CreateCommand(0, "User0", "Channel0", false);
        commandCreate.updateServerModel(model);
        Command commandDestroy = new LeaveCommand(0, "User0", "Channel0");
        
        Set<String> userSet = new TreeSet<String>();
        userSet.add("User0");

        // We manually create the expected Broadcast using the Broadcast factory methods.
        // In this case, we create a Broadcast with our new Nickname.
        Broadcast expected = Broadcast.okay(commandDestroy, userSet);

        // We then get the actual Broadcast returned by the method we are trying to test.
        // In this case, we use the updateServerModel method of the NicknameCommand.
        Broadcast actual = commandDestroy.updateServerModel(model);

        // The first assertEquals call tests whether the method returns the appropriate broacast.
        assertEquals("Broadcast", expected, actual);

        // We also want to test whether the state has been correctly changed.
        // In this case, the state that would be affected is the user's Collection.
        Collection<String> channels = model.getChannels();

        // We now check to see if our command updated the state appropriately.
        // In this case, we first ensure that no additional users have been added.
        assertEquals("Number of channels", 0, channels.size());

        // Finally, we check that the id 0 is still associated with the old, unchanged nickname.
        Map<Integer, String> emptyChannel = new TreeMap<Integer, String>();
        assertEquals("Call getChannels on map without channels", emptyChannel.keySet(), 
                     model.getChannels());
    }
    
    @Test
    public void testExistingGetNickname() {
        // A user must be registered before their nickname can be changed, so we first register a
        // user with an arbitrarily chosen id of 0.
        model.registerUser(0);
        model.registerUser(1);
        model.registerUser(2);

        // We manually create a Command that appropriately tests the case we are checking.
        // In this case, we create a NicknameCommand whose new Nickname is valid.
        Command command = new NicknameCommand(2, "User2", "Val1d");

        // We manually create the expected Broadcast using the Broadcast factory methods.
        // In this case, we create a Broadcast with our new Nickname.
        Broadcast expected = Broadcast.okay(command, (Set<String>) model.getUsers("User2"));

        // We then get the actual Broadcast returned by the method we are trying to test.
        // In this case, we use the updateServerModel method of the NicknameCommand.
        Broadcast actual = command.updateServerModel(model);

        // The first assertEquals call tests whether the method returns the appropriate broacast.
        assertEquals("Broadcast", expected, actual);

        // We also want to test whether the state has been correctly changed.
        // In this case, the state that would be affected is the user's Collection.
        Collection<String> users = model.getRegisteredUsers();

        // We now check to see if our command updated the state appropriately.
        // In this case, we first ensure that no additional users have been added.
        assertEquals("Number of registered users", 3, users.size());

        // We then check if the username was updated to an invalid value(it should not have been).
        assertTrue("Old nickname still registered", users.contains("Val1d"));

        // Finally, we check that the id 0 is still associated with the old, unchanged nickname.
        assertEquals("Retrieve existing nickname of user", "Val1d", model.getNickname(2));
    }
    
    @Test
    public void testNonexistingGetNickname() {
        // A user must be registered before their nickname can be changed, so we first register a
        // user with an arbitrarily chosen id of 0.
        model.registerUser(0);
        model.registerUser(1);
        model.registerUser(2);

        // We manually create a Command that appropriately tests the case we are checking.
        // In this case, we create a NicknameCommand whose new Nickname is valid.
        Command command = new NicknameCommand(2, "User2", "Val1d");

        // We manually create the expected Broadcast using the Broadcast factory methods.
        // In this case, we create a Broadcast with our new Nickname.
        Broadcast expected = Broadcast.okay(command, (Set<String>) model.getUsers("User2"));

        // We then get the actual Broadcast returned by the method we are trying to test.
        // In this case, we use the updateServerModel method of the NicknameCommand.
        Broadcast actual = command.updateServerModel(model);

        // The first assertEquals call tests whether the method returns the appropriate broacast.
        assertEquals("Broadcast", expected, actual);

        // We also want to test whether the state has been correctly changed.
        // In this case, the state that would be affected is the user's Collection.
        Collection<String> users = model.getRegisteredUsers();

        // We now check to see if our command updated the state appropriately.
        // In this case, we first ensure that no additional users have been added.
        assertEquals("Number of registered users", 3, users.size());

        // We then check if the username was updated to an invalid value(it should not have been).
        assertTrue("Old nickname still registered", users.contains("Val1d"));

        // Finally, we check that the id 0 is still associated with the old, unchanged nickname.
        assertEquals("Retrieve existing nickname of user", null, model.getNickname(3));
    }
    
    @Test
    public void testGetUsers() {
        // A user must be registered before their nickname can be changed, so we first register a
        // user with an arbitrarily chosen id of 0.
        model.registerUser(0);
        
        Set<String> userSet = new TreeSet<String>();
        userSet.add("User0");
        Set<String> allUsersSet = new TreeSet<String>();
        allUsersSet.add("User0");
        allUsersSet.add("User1");

        // We manually create a Command that appropriately tests the case we are checking.
        Command command = new CreateCommand(0, "User0", "Channel0", false);
        command.updateServerModel(model);
        Command joinChannel = new JoinCommand(0, "User1", "Channel0");
        joinChannel.updateServerModel(model);

        // We manually create the expected Broadcast using the Broadcast factory methods.
        // In this case, we create a Broadcast with our new Nickname.
        Broadcast expected = Broadcast.names(joinChannel, allUsersSet, "User0");

        // We then get the actual Broadcast returned by the method we are trying to test.
        // In this case, we use the updateServerModel method of the NicknameCommand.
        Broadcast actual = joinChannel.updateServerModel(model);

        // The first assertEquals call tests whether the method returns the appropriate broacast.
        assertEquals("Broadcast", expected, actual);

        // We also want to test whether the state has been correctly changed.
        // In this case, the state that would be affected is the user's Collection.
        Collection<String> users = model.getUsers("Channel0");

        // We now check to see if our command updated the state appropriately.
        // In this case, we first ensure that no additional users have been added.
        assertEquals("Number of users in channel", 2, users.size());

        // Finally, we check that the id 0 is still associated with the old, unchanged nickname.
        assertEquals("Users in the Channel", allUsersSet, model.getUsers("Channel0"));
    }
    
    @Test
    public void testEmptyGetUsers() {
        // A user must be registered before their nickname can be changed, so we first register a
        // user with an arbitrarily chosen id of 0.
        model.registerUser(0);
        
        Set<String> allUsersSet = new TreeSet<String>();
        allUsersSet.add("User0");

        // We manually create a Command that appropriately tests the case we are checking.
        Command command = new CreateCommand(0, "User0", "Channel0", false);
        
        Command leaveChannel = new LeaveCommand(0, "User0", "Channel0");
        
        model.joinChannel("Channel0", 0, "User0", leaveChannel);

        // We manually create the expected Broadcast using the Broadcast factory methods.
        // In this case, we create a Broadcast with our new Nickname.
        Broadcast expected = Broadcast.okay(command, allUsersSet);

        // We then get the actual Broadcast returned by the method we are trying to test.
        // In this case, we use the updateServerModel method of the NicknameCommand.
        Broadcast actual = command.updateServerModel(model);

        // The first assertEquals call tests whether the method returns the appropriate broacast.
        assertEquals("Broadcast", expected, actual);

        // We also want to test whether the state has been correctly changed.
        // In this case, the state that would be affected is the user's Collection.
        Collection<String> users = model.getUsers("Channel0");

        // We now check to see if our command updated the state appropriately.
        // In this case, we first ensure that no additional users have been added.
        assertEquals("Number of users in channel", 0, users.size());

        // Finally, we check that the id 0 is still associated with the old, unchanged nickname.
        assertEquals("No Users in the Channel", null, model.getUsers("Channel0"));
    }
    
    @Test
    public void testGetOwner() {
        // A user must be registered before their nickname can be changed, so we first register a
        // user with an arbitrarily chosen id of 0.
        model.registerUser(0);
        
        Set<String> allUsersSet = new TreeSet<String>();
        allUsersSet.add("User0");

        // We manually create a Command that appropriately tests the case we are checking.
        Command command = new CreateCommand(0, "User0", "Channel0", false);

        // We manually create the expected Broadcast using the Broadcast factory methods.
        // In this case, we create a Broadcast with our new Nickname.
        Broadcast expected = Broadcast.okay(command, allUsersSet);

        // We then get the actual Broadcast returned by the method we are trying to test.
        // In this case, we use the updateServerModel method of the NicknameCommand.
        Broadcast actual = command.updateServerModel(model);

        // The first assertEquals call tests whether the method returns the appropriate broacast.
        assertEquals("Broadcast", expected, actual);

        // We also want to test whether the state has been correctly changed.
        // In this case, the state that would be affected is the user's Collection.
        Collection<String> users = model.getUsers("Channel0");

        // We now check to see if our command updated the state appropriately.
        // In this case, we first ensure that no additional users have been added.
        assertEquals("Number of users in channel", 1, users.size());

        // Finally, we check that the id 0 is still associated with the old, unchanged nickname.
        assertEquals("No Users in the Channel", "User0", model.getOwner("Channel0"));
    }
    
    @Test
    public void testCreateExistingChannel() {
        model.registerUser(0);
        Command create = new CreateCommand(0, "User0", "java", false);
        Command createAgain =  new CreateCommand(0, "User0", "java", false);
        Broadcast expected = Broadcast.error(createAgain, ServerError.CHANNEL_ALREADY_EXISTS);
        assertEquals("broadcast", expected, create.updateServerModel(model));

        assertTrue("channel exists", model.getChannels().contains("java"));
        assertTrue("channel has creator", model.getUsers("java").contains("User0"));
        assertEquals("channel has owner", "User0", model.getOwner("java"));
    }
    
    
    @Test
    public void testInvalidNameChannel() {
        model.registerUser(0);
        Command create = new CreateCommand(0, "User0", "@@#$!", false);
        Broadcast expected = Broadcast.error(create, ServerError.INVALID_NAME);
        assertEquals("broadcast", expected, create.updateServerModel(model));
    }
    
    
    @Test
    public void testJoinChannelNotExist() {
        model.registerUser(0);
        Command create = new CreateCommand(0, "User0", "java", false);
        create.updateServerModel(model);

        Command join = new JoinCommand(0, "User0", "lava");
        Set<String> recipients = new TreeSet<>();
        recipients.add("User0");
        Broadcast expected = Broadcast.error(join, ServerError.NO_SUCH_CHANNEL);
        assertEquals("broadcast", expected, join.updateServerModel(model));

        assertTrue("User0 in channel", model.getUsers("java").contains("User0"));
        assertEquals("num. users in channel", 1, model.getUsers("java").size());
    }
    
    
    @Test
    public void testJoinChannelPrivate() {
        model.registerUser(0);
        model.registerUser(1);
        Command create = new CreateCommand(0, "User0", "java", true);
        create.updateServerModel(model);

        Command join = new JoinCommand(1, "User1", "java");
        Set<String> recipients = new TreeSet<>();
        recipients.add("User0");
        Broadcast expected = Broadcast.error(join, ServerError.JOIN_PRIVATE_CHANNEL);
        assertEquals("broadcast", expected, join.updateServerModel(model));

        assertTrue("User0 in channel", model.getUsers("java").contains("User0"));
        assertEquals("num. users in channel", 1, model.getUsers("java").size());
    }
    
    @Test
    public void testNickBroadcastsToChannelNameAlreadyInUse() {
        model.registerUser(0);
        model.registerUser(1);
        Command create = new CreateCommand(0, "User0", "java", false);
        create.updateServerModel(model);
        Command join = new JoinCommand(1, "User1", "java");
        join.updateServerModel(model);

        Command nick = new NicknameCommand(0, "User0", "User1");
        Set<String> recipients = new TreeSet<>();
        recipients.add("User1");
        recipients.add("Duke");
        Broadcast expected = Broadcast.error(nick, ServerError.NAME_ALREADY_IN_USE);
        assertEquals("broadcast", expected, nick.updateServerModel(model));

        assertTrue("old nick is in channel", model.getUsers("java").contains("User0"));
        assertTrue("unaffected user still in channel", model.getUsers("java").contains("User1"));
    }
    
    @Test
    public void testLeaveChannelNonExistent() {
        model.registerUser(0);
        model.registerUser(1);
        Command create = new CreateCommand(0, "User0", "java", false);
        create.updateServerModel(model);
        Command join = new JoinCommand(1, "User1", "java");
        join.updateServerModel(model);

        Command leave = new LeaveCommand(1, "User1", "lava");
        Set<String> recipients = new TreeSet<>();
        recipients.add("User1");
        recipients.add("User0");
        Broadcast expected = Broadcast.error(leave, ServerError.NO_SUCH_CHANNEL);
        assertEquals("broadcast", expected, leave.updateServerModel(model));

        assertTrue("User0 in channel", model.getUsers("java").contains("User0"));
        assertTrue("User1 in channel", model.getUsers("java").contains("User1"));
        assertEquals("num. users in channel", 2, model.getUsers("java").size());
    }
    
    @Test
    public void testLeaveChannelExistUserNotInChannel() {
        model.registerUser(0);
        model.registerUser(1);
        Command createChannel1 = new CreateCommand(0, "User0", "Channel1", false);
        createChannel1.updateServerModel(model);
        Command createChannel2 = new CreateCommand(1, "User1", "Channel2", false);
        createChannel2.updateServerModel(model);

        Command leave = new LeaveCommand(1, "User1", "Channel1");
        Set<String> recipients = new TreeSet<>();
        recipients.add("User1");
        recipients.add("User0");
        Broadcast expected = Broadcast.error(leave, ServerError.USER_NOT_IN_CHANNEL);
        assertEquals("broadcast", expected, leave.updateServerModel(model));

        assertTrue("User0 in channel1", model.getUsers("Channel1").contains("User0"));
        assertTrue("User1 in channel2", model.getUsers("Channel2").contains("User1"));
        assertFalse("User0 not in channel2", model.getUsers("Channel2").contains("User0"));
        assertFalse("User1 not in channel1", model.getUsers("Channel1").contains("User1"));
        assertEquals("num. users in channel1", 1, model.getUsers("Channel1").size());
        assertEquals("num. users in channel2", 1, model.getUsers("Channel2").size());
    }
    
    @Test
    public void testLeaveChannelExistsUserIsOwner() {
        model.registerUser(0);
        model.registerUser(1);
        Command create = new CreateCommand(0, "User0", "java", false);
        create.updateServerModel(model);
        Command join = new JoinCommand(1, "User1", "java");
        join.updateServerModel(model);

        Command leave = new LeaveCommand(0, "User0", "java");
        Set<String> recipients = new TreeSet<>();
        recipients.add("User1");
        recipients.add("User0");
        Broadcast expected = Broadcast.okay(leave, recipients);
        assertEquals("broadcast", expected, leave.updateServerModel(model));

        assertFalse("User0 not in channel", model.getUsers("java").contains("User0"));
        assertFalse("User1 not in channel", model.getUsers("java").contains("User1"));
        assertEquals("num. users in channel", 0, model.getUsers("java").size());
    }
    
    @Test
    public void testMesgChannelNonExistent() {
        model.registerUser(0);
        model.registerUser(1);
        Command create = new CreateCommand(0, "User0", "java", false);
        create.updateServerModel(model);
        Command join = new JoinCommand(1, "User1", "java");
        join.updateServerModel(model);

        Command mesg = new MessageCommand(0, "User0", "lava", "hey whats up hello");
        Set<String> recipients = new TreeSet<>();
        recipients.add("User1");
        recipients.add("User0");
        Broadcast expected = Broadcast.error(mesg, ServerError.NO_SUCH_CHANNEL);
        assertEquals("broadcast", expected, mesg.updateServerModel(model));
    }
    
    @Test
    public void testMesgChannelExistNonMember() {
        model.registerUser(0);
        model.registerUser(1);
        Command createJava = new CreateCommand(0, "User0", "java", false);
        createJava.updateServerModel(model);
        Command CreateLava = new CreateCommand(1, "User1", "lava", false);
        CreateLava.updateServerModel(model);

        Command mesg = new MessageCommand(0, "User0", "lava", "hey whats up hello");
        Set<String> recipients = new TreeSet<>();
        recipients.add("User1");
        recipients.add("User0");
        Broadcast expected = Broadcast.error(mesg, ServerError.USER_NOT_IN_CHANNEL);
        assertEquals("broadcast", expected, mesg.updateServerModel(model));
    }


}
