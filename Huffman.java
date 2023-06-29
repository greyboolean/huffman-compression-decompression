import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.PriorityQueue;

//define a class that represents nodes of the tree
class Node {
    // store character
    Character ch;
    // store frequency
    Integer frequency;
    // left and right child nodes
    Node left = null;
    Node right = null;
    // constructor without
    Node(Character ch, Integer frequency) {
        this.ch = ch;
        this.frequency = frequency;
    }
    // constructor with child nodes
    public Node(Character ch, Integer frequency, Node left, Node right) {
        this.ch = ch;
        this.frequency = frequency;
        this.left = left;
        this.right = right;
    }
}

public class Huffman {

    /* -------------------------------------------------------------------------------------------------------------- */

    // create a <character, frequency> HashMap from a string
    public static HashMap getFrequencyMap(String str) {
        // Create a HashMap containing character as a key and frequency as a value
        HashMap<Character, Integer> characterFrequencyMap = new HashMap<Character, Integer>();
        // Convert given string to char array
        char[] strArray = str.toCharArray();
        // check each char of strArray
        for (char c : strArray) {
            if (characterFrequencyMap.containsKey(c)) {
                // If char is present in characterFrequencyMap increment count by 1
                characterFrequencyMap.put(c, characterFrequencyMap.get(c) + 1);
            } else {
                // If char is not present in characterFrequencyMap put this char to characterFrequencyMap with 1 as value
                characterFrequencyMap.put(c, 1);
            }
        }
        // return map
        return characterFrequencyMap;
    }

    // convert <Character, Integer> HashMap to a string and return
    public static String hashmapToString(HashMap<Character, Integer> hashmap) {
        String str = "";
        // iterate map entries
        for (Map.Entry<Character, Integer> entry: hashmap.entrySet()) {
            if (entry.getKey() == '\n') {
                str += "\\n";
            } else if  (entry.getKey() == '\r') {
                str += "\\r";
            } else if (entry.getKey() == ' ') {
                str += "space";
            } else {
                str += entry.getKey();
            }
            str += ":" + entry.getValue() + "\n";
        }
        return str;
    }

