import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
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
	
	/**
	 * Attempts to detect whether this approach is multithreading. Only required
	 * when not benchmarking.
	 */
	@Nested
	public class F_MultithreadingTest {
		/**
		 * Attempts to detect whether multithreading is actually happening by
		 * triggering a long-running set of commands and making sure there are more
		 * than one active threads in the system.
		 * 
		 * This is a pretty fuzzy test; if you are failing it might actually be an
		 * issue with the test and not your code.
		 */
		@Test
		public void testMultithreading() {
			String[] args = {
					TestUtilities.Flags.PATH.text, TestUtilities.Paths.TEXT.path.toString(),
					TestUtilities.Flags.QUERY.text, TestUtilities.Paths.QUERY.path.resolve("complex.txt").toString(),
					TestUtilities.Flags.THREADS.text, Integer.toString(3)
			};
			
			// figure out how long a run with three threads takes
			long warmup = timedRun(args);
			long elapsed = timedRun(args);
			long delay = Math.min(warmup, elapsed) / 2;
			
			// create a thread to run with two threads
			Thread runner = new Thread(() -> {
				TestUtilities.checkExceptions(args);
			});
			
			Assertions.assertTimeoutPreemptively(ThreadOutputTest.TIMEOUT, () -> {
				// get the threads that are active before we start everything
				List<String> before = activeThreads();
				
				// start Driver running its code...
				runner.start();
				
				// wait just a little bit to make sure it gets going
				try {
					Thread.sleep(delay);
				}
				catch (InterruptedException e) {
					Assertions.fail("Unexpected interruption, possible timeout.");
				}
				
				// make sure we haven't finished running
				Assertions.assertTrue(runner.getState() != Thread.State.TERMINATED, 
						"Driver ran too fast. Check if Driver is performing the indexing and searching tasks properly.");
				
				// see how many active threads are now in the system
				List<String> active = activeThreads();
				
				// wait for Driver to finish up its work
				runner.join();
				
				String debug = String.format("%nThreads Before: %s%nThreads During: %s%n", before, active);
				System.out.println(debug);
				
				Assertions.assertTrue(active.size() - before.size() > 1, debug);
			});
		}
	}
	
	/**
	 * Returns a list of the active thread names (approximate).
	 *
	 * @return list of active thread names
	 */
	public static List<String> activeThreads() {
		int active = Thread.activeCount(); // only an estimate
		Thread[] threads = new Thread[active * 2]; // make sure large enough
		Thread.enumerate(threads);
		return Arrays.stream(threads)
				.filter(t -> t != null)
				.map(t -> t.getName())
				.collect(Collectors.toList());
	}
	
	/**
	 * Times how long a run takes.
	 * 
	 * @param args the arguments to time
	 * @return the elapsed milliseconds
	 */
	public static long timedRun(String[] args) {
		Instant start = Instant.now();
		Assertions.assertTimeoutPreemptively(ThreadOutputTest.TIMEOUT, () -> {
			TestUtilities.checkExceptions(args);
		});
		Duration elapsed = Duration.between(start, Instant.now());
		return elapsed.toMillis();
	}
}
