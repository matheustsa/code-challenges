import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Main {
    /*
     * Challenge: Given a string such as "onetwoplusfive", perform the mathematical operation and return the result as a string, accounting for both positive and negative outcomes.
     *
     * Examples:
     * - Input: "onetwoplusfive" (12+5)
     * - Output: "oneseven" (17)
     *
     * - Input: "fourzerominusthree" (40-3)
     * - Output: "threeseven" (37)
     *
     * - Input: "fiveminusonezero" (5-10)
     * - Output: "negativefive" (-5)
     */

    // Map to convert words and operators to digits
    private static final Map<String, String> WORD_TO_SYMBOL =
        Map.ofEntries(
            Map.entry("zero", "0"),
            Map.entry("one", "1"),
            Map.entry("two", "2"),
            Map.entry("three", "3"),
            Map.entry("four", "4"),
            Map.entry("five", "5"),
            Map.entry("six", "6"),
            Map.entry("seven", "7"),
            Map.entry("eight", "8"),
            Map.entry("nine", "9"),
            Map.entry("plus", "+"),
            Map.entry("minus", "-")
        );

    // Indexed array from 0 to 9, to convert digits back to words
    private static final String[] DIGIT_TO_WORD = {
        "zero", "one", "two", "three", "four",
        "five", "six", "seven", "eight", "nine"
    };

    public static String solve(String input) {
        String expression = input.toLowerCase();

        // 1. Format string to numeric expression using the Map
        for (Map.Entry<String, String> entry : WORD_TO_SYMBOL.entrySet()) {
            expression = expression.replace(entry.getKey(), entry.getValue());
        }

        // 2. Evaluate built expression
        long result = evaluateExpression(expression);

        // 3. Format numeric result back to words
        return numberToWords(result);
    }

    private static long evaluateExpression(String expression) {
        // Regex that captures numbers optionally preceded by '+' or '-'
        Matcher matcher = Pattern.compile("[+-]?\\d+").matcher(expression);
        long total = 0;
        while (matcher.find()) {
            // Parse number string (ex: '5') to long
            total += Long.parseLong(matcher.group());
        }

        return total;
    }

    private static String numberToWords(long number) {
        // Faster that creating various objects in memory
        StringBuilder result = new StringBuilder();

        if (number < 0) {
            result.append("negative");
            // Get absolute number to remove negative symbol and avoid "negative-5"
            number = Math.abs(number);
        }

        String numStr = String.valueOf(number);

        // Splits the string into an array of chars
        for (char c : numStr.toCharArray()) {
            /*
             * Every character is stored internally as an integer, following the ASCII table.
             * The character '0' does not have a value of 0 in memory—it has a specific numerical code.
             * In the ASCII table, the characters '0' through '9' appear in exact sequence, from code 48 to 57
             *
             * c - '0': In Java, the compiler subtracts the ASCII code of '0' (which is 48) from the ASCII code of the current character, c.
             * If c is '5' (ASCII code 53), the calculation Java performs behind the scenes is 53 - 48, which results in the integer 5.
             *
             * Character.digit(c, 10) or Character.getNumericValue(c) could be used as alternatives.
             */
            int digit = c - '0';

            // Format digit back to word
            result.append(DIGIT_TO_WORD[digit]);
        }

        return result.toString();
    }

    public static void main(String[] args) {
        // Input: 12+3
        System.out.println(solve("onetwoplusthree"));
        // Output: onedfive (15) -> onefive

        // Input: 5-10
        System.out.println(solve("fiveminusonezero"));
        // Output: negativefive (-5)

        // Input: 123+2
        System.out.println(solve("onetwothreeplustwo"));
        // Output: onetwofive (125) .

        // Multiple operations: 10 + 20 - 5 = 25
        System.out.println(solve("onezeroplustwozerominusfive"));
        // Output: twofive
    }
}