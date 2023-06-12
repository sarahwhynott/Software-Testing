import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FooBarBazTest {

    private FooBarBaz tester;

    @BeforeEach
    void setup() {
        this.tester = new FooBarBaz();
    }

    @Test
    void testTransform() {
        // partition is number divisible by 3 only
        assertEquals("foo", tester.transform(3));

        // partition is number divisible by 5 only
        assertEquals("bar", tester.transform(5));

        // partition is number divisible by 7 only
        assertEquals("baz", tester.transform(7));

        // partition is number divisible by 3 and 5
        assertEquals("foobar", tester.transform(15));

        // partition is number divisible by 3 and 7
        assertEquals("foobaz", tester.transform(21));

        // partition is number divisible by 5 and 7
        assertEquals("barbaz", tester.transform(35));

        // partition is number divisible by 3, 5,and 7
        assertEquals("foobarbaz", tester.transform(105));

        // partition is number not divisible by 3, 5, or 7
        assertEquals("11", tester.transform(11));
    }

    @Test
    void testCheckCommandLineInput() {
        String[] argsTester = new String[1];
        String[] emptyArgsTester = new String[0];
        String[] longArgsTester = new String[100];

        // command line input is console in all lowercase
        argsTester[0] = "console";
        assertEquals("console", tester.checkCommandLineInput(argsTester));

        // command line input is console in all caps
        argsTester[0] = "CONSOLE";
        assertEquals("console", tester.checkCommandLineInput(argsTester));

        // command line input is console in mixed cases
        argsTester[0] = "ConSolE";
        assertEquals("console", tester.checkCommandLineInput(argsTester));

        // command line input is batch in all lowercase
        argsTester[0] = "batch";
        assertEquals("batch", tester.checkCommandLineInput(argsTester));

        // command line input is batch in all caps
        argsTester[0] = "BATCH";
        assertEquals("batch", tester.checkCommandLineInput(argsTester));

        // command line input is batch in mixed cases
        argsTester[0] = "bAtcH";
        assertEquals("batch", tester.checkCommandLineInput(argsTester));

        // command line input is not console or batch
        argsTester[0] = "hello";
        assertEquals("Invalid argument. Please enter either \"console\" or \"batch\" in " + "command line arguments.",
                tester.checkCommandLineInput(argsTester));

        // command line input if args.length is greater than 1
        longArgsTester[0] = "batch";
        longArgsTester[1] = "console";
        assertEquals("Invalid argument. Please enter either \"console\" or \"batch\" in " + "command line arguments.",
                tester.checkCommandLineInput(longArgsTester));

        // command line input if args[] is empty
        assertEquals("console", tester.checkCommandLineInput(emptyArgsTester));

        // command line input if args[] is null
        argsTester = null;
        assertEquals("console", tester.checkCommandLineInput(argsTester));

    }

    @Test
    void testCheckConsoleInput() {

        // partition is negative integers
        assertEquals(false, tester.checkConsoleInput("-6"));

        // partition is positive integers
        assertEquals(true, tester.checkConsoleInput("6"));

        // partition is 0
        assertEquals(true, tester.checkConsoleInput("0"));

        // partition is very large integers
        assertEquals(false, tester.checkConsoleInput("100000000000000000"));

        // partition is very negative integers
        assertEquals(false, tester.checkConsoleInput("-100000000000000000"));

        // partition is decimals
        assertEquals(false, tester.checkConsoleInput("5.6"));

        // partition is integers with spaces
        assertEquals(false, tester.checkConsoleInput("5   6"));

        // partition is integers that start with spaces
        assertEquals(true, tester.checkConsoleInput("   6"));

        // partition is integers that end with spaces
        assertEquals(true, tester.checkConsoleInput("5   "));

        // partition is strings
        assertEquals(false, tester.checkConsoleInput("hello"));
    }

    @Test
    void testCheckBatchInput() throws IOException {

        // doesnt matter where this writes to
        Path newfilepath = Paths.get("src/resources/fbb-test-output.txt");
        BufferedWriter testWriter = new BufferedWriter(new FileWriter(newfilepath.toString()));

        // partition is negative integers
        assertEquals(false, tester.checkBatchInput("-6", testWriter));

        // partition is positive integers
        assertEquals(true, tester.checkBatchInput("6", testWriter));

        // partition is 0
        assertEquals(true, tester.checkBatchInput("0", testWriter));

        // partition is very large integers
        assertEquals(false, tester.checkBatchInput("100000000000000000", testWriter));

        // partition is very negative integers
        assertEquals(false, tester.checkBatchInput("-100000000000000000", testWriter));

        // partition is decimals
        assertEquals(false, tester.checkBatchInput("5.6", testWriter));

        // partition is integers with spaces
        assertEquals(false, tester.checkBatchInput("5   6", testWriter));

        // partition is integers that start with spaces
        assertEquals(true, tester.checkBatchInput("   6", testWriter));

        // partition is integers that end with spaces
        assertEquals(true, tester.checkBatchInput("5   ", testWriter));

        // partition is strings
        assertEquals(false, tester.checkBatchInput("hello", testWriter));

    }

    @Test
    void testConsoleMode() {

        // partition is entering string into console
        assertEquals("Invalid input.\n", tester.consoleMode("hello"));

        // partition is entering 0 into console
        assertEquals("0=0\n", tester.consoleMode("0"));

        // partition is entering positive integer into console
        assertEquals("0=0\n" + "1=1\n" + "2=2\n" + "3=foo\n" + "4=4\n" + "5=bar\n", tester.consoleMode("5"));
    }

    @Test
    void testStopChecker() {
        // partition is entering stop into console
        assertEquals(true, tester.stopChecker("stop"));

        // partition is entering stop in uppercase into console
        assertEquals(true, tester.stopChecker("STOP"));

        // partition is entering stop in mixed case into console
        assertEquals(true, tester.stopChecker("sToP"));

        // partition is entering string that is not stop into console
        assertEquals(false, tester.stopChecker("hello"));

        // partition is entering number into console
        assertEquals(false, tester.stopChecker("10"));
    }

    @Test
    void testBatchMode() throws FileNotFoundException {

        Path expectedFilePath = Paths.get("src/resources/fbb-expected-output.txt");

        File expectedFile = expectedFilePath.toFile();
        Path realPath = Paths.get("src/resources/fbb-input.txt");

        File actual = tester.batchMode(realPath);
        Scanner expectedScan = new Scanner(expectedFile);
        Scanner actualScan = new Scanner(actual);

        String expectedLine;
        String actualLine;

        while (expectedScan.hasNextLine() && actualScan.hasNextLine()) {
            expectedLine = expectedScan.nextLine();
            actualLine = actualScan.nextLine();

            assertEquals(expectedLine, actualLine);
        }

        // again set up a "net" to catch and verify output
        ByteArrayOutputStream outputByteStream = new ByteArrayOutputStream();
        PrintStream outputNet = new PrintStream(outputByteStream);
        System.setOut(outputNet);
        String expectedOutput = "An error occurred." + System.lineSeparator();

        Path fakePath = Paths.get("src/resources/fake.txt");
        tester.batchMode(fakePath);
        assertEquals(expectedOutput, outputByteStream.toString());

        System.setOut(System.out);

    }

    @Test
    void testEnterNewNumber() {

        // control console input with no human intervention
        String result = tester.enterNewNumber();
        assertEquals("stop", result);

    }

}
