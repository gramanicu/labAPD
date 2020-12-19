#include <mpi.h>
#include <iostream>
#include <stdlib.h>

#define ROOT 0

int main (int argc, char *argv[])
{
    int  numtasks, rank;

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &numtasks);
    MPI_Comm_rank(MPI_COMM_WORLD,&rank);

    // Checks the number of processes allowed.
    if (numtasks != 2) {
        printf("Wrong number of processes. Only 2 allowed!\n");
        MPI_Finalize();
        return 0;
    }

    // How many numbers will be sent.
    int send_numbers = 10;
    int value;

    if (rank == 0) {
        srand(time(NULL));

        for(int i = 0; i < send_numbers; ++i) {
            // Generate the random numbers.
            value = rand() % 50 + 1;

            // Generate the random tags.
            int tag = rand() % 50 + 1;

            // Sends the numbers with the tags to the second process.
            MPI_Send(&value, 1, MPI_INT, 1, tag, MPI_COMM_WORLD);
        }
    } else {
        for(int i = 0; i < send_numbers; ++i) {
            // Receives the information from the first process.
            MPI_Status status;
            MPI_Recv(&value, 1, MPI_INT, ROOT, MPI_ANY_TAG, MPI_COMM_WORLD, &status);

            // Prints the numbers with their corresponding tags.
            std::cout << "Received value " << value << " with tag " << status.MPI_TAG << "\n";
        }
    }

    MPI_Finalize();

}

