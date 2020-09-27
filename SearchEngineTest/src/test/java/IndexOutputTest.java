import java.nio.file.Path;
import java.time.Duration;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * JUnit tests for the inverted index functionality.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2020
 */
@TestMethodOrder(OrderAnnotation.class)
public class IndexOutputTest extends TestUtilities {
	/** Amount of time to wait for an individual test to finish. */
	public static final Duration TIMEOUT = Duration.ofMinutes(1);

	/**
	 * Generates the arguments to use for this test case. Designed to be used
	 * inside a JUnit test.
	 *
	 * @param subdir the output subdirectory to use
	 * @param input the input path to use
	 */
	public static void test(String subdir, Path input) {
		String filename = outputFileName("index", input);

		Path actual = Paths.ACTUAL.path.resolve(filename);
		Path expected = Paths.EXPECTED.path.resolve("index").resolve(subdir).resolve(filename);

		String[] args = {
				Flags.PATH.text, input.normalize().toString(),
				Flags.INDEX.text, actual.normalize().toString()
		};

		Assertions.assertTimeoutPreemptively(TIMEOUT, () -> {
			checkOutput(args, actual, expected);
		});
	}

	/**
	 * Tests the index output for files in the simple subdirectory.
	 *
	 * @param filename filename of a text file in the simple subdirectory
	 */
	@Order(1)
	@ParameterizedTest
	@ValueSource(strings = {
			"hello.txt",
			"animals.text",
			"capitals.txt",
			"digits.txt",
			"position.teXt",
			"words.tExT",
			"empty.txt",
			"sentences.md"
	})
	public void testSimpleFiles(String filename) {
		Path input = Paths.TEXT.path.resolve("simple").resolve(filename);
		test("index-simple", input);
	}

	/**
	 * Tests the index output for files in the rfcs subdirectory.
	 *
	 * @param filename filename of a text file in the rfcs subdirectory
	 */
	@Order(2)
	@ParameterizedTest
	@ValueSource(strings = {
			"rfc3629.txt",
			"rfc475.txt",
			"rfc5646.txt",
			"rfc6805.txt",
			"rfc6838.txt",
			"rfc7231.txt"
	})
	public void testRfcFiles(String filename) {
		Path input = Paths.TEXT.path.resolve("rfcs").resolve(filename);
		test("index-rfcs", input);
	}

	/**
	 * Tests the index output for files in the guten subdirectory.
	 *
	 * @param filename filename of a text file in the guten subdirectory
	 */
	@Order(3)
	@ParameterizedTest
	@ValueSource(strings = {
			"50468-0.txt",
			"pg37134.txt",
			"pg22577.txt",
			"pg1661.txt",
			"pg1322.txt",
			"pg1228.txt",
			"1400-0.txt"
	})
	public void testGutenFiles(String filename) {
		Path input = Paths.TEXT.path.resolve("guten").resolve(filename);
		test("index-guten", input);
	}

	/**
	 * Tests that parsing a single non-text file still works.
	 */
	@Order(4)
	@Test
	public void testMarkdownIndex() {
		Path input = Paths.TEXT.path.resolve("simple").resolve("sentences.md");
		test("index-simple", input);
	}

	/**
	 * Tests the index output for the simple subdirectory.
	 */
	@Order(5)
	@Test
	public void testSimpleDirectory() {
		Path input = Paths.TEXT.path.resolve("simple");
		test("index-simple", input);
	}

	/**
	 * Tests the index output for the rfcs subdirectory.
	 */
	@Order(6)
	@Test
	public void testRfcDirectory() {
		Path input = Paths.TEXT.path.resolve("rfcs");
		test("index-rfcs", input);
	}

	/**
	 * Tests the index output for the guten subdirectory.
	 */
	@Order(7)
	@Test
	public void testGutenDirectory() {
		Path input = Paths.TEXT.path.resolve("guten");
		test("index-guten", input);
	}

	/**
	 * Tests the index output for all of the text files.
	 */
	@Order(8)
	@Test
	public void testText() {
		Path input = Paths.TEXT.path;
		test(".", input);
	}
}
