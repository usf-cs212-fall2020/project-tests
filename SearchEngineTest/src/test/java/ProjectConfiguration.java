import java.nio.file.Path;

/**
 * Project configuration.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2020
 */
public class ProjectConfiguration {

	/**
	 * The paths the project should use.
	 */
	public static enum Paths {
		/** Path to the actual output files. */
		ACTUAL(Path.of("actual")),

		/** Path to the expected output files. */
		EXPECTED(Path.of("expected")),

		/** Path to the directory of text files. */
		TEXT(Path.of("input", "text")),

		/** Path to the directory of query files. */
		QUERY(Path.of("input", "query")),

		/** Path to the default index JSON file. */
		INDEX(Path.of("index.json")),

		/** Path to the default search result JSON file. */
		RESULTS(Path.of("results.json"));

		/** The path. */
		public final Path path;

		/** The path as text, useful for String[] args. */
		public final String text;

		/**
		 * Initializes this enum.
		 * @param path the path
		 */
		private Paths(Path path) {
			this.path = path;
			this.text = path.normalize().toString();
		}

		@Override
		public String toString() {
			return text;
		}
	}

	/**
	 * Command-line flags the project should use.
	 */
	public static enum Flags {
		/** Flag to indicate file or directory of text files to index. */
		PATH("-path"),

		/** Flag to indicate seed url to index. */
		SEED("-url"),

		/** Flag to indicate file with queries to search for. */
		QUERY("-queries"),

		/** Flag to indicate JSON file to output index. */
		INDEX("-index"),

		/** Flag to indicate JSON file to output counts. */
		COUNTS("-counts"),

		/** Flag to indicate JSON file to output search results. */
		RESULTS("-results"),

		/** Flag to indicate exact search enabled. */
		EXACT("-exact"),

		/** Flag to indicate multithreading should be used. */
		THREADS("-threads"),

		/** Flag to indicate maximum number of pages to crawl. */
		LIMIT("-max"),

		/** Flag to indicate the web server should be launched. */
		SERVER("-server");

		/** The flag being used as text. */
		public final String text;

		/** Initializes the flag.
		 * @param text the flag
		 */
		private Flags(String text) {
			this.text = text;
		}

		@Override
		public String toString() {
			return text;
		}
	}
}
