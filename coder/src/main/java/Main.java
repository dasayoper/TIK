import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(System.in);

        String alphabet = getAlphabet(); // получаем алфавит из файла

        String message = sc.nextLine();

        List<String> bwtArr = getBWT(message); // получаем результат выполнения преобразования Броуза Уиллера
        String res = bwtArr.get(0);
        String num = bwtArr.get(1);
        System.out.println(res);
        String res2 = getStopkaKnig(res, alphabet); // кодируем стопкой книг получившуюся после BWT строку
        System.out.println(res2);
        System.out.println(num);
    }

    public static List<String> getBWT(String message) {
        List<String> results = new ArrayList<>(2); // результаты преобразования Барроуза-Уилера
        StringBuilder result = new StringBuilder(); // последний стоблец отсортированных строк
        int size = message.length();
        int number = 0;
        List<String> strings = new ArrayList<>(size); // массив циклических сдвигов входной строки
        List<String> sortedStrings;
        for (int i = 0; i < size; i++) {
            String s = message.substring(i, size) + message.substring(0, i); // сдвигаем строку на один элемент влево
            strings.add(s);
        }
        //сортируем в алфавитном порядке
        sortedStrings = strings.stream()
                .sorted()
                .toList();
        for (String s : sortedStrings) { // проходим по отсортированным строкам
            result.append(s.charAt(size - 1)); // к результирующей строке добавляем последний символ текущей строки
            if (s.equals(message)) {
                number = sortedStrings.indexOf(s); // записываем номер строки, совпадающей с исходной строки
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
            char c = message.charAt(i); // символ входной строки
            int cPos = alphabet.indexOf(c); // находим порядковый номер(позицию) этого символа в алфавите
            alphabet = c + alphabet.substring(0, cPos) + alphabet.substring(cPos + 1); // сдвигаем алфавит(текущий символ + то что было до него раньше + то что было после него раньше)
            result.append(cPos); // приписываем код к результату
        }
        return result.toString();
    }

    public static String getAlphabet()  throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("D:/Proghomework/TIK/stopka knig alphabet.txt"));
        List<String> alphabet = Arrays.stream(reader.readLine().split(" ")).sorted().toList();
        System.out.println(alphabet.toString());
        StringBuilder simplifiedAlphabet = new StringBuilder();

        for(String symbol: alphabet) {
            simplifiedAlphabet.append(symbol);
        }

        return simplifiedAlphabet.toString();
    }
}
