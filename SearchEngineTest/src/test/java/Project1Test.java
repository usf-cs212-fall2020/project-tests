import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * A test suite for project 1. During development, run individual tests instead
 * of this test suite.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2020
 */
@TestMethodOrder(Alphanumeric.class)
public class Project1Test {

	/**
	 * Makes sure the expected environment is setup before running any tests.
	 */
	@BeforeAll
	public static void testEnvironment() {
		assertTrue(TestUtilities.isEnvironmentSetup());
	}

	/**
	 * Includes all of the tests from the extended class.
	 * @see IndexOutputTest
	 */
	@Nested
	public class A_IndexOutput extends IndexOutputTest {

	}

	/**
	 * Includes all of the tests from the extended class.
	 * @see IndexExceptionsTest
	 */
	@Nested
	public class B_IndexExceptions extends IndexExceptionsTest {

	}

}
