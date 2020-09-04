import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.OutputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Path;
import java.time.Duration;
import java.time.Instant;

import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

/**
 * This suite of JUnit tests estimate how efficiently the multithreaded versus
 * single threaded code performs. Please keep in mind these only provide an
 * estimate and the runtime output needs to be investigated for anomalies before
 * trusting the results.
 *
 * WARNING: These tests take considerable time, and should not be run unless
 * absolutely necessary.
 *
 * @author CS 212 Software Development
 * @author University of San Francisco
 * @version Fall 2020
 */
@TestMethodOrder(OrderAnnotation.class)
public class ThreadRuntimeTest extends TestUtilities {

	/** The number of warmup runs when benchmarking. */
	public static final int WARM_RUNS = 4;

	/** The number of timed runs when benchmarking. */
	public static final int TIME_RUNS = 6;

	/** The default number of threads to use when benchmarking. */
	public static final int THREADS = 3;

	/** The timeout per run. If code times out, it is likely not ready for benchmarking. */
	public static final Duration TIMEOUT = Duration.ofMinutes(1);

	/** The timeout for long runs (involving multiple runs of Driver). */
	public static final Duration LONG_TIMEOUT = Duration.ofMinutes(5);

	/** Path to the complex.txt file used for testing. */
	public static final String COMPLEX = Paths.QUERY.path.resolve("complex.txt").toString();

	/**
	 * Tests that the inverted index output remains consistent when repeated.
	 */
	@Order(1)
	@RepeatedTest(5)
	public void testIndexConsistency() {
		String filename = "index-text.json";
		Path actual = Paths.ACTUAL.path.resolve(filename);
		Path expected = Paths.EXPECTED.path.resolve("index").resolve(filename);

		String[] args = {
				Flags.PATH.text, Paths.TEXT.text,
				Flags.INDEX.text, actual.toString(),
				Flags.THREADS.text, Integer.toString(THREADS)
		};

		assertTimeoutPreemptively(TIMEOUT, () -> checkOutput(args, actual, expected));
	}

	/**
	 * Tests that the search result output remains consistent when repeated.
	 */
	@Order(2)
	@RepeatedTest(5)
	public void testSearchConsistency() {
		String filename = "search-partial-text.json";
		Path actual = Paths.ACTUAL.path.resolve(filename);
		Path expected = Paths.EXPECTED.path.resolve("search").resolve("partial").resolve(filename);

		String[] args = {
				Flags.PATH.text, Paths.TEXT.text,
				Flags.QUERY.text, COMPLEX,
				Flags.RESULTS.text, actual.toString(),
				Flags.THREADS.text, Integer.toString(THREADS)
		};

		assertTimeoutPreemptively(TIMEOUT, () -> checkOutput(args, actual, expected));
	}

	/**
	 * Tests that code runs faster with {@value #THREADS} threads is faster versus
	 * just 1 worker thread.
	 */
	@Order(3)
	@Test
	public void testIndexMultithreaded() {
		String[] args1 = {
				Flags.PATH.text, Paths.TEXT.text,
				Flags.THREADS.text, String.valueOf(1) };

		String[] args2 = {
				Flags.PATH.text, Paths.TEXT.text,
				Flags.THREADS.text, String.valueOf(THREADS) };

		System.out.println();
		System.out.printf("### Testing Build 1 vs %d Workers...%n", THREADS);

		// make sure code runs without exceptions before testing
		assertTimeoutPreemptively(TIMEOUT, () -> checkExceptions(args1));
		assertTimeoutPreemptively(TIMEOUT, () -> checkExceptions(args2));

		// then test the timing
		assertTimeoutPreemptively(LONG_TIMEOUT, () -> {
			double result = compare("1 Worker", args1, String.valueOf(THREADS) + " Workers", args2);

			assertTrue(result > 0, () -> String.format(
					"%s is %.2f seconds faster than %d workers.", "1 worker",
					-result, THREADS));
		});
	}

	/**
	 * Tests that code runs faster with {@value #THREADS} threads is faster versus
	 * just 1 worker thread.
	 */
	@Order(4)
	@Test
	public void testSearchMultithreaded() {
		String[] args1 = {
				Flags.PATH.text, Paths.TEXT.text,
				Flags.QUERY.text, COMPLEX,
				Flags.THREADS.text, String.valueOf(1) };

		String[] args2 = {
				Flags.PATH.text, Paths.TEXT.text,
				Flags.QUERY.text, COMPLEX,
				Flags.THREADS.text, String.valueOf(THREADS) };

		System.out.println();
		System.out.printf("### Testing Search 1 vs %d Workers...%n", THREADS);

		// make sure code runs without exceptions before testing
		assertTimeoutPreemptively(TIMEOUT, () -> checkExceptions(args1));
		assertTimeoutPreemptively(TIMEOUT, () -> checkExceptions(args2));

		// then test the timing
		assertTimeoutPreemptively(LONG_TIMEOUT, () -> {
			double result = compare("1 Worker", args1, String.valueOf(THREADS) + " Workers", args2);

			assertTrue(result > 0, () -> String.format(
					"%s is %.2f seconds faster than %d workers.", "1 worker",
					-result, THREADS));
			});
	}

