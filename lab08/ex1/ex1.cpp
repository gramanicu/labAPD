#include <mpi.h>
#include <iostream>
#include <stdlib.h>
#include <time.h> 

int main (int argc, char *argv[])
{
    int  numtasks, rank;

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &numtasks);
    MPI_Comm_rank(MPI_COMM_WORLD,&rank);

    int recv_num;

    // First process starts the circle.
    if (rank == 0) {
        // First process starts the circle.
        // Generate a random number.
        srand(time(NULL));
        int num = rand() % 100 + 1;
        std::cout << "The initial value is " << num << "\n";

        // Send the number to the next process.
        MPI_Send(&num, 1, MPI_INT, 1, 0, MPI_COMM_WORLD);

        // "Close the ring"
        MPI_Recv(&recv_num, 1, MPI_INT, numtasks - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);
        std::cout << "The final value after " << numtasks - 1 << " hops is " << recv_num << "\n";
    } else if (rank == numtasks - 1) {
        // Last process close the circle.
        // Receives the number from the previous process.
        MPI_Recv(&recv_num, 1, MPI_INT, rank - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        // Increments the number.
        recv_num += 2;

        // Sends the number to the first process.
        MPI_Send(&recv_num, 1, MPI_INT, 0, 0, MPI_COMM_WORLD);
    } else {
        // Middle process.
        // Receives the number from the previous process.
        MPI_Recv(&recv_num, 1, MPI_INT, rank - 1, 0, MPI_COMM_WORLD, MPI_STATUS_IGNORE);

        // Increments the number.
        recv_num += 2;

        // Sends the number to the next process.
        MPI_Send(&recv_num, 1, MPI_INT, rank + 1, 0, MPI_COMM_WORLD);

    }

    MPI_Finalize();

}