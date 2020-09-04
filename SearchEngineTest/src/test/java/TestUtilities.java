import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.util.regex.Matcher;

import org.junit.jupiter.api.Assertions;

/**
 * Utility methods used by other JUnit test classes.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2020
 */
public class TestUtilities extends ProjectConfiguration {

	/** Format string for JUnit error output. */
	private static final String ERROR_FORMAT =
			"%nWorking Directory:%n    %s%nActual File:%n    %s%nExpected File:%n    %s%nArguments:%n    %s%nMessage:%n    %s%n";

	/** Timeout to use when testing exceptions. */
	public static final Duration TIMEOUT = Duration.ofSeconds(30);

	/**
	 * Produces debug-friendly output when a JUnit test fails.
	 * @param args     original arguments sent to {@link Driver}
	 * @param actual   path to actual output
	 * @param expected path to expected output
	 * @param message  error message with more details
	 *
	 * @return formatted error message
	 */
	public static String errorMessage(String[] args, Path actual, Path expected, String message) {
		Path working = Path.of(".").toAbsolutePath().normalize();
		return String.format(ERROR_FORMAT, working.toString(), actual.toString(),
				expected.toString(), String.join(" ", args), message);
	}

	/**
	 * Checks whether environment setup is correct, with a input and output
	 * directory located within the base directory.
	 *
	 * @return true if expected paths are found and readable/writable
	 */
	public static boolean isEnvironmentSetup() {
		try {
			Files.createDirectories(Paths.ACTUAL.path);
		}
		catch (IOException e) {
			System.err.println("Unable to create actual output directory.");
			return false;
		}

		return Files.isReadable(Paths.EXPECTED.path)
				&& Files.isWritable(Paths.ACTUAL.path)
				&& Files.isDirectory(Paths.TEXT.path)
				&& Files.isDirectory(Paths.QUERY.path);
	}

	/**
	 * Checks line-by-line if two files are equal. If one file contains extra blank
	 * lines at the end of the file, the two are still considered equal. Works even
	 * if the path separators in each file are different.
	 *
	 * @param path1 path to first file to compare with
	 * @param path2 path to second file to compare with
	 * @return positive value if two files are equal, negative value if not
	 *
	 * @throws IOException if IO error occurs
	 */
	public static int checkFiles(Path path1, Path path2) throws IOException {
		String separator = Matcher.quoteReplacement(File.separator);

		// used to output line mismatch
		int count = 0;

		try (
				BufferedReader reader1 = Files.newBufferedReader(path1, StandardCharsets.UTF_8);
				BufferedReader reader2 = Files.newBufferedReader(path2, StandardCharsets.UTF_8);
		) {
			String line1 = reader1.readLine();
			String line2 = reader2.readLine();

			while (true) {
				count++;

				// compare lines until we hit a null (i.e. end of file)
				if (line1 != null && line2 != null) {
					// use consistent path separators
					line1 = line1.replaceAll(separator, "/");
					line2 = line2.replaceAll(separator, "/");

					// remove trailing spaces
					line1 = line1.strip();
					line2 = line2.strip();

					// check if lines are equal
					if (!line1.equals(line2)) {
						return -count;
					}

					// read next lines if we get this far
					line1 = reader1.readLine();
					line2 = reader2.readLine();
				}
				else {
					// discard extra blank lines at end of reader1
					while (line1 != null && line1.isBlank()) {
						line1 = reader1.readLine();
					}

					// discard extra blank lines at end of reader2
					while (line2 != null && line2.isBlank()) {
						line2 = reader2.readLine();
					}

					if (line1 == line2) {
						// only true if both are null, otherwise one file had
						// extra non-empty lines
						return count;
					}
					else {
						// extra blank lines found in one file
						return -count;
					}
				}
			}
		}
	}