	/**
	 * Tests that the multithreading code with {@value #THREADS} is faster than
	 * the single threaded code (without the -threads parameter).
	 */
	@Order(5)
	@Test
	public void testIndexSingleMulti() {
		String[] args1 = { Flags.PATH.text, Paths.TEXT.text };

		String[] args2 = {
				Flags.PATH.text, Paths.TEXT.text,
				Flags.THREADS.text, String.valueOf(THREADS) };

		System.out.println();
		System.out.printf("### Testing Build Single vs Multi...%n", THREADS);

		// make sure code runs without exceptions before testing
		assertTimeoutPreemptively(TIMEOUT, () -> checkExceptions(args1));
		assertTimeoutPreemptively(TIMEOUT, () -> checkExceptions(args2));

		// then test the timing
		assertTimeoutPreemptively(LONG_TIMEOUT, () -> {
			double result = compare("Single", args1, String.valueOf(THREADS) + " Workers", args2);

			assertTrue(result > 0, () -> String.format(
					"%s is %.2f seconds faster than %d workers.", "0 workers",
					-result, THREADS));
			});
	}

	/**
	 * Tests that the multithreading code with {@value #THREADS} is faster than
	 * the single threaded code (without the -threads parameter).
	 */
	@Order(6)
	@Test
	public void testSearchSingleMulti() {
		String[] args1 = {
				Flags.PATH.text, Paths.TEXT.text,
				Flags.QUERY.text, COMPLEX };

		String[] args2 = {
				Flags.PATH.text, Paths.TEXT.text,
				Flags.QUERY.text, COMPLEX,
				Flags.THREADS.text, String.valueOf(THREADS) };

		System.out.println();
		System.out.printf("### Testing Search Single vs Multi...%n", THREADS);

		// make sure code runs without exceptions before testing
		assertTimeoutPreemptively(TIMEOUT, () -> checkExceptions(args1));
		assertTimeoutPreemptively(TIMEOUT, () -> checkExceptions(args2));

		// then test the timing
		assertTimeoutPreemptively(LONG_TIMEOUT, () -> {
			double result = compare("Single", args1, String.valueOf(THREADS) + " Workers", args2);

			assertTrue(result > 0, () -> String.format(
					"%s is %.2f seconds faster than %d workers.", "0 workers",
					-result, THREADS));
		});
	}

	/**
	 * Compares the runtime using two different sets of arguments. Outputs the
	 * runtimes to the console just in case there are any anomalies.
	 *
	 * @param label1 the label of the first argument set
	 * @param args1 the first argument set
	 * @param label2 the label of the second argument set
	 * @param args2 the second argument set
	 * @return the runtime difference between the first and second set of arguments
	 */
	public static double compare(String label1, String[] args1, String label2, String[] args2) {
		long[] runs1 = benchmark(args1);
		long[] runs2 = benchmark(args2);

		long total1 = 0;
		long total2 = 0;

		String labelFormat = "%-6s    %10s    %10s%n";
		String valueFormat = "%-6d    %10.6f    %10.6f%n";

		System.out.printf("%n```%n");
		System.out.printf(labelFormat, "Warmup", label1, label2);
		for (int i = 0; i < WARM_RUNS; i++) {
			System.out.printf(valueFormat, i + 1,
					(double) runs1[i] / Duration.ofSeconds(1).toMillis(),
					(double) runs2[i] / Duration.ofSeconds(1).toMillis());
		}

		System.out.println();
		System.out.printf(labelFormat, "Timed", label1, label2);
		for (int i = WARM_RUNS; i < WARM_RUNS + TIME_RUNS; i++) {
			total1 += runs1[i];
			total2 += runs2[i];
			System.out.printf(valueFormat, i + 1,
					(double) runs1[i] / Duration.ofSeconds(1).toMillis(),
					(double) runs2[i] / Duration.ofSeconds(1).toMillis());
		}

		double average1 = (double) total1 / TIME_RUNS;
		double average2 = (double) total2 / TIME_RUNS;

		System.out.println();
		System.out.printf("%10s:  %10.6f seconds%n", label1, average1 / Duration.ofSeconds(1).toMillis());
		System.out.printf("%10s:  %10.6f seconds%n%n", label2, average2 / Duration.ofSeconds(1).toMillis());
		System.out.printf("%10s: x%10.6f %n", "Speedup", average1 / average2);
		System.out.printf("```%n%n");

		return average1 - average2;
	}

	/**
	 * Benchmarks the {@link Driver#main(String[])} method with the provided
	 * arguments. Tracks the timing of every run to allow of visual inspection.
	 *
	 * @param args the arguments to run
	 * @return an array of all the runtimes, including warmup runs and timed runs
	 */
	public static long[] benchmark(String[] args) {
		long[] runs = new long[WARM_RUNS + TIME_RUNS];

		Instant start;
		Duration elapsed;

		// suppress all console output for the warmup and timed runs
		PrintStream systemOut = System.out;
		PrintStream systemErr = System.err;

		PrintStream nullStream = new PrintStream(OutputStream.nullOutputStream());
		System.setOut(nullStream);
		System.setErr(nullStream);

		try {
			for (int i = 0; i < WARM_RUNS; i++) {
				start = Instant.now();
				Driver.main(args);
				elapsed = Duration.between(start, Instant.now());
				runs[i] = elapsed.toMillis();
			}

			for (int i = 0; i < TIME_RUNS; i++) {
				start = Instant.now();
				Driver.main(args);
				elapsed = Duration.between(start, Instant.now());
				runs[i + WARM_RUNS] = elapsed.toMillis();
			}
		}
		catch (Exception e) {
			StringWriter writer = new StringWriter();
			e.printStackTrace(new PrintWriter(writer));

			String debug = String.format("%nArguments:%n    [%s]%nException:%n    %s%n",
					String.join(" ", args), writer.toString());
			fail(debug);
		}
		finally {
			// restore console output
			System.setOut(systemOut);
			System.setErr(systemErr);
		}

		return runs;
	}
}
