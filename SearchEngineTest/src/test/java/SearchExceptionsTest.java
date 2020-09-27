import java.io.IOException;
import java.nio.file.Files;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * Tests that code runs without throwing exceptions.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2020
 */
@TestMethodOrder(OrderAnnotation.class)
public class SearchExceptionsTest extends TestUtilities {

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
	public void testMissingQueryPath() {
		String[] args = { Flags.PATH.text, HELLO, Flags.QUERY.text };
		testNoExceptions(args);
	}

	/**
	 * Tests that no exceptions are thrown with the provided arguments.
	 */
	@Order(2)
	@Test
	public void testInvalidQueryPath() {
		String query = Long.toHexString(Double.doubleToLongBits(Math.random()));
		String[] args = { Flags.PATH.text, HELLO, Flags.QUERY.text, query };
		testNoExceptions(args);
	}

	/**
	 * Tests that no exceptions are thrown with the provided arguments.
	 */
	@Order(3)
	@Test
	public void testInvalidExactPath() {
		String query = Long.toHexString(Double.doubleToLongBits(Math.random()));
		String[] args = {
				Flags.PATH.text, HELLO,
				Flags.QUERY.text, query, Flags.EXACT.text};
		testNoExceptions(args);
	}

	/**
	 * Tests that no exceptions are thrown with the provided arguments.
	 * @throws IOException if an IO error occurs
	 */
	@Order(4)
	@Test
	public void testNoOutput() throws IOException {
		String[] args = { Flags.PATH.text, HELLO, Flags.QUERY.text, SIMPLE };

		// make sure to delete old index.json and results.json if it exists
		Files.deleteIfExists(Paths.INDEX.path);
		Files.deleteIfExists(Paths.RESULTS.path);

		testNoExceptions(args);

		// make sure a new index.json and results.json were not created
		Assertions.assertFalse(Files.exists(Paths.INDEX.path)
				|| Files.exists(Paths.RESULTS.path));
	}

	/**
	 * Tests that no exceptions are thrown with the provided arguments.
	 * @throws IOException if an IO error occurs
	 */
	@Order(5)
	@Test
	public void testDefaultOutput() throws IOException {
		String[] args = {
				Flags.PATH.text, HELLO,
				Flags.QUERY.text, SIMPLE,
				Flags.RESULTS.text };

		// make sure to delete old index.json and results.json if it exists
		Files.deleteIfExists(Paths.INDEX.path);
		Files.deleteIfExists(Paths.RESULTS.path);

		testNoExceptions(args);

		// make sure a new results.json was not created (but index.json was not)
		Assertions.assertTrue(Files.exists(Paths.RESULTS.path)
				&& !Files.exists(Paths.INDEX.path));
	}

	/**
	 * Tests that no exceptions are thrown with the provided arguments.
	 * @throws IOException if an IO error occurs
	 */
	@Order(6)
	@Test
	public void testEmptyIndex() throws IOException {
		String[] args = { Flags.QUERY.text, SIMPLE, Flags.RESULTS.text };

		// make sure to delete old index.json and results.json if it exists
		Files.deleteIfExists(Paths.INDEX.path);
		Files.deleteIfExists(Paths.RESULTS.path);

		testNoExceptions(args);

		// make sure a new results.json was not created (but index.json was not)
		Assertions.assertTrue(Files.exists(Paths.RESULTS.path)
				&& !Files.exists(Paths.INDEX.path));
	}

	/**
	 * Tests that no exceptions are thrown with the provided arguments.
	 * @throws IOException if an IO error occurs
	 */
	@Order(7)
	@Test
	public void testEmptyQuery() throws IOException {
		String[] args = { Flags.RESULTS.text };

		// make sure to delete old index.json and results.json if it exists
		Files.deleteIfExists(Paths.INDEX.path);
		Files.deleteIfExists(Paths.RESULTS.path);

		testNoExceptions(args);

		// make sure a new results.json was not created (but index.json was not)
		Assertions.assertTrue(Files.exists(Paths.RESULTS.path)
				&& !Files.exists(Paths.INDEX.path));
	}

	/**
	 * Tests that no exceptions are thrown with the provided arguments.
	 * @throws IOException if an IO error occurs
	 */
	@Order(8)
	@Test
	public void testSwitchedOrder() throws IOException {
		String[] args = {
				Flags.QUERY.text, SIMPLE,
				Flags.RESULTS.text,
				Flags.PATH.text, HELLO,
				Flags.EXACT.text
		};

		// make sure to delete old index.json and results.json if it exists
		Files.deleteIfExists(Paths.INDEX.path);
		Files.deleteIfExists(Paths.RESULTS.path);

		testNoExceptions(args);

		// make sure a new results.json was not created (but index.json was not)
		Assertions.assertTrue(Files.exists(Paths.RESULTS.path)
				&& !Files.exists(Paths.INDEX.path));
	}
}
