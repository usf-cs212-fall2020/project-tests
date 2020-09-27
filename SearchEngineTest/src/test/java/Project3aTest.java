import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * A test suite for project 3 part a (functionality). During development, run
 * individual tests instead of this test suite.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2020
 */
@TestMethodOrder(Alphanumeric.class)
public class Project3aTest {

	/**
	 * Makes sure the expected environment is setup before running any tests.
	 */
	@BeforeAll
	public static void testEnvironment() {
		assertTrue(TestUtilities.isEnvironmentSetup());
	}

	/*
	 * Make sure project 1 and 2 tests still pass.
	 */

	/**
	 * Includes all of the tests from the extended class.
	 * @see IndexOutputTest
	 */
	@Nested
	@TestMethodOrder(OrderAnnotation.class)
	public class A_SingleThread {

		/**
		 * Tests the word counts functionality of the inverted index on the entire
		 * input directory.
		 */
		@Order(1)
		@Test
		public void testCounts() {
			String filename = "counts-text.json";

			Path actual = TestUtilities.Paths.ACTUAL.path.resolve(filename);
			Path expected = TestUtilities.Paths.EXPECTED.path.resolve("counts").resolve(filename);

			String[] args = {
					TestUtilities.Flags.PATH.text, TestUtilities.Paths.TEXT.text,
					TestUtilities.Flags.COUNTS.text, actual.normalize().toString()
			};

			TestUtilities.checkOutput(args, actual, expected);
		}

		/**
		 * Tests the index output for all of the text files.
		 */
		@Order(2)
		@Test
		public void testBuildText() {
			Path input = TestUtilities.Paths.TEXT.path;
			IndexOutputTest.test(".", input);
		}

		/**
		 * Tests the exact search result output for the text subdirectory.
		 */
		@Order(3)
		@Test
		public void testSearchExact() {
			Path input = TestUtilities.Paths.TEXT.path;
			String query = "complex.txt";
			SearchOutputTest.test("exact", input, query, true);
		}

		/**
		 * Tests the exact search result output for the text subdirectory.
		 */
		@Order(4)
		@Test
		public void testSearchPartial() {
			Path input = TestUtilities.Paths.TEXT.path;
			String query = "complex.txt";
			SearchOutputTest.test("partial", input, query, false);
		}
	}

	/**
	 * Includes all of the tests from the extended class.
	 * @see IndexExceptionsTest
	 */
	@Nested
	public class B_IndexExceptions extends IndexExceptionsTest {

	}

	/**
	 * Includes all of the tests from the extended class.
	 * @see SearchExceptionsTest
	 */
	@Nested
	public class C_SearchExceptions extends SearchExceptionsTest {

	}

	/*
	 * Include new project 3 functionality (not runtime) tests
	 */

	/**
	 * Includes all of the tests from the extended class.
	 * @see ThreadOutputTest
	 */
	@Nested
	public class D_ThreadOutput extends ThreadOutputTest {

	}

	/**
	 * Includes all of the tests from the extended class.
	 * @see ThreadExceptionsTest
	 */
	@Nested
	public class E_ThreadExceptions extends ThreadExceptionsTest {

	}
}
