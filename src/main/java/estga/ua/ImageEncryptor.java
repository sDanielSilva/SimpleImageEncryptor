package estga.ua;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.security.SecureRandom;
import java.util.Scanner;

public class ImageEncryptor {

    private static SecretKey generateKey(String algorithm, int keySize) throws Exception {
        KeyGenerator keyGen = KeyGenerator.getInstance(algorithm);
        keyGen.init(keySize);
        return keyGen.generateKey();
    }

    private static byte[] processFile(byte[] data, SecretKey key, String algorithm, String mode, int opMode) throws Exception {
        Cipher cipher = Cipher.getInstance(algorithm + "/" + mode + "/PKCS5Padding");
        if ("CBC".equals(mode)) {
            IvParameterSpec iv = new IvParameterSpec(generateIV(cipher.getBlockSize()));
            cipher.init(opMode, key, iv);
        } else {
            cipher.init(opMode, key);
        }
        return cipher.doFinal(data);
    }

    private static void saveKey(SecretKey key, String algorithm) throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("encryptionKey." + algorithm.toLowerCase()))) {
            out.writeObject(key);
        }
    }

    private static SecretKey loadKey(String path) throws Exception {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(path))) {
            return (SecretKey) in.readObject();
        }
    }

    private static byte[] generateIV(int blockSize) {
        byte[] iv = new byte[blockSize];
        new SecureRandom().nextBytes(iv);
        return iv;
    }

    public static void main(String[] args) {
        try (Scanner scanner = new Scanner(System.in)) {
            while (true) {
                System.out.print("Select file type (1 - Image, 2 - Other) [ENTER to quit]: ");
                String fileTypeChoice = scanner.nextLine();
                if (fileTypeChoice.isEmpty()) return;

                boolean isImage = "1".equals(fileTypeChoice);

                int operation = getChoice(scanner, "Choose operation (1 - Encrypt, 2 - Decrypt) [ENTER to quit]: ", 1, 2);
                if (operation == -1) return;

                SecretKey key = null;
                String algorithm = null;
                String mode = getMode(scanner);
                if (mode == null) return;

                if (operation == Cipher.ENCRYPT_MODE) {
                    algorithm = getAlgorithm(scanner);
                    if (algorithm == null) return;

                    int keySize = getKeySize(scanner, algorithm);
                    if (keySize == -1) return;

                    try {
                        key = generateKey(algorithm, keySize);
                        saveKey(key, algorithm);
                    } catch (Exception e) {
                        System.out.println("[ERROR] generating or saving key: " + e.getMessage());
                        return;
                    }
                } else {
                    String keyFilePath = getFilePath(scanner, "Enter key file path for decryption (e.g., 'encryptionKey.des' || encryptionKey.aes') [ENTER to quit]: ");
                    if (keyFilePath == null || keyFilePath.trim().isEmpty()) {
                        return;
                    }

                    algorithm = getAlgorithmFromKeyFile(keyFilePath);
                    if (algorithm == null) continue;

                    if ("AES".equals(algorithm)) {
                        int keySize = getKeySize(scanner, algorithm);
                        if (keySize == -1) return;
                    }

                    try {
                        key = loadKey(keyFilePath);
                    } catch (Exception e) {
                        System.out.println("[ERROR] loading key file: " + e.getMessage());
                        return;
                    }
                }

                String inputFilePath = getFilePath(scanner, "Enter input file path: [ENTER to quit]: ");
                if (inputFilePath == null) return;

                String outputFilePath = getOutputFilePath(scanner, isImage);

                if (isImage) {
                    processAndSaveImageFile(inputFilePath, outputFilePath, key, algorithm, mode, operation);
                } else {
                    processAndSaveOtherFile(inputFilePath, outputFilePath, key, algorithm, mode, operation);
                }
            }
        } catch (Exception e) {
            System.out.println("[ERROR]: " + e.getMessage());
        }
    }

    private static void processAndSaveImageFile(String inputFilePath, String outputFilePath, SecretKey key, String algorithm, String mode, int operation) {
        try {
            byte[] fileContent = readFile(inputFilePath);
            if (fileContent == null) return;

            byte[] header = new byte[54];
            byte[] pixelData = new byte[fileContent.length - 54];
            System.arraycopy(fileContent, 0, header, 0, 54);
            System.arraycopy(fileContent, 54, pixelData, 0, pixelData.length);

            byte[] processedData = processFile(pixelData, key, algorithm, mode, operation);

            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                fos.write(header);
                fos.write(processedData);
            }

            System.out.println((operation == Cipher.ENCRYPT_MODE ? "Encrypted" : "Decrypted") + " image successfully to: " + outputFilePath + "\n");
        } catch (Exception e) {
            System.out.println("[ERROR] processing image file: " + e.getMessage());
        }
    }

    private static void processAndSaveOtherFile(String inputFilePath, String outputFilePath, SecretKey key, String algorithm, String mode, int operation) {
        try {
            byte[] fileContent = readFile(inputFilePath);
            if (fileContent == null) return;

            byte[] processedData = processFile(fileContent, key, algorithm, mode, operation);

            try (FileOutputStream fos = new FileOutputStream(outputFilePath)) {
                fos.write(processedData);
            }

            System.out.println((operation == Cipher.ENCRYPT_MODE ? "Encrypted" : "Decrypted") + " file successfully to: " + outputFilePath + "\n");
        } catch (Exception e) {
            System.out.println("[ERROR] processing file: " + e.getMessage());
        }
    }

    private static byte[] readFile(String filePath) {
        try {
            return new FileInputStream(filePath).readAllBytes();
        } catch (IOException e) {
            System.out.println("[ERROR] reading file: " + e.getMessage());
            return null;
        }
    }

    private static int getChoice(Scanner scanner, String prompt, int min, int max) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine();
            if (input.isEmpty()) return -1;

            try {
                int choice = Integer.parseInt(input);
                if (choice >= min && choice <= max) return choice;
                else System.out.println("Invalid choice. Please try again.");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please try again.");
            }
        }
    }

    private static String getAlgorithm(Scanner scanner) {
        int choice = getChoice(scanner, "Choose algorithm (1 - AES, 2 - DES) [ENTER to quit]: ", 1, 2);
        return choice == 1 ? "AES" : choice == 2 ? "DES" : null;
    }

    private static String getMode(Scanner scanner) {
        int choice = getChoice(scanner, "Choose mode (1 - ECB, 2 - CBC) [ENTER to quit]: ", 1, 2);
        return choice == 1 ? "ECB" : choice == 2 ? "CBC" : null;
    }

    private static int getKeySize(Scanner scanner, String algorithm) {
        if ("DES".equals(algorithm)) return 56;
        while (true) {
            System.out.print("Enter AES key size (128, 192, 256) [ENTER to quit]: ");
            String input = scanner.nextLine();
            if (input.isEmpty()) return -1;

            try {
                int keySize = Integer.parseInt(input);
                if (keySize == 128 || keySize == 192 || keySize == 256) return keySize;
                else System.out.println("Invalid key size. Please enter a number between [128, 192 or 256].");
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Please enter a valid AES key size.");
            }
        }
    }

    private static String getFilePath(Scanner scanner, String prompt) {
        while (true) {
            System.out.print(prompt);
            String input = scanner.nextLine().trim();
            if (input.isEmpty()) return null;

            if (new File(input).exists()) return input;
            System.out.println("File not found. Please try again.");
        }
    }

    private static String getOutputFilePath(Scanner scanner, boolean isImage) {
        System.out.print("Enter output file path [ENTER to quit]: ");
        String outputFilePath = scanner.nextLine().trim();
        if (isImage && !outputFilePath.contains(".")) outputFilePath += ".bmp";
        return outputFilePath;
    }

    private static String getAlgorithmFromKeyFile(String keyFilePath) {
        if (keyFilePath.endsWith(".aes")) return "AES";
        if (keyFilePath.endsWith(".des")) return "DES";
        System.out.println("Invalid key file extension. Please use '.aes' or '.des'.");
        return null;
    }
}
