build:
	mpicc reduce.c -o reduce
clear:
	rm reduce
run: clear build
	mpirun -np 4 reduce