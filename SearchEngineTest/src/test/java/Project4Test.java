import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * A test suite for project 4. During development, run individual tests instead
 * of this test suite.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2020
 */
@TestMethodOrder(Alphanumeric.class)
public class Project4Test {

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
	 * Include project 3 functionality (not runtime) tests
	 */

	/**
	 * Includes all of the tests from the extended class.
	 * @see ThreadOutputTest
	 */
	@Nested
	@TestMethodOrder(OrderAnnotation.class)
	public class D_ThreadOutput {
		/**
		 * Tests the index output for all of the text files.
		 *
		 * @param threads the number of worker threads to use
		 */
		@Order(1)
		@ParameterizedTest(name = "{0} thread(s)")
		@ValueSource(ints = {1, 2, 5})
		public void testText(int threads) {
			Path input = TestUtilities.Paths.TEXT.path;
			ThreadOutputTest.testBuilding(".", input, threads);
		}

		/**
		 * Tests the search result output for the text subdirectory.
		 *
		 * @param threads the number of worker threads to use
		 */
		@Order(2)
		@ParameterizedTest(name = "{0} thread(s)")
		@ValueSource(ints = {1, 2, 5})
		public void testSearchExact(int threads) {
			Path input = TestUtilities.Paths.TEXT.path;
			String query = "complex.txt";
			ThreadOutputTest.testSearching("exact", input, query, true, threads);
		}

		/**
		 * Tests the search result output for the text subdirectory.
		 *
		 * @param threads the number of worker threads to use
		 */
		@Order(3)
		@ParameterizedTest(name = "{0} thread(s)")
		@ValueSource(ints = {1, 2, 5})
		public void testSearchPartial(int threads) {
			Path input = TestUtilities.Paths.TEXT.path;
			String query = "complex.txt";
			ThreadOutputTest.testSearching("partial", input, query, false, threads);
		}
	}

	/**
	 * Includes all of the tests from the extended class.
	 * @see ThreadExceptionsTest
	 */
	@Nested
	public class E_ThreadExceptions extends ThreadExceptionsTest {

	}

	/**
	 * Include project 4 web crawler functionality.
	 */
	@Nested
	public class F_CrawlOutput extends CrawlOutputTest {

	}

	/**
	 * Include crawler runtime test.
	 */
	@Nested
	public class G_CrawlRuntime {
		/**
		 * Tests the crawler runtime.
		 */
		@Test
		public void testRuntime() {
			String link = "https://www.cs.usfca.edu/~cs212/docs/jdk-14.0.2_doc-all/api/allclasses-index.html";
			String limit = "30";

			String[] args1 = {
					TestUtilities.Flags.SEED.text, link,
					TestUtilities.Flags.LIMIT.text, limit,
					TestUtilities.Flags.THREADS.text, String.valueOf(1) };

			String[] args2 = {
					TestUtilities.Flags.SEED.text, link,
					TestUtilities.Flags.LIMIT.text, limit,
					TestUtilities.Flags.THREADS.text, String.valueOf(ThreadRuntimeTest.THREADS) };

			System.out.println();
			System.out.printf("### Testing Crawl 1 vs %d Workers...%n", ThreadRuntimeTest.THREADS);

			// make sure code runs without exceptions before testing
			assertTimeoutPreemptively(ThreadRuntimeTest.TIMEOUT, () -> TestUtilities.checkExceptions(args1));
			assertTimeoutPreemptively(ThreadRuntimeTest.TIMEOUT, () -> TestUtilities.checkExceptions(args2));

			// then test the timing
			assertTimeoutPreemptively(ThreadRuntimeTest.LONG_TIMEOUT, () -> {
				double result = ThreadRuntimeTest.compare("1 Worker", args1,
						String.valueOf(ThreadRuntimeTest.THREADS) + " Workers", args2);

				assertTrue(result > 0, () -> String.format(
						"%s is %.2f seconds faster than %d workers.", "1 worker",
						-result, ThreadRuntimeTest.THREADS));
			});
		}
	}

}
