build: clear
	mpic++ ex1.cpp -o ex1

run: build
	mpirun -np 8 ex1
clear:
	rm -f ex1