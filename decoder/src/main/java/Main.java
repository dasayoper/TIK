import java.io.*;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        TreeMap<Character, Integer> frequencies = countFrequency();

        Scanner sc = new Scanner(System.in);
        String message = sc.nextLine();

        // генерируем список листов дерева
        ArrayList<CodeTreeNode> codeTreeNodes = new ArrayList<>();
        for (Character c : frequencies.keySet()) {
            codeTreeNodes.add(new CodeTreeNode(c, frequencies.get(c)));
        }

        // строим кодовое дерево с помощью алгоритма Хаффмана
        CodeTreeNode tree = huffman(codeTreeNodes);

        // генерируем таблицу префиксных кодов для кодируемых символов с помощью кодового дерева
        TreeMap<Character, String> codes = new TreeMap<>();
        for (Character c : frequencies.keySet()) {
            System.err.println(c);
            codes.put(c, tree.getCodeForCharacter(c, ""));
        }

        for (Map.Entry<Character, String> entry : codes.entrySet()) {
            System.out.println("Symbol: " + entry.getKey() + ", Code: " + entry.getValue());
        }

        // кодируем текст, заменяем сиволы соответствующими кодами
        StringBuilder encoded = new StringBuilder();
        for (int i = 0; i < message.length(); i++) {
            encoded.append(codes.get(message.charAt(i)));
        }

        System.out.println("Encoded message: " + encoded);
        // декодируем сжатую информацию обратно
        String decoded = huffmanDecode(encoded.toString(), tree);

        System.out.println("Decoded message: " + decoded);
    }

    private static TreeMap<Character, Integer> countFrequency() throws IOException {
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

        TreeMap<Character, Integer> freqMap = new TreeMap<>();

        for (int i = 0; i < alphabet.size(); i++) {
            Character c = alphabet.get(i).charAt(0);
            Integer freq = null;
            if (frequencies.get(i).contains(",") || frequencies.get(i).contains(".")) {
                freq = (int) (Float.parseFloat(frequencies.get(i).replace(",", ".")) * 1000000);
            } else {
                freq = Integer.parseInt(frequencies.get(i));
            }
            freqMap.put(c, freq);
        }
        return freqMap;
    }

    private static CodeTreeNode huffman(ArrayList<CodeTreeNode> codeTreeNodes) {
        while (codeTreeNodes.size() > 1) {
            Collections.sort(codeTreeNodes, Collections.reverseOrder());
            CodeTreeNode left = codeTreeNodes.remove(0);
            CodeTreeNode right = codeTreeNodes.remove(0);
            System.err.println(left.weight + " " + left.content + " : " + right.weight + " " + right.content);

            CodeTreeNode parent = new CodeTreeNode(null, right.weight + left.weight, left, right);
            codeTreeNodes.add(parent);
        }
        return codeTreeNodes.get(0);
    }

    private static String huffmanDecode(String encoded, CodeTreeNode tree) {
        StringBuilder decoded = new StringBuilder();

        CodeTreeNode node = tree;
        for (int i = 0; i < encoded.length(); i++) {
            node = encoded.charAt(i) == '0' ? node.left : node.right;
            if (node.content != null) {
                decoded.append(node.content);
                node = tree;
            }
        }
        return decoded.toString();
    }

    // класс для представления кодового дерева
    private static class CodeTreeNode implements Comparable<CodeTreeNode> {

        Character content;
        int weight;
        CodeTreeNode left;
        CodeTreeNode right;

        public CodeTreeNode(Character content, int weight) {
            this.content = content;
            this.weight = weight;
        }

        public CodeTreeNode(Character content, int weight, CodeTreeNode left, CodeTreeNode right) {
            this.content = content;
            this.weight = weight;
            this.left = left;
            this.right = right;
        }

        @Override
        public int compareTo(CodeTreeNode o) {
            return o.weight - weight;
        }

        // извлечение кода для символа
        public String getCodeForCharacter(Character ch, String parentPath) {
            if (content == ch) {
                return parentPath;
            } else {
                if (left != null) {
                    String path = left.getCodeForCharacter(ch, parentPath + 0);
                    if (path != null) {
                        return path;
                    }
                }
                if (right != null) {
                    String path = right.getCodeForCharacter(ch, parentPath + 1);
                    if (path != null) {
                        return path;
                    }
                }
            }
            return null;
        }
    }
}