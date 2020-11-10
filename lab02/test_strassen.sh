#!/bin/bash

N=1000

make

if [ ! -f "multiply_seq" ]
then
    echo "Nu exista binarul multiply_seq"
    exit
fi

if [ ! -f "strassen" ]
then
    echo "Nu exista binarul strassen"
    exit
fi

./multiply_seq $N > seq.txt
./strassen $N > par.txt

diff seq.txt par.txt

rm -rf seq.txt par.txt
