#include <pthread.h>
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h> 	// sysconf

#define NUM_THREADS 2
// #define NUM_THREADS 4

void *f(void *arg) {
  	long id = *(long*) arg;

	for(int i = 0 ;i < 100; ++i) {
  		printf("Hello World %d din thread-ul %ld!\n", i, id);
	}
  	pthread_exit(NULL);
}

void *f1(void *arg) {
	printf("This is function 1, with parameter %d\n", *((int*)arg));
}

void *f2(void *arg) {
	printf("This is function 2, with parameter %d\n", *((int*)arg));
}

int main(int argc, char *argv[]) {
	int numThreads = sysconf(_SC_NPROCESSORS_CONF);

	pthread_t threads[NUM_THREADS]; 	// Ex 2 - numThreads
  	int r;
  	long id;
  	void *status;
  	long arguments[NUM_THREADS];		// Ex 2 - numThreads


	// Ex 1, 2, 3
  	// for (id = 0; id < NUM_THREADS; id++) {
  	// 	arguments[id] = id;
	// 	r = pthread_create(&threads[id], NULL, f, &arguments[id]);

	// 	if (r) {
	//   		printf("Eroare la crearea thread-ului %ld\n", id);
	//   		exit(-1);
	// 	}
  	// }

	// Ex4 - 2 threaduri, cu 2 functii
	int num = 1;
	pthread_create(&threads[0], NULL, f1, &num);
	pthread_create(&threads[1], NULL, f2, &num);


	// Threads Join
  	for (id = 0; id < NUM_THREADS; id++) {
		r = pthread_join(threads[id], &status);

		if (r) {
	  		printf("Eroare la asteptarea thread-ului %ld\n", id);
	  		exit(-1);
		}
  	}

  	pthread_exit(NULL);
}
