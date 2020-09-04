import java.nio.file.Path;

import org.junit.jupiter.api.MethodOrderer.Alphanumeric;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * JUnit tests for the inverted index search functionality.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2020
 */
@TestMethodOrder(Alphanumeric.class)
public class SearchOutputTest extends TestUtilities {

	/**
	 * Generates the arguments to use for this test case. Designed to be used
	 * inside a JUnit test.
	 *
	 * @param subdir the output subdirectory to use
	 * @param input the input path to use
	 * @param query the query file to use for search
	 * @param exact whether to perform exact or partial search
	 */
	public static void test(String subdir, Path input, String query, boolean exact) {
		String type = exact ? "exact" : "partial";
		String prefix = "search-" + type;
		String filename = outputFileName(prefix, input);

		Path actual = Paths.ACTUAL.path.resolve(filename).normalize();
		Path expected = Paths.EXPECTED.path.resolve("search").resolve(subdir).resolve(filename).normalize();

		String[] args = {
				Flags.PATH.text, input.normalize().toString(),
				Flags.QUERY.text, Paths.QUERY.path.resolve(query).toString(),
				Flags.RESULTS.text, actual.normalize().toString(),
				exact ? Flags.EXACT.text : ""
		};

		checkOutput(args, actual, expected);
	}

	/**
	 * Junit tests for the search results output for exact search.
	 */
	@Nested
	@TestMethodOrder(OrderAnnotation.class)
	public class A_ExactSearchTest {

		/** The default subdir for this nested class. */
		public final String subdir = "exact";

		/** The default search mode for this nested class. */
		public final boolean exact = true;

		/**
		 * Tests the search result output for the simple directory.
		 */
		@Order(1)
		@Test
		public void testSimpleDirectory() {
			Path input = Paths.TEXT.path.resolve("simple");
			String query = "simple.txt";
			test(subdir, input, query, exact);
		}

		/**
		 * Tests the search result output for the rfcs subdirectory.
		 */
		@Order(2)
		@Test
		public void testRfcDirectory() {
			Path input = Paths.TEXT.path.resolve("rfcs");
			String query = "letters.txt";
			test(subdir, input, query, exact);
		}

		/**
		 * Tests the search result output for files in the guten subdirectory.
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
			String query = "letters.txt";
			test(subdir, input, query, exact);
		}

		/**
		 * Tests the search result output for the guten subdirectory.
		 */
		@Order(4)
		@Test
		public void testGutenDirectory() {
			Path input = Paths.TEXT.path.resolve("guten");
			String query = "letters.txt";
			test(subdir, input, query, exact);
		}

		/**
		 * Tests the search result output for the guten subdirectory.
		 */
		@Order(5)
		@Test
		public void testTextDirectory() {
			Path input = Paths.TEXT.path;
			String query = "complex.txt";
			test(subdir, input, query, exact);
		}
	}

	/**
	 * Junit tests for the search results output for partial search.
	 */
	@Nested
	@TestMethodOrder(OrderAnnotation.class)
	public class B_PartialSearchTest {

		/** The default subdir for this nested class. */
		public final String subdir = "partial";

		/** The default search mode for this nested class. */
		public final boolean exact = false;

		/**
		 * Tests the search result output for the simple directory.
		 */
		@Order(1)
		@Test
		public void testSimpleDirectory() {
			Path input = Paths.TEXT.path.resolve("simple");
			String query = "simple.txt";
			test(subdir, input, query, exact);
		}

		/**
		 * Tests the search result output for the rfcs subdirectory.
		 */
		@Order(2)
		@Test
		public void testRfcDirectory() {
			Path input = Paths.TEXT.path.resolve("rfcs");
			String query = "letters.txt";
			test(subdir, input, query, exact);
		}

		/**
		 * Tests the search result output for the guten subdirectory.
		 */
		@Order(3)
		@Test
		public void testGutenDirectory() {
			Path input = Paths.TEXT.path.resolve("guten");
			String query = "letters.txt";
			test(subdir, input, query, exact);
		}

		/**
		 * Tests the search result output for the text subdirectory.
		 */
		@Order(4)
		@Test
		public void testTextDirectory() {
			Path input = Paths.TEXT.path;
			String query = "complex.txt";
			test(subdir, input, query, exact);
		}
	}
}
