#include <stdio.h>


void storeMultipleTable(int n)
{
	int table[n][n];
	for(int x = 0; x < n; x++)
	{
		for(int y = 0; y < n; y++)
		{
			table[x][y] = (x+1)*(y+1);
			printf("%d",(x+1)*(y+1));
			if(y != (n-1)){ printf(","); }
			printf(" ");
		}
		printf("\n");
	}
	printf("\n");
}



int main()
{
	storeMultipleTable(5);
	storeMultipleTable(3);
	storeMultipleTable(8);
	return 0;
}