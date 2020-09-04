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
public class IndexExceptionsTest extends TestUtilities {

	/**
	 * Path to the hello.txt file used for testing.
	 */
	private static final String HELLO = Paths.TEXT.path.resolve("simple")
			.resolve("hello.txt").toString();

	/**
	 * Tests no exceptions are thrown with no arguments.
	 */
	@Order(1)
	@Test
	public void testNoArguments() {
		String[] args = {};
		testNoExceptions(args);
	}

	/**
	 * Tests no exceptions are thrown with invalid arguments.
	 */
	@Order(2)
	@Test
	public void testBadArguments() {
		String[] args = { "hello", "world" };
		testNoExceptions(args);
	}

	/**
	 * Tests no exceptions are thrown with a missing path value.
	 */
	@Order(3)
	@Test
	public void testMissingPath() {
		String[] args = { Flags.PATH.text };
		testNoExceptions(args);
	}

	/**
	 * Tests no exceptions are thrown with an invalid path value.
	 */
	@Order(4)
	@Test
	public void testInvalidPath() {
		// generates a random path name
		String path = Long.toHexString(Double.doubleToLongBits(Math.random()));
		String[] args = { Flags.PATH.text, path };
		testNoExceptions(args);
	}

	/**
	 * Tests no exceptions are thrown with no index output.
	 *
	 * @throws IOException if IO error occurs
	 */
	@Order(5)
	@Test
	public void testNoOutput() throws IOException {
		String[] args = { Flags.PATH.text, HELLO };

		// make sure to delete old index.json if it exists
		Files.deleteIfExists(Paths.INDEX.path);

		// make sure a new index.json was not created
		testNoExceptions(args);
		Assertions.assertFalse(Files.exists(Paths.INDEX.path));
	}

	/**
	 * Tests no exceptions are thrown with a default output value.
	 *
	 * @throws IOException if IO error occurs
	 */
	@Order(6)
	@Test
	public void testDefaultOutput() throws IOException {
		String[] args = { Flags.PATH.text, HELLO, Flags.INDEX.text };

		// make sure to delete old index.json if it exists
		Files.deleteIfExists(Paths.INDEX.path);

		// make sure a new index.json was created
		testNoExceptions(args);
		Assertions.assertTrue(Files.exists(Paths.INDEX.path));
	}

	/**
	 * Tests no exceptions are thrown with only output (no input path).
	 *
	 * @throws IOException if IO error occurs
	 */
	@Order(7)
	@Test
	public void testEmptyOutput() throws IOException {
		String[] args = { Flags.INDEX.text };

		// make sure to delete old index.json if it exists
		Files.deleteIfExists(Paths.INDEX.path);

		// make sure a new index.json was created
		testNoExceptions(args);
		Assertions.assertTrue(Files.exists(Paths.INDEX.path));
	}

	/**
	 * Tests no exceptions are thrown with arguments in a different order.
	 *
	 * @throws IOException if IO error occurs
	 */
	@Order(8)
	@Test
	public void testSwitchedOrder() throws IOException {
		String[] args = { Flags.INDEX.text, Flags.PATH.text, HELLO };

		// make sure to delete old index.json if it exists
		Files.deleteIfExists(Paths.INDEX.path);

		// make sure a new index.json was created
		testNoExceptions(args);
		Assertions.assertTrue(Files.exists(Paths.INDEX.path));
	}
}
