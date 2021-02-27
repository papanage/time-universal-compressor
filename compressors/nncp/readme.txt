Lossless Data Compression with Neural Networks
==============================================

1) Overview
-----------

Available programs:

nncp        LSTM compressorBase
trfcp       Transformer compressorBase
preprocess  Preprocessor (may be applied before compression and after
                          decompression)
dump_coef   Analyze model parameters

The paper doc/nncp.pdf describes the algorithms of the first
version. They have been slightly modified in the next versions.

Use './test.sh all' to do a regression test.

2) NNCP: LSTM based compressorBase
------------------------------

The model parameters can be modified with command line options. In
addition to the LSTM cell described in the article, standard LSTM and
tied LSTM cells are implemented.

The '-n_symb' option sets the vocabulary size. When it is larger than
256, the compressorBase expects big endian 16 bit symbols instead of
bytes.

Use the "-T" option to set the number of threads. You can measure the
running time with small files to find the optimal number of
threads. The compressorBase output does not depend on the number of
threads.

The batch size specifies for number of independently compressed
streams. For small files (say < 5 MB), a small batch size
(e.g. "-batch_size 1 -lr 3.2e-3") gives a slightly better compression
but it is much slower.

The learning rate (-lr option) needs to be adjusted at least when
changing the batch size (-batch_size option) or the truncated back
propagation length (-time_steps option).

"-full_connect 1" slightly improves the compression but it adds more
parameters hence it is slower.

3) TRFCP: Transformer based compressorBase
--------------------------------------

The model parameters can be modified with command line options.

4) Models from the paper
------------------------

4.1) LSTM small model
---------------------

  ./nncp -n_layer 3 -hidden_size 90 -lr 7e-3 -full_connect 1 c enwik8.pre out.bin

where enwik8.pre is the CMIX preprocessed enwik8.

4.2) Large models
-----------------

enwik8 preprocessing:

  ./preprocess c out.words enwik8 out.pre 16384 64

enwik9 preprocessing:

  ./preprocess c out.words enwik9 out.pre 16384 512

LSTM large1 model:

  ./nncp -n_layer 5 -hidden_size 352 -n_symb 16388 -full_connect 1 -lr 6e-3 c out.pre out.bin
  
LSTM large2 model:

  ./nncp -n_layer 7 -hidden_size 384 -n_embed_out 5 -n_symb 16388 -full_connect 1 -lr 6e-3 c out.pre out.bin

Transformer large model:

  ./trfcp -n_layer 6 -d_model 512 -d_pos 64 -d_inner 2048 -n_symb 16388 -lr 1e-4,5000000,5e-5,28000000,3e-5 c out.pre out.bin

The parameters are saved in the compressed file so no parameter option
is needed when decompressing.

5) Current best models for enwik8/enwik9
----------------------------------------

enwik8:

  ./preprocess c out.words enwik8 out.pre 16384 64

  ./nncp -n_layer 7 -hidden_size 512 -n_symb 16388 -full_connect 1 -lr 5e-3 -n_embed_out 5 -block_len 500000 -time_steps 40 -seg_len 20 c out.pre out.bin

  size: 16292774 bytes, 
  speed: 0.28 kS/s (Xeon E5-2640 @ 2.6 GHz, 8 threads)

enwik9:

  ./preprocess c out.words enwik9 out.pre 32768 256
  ./nncp -n_layer 7 -hidden_size 512 -n_symb 32768 -full_connect 1 -lr 5e-3 -n_embed_out 5 -block_len 500000 -time_steps 40 -seg_len 20 c out.pre out.bin

  size: 119167224 bytes
  speed: 0.21 kS/s (Xeon E5-2640 @ 2.6 GHz, 8 threads)

Note: 'preprocess' uses the log2() function whose precision
depends on its implementation in the C library. The results listed
here were achieved on a Linux x86 system.

6) Licence
----------

The source code is released under the MIT licence.

The LibNC library is provided in binary form and cannot be
redistributed without the author permission.
