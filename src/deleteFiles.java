import java.io.IOException;
import java.nio.file.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author gedos
 */


public class deleteFiles {

    public static void main(String[] args) {
        String directoryPath = "D:\\Downloads\\cobadelete";
        try {
            deleteDuplicateFiles(directoryPath);
        } catch (IOException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private static void deleteDuplicateFiles(String directoryPath) throws IOException, NoSuchAlgorithmException {
        Map<String, Path> fileChecksumMap = new HashMap<>();

        try (DirectoryStream<Path> directoryStream = Files.newDirectoryStream(Paths.get(directoryPath))) {
            for (Path filePath : directoryStream) {
                if (Files.isRegularFile(filePath)) {
                    String checksum = getChecksum(filePath);
                    if (fileChecksumMap.containsKey(checksum)) {
                        System.out.println("Deleting duplicate file: " + filePath);
                        Files.delete(filePath);
                    } else {
                        fileChecksumMap.put(checksum, filePath);
                    }
                }
            }
        }
    }

    private static String getChecksum(Path filePath) throws IOException, NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        try (DigestInputStream dis = new DigestInputStream(Files.newInputStream(filePath), md)) {
            while (dis.read() != -1);
        }

        byte[] digest = md.digest();
        StringBuilder checksum = new StringBuilder();
        for (byte b : digest) {
            checksum.append(String.format("%02x", b));
        }

        return checksum.toString();
    }
}
