#include <stdio.h>

void readInFromFile(char string[]*){
	char nameOfFile[10000];
	FILE* fileOfNames;
	char contents[100000];
	
	
	printf("Enter the name of your file of names: e.g. names.txt\n");
	scanf("%s",&nameOfFile);
	if((fileOfNames = fopen(nameOfFile,"r"))==NULL){
	    printf("Error opening file, closing program");
	    exit(1);
	}
	
	fscanf(fileOfNames,"%s",contents);
	
	&string = contents;
}

void writeToNewFile(){
	
}

void quickSortStringsAlpha(){
	
}

void swapStringsInList(){
	
}

void compareStrings(){
	
}

void splitStringInList(){
	
}

int main(){
	char line[10000];
	readInFromFile(&line);
	printf(line);
	printf("\n");
	
	return 0;
}