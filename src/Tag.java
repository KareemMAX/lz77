import java.util.ArrayList;
import java.util.BitSet;
import java.util.List;

public class Tag {
    static int bufferBitLength = 3;

    private long _position;
    private long _length;
    private char _nextChar;

    public Tag(long position, long length, char nextChar) {
        _position = position;
        _length = length;
        _nextChar = nextChar;
    }

    public Tag(BitSet tagBits) {
        BitSet positionBitSet = tagBits.get(0, bufferBitLength);
        BitSet lengthBitSet = tagBits.get(bufferBitLength, bufferBitLength * 2);
        BitSet charBitSet = tagBits.get(bufferBitLength * 2, bufferBitLength * 2 + 8);

        if (positionBitSet.isEmpty())
            _position = 0;
        else
            _position = positionBitSet.toLongArray()[0];
        if (lengthBitSet.isEmpty())
            _length = 0;
        else
            _length = lengthBitSet.toLongArray()[0];
        if (charBitSet.isEmpty())
            _nextChar = (char) 0;
        else
            _nextChar = (char) charBitSet.toByteArray()[0];
    }

    public static BitSet convertTagsToBits(List<Tag> tags) {
        BitSet result = new BitSet(tags.size() * (bufferBitLength * 2 + 8));

        for (int i = 0; i < tags.size(); i++) {
            BitSet tagBits = tags.get(i).convertToBits();
            for (int j = 0; j < (bufferBitLength * 2 + 8); j++) {
                result.set(((bufferBitLength * 2 + 8) * i) + j, tagBits.get(j));
            }
        }

        return result;
    }

    public static List<Tag> convertBitsToTags(BitSet tagsBit) {
        List<Tag> result = new ArrayList<>();
        for (int i = 0; i < tagsBit.size() / (bufferBitLength * 2 + 8); i++) {
            result.add(new Tag(tagsBit.get(i * (bufferBitLength * 2 + 8), (i + 1) * (bufferBitLength * 2 + 8))));
        }

        return result;
    }

    public long getPosition() {
        return _position;
    }

    public void setPosition(long position) {
        if (position >= Math.pow(2, bufferBitLength))
            throw new IllegalArgumentException("Given position can't fit in the current bit length");
        _position = position;
    }

    public long getLength() {
        return _length;
    }

    public void setLength(long length) {
        if (length >= Math.pow(2, bufferBitLength))
            throw new IllegalArgumentException("Given length can't fit in the current bit length");
        _length = length;
    }

    public char getNextChar() {
        return _nextChar;
    }

    public void setNextChar(char nextChar) {
        _nextChar = nextChar;
    }

    public BitSet convertToBits() {
        // Tag is <Position  , Length    , Next Symbol>
        //         lengthBits, lengthBits, 8 (ASCII)
        BitSet result = new BitSet(bufferBitLength * 2 + 8);

        for (int i = 0; i < bufferBitLength; i++) {
            boolean positionBit = ((_position >> i) & 1) == 1;
            boolean lengthBit = ((_length >> i) & 1) == 1;

            result.set(i, positionBit);
            result.set(i + bufferBitLength, lengthBit);
        }

        for (int i = 0; i < 8; i++) {
            result.set(i + (2 * bufferBitLength), ((((int) _nextChar) >> i) & 1) == 1);
        }

        return result;
    }
}
