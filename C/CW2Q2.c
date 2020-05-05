//Principles of Programming coursework 2, Question 2: Quick Sort in C
//CM10228, jib25

#include <stdio.h>
#include <string.h>

//store the name of the file, and the number of words in it, publically accessible
char nameOfFile[100];
int numOfWords = 0;

//function to get the file name from the user
void getTextFileName(){
    //get the filename from the user, try to open if it exists
    printf("Please enter the name of the file to quick sort, e.g. names.txt:\n");
	scanf("%s",&nameOfFile);
	FILE *file = fopen(nameOfFile, "r");
	
	//until the user enters a file that exists, get the user to re-enter
	while(file == NULL){
	    printf("That file doesn't exist in this directory, try again:\n");
	    scanf("%s",&nameOfFile);
	    file = fopen(nameOfFile, "r");
	}
	
	//close the file after use
	fclose(file);
}

//function to get the length of text (number of characters) in a file
//and the number of words
//*param nameOfFile, the filename of the file whose length to find
//@return int, the length of text in the file
int getLengthTextInFile(char nameOfFile[]){
    //store the length and the current char being tested
    int length = 0;
    char curChar;
    //open the given file
    FILE *file = fopen(nameOfFile,"r");
    
    //until the end of the file...
    while((curChar = fgetc(file)) != EOF ){
        //increment the length for each character 
        length++;
        //if the word is a double quote '"', increment the number of words in the file
        if(curChar == '"'){numOfWords++;}
    }
    //divide number of double quotes by 2 to get the number of words in the file
    numOfWords = numOfWords/2;
    
    //return the length gathered
    return length;
    //close file after use
    fclose(file);
}

//function to get the length of a string
//*param string = the string to find the length of
//@return int, the length of the string, how many characters are in it
int getLengthString(char string[]){
    //store the length and the current character being tested
    int length = 0;
    char curChar;
    
    //until the end character is reached...
    while((curChar = string[length]) != '\0'){
        //increment the length found
        length++; 
    }
    
    //return the length found
    return length;
}

//function to split a file of words into a list of strings
//*param list = the list to fill with words
//*param nameOfFile = the name of the file of words to split
void splitFileWordsIntoList(char list[][20], char nameOfFile[]){
    //store the current character in the file, the current word and character being checked
    int charCounter = 0;
    int wordCounter = 0;
    char curChar;
    
    //store whether a character is part of a word or not
    int inWord = 0;
    
    //open the file specified by user
    FILE *file = fopen(nameOfFile, "r");
    
    //until the end of the file...
    while((curChar = fgetc(file)) != EOF){
        //...if the current character is a double quote...
        if(curChar == '"'){
            //...if it marks the end of a word
            if(inWord){
                //add an end character, reset the current character in word
                //set that characters are no longer in a word
                list[wordCounter++][charCounter] = '\0';
                charCounter = 0;
                inWord = 0;
            }
            //...if it is the start of a word...
            else{
                //mark that following characters are part of a word
                inWord = 1;
            }
            
        }
        //...otherwise add the current character to the current word being processed
        else{
            if(inWord){list[wordCounter][charCounter++] = curChar;}
        }
    }
    
}

//function to compare two strings, if string1 is greater alphabetically
//*param string1, the string to check if it comes before or after string2 alphabetically
//*param string2, the string being string1 is being compared against
//@return true or false (1/0) if string1 is greater than string 2 alphabetically or not
int compareStrings(char string1[], char string2[]){
    //for each character in string 2...
    for(int curChar = 0; curChar < getLengthString(string2); curChar++){
        //...if the characyer is greater in string 1, return that string1 is greater
        if(string1[curChar] > string2[curChar]){return 1;}
        //...if the character is smaller in string 1, return that string1 is smaller
        else if(string1[curChar] < string2[curChar]){return 0;}
        //...otherwise move on to the next character
    }
    //if the words are the same, or string 1 is longer, return string1 is greater
    return 1;
}

//function to copy the contents of one list of strings to another
//*param list1 = the destination
//*param list2 = the list having its contents copied
//*param stringsInList = the number of strings in both lists
void copyListsOfStrings(char list1[][20],char list2[][20],int stringsInList){
    //for each string in list 2, copy it to the same position in list 1
    for(int currentString = 0; currentString < stringsInList; currentString++){
        strcpy(list1[currentString],list2[currentString]);
    }
}

//function to quick sort a list of strings
//*param list = the list of strings to quick sort
//*param stringsInList = the length of the list
//*param low = the starting point of a partition being sorted
//*param high = the end point of a partition being sorted
void quickSortListOfStrings(char list[][20],int stringsInList, int low, int high){
    //count the current string being added
    int stringCounter = 0;
    //make a new list with this pass' contents
    char newList[stringsInList][20];
    //if the partition is at least two items long
    if(high-low>1){
        //for every element smaller than the pivot, add it in order to the new list
        for(int curString = low; curString < high; curString++){
            if(!compareStrings(list[curString],list[high])){strcpy(newList[stringCounter++],list[curString]);}
        }
        
        //add the pivot to its place after those lower than it
        strcpy(newList[stringCounter++],list[high]);
        int pivot = stringCounter;
        
        //for every element greater than the pivot, add it to the list after the pivot, in order
        for(int curString = low; curString < high; curString++){
            if(compareStrings(list[curString],list[high])){strcpy(newList[stringCounter++],list[curString]);}
        }
        
        //update the contents of the list being sorted
        copyListsOfStrings(list,newList,stringsInList);
        //perform the same with partitions above and below the pivot
        quickSortListOfStrings(list,stringsInList,0,pivot);
        quickSortListOfStrings(list,stringsInList,pivot,stringsInList);
    }
    
    
}

//function to write the contents of a list of strings to a new file
//*param list = the list of strings to write to a file
//*param length = the number of strings in the list
void writeContentsToNewFile(char list[][20],int length){
    //open a new file to write to
    FILE *file = fopen("quicksort.txt","w");
    
    //for each string in the list...
    for(int curString = 0; curString < length; curString++){
        //add the string to the new file
        fputs(list[curString],file);
        //add a comma between each string
        fputc(',',file);
    }
    
    //close the file after use
    fclose(file);
    
    //tell the user where the contents have been stored
    printf("The sorted contents have been written to: quicksort.txt\n");
}

int main(){
    //get the name of the file from the user
    getTextFileName();
    //get length of text in the file and the number of words
    int length = getLengthTextInFile(nameOfFile);
    //create a list of words, each length 20 characters
    char listOfNames[numOfWords][20];
    //split the text from file into a list of words
    splitFileWordsIntoList(listOfNames,nameOfFile);
    
    //perform a quick sort on the list of words
    quickSortListOfStrings(listOfNames,numOfWords,0,numOfWords+1);
    
    //store the quick sorted list to a new file
    writeContentsToNewFile(listOfNames,numOfWords);
  
}




