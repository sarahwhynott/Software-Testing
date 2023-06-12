import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Scanner;

public class FooBarBaz {
    private static final String NUMBER_PROMPT = "Enter a new number to count up to or write \"stop\" to end program:";
    private static final String INVALID_CL_ARGUMENT = "Invalid argument. Please enter either \"console\" or \"batch\" in command line arguments.";
    private static final String INVALID_OUTPUT = "=Invalid";
    private static final String INVALID_INPUT = "Invalid input.";
    private static final int FOO = 3;
    private static final int BAR = 5;
    private static final int BAZ = 7;

    public static void main(String[] args) {
        FooBarBaz fbb = new FooBarBaz();

        String count;
        String comLineInput = "";

        // Check command line argument for mode type
        comLineInput = fbb.checkCommandLineInput(args);

        // Batch mode
        // can put try catch here
        if ("batch".equals(comLineInput)) {
            Path inputFilePath = Paths.get("src/resources/fbb-input.txt");
            fbb.batchMode(inputFilePath);
        }

        // Console mode
        if ("console".equals(comLineInput)) {
            count = fbb.enterNewNumber();

            while (!fbb.stopChecker(count)) {

                System.out.print(fbb.consoleMode(count));

                count = fbb.enterNewNumber();

                count = count.trim();

            }
        }

    }

    public String enterNewNumber() {
        Scanner scan = new Scanner(System.in);
        String count;
        System.out.println(NUMBER_PROMPT);
        count = scan.nextLine();
        return count;
    }

    public File batchMode(Path userPath) {
        boolean validInput;
        String fileInputLine, trimFileInputLine;
        int fileInputInt;
        Path inputFilePath = userPath;
        Path outputFilePath = Paths.get("src/resources/fbb-actual-output.txt");

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath.toString()));
            File inputFile = new File(inputFilePath.toString());
            Scanner sc = new Scanner(inputFile);
            while (sc.hasNextLine()) {
                fileInputLine = sc.nextLine();
                validInput = this.checkBatchInput(fileInputLine, writer);
                trimFileInputLine = fileInputLine.trim();
                if (validInput) {
                    fileInputInt = Integer.parseInt(trimFileInputLine);
                    if (fileInputInt == 0) {
                        writer.write("0=0" + System.lineSeparator());
                    }
                    else {
                        writer.write(fileInputLine + "=");
                        writer.write(this.transform(fileInputInt));
                        if (sc.hasNextLine()) {
                            writer.write(System.lineSeparator());
                        }
                    }
                }
                validInput = true;
            }
            writer.close();
        }

        catch (FileNotFoundException exception) {
            // can return an empty file to compare or compare console output
            System.out.println("An error occurred.");
            // exception.printStackTrace();
        }
        catch (IOException exception2) {
            // TODO Auto-generated catch block
            exception2.printStackTrace();
        }

        return outputFilePath.toFile();
    }

    public boolean stopChecker(String count) {
        boolean isStop = false;
        if ("stop".equalsIgnoreCase(count)) {
            isStop = true;
        }
        return isStop;
    }

    public String consoleMode(String count) {
        boolean validInput;
        String results = "";
        validInput = this.checkConsoleInput(count);

        if (validInput) {
            for (int i = 0; i <= Integer.parseInt(count); i++) {
                // if loop here because otherwise it would write
                // 0=foobarbaz
                if (i == 0) {
                    results = results + "0=0" + System.lineSeparator();
                }
                else {
                    results = results + i + "=";
                    results = results + this.transform(i) + System.lineSeparator();
                }
            }

        }
        else {
            return INVALID_INPUT + System.lineSeparator();
        }

        validInput = true;
        return results;

    }

    public boolean checkBatchInput(String fileInputLine, BufferedWriter writer) throws IOException {
        boolean result = true;
        int fileInputInt;
        String trimFileInputLine = fileInputLine.trim();
        try {
            fileInputInt = Integer.parseInt(trimFileInputLine);
            if (fileInputInt < 0) {
                writer.write(fileInputLine + INVALID_OUTPUT + System.lineSeparator());
                result = false;
            }
        }
        catch (NumberFormatException exception) {
            writer.write(fileInputLine + INVALID_OUTPUT + System.lineSeparator());
            result = false;
        }
        return result;
    }

    public boolean checkConsoleInput(String count) {
        count = count.trim();
        boolean result = true;
        try {
            if (Integer.parseInt(count) < 0) {
                // System.out.println(INVALID_INPUT);
                result = false;
            }
        }
        catch (NumberFormatException exception) {
            // System.out.println(INVALID_INPUT);
            result = false;
        }
        return result;
    }

    public String checkCommandLineInput(String[] args) {
        String comLineInput = "";
        if (args == null || args.length == 0 || args[0].equalsIgnoreCase("console")) {
            comLineInput = "console";
        }
        else if (args.length > 1) {
            comLineInput = INVALID_CL_ARGUMENT;
        }
        else if (args[0].equalsIgnoreCase("batch")) {
            comLineInput = "batch";
        }
        else {
            comLineInput = INVALID_CL_ARGUMENT;
        }
        return comLineInput;
    }

    public String transform(int i) {
        String result = "";
        if (i % FOO != 0 && i % BAR != 0 && i % BAZ != 0) {
            result = result + i;
        }
        else {
            if (i % FOO == 0) {
                result = result + "foo";
            }
            if (i % BAR == 0) {
                result = result + "bar";
            }
            if (i % BAZ == 0) {
                result = result + "baz";
            }
        }
        return result;
    }

}
