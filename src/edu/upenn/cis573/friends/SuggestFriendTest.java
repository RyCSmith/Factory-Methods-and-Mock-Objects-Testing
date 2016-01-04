package edu.upenn.cis573.friends;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class SuggestFriendTest {

	@Before
	public void setUp() throws Exception {
	}

	/**
	 * CASE: User has no friends.
	 */
	@Test
	public void testNoFriends() {
		FriendFinder ff = new FriendFinder() {
			//override factory method
			protected DataSource createDataSource() {
				return new DataSource() {
					public List<String> get(String arg) {
						return new ArrayList<String>();
					}
				};
			}
		};
		
		assertNull("Method result not null when user has no friends.", ff.suggestFriend("me"));
	}
	
	/**
	 * CASE: User has two friends, neither of which have any friends.
	 */
	@Test
	public void testFriendsHaveNoFriends() {
		FriendFinder ff = new FriendFinder() {
			//override factory method
			protected DataSource createDataSource() {
				return new DataSource() {
					public List<String> get(String arg) {
						//this returns 2 friends for me, not null if it is called with me's friends as arguments
						if (arg.equalsIgnoreCase("me")) {
							String[] friendsArray = { "Fred", "Molly" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						return new ArrayList<String>();
					}
				};
			}
		};
		
		assertNull("Method result not null when user's friends have no friends.", ff.suggestFriend("me"));
	}
	
	/**
	 * CASE: User has two friends that each are friends with user and each other (no friends involved
	 * that would be new to the user).
	 */
	@Test
	public void testNoNewFriends() {
		FriendFinder ff = new FriendFinder() {
			//override factory method
			protected DataSource createDataSource() {
				return new DataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("me")) {
							String[] friendsArray = { "Fred", "Molly" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						if (arg.equalsIgnoreCase("Fred")){
							String[] friendsArray = { "me", "Molly" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						if (arg.equalsIgnoreCase("Molly")) {
							String[] friendsArray = { "Fred", "me" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						return new ArrayList<String>();
					}
				};
			}
		};
		
		assertNull("Method result not null when user's friends no not have "
				+ "any friends that are not already friends with the user.", ff.suggestFriend("me"));
	}
	
	/**
	 * CASE: User has one friend who has two friends; the user and a friend who would be new to the user.
	 */
	@Test
	public void testUserHasOneFriendWhoHasOneNewFriends() {
		FriendFinder ff = new FriendFinder() {
			//override factory method
			protected DataSource createDataSource() {
				return new DataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("me")) {
							String[] friendsArray = { "Molly" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						if (arg.equalsIgnoreCase("Molly")) {
							String[] friendsArray = { "Fred", "me" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						return new ArrayList<String>();
					}
				};
			}
		};
		String expected = "Fred";
		String actual = ff.suggestFriend("me");
		assertEquals("Method did not suggest the friend of the"
				+ " user's friend who would be new to the user.", expected.toLowerCase(), actual.toLowerCase());
	}
	
	/**
	 * CASE: User has one friend who has three friends; the user and multiple friends who would be new to the user.
	 */
	@Test
	public void testUserHasOneFriendWhoHasMultipleNewFriends() {
		FriendFinder ff = new FriendFinder() {
			//override factory method
			protected DataSource createDataSource() {
				return new DataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("me")) {
							String[] friendsArray = { "Molly" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						if (arg.equalsIgnoreCase("Molly")) {
							String[] friendsArray = { "Fred", "Jose", "me" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						return new ArrayList<String>();
					}
				};
			}
		};
		
		String possibleOne = "Fred";
		String possibleTwo = "Jose";
		String actual = ff.suggestFriend("me");
		
		assertNotNull("Method did not suggest a new friend when one existed.", actual);
		//this catch block allows us to check against both results that would be valid answers
		//it will only result in test failure if neither match
		try {
			assertEquals(possibleOne.toLowerCase(), actual.toLowerCase());
		} catch (AssertionError e) {
			assertEquals("Method did not return either of the two possible "
					+ "friends that would have been new to the user.", possibleTwo.toLowerCase(), actual.toLowerCase());
		}
	}
	
	/**
	 * CASE: User has two friends who are both friends with the user and another person (who would be
	 * new to the user).
	 */
	@Test
	public void testUserHasTwoFriendsWithSameNewFriend() {
		FriendFinder ff = new FriendFinder() {
			//override factory method
			protected DataSource createDataSource() {
				return new DataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("me")) {
							String[] friendsArray = { "Molly", "Fred" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						if (arg.equalsIgnoreCase("Molly")) {
							String[] friendsArray = { "Jose", "me" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						if (arg.equalsIgnoreCase("Fred")) {
							String[] friendsArray = { "Jose", "me" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						return new ArrayList<String>();
					}
				};
			}
		};
		String expected = "Jose";
		String actual = ff.suggestFriend("me");
		assertEquals("Method did not suggest the friend of the"
				+ " user's friends who would be new to the user.", expected.toLowerCase(), actual.toLowerCase());
	}
	
	/**
	 * CASE: User has two friends who each have three friends (no friends are shared, four
	 * people would be new to the user with equal ranking).
	 */
	@Test
	public void testUserHasTwoFriendsWithMultipleNewFriends() {
		FriendFinder ff = new FriendFinder() {
			//override factory method
			protected DataSource createDataSource() {
				return new DataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("me")) {
							String[] friendsArray = { "Molly", "Fred" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						if (arg.equalsIgnoreCase("Molly")) {
							String[] friendsArray = { "Jose", "Said", "me" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						if (arg.equalsIgnoreCase("Fred")) {
							String[] friendsArray = { "Danilo", "Leonid", "me" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						return new ArrayList<String>();
					}
				};
			}
		};
		
		String possibleOne = "Jose";
		String possibleTwo = "Said";
		String possibleThree = "Danilo";
		String possibleFour = "Leonid";
		String actual = ff.suggestFriend("me");
		
		assertNotNull("Method did not suggest a new friend when one existed.", actual);
		//this catch block allows us to check against all results that would be valid answers
		//it will only result in test failure if all possibilities fail to match
		try {
			assertEquals(possibleOne.toLowerCase(), actual.toLowerCase());
		} catch (AssertionError e) {
			try {
				assertEquals(possibleTwo.toLowerCase(), actual.toLowerCase());
			} catch (AssertionError e1) {
				try {
					assertEquals(possibleThree.toLowerCase(), actual.toLowerCase());
				} catch (AssertionError e2) {
					assertEquals("Method did not return any of the possible "
							+ "friends that would have been new to the user.", possibleFour.toLowerCase(), actual.toLowerCase());
				}
			}
		}
	}
	
	/**
	 * CASE: User has three friends who all share exactly one friend who would be new to the user.
	 * (some friends share another friend who would be new to the user, but not all)
	 */
	@Test
	public void testUserHasMultipleFriendsSharingSingleNewFriend() {
		FriendFinder ff = new FriendFinder() {
			//override factory method
			protected DataSource createDataSource() {
				return new DataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("me")) {
							String[] friendsArray = { "Jose", "Carlos", "Quiue" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						if (arg.equalsIgnoreCase("Jose")) {
							String[] friendsArray = { "me", "Danilo", "Leonid" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						if (arg.equalsIgnoreCase("Carlos")) {
							String[] friendsArray = { "me", "Danilo", "Leonid" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						if (arg.equalsIgnoreCase("Quiue")) {
							String[] friendsArray = { "me", "Danilo", "Pinnsvin" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						return new ArrayList<String>();
					}
				};
			}
		};
		String expected = "Danilo";
		String actual = ff.suggestFriend("me");
		assertEquals("Method did not suggest the friend who is friends with the largest "
				+ "number of the user's friends and would be new to the user.", expected.toLowerCase(), actual.toLowerCase());
	}
	
	/**
	 * CASE: User has three friends who all share exactly one friend who would be new to the user.
	 * (some friends share another friend who would be new to the user, but not all)
	 */
	@Test
	public void testUserHasMultipleFriendsSharingMultipleNewFriend() {
		FriendFinder ff = new FriendFinder() {
			//override factory method
			protected DataSource createDataSource() {
				return new DataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("me")) {
							String[] friendsArray = { "Jose", "Carlos", "Quiue" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						if (arg.equalsIgnoreCase("Jose")) {
							String[] friendsArray = { "me", "Danilo", "Leonid", "Paul" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						if (arg.equalsIgnoreCase("Carlos")) {
							String[] friendsArray = { "me", "Danilo", "Leonid", "John" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						if (arg.equalsIgnoreCase("Quiue")) {
							String[] friendsArray = { "me", "Danilo", "Leonid", "George" };
							List<String> friendsList = Arrays.asList(friendsArray);
							return friendsList;
						}
						return new ArrayList<String>();
					}
				};
			}
		};
		String possibleOne = "Danilo";
		String possibleTwo = "Leonid";
		String actual = ff.suggestFriend("me");
		assertNotNull("Method did not suggest a new friend when one existed.", actual);
		//this catch block allows us to check against both valid results
		//it will only result in test failure if both possibilities fail to match
		try {
			assertEquals(possibleOne.toLowerCase(), actual.toLowerCase());
		} catch (AssertionError e) {
			assertEquals("Method did not return any of the possible "
					+ "friends that would have been new to the user.", possibleTwo.toLowerCase(), actual.toLowerCase());
		}
	}
}