    // read the file specified by inputFilePath and return the content as a string
    public static String readFile(String inputFilePath) {
        // create a path choosing file from local directory by creating an object of Path class
        Path path = Path.of(inputFilePath);
        String str = null;
        try {
            // read the file and create a string
            str = Files.readString(path);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // return string
        return str;
    }

    // write a string to a file specified by outputFilePath
    public static void writeFile(String str, String outputFilePath) {
        // create a path choosing file from local directory by creating an object of Path class
        Path path = Path.of(outputFilePath);
        try {
            // write the string to a file
            Files.writeString(path, str);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    // covert <Character, Integer>HashMap to a HuffmanTree of type priorityQueue
    public static PriorityQueue<Node> getHuffmanTree(HashMap<Character, Integer> frequencyMap) {
        // create a priority queue that stores current nodes of the Huffman tree (highest priority = lowest frequency)
        PriorityQueue<Node> huffmanTree = new PriorityQueue<>(Comparator.comparingInt(l -> l.frequency));
        // iterate over the frequencyMap creating nodes for each element in map and add to the priority queue
        for (Map.Entry<Character, Integer> entry : frequencyMap.entrySet()) {
            // create a leaf node and add it to the priorityQueue
            huffmanTree.add(new Node(entry.getKey(), entry.getValue()));
        }
        // remove nodes according to priority and create human tree
        // loop runs until there is only one node in the queue
        while (huffmanTree.size() > 1) {
            // remove the nodes having the highest priority (lowest frequency) from the queue
            Node left = huffmanTree.poll();
            Node right = huffmanTree.poll();
            // create a new node with these two nodes as children and with a frequency equal to the sum of both nodes' frequencies. Add the new node to the priority queue
            // sum up the frequency of the nodes (left and right) that were removed from the priorityQueue
            int sum = left.frequency + right.frequency;
            // add this new node to the priorityQueue
            huffmanTree.add(new Node(null, sum, left, right));
        }
        // return the priorityQueue
        return huffmanTree;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    public static HashMap<Character, String> getHuffmanCodeMap(PriorityQueue<Node> huffmanTree) {
        // store pointer to the root of Huffman Tree
        Node root = huffmanTree.peek();
        // create a map to store Huffman Codes
        HashMap<Character, String> huffmanCode = new HashMap<>();
        // traverse through the Huffman tree and store the Huffman codes in huffmanCode hashmap
        encode(root, "", huffmanCode);
        // return HuffmanCode
        return huffmanCode;
    }

    // traverse the Huffman Tree recursively encoding values and store in huffmanCode hashmap
    public static void encode(Node root, String str, HashMap<Character, String> huffmanCode) {
        if (root == null) {
            return;
        }
        // if the node is a leaf enter corresponding character and encoded value (str) into the huffmanCode
        if (isLeaf(root)) {
            huffmanCode.put(root.ch, str.length() > 0 ? str : "1");
        }
        // call the function recursively for left and right nodes of current node
        encode(root.left, str + '0', huffmanCode);
        encode(root.right, str + '1', huffmanCode);
    }

    // check if the Huffman Tree contains a single node
    public static boolean isLeaf(Node root) {
        // return true if both left node and right node are null
        return root.left == null && root.right == null;
    }

    // convert huffmanCode (<Character, String> HashMap) to a string and return
    public static String huffmanCodeToString(HashMap<Character, String> hashmap) {
        String str = "";
        // iterate map entries
        for (Map.Entry<Character, String> entry: hashmap.entrySet()) {
            if (entry.getKey() == '\n') {
                str += "\\n";
            } else if  (entry.getKey() == '\r') {
                str += "\\r";
            } else if (entry.getKey() == ' ') {
                str += "space";
            } else {
                str += entry.getKey();
            }
            str += ":" + entry.getValue() + "\n";
        }
        return str;
    }

    // create encoded string using original string and huffmanCode
    public static String getEncodedString(String str, HashMap<Character, String> huffmanCode) {
        // encoded value as a String
        String encodedValue = "";
        // iterate over the character array of input string
        for (char c : str.toCharArray()) {
            // create encoded string using huffmanCode values corresponding to each character of input string
            encodedValue += huffmanCode.get(c);
        }
        return encodedValue;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    // create decoded string using encoded string and huffmanCode
    public static String getDecodedString(String encodedValue, HashMap<Character, String> huffmanCode) {
        // decoded String
        String decodedString = "";
        // current encodedValue
        String currentEncodedValue = "";
        // iterate over the character array of encoded String
        for (char c : encodedValue.toCharArray()) {
            // append current character to currentEncodedValue
            currentEncodedValue += c;
            // if currentEncodedValue is in huffmanCode HashMap get corresponding character from huffmanCode and reset currenEncodedValue
            if (huffmanCode.containsValue(currentEncodedValue)) {
                // iterate through huffmanCode HashMap
                for (Map.Entry<Character, String> entry: huffmanCode.entrySet()) {
                    // if currentEncodedValue is equals to a value in HashMap get corresponding key
                    if (currentEncodedValue.equals(entry.getValue())) {
                        // append key character to decodedString
                        decodedString += entry.getKey();
                        break;
                    }
                }
                // reset currentEncodedValue
                currentEncodedValue = "";
            }
        }
        // return final decoded string
        return decodedString;
    }

    /* -------------------------------------------------------------------------------------------------------------- */

    // driver code (main method)
    public static void main(String[] args) {

        // file paths - absoulte
        // String inputFilePath = "D:\\...\\file4.txt";
        // String frequencyMapFilePath = "D:\\...\\freqFile4.txt";
        // String encodedFilePath = "D:\\...\\encodedFile4.txt";
        // String decodedFilePath = "D:\\...\\decodedFile4.txt";
        // file paths - relative
        String inputFilePath = "file.txt";
        String frequencyMapFilePath = "freqFile.txt";
        String encodedFilePath = "encodedFile.txt";
        String decodedFilePath = "decodedFile.txt";

        // read file and get string using readFile method (input: inputFilePath)
        String text = readFile(inputFilePath);

        // create character : frequency HashMap using gerFrequencyMap method (input: text)
        HashMap<Character, Integer> frequencyMap = getFrequencyMap(text);
        // convert frequencyMap to a string
        String frequencyMapString = hashmapToString(frequencyMap);
        // print frequencyMap
        System.out.println("Characters and Frequencies ===>");
        System.out.println("(freqFile in path : " + frequencyMapFilePath + ")");
        System.out.println(frequencyMapString);
        // write frequencyMapString into a file in the path specified by frequencyMapFilePath
        writeFile(frequencyMapString, frequencyMapFilePath);

        // create huffmanTree using getHuffmanTree (input: frequencyMap)
        PriorityQueue<Node> huffmanTree = getHuffmanTree(frequencyMap);

        // create character : huffmanCode Hashmap using getHuffmanCodeMap method (input: huffmanTree)
        HashMap<Character, String> huffmanCode = getHuffmanCodeMap(huffmanTree);
        // convert huffmanCode to a string
        String huffmanCodeString = huffmanCodeToString(huffmanCode);
        // print huffmanCode
        System.out.println("Characters and Huffman codes ===>");
        System.out.println(huffmanCodeString);

        // create encoded text
        String encodedText = getEncodedString(text, huffmanCode);
        // print encoded text
        System.out.println("Encoded Text ===>");
        System.out.println("(encodedFile in path : " + encodedFilePath + ")");
        System.out.println(encodedText);
        // write encodedText into a file in the path specified by encodedFilePath
        writeFile(encodedText, encodedFilePath);

        System.out.println("");

        // create decoded text
        String decodedText = getDecodedString(encodedText, huffmanCode);
        // print decoded text
        System.out.println("Decoded Text ===>");
        System.out.println("(decodedFile in path : " + decodedFilePath + ")");
        System.out.println(decodedText);
        // write decodedText into a file in the path specified by decodedFilePath
        writeFile(decodedText, decodedFilePath);

    }
}
