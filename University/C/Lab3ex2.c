#include <stdio.h>

void tree(int width, int length)
{
	int gap = width / 2;
	for(int x = 1; x <= width; x+=2)
	{
		for(int y = 0; y < gap; y++)
		{
			printf(" ");
		}
		for(int z = 0; z < x; z++)
		{
			printf("*");
		}
		printf("\n");
		gap--;
	}
	gap = (width/2)-1;
	for(int a = 0; a < length; a++)
	{
		for(int b = 0; b < gap; b++)
		{
			printf(" ");
		}
		printf("***\n");
	}
	printf("\n");
	
}

int main()
{
	tree(9,4);
	tree(5,2);
	tree(11,6);
	return 0;
}