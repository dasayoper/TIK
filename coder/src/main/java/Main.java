import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) throws IOException {
        String message;
        Scanner sc = new Scanner(System.in);
        message = sc.nextLine();

        //читаем алфавит с файла и убираем ненужные пробелы
        BufferedReader reader = new BufferedReader(new FileReader("D:/Proghomework/TIK/stopka knig alphabet.txt"));
        String alphabet = reader.lines().collect(Collectors.joining(System.lineSeparator()));
        StringBuilder simplifiedAlphabet = new StringBuilder();
        for (int i = 0; i < alphabet.length(); i++) {
            if (i % 2 == 0) {
                simplifiedAlphabet.append(alphabet.charAt(i));
            }
        }
        //System.out.println(simplifiedAlphabet);
        List<String> bwtArr = getBWT(message);
        String res = bwtArr.get(0);
        String num = bwtArr.get(1);
        System.out.println(res + " " + num);
        String res2 = getStopkaKnig(res, simplifiedAlphabet.toString());
        System.out.println(res2);
    }

    public static List<String> getBWT(String message) {
        List<String> results = new ArrayList<>(2);
        StringBuilder result = new StringBuilder();
        int size = message.length();
        int number = 0;
        List<String> strings = new ArrayList<>(size);
        List<String> sortedStrings;
        for (int i = 0; i < size; i++) {
            String s = message.substring(i, size) + message.substring(0, i);
            strings.add(s);
            System.out.println(s);
        }
        sortedStrings = strings.stream()
                .sorted()
                .toList();
//        System.out.println("\n");
//        for (int i = 0; i < size; i++) {
//            System.out.println(sortedStrings.get(i));
//        }
//        System.out.println("\n");
        for (String s : sortedStrings) {
            result.append(s.charAt(size - 1));
            if (s.equals(message)) {
                number = sortedStrings.indexOf(s);
            }
        }
        results.add(result.toString());
        results.add(String.valueOf(number));
        return results;
    }

    public static String getStopkaKnig(String message, String alphabet) {
        StringBuilder result = new StringBuilder();
        int size = message.length();
        for (int i = 0; i < size; i++) {
            char c = message.charAt(i);
            int cost = alphabet.indexOf(c);
            alphabet = c + alphabet.substring(0, alphabet.indexOf(c)) + alphabet.substring(alphabet.indexOf(c) + 1);
            result.append(cost);
            System.out.println(alphabet);
        }
        return result.toString();
    }
}
