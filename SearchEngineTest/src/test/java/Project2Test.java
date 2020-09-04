import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * A test suite for project 2. During development, run individual tests instead
 * of this test suite.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2020
 */
@TestMethodOrder(Alphanumeric.class)
public class Project2Test {

	/**
	 * Makes sure the expected environment is setup before running any tests.
	 */
	@BeforeAll
	public static void testEnvironment() {
		assertTrue(TestUtilities.isEnvironmentSetup());
	}

	/*
	 * Make sure project 1 tests still pass.
	 */

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

	/*
	 * Include new project 2 tests.
	 */

	/**
	 * Includes all of the tests from the extended class.
	 * @see SearchOutputTest
	 */
	@Nested
	public class C_CountOutput extends CountOutputTest {

	}

	/**
	 * Includes all of the tests from the extended class.
	 * @see SearchOutputTest
	 */
	@Nested
	public class D_SearchOutput extends SearchOutputTest {

	}

	/**
	 * Includes all of the tests from the extended class.
	 * @see SearchExceptionsTest
	 */
	@Nested
	public class E_SearchExceptions extends SearchExceptionsTest {

	}

}
