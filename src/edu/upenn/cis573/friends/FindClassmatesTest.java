package edu.upenn.cis573.friends;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class FindClassmatesTest {

	@Before
	public void setUp() throws Exception {
	}
	
	/**
	 * CASE: User not taking any classes
	 */
	@Test
	public void testNoClasses() {
		FriendFinder ff = new FriendFinder() {
			//override factory method
			protected ClassesDataSource createClassesDataSource() {
				return new ClassesDataSource() {
					public List<String> get(String arg) {
						return new ArrayList<String>();
					}
				};
			}
			//override factory method
			protected StudentsDataSource createStudentsDataSource() {
				return new StudentsDataSource() {
					public List<String> get(String arg) {
						return new ArrayList<String>();
					}
				};
			}
		};
		
		assertNull("Method did not return null when user is not taking any classes.", ff.findClassmates("me"));
	}
	
	/**
	 * CASE: User taking classes but no other students in classes.
	 */
	@Test
	public void testClassesNoOtherStudents() {
		FriendFinder ff = new FriendFinder() {
			//override factory method
			protected ClassesDataSource createClassesDataSource() {
				return new ClassesDataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("me")) {
							String[] classesArray = { "CIS573" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						return new ArrayList<String>();
					}
				};
			}
			//override factory method
			protected StudentsDataSource createStudentsDataSource() {
				return new StudentsDataSource() {
					public List<String> get(String arg) {
						String[] studentsArray = { "me" };
						List<String> studentsList = Arrays.asList(studentsArray);
						return studentsList;
					}
				};
			}
		};
		
		assertNull("Method did not return null when user is not taking any classes.", ff.findClassmates("me"));
	}
	

	/**
	 * CASE: User taking classes with other students but no student taking all classes user is taking.
	 */
	@Test
	public void testClassesNoScheduleMatches() {
		FriendFinder ff = new FriendFinder() {
			//override factory method
			protected ClassesDataSource createClassesDataSource() {
				return new ClassesDataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("me")) {
							String[] classesArray = { "CIS573", "CIT595" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						if (arg.equals("Drake")){
							String[] classesArray = { "CIS573" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						return new ArrayList<String>();
					}
				};
			}
			//override factory method
			protected StudentsDataSource createStudentsDataSource() {
				return new StudentsDataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("CIS573")) {
							String[] studentsArray = { "me", "Drake" };
							List<String> studentsList = Arrays.asList(studentsArray);
							return studentsList;
						}
						if (arg.equalsIgnoreCase("CIT595")) {
							String[] studentsArray = { "me" };
							List<String> studentsList = Arrays.asList(studentsArray);
							return studentsList;
						}
						return new ArrayList<String>();
					}
				};
			}
		};
		
		assertNull("Method did not return null when user is not taking any classes.", ff.findClassmates("me"));
	}
	
	/**
	 * CASE: User taking classes with other students, exactly one other student has the same exact schedule
	 * and another student is taking classes but not all the same ones.
	 */
	@Test
	public void testClassesSingleScheduleMatches() {
		FriendFinder ff = new FriendFinder() {
			//override factory method
			protected ClassesDataSource createClassesDataSource() {
				return new ClassesDataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("me")) {
							String[] classesArray = { "CIS573", "CIT595" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						if (arg.equalsIgnoreCase("Pinnz")) {
							String[] classesArray = { "CIS573", "CIT595" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						if (arg.equalsIgnoreCase("Drake")){
							String[] classesArray = { "CIS573" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						return new ArrayList<String>();
					}
				};
			}
			//override factory method
			protected StudentsDataSource createStudentsDataSource() {
				return new StudentsDataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("CIS573")) {
							String[] studentsArray = { "me", "Drake", "Pinnz" };
							List<String> studentsList = Arrays.asList(studentsArray);
							return studentsList;
						}
						if (arg.equalsIgnoreCase("CIT595")) {
							String[] studentsArray = { "me", "Pinnz" };
							List<String> studentsList = Arrays.asList(studentsArray);
							return studentsList;
						}
						return new ArrayList<String>();
					}
				};
			}
		};
		List<String> returnedList = ff.findClassmates("me");
		int actualSize = returnedList.size();
		int expectedSize = 1;
		//check correct number of elements were returned
		assertEquals("Method did not return the correct number of classmates.", expectedSize, actualSize);
		//check that it's the right element
		String actual = returnedList.get(0);
		String expected = "Pinnz";
		assertEquals("Method did not return the correct student.", expected, actual);
	}
	
	/**
	 * CASE: User taking classes with other students, multiple other students have the same exact schedule
	 * and another student is taking classes but not all the same ones.
	 */
	@Test
	public void testClassesMultipleScheduleMatches() {
		FriendFinder ff = new FriendFinder() {
			//override factory method
			protected ClassesDataSource createClassesDataSource() {
				return new ClassesDataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("me")) {
							String[] classesArray = { "CIS573", "CIT595" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						if (arg.equalsIgnoreCase("Pinnz")) {
							String[] classesArray = { "CIS573", "CIT595" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						if (arg.equalsIgnoreCase("Danilo")) {
							String[] classesArray = { "CIS573", "CIT595" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						if (arg.equalsIgnoreCase("Drake")){
							String[] classesArray = { "CIS573" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						return new ArrayList<String>();
					}
				};
			}
			//override factory method
			protected StudentsDataSource createStudentsDataSource() {
				return new StudentsDataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("CIS573")) {
							String[] studentsArray = { "me", "Drake", "Pinnz", "Danilo" };
							List<String> studentsList = Arrays.asList(studentsArray);
							return studentsList;
						}
						if (arg.equalsIgnoreCase("CIT595")) {
							String[] studentsArray = { "me", "Pinnz", "Danilo" };
							List<String> studentsList = Arrays.asList(studentsArray);
							return studentsList;
						}
						return new ArrayList<String>();
					}
				};
			}
		};
		List<String> returnedList = ff.findClassmates("me");
		int actualSize = returnedList.size();
		int expectedSize = 2;
		//check correct number of elements were returned
		assertEquals("Method did not return the correct number of classmates.", expectedSize, actualSize);
		//check that it's the right element
		String actualOne = returnedList.get(0);
		String actualTwo = returnedList.get(1);
		String possibleOne = "Pinnz";
		String possibleTwo = "Danilo";
		//we cannot be sure of order so try the first combination, catch any errors and try switching
		//if still error at this point, we know incorrect results were returned
		try {
			assertEquals(possibleOne, actualOne);
			assertEquals("Method returned only one of two correct students", possibleTwo, actualTwo);
		} catch (AssertionError e) {
			assertEquals(possibleTwo, actualOne);
			assertEquals("Method returned only one of two correct students", possibleOne, actualTwo);
		}
	}
	
	/**
	 * CASE: User taking classes with other students, multiple other students have supersets of the user's schedule
	 * and another student is taking classes but not all the same ones.
	 */
	@Test
	public void testClassesMultipleSupersetMatches() {
		FriendFinder ff = new FriendFinder() {
			//override factory method
			protected ClassesDataSource createClassesDataSource() {
				return new ClassesDataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("me")) {
							String[] classesArray = { "CIS573", "CIT595" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						if (arg.equalsIgnoreCase("Pinnz")) {
							String[] classesArray = { "CIS573", "CIT595", "CIT596" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						if (arg.equalsIgnoreCase("Danilo")) {
							String[] classesArray = { "CIS573", "CIT595", "CIT596" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						if (arg.equalsIgnoreCase("Drake")){
							String[] classesArray = { "CIS573" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						return new ArrayList<String>();
					}
				};
			}
			//override factory method
			protected StudentsDataSource createStudentsDataSource() {
				return new StudentsDataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("CIS573")) {
							String[] studentsArray = { "me", "Drake", "Pinnz", "Danilo" };
							List<String> studentsList = Arrays.asList(studentsArray);
							return studentsList;
						}
						if (arg.equalsIgnoreCase("CIT595")) {
							String[] studentsArray = { "me", "Pinnz", "Danilo" };
							List<String> studentsList = Arrays.asList(studentsArray);
							return studentsList;
						}
						if (arg.equalsIgnoreCase("CIT596")) {
							String[] studentsArray = { "Pinnz", "Danilo" };
							List<String> studentsList = Arrays.asList(studentsArray);
							return studentsList;
						}
						return new ArrayList<String>();
					}
				};
			}
		};
		List<String> returnedList = ff.findClassmates("me");
		int actualSize = returnedList.size();
		int expectedSize = 2;
		//check correct number of elements were returned
		assertEquals("Method did not return the correct number of classmates.", expectedSize, actualSize);
		//check that it's the right element
		String actualOne = returnedList.get(0);
		String actualTwo = returnedList.get(1);
		String possibleOne = "Pinnz";
		String possibleTwo = "Danilo";
		//we cannot be sure of order so try the first combination, catch any errors and try switching
		//if still error at this point, we know incorrect results were returned
		try {
			assertEquals(possibleOne, actualOne);
			assertEquals("Method returned only one of two correct students", possibleTwo, actualTwo);
		} catch (AssertionError e) {
			assertEquals(possibleTwo, actualOne);
			assertEquals("Method returned only one of two correct students", possibleOne, actualTwo);
		}
	}
	
	/**
	 * CASE: User taking classes with other students, multiple other students have the exact schedule as the user,
	 * multiple other students have supersets of the user's schedule and another student is taking classes but not all the same ones.
	 */
	@Test
	public void testClassesExactAndSuperSetMatchesAndSubsetPresent() {
		FriendFinder ff = new FriendFinder() {
			//override factory method
			protected ClassesDataSource createClassesDataSource() {
				return new ClassesDataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("me")) {
							String[] classesArray = { "CIS573", "CIT595" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						if (arg.equalsIgnoreCase("Leonid")) {
							String[] classesArray = { "CIS573", "CIT595" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						if (arg.equalsIgnoreCase("Carlos")) {
							String[] classesArray = { "CIS573", "CIT595" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						if (arg.equalsIgnoreCase("Pinnz")) {
							String[] classesArray = { "CIS573", "CIT595", "CIT596" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						if (arg.equalsIgnoreCase("Danilo")) {
							String[] classesArray = { "CIS573", "CIT595", "CIT596" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						if (arg.equalsIgnoreCase("Drake")){
							String[] classesArray = { "CIS573" };
							List<String> classesList = Arrays.asList(classesArray);
							return classesList;
						}
						return new ArrayList<String>();
					}
				};
			}
			//override factory method
			protected StudentsDataSource createStudentsDataSource() {
				return new StudentsDataSource() {
					public List<String> get(String arg) {
						if (arg.equalsIgnoreCase("CIS573")) {
							String[] studentsArray = { "me", "Leonid", "Carlos", "Drake", "Pinnz", "Danilo" };
							List<String> studentsList = Arrays.asList(studentsArray);
							return studentsList;
						}
						if (arg.equalsIgnoreCase("CIT595")) {
							String[] studentsArray = { "me", "Leonid", "Carlos", "Pinnz", "Danilo"  };
							List<String> studentsList = Arrays.asList(studentsArray);
							return studentsList;
						}
						if (arg.equalsIgnoreCase("CIT596")) {
							String[] studentsArray = { "Pinnz", "Danilo" };
							List<String> studentsList = Arrays.asList(studentsArray);
							return studentsList;
						}
						return new ArrayList<String>();
					}
				};
			}
		};
		List<String> returnedList = ff.findClassmates("me");
		int actualSize = returnedList.size();
		int expectedSize = 4;
		//check correct number of elements were returned
		assertEquals("Method did not return the correct number of classmates.", expectedSize, actualSize);
		//check that it's the right element
		String expectedOne = "Pinnz";
		String expectedTwo = "Danilo";
		String expectedThree = "Leonid";
		String expectedFour = "Carlos";
		//make sure all expected matches are in the returned list (and consequently no unexpected user's are in the list)
		assertTrue("Result did not contain a student that is in all the user's classes.", returnedList.contains(expectedOne));
		assertTrue("Result did not contain a student that is in all the user's classes.", returnedList.contains(expectedTwo));
		assertTrue("Result did not contain a student that is in all the user's classes.", returnedList.contains(expectedThree));
		assertTrue("Result did not contain a student that is in all the user's classes.", returnedList.contains(expectedFour));
	}

}
