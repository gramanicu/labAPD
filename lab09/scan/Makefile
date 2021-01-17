build:
	mpicc scan.c -o scan
clear:
	rm scan
run: clear build
	mpirun -np 4 scan