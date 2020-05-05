#include <stdio.h>
#include <string.h>

void ReverseCommandInput(int argc, char *argv[])
{
	char result[] = ""; 
	for(int x = argc - 1; x > 0; x--)
	{
		strcat(result,argv[x]);
		strcat(result," ");
	}
	printf("%s\n",result);
}

int main(int argc, char *argv[])
{
	ReverseCommandInput(argc,argv);
	return 0;
}	