import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;

import java.time.Duration;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Tests that multithreading code runs without throwing exceptions.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2020
 */
@TestMethodOrder(OrderAnnotation.class)
public class ThreadExceptionsTest extends TestUtilities {
	/** Amount of time to wait for an individual test to finish. */
	private static final Duration TIMEOUT = Duration.ofMinutes(3);

	/** Path to the hello.txt file used for testing. */
	private static final String HELLO = Paths.TEXT.path.resolve("simple")
			.resolve("hello.txt").toString();

	/** Path to the simple.txt file used for testing. */
	private static final String SIMPLE = Paths.QUERY.path.resolve("simple.txt").toString();

	/**
	 * Tests that no exceptions are thrown with the provided arguments.
	 */
	@Order(1)
	@Test
	public void testNegativeThreads() {
		String[] args = {
				Flags.PATH.text, HELLO,
				Flags.QUERY.text, SIMPLE,
				Flags.THREADS.text, "-1" };
		assertTimeoutPreemptively(TIMEOUT, () -> checkExceptions(args));
	}

	/**
	 * Tests that no exceptions are thrown with the provided arguments.
	 */
	@Order(2)
	@Test
	public void testZeroThreads() {
		String[] args = {
				Flags.PATH.text, HELLO,
				Flags.QUERY.text, SIMPLE,
				Flags.THREADS.text, "0" };
		assertTimeoutPreemptively(TIMEOUT, () -> checkExceptions(args));
	}

	/**
	 * Tests that no exceptions are thrown with the provided arguments.
	 */
	@Order(3)
	@Test
	public void testFractionThreads() {
		String[] args = {
				Flags.PATH.text, HELLO,
				Flags.QUERY.text, SIMPLE,
				Flags.THREADS.text, "3.14" };
		assertTimeoutPreemptively(TIMEOUT, () -> checkExceptions(args));
	}

	/**
	 * Tests that no exceptions are thrown with the provided arguments.
	 */
	@Order(4)
	@Test
	public void testWordThreads() {
		String[] args = {
				Flags.PATH.text, HELLO,
				Flags.QUERY.text, SIMPLE,
				Flags.THREADS.text, "fox" };
		assertTimeoutPreemptively(TIMEOUT, () -> checkExceptions(args));
	}

	/**
	 * Tests that no exceptions are thrown with the provided arguments.
	 */
	@Order(5)
	@Test
	public void testDefaultThreads() {
		String[] args = {
				Flags.PATH.text, HELLO,
				Flags.QUERY.text, SIMPLE,
				Flags.THREADS.text };
		assertTimeoutPreemptively(TIMEOUT, () -> checkExceptions(args));
	}

	/**
	 * Tests that no exceptions are thrown with the provided arguments.
	 */
	@Order(6)
	@Test
	public void testNoOutputBuild() {
		String[] args = {
				Flags.PATH.text, Paths.TEXT.text,
				Flags.THREADS.text, String.valueOf(3) };
		assertTimeoutPreemptively(TIMEOUT, () -> checkExceptions(args));
	}

	/**
	 * Tests that no exceptions are thrown with the provided arguments.
	 */
	@Order(7)
	@Test
	public void testNoOutputSearch() {
		String[] args = {
				Flags.PATH.text, Paths.TEXT.text,
				Flags.QUERY.text, Paths.QUERY.path.resolve("complex.txt").toString(),
				Flags.THREADS.text, String.valueOf(3) };
		assertTimeoutPreemptively(TIMEOUT, () -> checkExceptions(args));
	}
}
