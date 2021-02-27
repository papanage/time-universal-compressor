phda9 is an experimental compressor designed for enwik9 and
     similar files with mostly English text in 'plain text'
     format.
     It's a single-threaded app compiled for Intel Core i7.

To compress enwik9:
===================
./phda9 C9 enwik9 compressed_enwik9
To decompress:
./phda9dec compressed_enwik9 decompressed_enwik9

phda stands for Pack Hundred Days Ahead.
Compression of enwik9 takes about 24 hours on a desktop
computer with Core i7-7700K @ 4.7 GHz  (and no other
processes actively running). Decompression time is similar.

Memory usage reported by /usr/bin/time is
approximately 6316000 KiB, that is, ~6.02 GiB.
If allocation fails, app just crashes.

If you try compressing/decompressing either other files
with 'C9', or enwik9 differently, that will not work:
even if encoding/decoding complete correctly, decompressed
file will not be a bit-exact copy of the original.
YOU MAY LOSE YOUR DATA! and a lot of time.

You can also try compressing other files with English text:
===========================================================
./phda9 C plain_text_file compressed_file
To decompress:
./phda9 D compressed_file decompressed_file

Plain English text or XML, HTML, etc. Files must be smaller
than ~900 MB. Other problems? Please report them! Problems
in the (de)compression algorithms most likely will be fixed.

You can also try compressing/decompressing other files
smaller than ~500 MB using lowercase c/d instead of C/D,
but this makes little sense because phda9 was not designed
for arbitrary files, therefore issues will not be fixed.

To compress with an external distionary:
./phda9 C plain_text_file compressed_file dictionary_file
The same dictionary must be specified for correct decompression!

Dictionary must contain not more than 188240 lines with words,
with "80000" followed by 12 empty lines at the beginning,
end-of-line symbols must be Windows-style '\r\n', and
size of the dictionary file must be 1930550 bytes or less.

===========================================================
The executable phda9_no_LSTM is approximately 2.5 times faster
but the compressed file size is ~2% bigger, at least on enwik9.

The MS Windows executable phda9dec.exe requires files
with standard C++ libraries from the MinGW-W64 package:
libstdc++-6.dll
libgcc_s_seh-1.dll
libwinpthread-1.dll

Please email questions/comments to Alexander Rhatushnyak:
g r a l i c 1 (at) g m a i l (.d.o.t.) com
