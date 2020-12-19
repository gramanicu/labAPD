#include <mpi.h>
#include <iostream>
#include <stdlib.h>
#include <sstream>

#define MULTI 5 // chunk dimension
#define ROOT 0

int main (int argc, char *argv[])
{
    int  numtasks, rank, len;
    char hostname[MPI_MAX_PROCESSOR_NAME];

    MPI_Init(&argc, &argv);
    MPI_Comm_size(MPI_COMM_WORLD, &numtasks);
    MPI_Comm_rank(MPI_COMM_WORLD,&rank);
    MPI_Get_processor_name(hostname, &len);

    int num_elements = MULTI * numtasks; // total elements
    int *v_send = NULL; // full vector
    int *v_recv = (int *)malloc(MULTI * sizeof(int)); // partial vector

    // ROOT process generates the values for the full vector.
    if(rank == ROOT) {
        v_send = (int *)malloc(num_elements * sizeof(int));
        srand(time(NULL));
        for(int i = 0; i < num_elements; ++i) {
            v_send[i] = rand() % 100 + 1;
        }
    }

    // Scatter the vector to all processes.
    MPI_Scatter(v_send, MULTI, MPI_INT, v_recv, MULTI, MPI_INT, ROOT, MPI_COMM_WORLD);

    std::stringstream ss;
    ss << "Process" << rank << ": have elements ";

    // Each process increments the values of the partial vector received.
    for(int i = 0; i < MULTI; ++i) {
        ss << v_recv[i] << " ";
        v_recv[i] += rank;
    }
    ss << "\n";
    std::cout << ss.str();

    // Gathers the values from all the processes.
    MPI_Gather(v_recv, MULTI, MPI_INT, v_send, MULTI, MPI_INT, ROOT, MPI_COMM_WORLD);

    if(rank == ROOT) {
        // The ROOT process prints the elements received.
        ss.clear();

        ss << "The final array is :";
        for(int i = 0; i < num_elements; ++i) {
           ss << v_send[i] << " ";
        }
        ss << "\n";
        std::cout << ss.str();
    }

    MPI_Finalize();

}

