#include <stdio.h>
#include <stdlib.h>
#include <pthread.h>
#include <math.h>

int N;
int P;
int *v;
int *vQSort;

pthread_barrier_t barrier;

typedef enum {false, true} bool;

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

void compare_vectors(int *a, int *b) {
	int i;

	for (i = 0; i < N; i++) {
		if (a[i] != b[i]) {
			printf("Sortare incorecta\n");
			return;
		}
	}

	printf("Sortare corecta\n");
}

void display_vector(int *v) {
	int i;
	int display_width = 2 + log10(N);

	for (i = 0; i < N; i++) {
		printf("%*i", display_width, v[i]);
	}

	printf("\n");
}

/**
 * @brief Compares two values, and swaps them if the first is greater 
 * than the second
 * 
 * @param a The first number
 * @param b The second number
 * @return bool if the swap occured
 */
bool cmpAndSwap(int *a, int *b) {
	if(*a > *b) {
		*a ^= *b;
   		*b ^= *a;
   		*a ^= *b;
		return true;
	}
	return false;
}

int cmp(const void *a, const void *b) {
	int A = *(int*)a;
	int B = *(int*)b;
	return A - B;
}

void get_args(int argc, char **argv)
{
	if(argc < 3) {
		printf("Numar insuficient de parametri: ./oets N P\n");
		exit(1);
	}

	N = atoi(argv[1]);
	P = atoi(argv[2]);
}

void init()
{
	int i;
	v = malloc(sizeof(int) * N);
	vQSort = malloc(sizeof(int) * N);

	if (v == NULL || vQSort == NULL) {
		printf("Eroare la malloc!");
		exit(1);
	}

	srand(42);

	for (i = 0; i < N; i++)
		v[i] = rand() % N;
}

void print()
{
	printf("v:\n");
	display_vector(v);
	printf("vQSort:\n");
	display_vector(vQSort);
	compare_vectors(v, vQSort);
}

void *thread_function(void *arg)
{
	int thread_id = *(int *)arg;
	int start, end;
	int odd, even;

    start = thread_id * (double) N / P;
    end = min((thread_id + 1) * (double) N / P, N);

	if(start % 2 == 0) {
		odd = start + 1;
		even = start;
	} else {
		odd = start;
		even = start + 1;
	}

	for(int it = 0; it < N; ++it) {
		for (int i = even; (i < end) && (i < N - 1); i += 2) {
			cmpAndSwap(v + i, v + i + 1);
		}
		pthread_barrier_wait(&barrier);
		
		for (int i = odd; (i < end) && (i < N - 1); i += 2) {
			cmpAndSwap(v + i, v + i + 1);
		}
		pthread_barrier_wait(&barrier);
	}


	pthread_exit(NULL);
}

int main(int argc, char *argv[])
{
	get_args(argc, argv);
	init();

	int i;
	pthread_t tid[P];
	int thread_id[P];


	pthread_barrier_init(&barrier, NULL, P);

	// se sorteaza vectorul etalon
	// for (i = 0; i < N; i++)
	// 	vQSort[i] = v[i];
	// qsort(vQSort, N, sizeof(int), cmp);

	// se creeaza thread-urile
	for (i = 0; i < P; i++) {
		thread_id[i] = i;
		pthread_create(&tid[i], NULL, thread_function, &thread_id[i]);
	}

	// se asteapta thread-urile
	for (i = 0; i < P; i++) {
		pthread_join(tid[i], NULL);
	}
 
	// {
	// int aux;
	// int sorted = 0;
	// while (!sorted) {
	// 	sorted = 1;

	// 	for (i = 0; i < N-1; i++) {
	// 		if(v[i] > v[i + 1]) {
	// 			aux = v[i];
	// 			v[i] = v[i + 1];
	// 			v[i + 1] = aux;
	// 			sorted = 0;
	// 		}
	// 	}
	// }
	// }

	// se afiseaza vectorul etalon
	// se afiseaza vectorul curent
	// se compara cele doua
	// print();

	free(v);
	// free(vQSort);
	pthread_barrier_destroy(&barrier);


	return 0;
}