	/**
	 * Checks whether {@link Driver} generates the expected output without any
	 * exceptions. Will print the stack trace if an exception occurs. Designed to be
	 * used within an unit test. If the test was successful, deletes the actual
	 * file. Otherwise, keeps the file for debugging purposes.
	 *
	 * @param args     arguments to pass to {@link Driver}
	 * @param actual   path to actual output
	 * @param expected path to expected output
	 */
	public static void checkOutput(String[] args, Path actual, Path expected) {

		try {
			// Remove old actual file (if exists), setup directories if needed
			Files.deleteIfExists(actual);
			Files.createDirectories(actual.getParent());

			// Generate actual output file
			System.out.printf("%nRunning: %s...%n", actual.toString());
			Driver.main(args);

			// Double-check we can read the expected output file
			if (!Files.isReadable(expected)) {
				String message = "Unable to read expected output file.";
				Assertions.fail(errorMessage(args, actual, expected, message));
			}

			// Double-check we can read the actual output file
			if (!Files.isReadable(actual)) {
				String message = "Unable to read actual output file.";
				Assertions.fail(errorMessage(args, actual, expected, message));
			}

			// Compare the two files
			int count = checkFiles(actual, expected);

			if (count <= 0) {
				String message = "Difference detected on line: " + -count + ".";
				Assertions.fail(errorMessage(args, actual, expected, message));
			}

			// At this stage, the files were the same and we can delete actual.
			Files.deleteIfExists(actual);
		}
		catch (Exception e) {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));

			String message = writer.toString();
			Assertions.fail(errorMessage(args, actual, expected, message));
		}
	}

	/**
	 * Checks whether {@link Driver} will run without generating any exceptions.
	 * Will print the stack trace if an exception occurs. Designed to be used within
	 * an unit test.
	 *
	 * @param args arguments to pass to {@link Driver}
	 */
	public static void checkExceptions(String[] args) {
		try {
			System.out.printf("%nRunning Driver %s...%n", String.join(" ", args));
			Driver.main(args);
		}
		catch (Exception e) {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));

			String debug = String.format("%nArguments:%n    [%s]%nException:%n    %s%n",
					String.join(" ", args), writer.toString());
			Assertions.fail(debug);
		}
	}

	/**
	 * Tests whether {@link Driver} runs without generating any exceptions and ends
	 * within the {@link #TIMEOUT}. Designed to be used within an unit test.
	 *
	 * @param args arguments to pass to {@link Driver}
	 */
	public static void testNoExceptions(String[] args) {
		Assertions.assertTimeoutPreemptively(TIMEOUT, () -> {
			checkExceptions(args);
		});
	}

	/**
	 * Generates the output file name to use given the prefix and path.
	 *
	 * @param prefix the prefix to use, like "index" or "results"
	 * @param path the input path being tested
	 * @return the output file name to use
	 */
	public static String outputFileName(String prefix, Path path) {
		// determine filename to use for actual/expected output
		if (Files.isDirectory(path)) {
			// e.g. index-simple.json
			return String.format("%s-%s.json", prefix, path.getFileName().toString());
		}
		else {
			// e.g. index-simple-hello.json
			String[] parts = path.getFileName().toString().split("\\.");
			String subdir = path.getParent().getFileName().toString();
			return String.format("%s-%s-%s.json", prefix, subdir, parts[0]);
		}
	}

	/**
	 * Generates the output file name to use given the prefix, path, and number of threads.
	 *
	 * @param prefix the prefix to use, like "index" or "results"
	 * @param path the input path being tested
	 * @param threads the number of threads
	 * @return the output file name to use
	 */
	public static String outputFileName(String prefix, Path path, int threads) {
		// determine filename to use for actual/expected output
		if (Files.isDirectory(path)) {
			// e.g. index-simple-1.json
			return String.format("%s-%s-%d.json", prefix, path.getFileName().toString(), threads);
		}
		else {
			// e.g. index-simple-hello-1.json
			String[] parts = path.getFileName().toString().split("\\.");
			String subdir = path.getParent().getFileName().toString();
			return String.format("%s-%s-%s-%d.json", prefix, subdir, parts[0], threads);
		}
	}
}
