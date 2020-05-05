#include <stdio.h>

void factorialFor(int num)
{
	int factorial = num;
	for(int x = 2; x < num; x++)
	{
		factorial *= x;
	}
	printf("%d\n",factorial);
}

void factorialWhile(int num)
{
	int mult = 2;
	int factorial = num;
	while(mult < num)
	{
		factorial *= mult;
		mult++;
	}
	printf("%d\n",factorial);
}

int main()
{
	factorialFor(1);
	factorialFor(2);
	factorialFor(4);
	factorialFor(5);
	factorialFor(8);
	factorialWhile(1);
	factorialWhile(2);
	factorialWhile(4);
	factorialWhile(5);
	factorialWhile(8);
	return 0;
}