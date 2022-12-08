import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        TreeMap<Character, Float> frequencies = countFrequency();

        Scanner sc = new Scanner(System.in);

        // генерируем список листьев дерева
        ArrayList<CodeTreeNode> codeTreeNodes = new ArrayList<>();
        for (Character c : frequencies.keySet()) {
            codeTreeNodes.add(new CodeTreeNode(c, frequencies.get(c)));
        }

        // строим кодовое дерево с помощью алгоритма Хаффмана
        CodeTreeNode tree = huffman(codeTreeNodes);

        // генерируем таблицу префиксных кодов для кодируемых символов с помощью кодового дерева
        TreeMap<Character, String> codes = new TreeMap<>();
        for (Character c : frequencies.keySet()) { // проходимся по всем символам алфавита
            codes.put(c, tree.getCodeForCharacter(c, "")); // ищем код для текущего символа
        }

        // вывод кодов для символов алфавита
        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.println("Symbol: " + entry.getKey() + ", Code: " + entry.getValue());
        }
        System.out.println();
        System.out.println("Press 1 to encode | Press 2 to decode");
        switch (Integer.parseInt(sc.nextLine())) {
            case 1:
                // кодируем сообщение
                StringBuilder encoded = new StringBuilder();
                System.out.println("Enter message:");
                String message = sc.nextLine();
                for (int i = 0; i < message.length(); i++) {
                    encoded.append(codes.get(message.charAt(i)));
                }
                System.out.println("Encoded message: " + encoded);
                break;
            case 2:
                // декодируем сообщение
                System.out.println("Enter message:");
                message = sc.nextLine();
                String decoded = huffmanDecode(message, tree);
                System.out.println("Decoded message: " + decoded);
                break;
        }
    }

    // считываем вероятности из файла
    private static TreeMap<Character, Float> countFrequency() throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("D:/Proghomework/TIK/huffman alphabet.txt"));
        String hardAlphabet = reader.readLine();
        StringBuilder simplifiedAlphabet = new StringBuilder();
        for (int i = 0; i < hardAlphabet.length(); i++) {
            if (i % 2 == 0) {
                simplifiedAlphabet.append(hardAlphabet.charAt(i));
            }
        }
        List<String> alphabet = Arrays.stream(simplifiedAlphabet.toString().split("")).toList();
        List<String> frequencies = Arrays.stream(reader.readLine().split(" ")).toList();

        TreeMap<Character, Float> freqMap = new TreeMap<>(); // treemap, где ключ - символ, вероятность - значение

        for (int i = 0; i < alphabet.size(); i++) {
            Character c = alphabet.get(i).charAt(0);
            Float freq = null;
            if (frequencies.get(i).contains(",") || frequencies.get(i).contains(".")) {
                freq = Float.parseFloat(frequencies.get(i).replace(",", ".")); // заменяем запятую на точку чтобы ничего не ломалось
            }
            freqMap.put(c, freq);
        }
        return freqMap;
    }

    // строим кодовое дерево
    private static CodeTreeNode huffman(ArrayList<CodeTreeNode> codeTreeNodes) {
        while (codeTreeNodes.size() > 1) { // пока больше одного узла
            codeTreeNodes.sort(Collections.reverseOrder()); // упорядочиваем узлы по возрастанию весов(вероятностей)

            // берем два узла с наименьшей вероятностью записываем в переменные и удаляем их
            CodeTreeNode left = codeTreeNodes.remove(0);
            CodeTreeNode right = codeTreeNodes.remove(0);
            System.err.println(left.weight + " " + left.content + " : " + right.weight + " " + right.content);

            // создаем новый узел, вероятность которого - сумма вероятностей удаленных узлов
            CodeTreeNode parent = new CodeTreeNode(null, right.weight + left.weight, left, right);
            codeTreeNodes.add(parent);
        }
        return codeTreeNodes.get(0);
    }

    private static String huffmanDecode(String encoded, CodeTreeNode tree) {
        StringBuilder decoded = new StringBuilder();
        CodeTreeNode node = tree; // текущий узел при спуске по дереву, изначально корневой
        for (int i = 0; i < encoded.length(); i++) {
            node = encoded.charAt(i) == '0' ? node.left : node.right;
            if (node.content != null) { // если дошли до листа
                decoded.append(node.content); // добавляем символов
                node = tree; // возвращаемся на корень дерева
            }
        }
        return decoded.toString();
    }

    // узел дерева
    private static class CodeTreeNode implements Comparable<CodeTreeNode> {

        Character content; // символ алфавита
        float weight; // вероятность или сумма вероятностей
        CodeTreeNode left; // левый потомок
        CodeTreeNode right; // правый потомок

        public CodeTreeNode(Character content, float weight) {
            this.content = content;
            this.weight = weight;
        }

        public CodeTreeNode(Character content, float weight, CodeTreeNode left, CodeTreeNode right) {
            this.content = content;
            this.weight = weight;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(CodeTreeNode o) {
            if (o.weight - weight > 0) {
                return 1;
            } else if (o.weight - weight < 0) {
                return -1;
            } else {
                return 0;
            }
        }

        // получение кода для символа
        public String getCodeForCharacter(Character ch, String parentPath) {
            if (content == ch) { // если контент текущего листа и есть наш символ возвращаем parentPath
                return parentPath;
            } else {
                if (left != null) { // если есть левое поддерево
                    String path = left.getCodeForCharacter(ch, parentPath + 0); // рекурсивно вызвываем ту же функцию для поддерева и к пути добавляем 0
                    if (path != null) {
                        return path;
                    }
                }
                if (right != null) { // если есть правое поддерево
                    String path = right.getCodeForCharacter(ch, parentPath + 1); // рекурсивно вызвываем ту же функцию для поддерева и к пути добавляем 1
                    if (path != null) {
                        return path;
                    }
                }
            }
            return null;
        }
    }
}