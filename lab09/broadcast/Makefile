build:
	mpicc broadcast.c -o broadcast
clear:
	rm broadcast
run: clear build
	mpirun -np 4 broadcast