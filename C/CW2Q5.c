// principles of programming, question 5: redacting text in C
// CM10228, jib25
#include <stdio.h>


//taken from CW2Q2
//function to read in a line from text file
// *param string = pointer to the string to set the line's value to
void readInFromFile(char * contents){
	char nameOfFile[100];
	scanf("%s",&nameOfFile);
	char curChar;
	int charCounter = 0;
	
	FILE *file = fopen(nameOfFile, "r");
	
	while(file == NULL){
	    printf("That file doesn't exist in this directory, try again: ");
	    scanf("%s",&nameOfFile);
	    file = fopen(nameOfFile, "r");
	}
	
	while((curChar = (fgetc(file))) != EOF){
	    contents[charCounter++] = curChar;
	   
	}
	contents[charCounter] = '\0';
	
	printf("%s",contents);
	fclose(file);
	
	
}

void splitUpRedactWords(char * redactWords[], char filters[]){
    
}


int main(){
    char contents[100000];
    printf("Enter the name of your file with the text to be censored in it: ");
    readInFromFile(contents);
    printf("%s\n",contents);
    
    char filters[1000];
    printf("Enter the name of your file with the words to redact in it: ");
    readInFromFile(filters);
    printf("%s",filters);
    
    char redactWords[1000][20];
    splitUpRedactWords(redactWords, filters);
    return 0;
} 