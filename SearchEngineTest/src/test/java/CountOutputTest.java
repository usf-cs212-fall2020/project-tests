import java.nio.file.Path;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * JUnit tests for the inverted index count functionality.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2020
 */
@TestMethodOrder(OrderAnnotation.class)
public class CountOutputTest extends TestUtilities {

	/**
	 * Tests that parsing a single non-text file still works.
	 */
	@Order(1)
	@Test
	public void testMarkdownCounts() {
		String filename = "counts-sentences.json";

		Path input = Paths.TEXT.path.resolve("simple").resolve("sentences.md");
		Path actual = Paths.ACTUAL.path.resolve(filename);
		Path expected = Paths.EXPECTED.path.resolve("counts").resolve(filename);

		String[] args = {
				Flags.PATH.text, input.normalize().toString(),
				Flags.COUNTS.text, actual.normalize().toString()
		};

		checkOutput(args, actual, expected);
	}

	/**
	 * Tests that parsing a single empty file still works.
	 */
	@Order(2)
	@Test
	public void testEmptyCounts() {
		String filename = "counts-empty.json";

		Path input = Paths.TEXT.path.resolve("simple").resolve("empty.txt");
		Path actual = Paths.ACTUAL.path.resolve(filename);
		Path expected = Paths.EXPECTED.path.resolve("counts").resolve(filename);

		String[] args = {
				Flags.PATH.text, input.normalize().toString(),
				Flags.COUNTS.text, actual.normalize().toString()
		};

		checkOutput(args, actual, expected);
	}

	/**
	 * Tests the word counts functionality of the inverted index on the entire
	 * input directory.
	 */
	@Order(3)
	@Test
	public void testCounts() {
		String filename = "counts-text.json";

		Path actual = Paths.ACTUAL.path.resolve(filename);
		Path expected = Paths.EXPECTED.path.resolve("counts").resolve(filename);

		String[] args = {
				Flags.PATH.text, Paths.TEXT.path.normalize().toString(),
				Flags.COUNTS.text, actual.normalize().toString()
		};

		checkOutput(args, actual, expected);
	}

}
