# LZ77 compression algorithm

An [LZ77](https://en.wikipedia.org/wiki/LZ77_and_LZ78) compression algorithm implementation using java. It was an assignment for the Compression and Information theory course in Cairo University Faculty of Computers and Artificial Intelligence.  
This implementation theoretically can compress text and binary files, although it is not designed to.

## How to use

To compress a file run the program with the following arguments:
```shell
java -jar c <filename>
```

For decompression:
```shell
java -jar d <filename>
```

## Example

Using 5 paragraphs of [Lorem ipsum](https://www.lipsum.com/feed/html):  
[Input file](examples/lipsum.txt) is 2,588 bytes  
[Compressed file](examples/lipsum.txt.lz77) is 3,245 bytes (Bigger with this implementation)


## See also

- [LZ88 implementation](https://github.com/KareemMAX/lz78)
- [LZW implementation](https://github.com/KareemMAX/lzw)
- [Huffman coding implementation](https://github.com/mAshrafDawood/Huffman)