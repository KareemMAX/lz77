import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class LZ77 {
    public static void main(String[] args) {
//        String input = "ABAABABAABBBBBBBBBBBBA";
//        List<Tag> compressed = compress(input);
//
//        for (Tag tag :
//                compressed) {
//            System.out.println("Tag<" + tag.getPosition() + ", " + tag.getLength() + ", " + tag.getNextChar() + ">");
//        }
//
//        String decompressedString = decompress(compressed);
//        System.out.println(decompressedString);
//
//        System.out.println("Decompressed = Compressed: " + (input.equals(decompressedString)));

        try
        {
            if (args.length == 2) {
                File file = new File(args[1]);
                if (file.exists()) {
                    if (args[0].toLowerCase().contains("c")) { // Compress
                        String input = Files.readString(file.toPath());

                        List<Tag> compressedTags = compress(input);
                        for (Tag tag :
                                compressedTags) {
                            System.out.println("Tag<" + tag.getPosition() + ", " + tag.getLength() + ", " + tag.getNextChar() + ">");
                        }

                        byte[] compressedBytes = Tag.convertTagsToBits(compressedTags).toByteArray();

                        String newPath = file.getPath() + ".lz77";
                        File compressedFile = new File(newPath);
                        compressedFile.createNewFile();
                        Files.write(compressedFile.toPath(), compressedBytes);
                    } else if (args[0].toLowerCase().contains("d")) { // Decompress
                        byte[] input = Files.readAllBytes(file.toPath());

                        BitSet compressedBits = BitSet.valueOf(input);

                        String decompressedTxt = decompress(Tag.convertBitsToTags(compressedBits));
                        System.out.println(decompressedTxt);

                        String newPath = file.getPath() + ".txt";

                        File decompressedFile = new File(newPath);
                        decompressedFile.createNewFile();

                        List<String> lines = new ArrayList<>();
                        lines.add(decompressedTxt);
                        Files.write(decompressedFile.toPath(), lines);
                    } else {
                        System.out.println(args[0] + " is invalid argument");
                    }
                } else {
                    System.out.println(args[1] + " is not an existing file");
                }
            } else {
                System.out.println("No arguments were supplied");
            }
        }
        catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    static List<Tag> compress(String str) {
        List<Tag> result = new ArrayList<>();

        int searchWindow = (int) Math.pow(2, Tag.bufferBitLength);
        for (int i = 0; i < str.length(); i++) {
            int l = Math.max(0, i - searchWindow);
            int r = i;

            String searchBuffer = str.substring(l, r);
            boolean compressed = false;
            for (int j = Math.min(str.length() - 1, (i + searchWindow)); j >= i + 1; j--) {
                String search = str.substring(i, j);
                int pos = searchBuffer.lastIndexOf(search);
                if (pos != -1) {
                    if (str.length() > j)
                        result.add(new Tag(searchBuffer.length() - pos, j - i, str.charAt(j)));
                    else
                        result.add(new Tag(searchBuffer.length() - pos, j - i, (char) 0));
                    i = j;
                    compressed = true;
                    break;
                }
            }

            if (!compressed) {
                result.add(new Tag(0, 0, str.charAt(i)));
            }
        }

        return result;
    }

    static String decompress(List<Tag> tags) {
        StringBuilder builder = new StringBuilder();

        for (Tag tag :
                tags) {
            builder.append(builder.substring((int) (builder.length() - tag.getPosition()), (int) ((builder.length() - tag.getPosition()) + tag.getLength())));
            if (tag.getNextChar() != 0)
                builder.append(tag.getNextChar());
        }

        return builder.toString();
    }
}
