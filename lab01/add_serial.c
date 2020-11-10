#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>

/**
 * @brief Returns the minimum of two numbers
 * 
 * @param a The first number
 * @param b The second number
 * @return double The smallest of the ones
 */
double min(double a, double b) {
    return a < b ? a : b;
}

typedef struct {
    int start;
    int end;
    int* vec;
} arrInfo;

/**
 * @brief Adds 100 to the elements of an array
 * 
 * @param arg A pointer to an arrInfo struct, containing the pointer to the array,
 * the start and end position of the elements to be increased.
 * @return void* 
 */
void *vecSum(void *arg) {
    arrInfo *arrI = (arrInfo*)arg;
    int* arr = arrI->vec;

    // The function actually adds 100 10000 times, to increase the operation duration
    for(int k = 0 ; k < 10000; ++k) {
        for(int i = arrI->start; i < arrI->end; ++i) {
            arr[i] += 100;
        }
    }
}

/**
 * @brief Print the contents of a int array
 * 
 * @param arr Pointer to the array
 * @param size The size of the array
 */
void printArr(int* arr, int size) {
    for (int i = 0; i < size; i++) {
        printf("%d", arr[i]);
        if (i != size - 1) {
            printf(" ");
        } else {
            printf("\n");
        }
    }
}

int main(int argc, char *argv[]) {
    int* arr;
    int array_size;

    if (argc < 2) {
        perror("Specificati dimensiunea array-ului\n");
        exit(-1);
    }

    int tCount = sysconf(_SC_NPROCESSORS_CONF);
    // int tCount = 1;  // To test single-threaded speed
    array_size = atoi(argv[1]);

    // Creates the array
    arr = malloc(array_size * sizeof(int));
    for (int i = 0; i < array_size; i++) {
        arr[i] = i;
    }

    // The array contents before processing
    // printArr(arr, array_size);

    // The processing of the array
    arrInfo *arrI = malloc(tCount * sizeof(arrInfo));
    pthread_t threads[tCount];
  	void *status;
  	int r;

    // Prepare the function arguments
    for(int i = 0; i < tCount; ++i) {
        arrI[i].vec = arr;
        arrI[i].start = i * (double) array_size / tCount;
        arrI[i].end = min((i + 1) * (double) array_size / tCount, array_size);
    }

    // Create the threads
    for(int i = 0; i < tCount; ++i) {

		r = pthread_create(&threads[i], NULL, vecSum, &arrI[i]);

		if (r) {
	  		printf("Error on thread %d creation\n", i);
	  		exit(-1);
		}
    }

    // Join the threads
    for (int i = 0; i < tCount; i++) {
		r = pthread_join(threads[i], &status);

		if (r) {
	  		printf("Error on thread %d join\n", i);
	  		exit(-1);
		}
  	}

    // Print the array contents after processing
    // printArr(arr, array_size);

    free(arrI);
    free(arr);

  	pthread_exit(NULL);
}
