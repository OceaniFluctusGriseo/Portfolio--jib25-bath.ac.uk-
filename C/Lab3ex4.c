#include <stdio.h>
#include <math.h>
#include <string.h>

int SymbolHexInDec(char character)
{
	switch(character)
	{
		case 'A': return 10; break;
		case 'B': return 11; break;
		case 'C': return 12; break;
		case 'D': return 13; break;
		case 'E': return 14; break;
		case 'F': return 15; break;
		default: return (int)character - 48; break;
	}	
}	
 
void HexToDec(char hexString[])
{
	int decimalValue = 0;
	int length = strlen(hexString);
	for(int x = length-1; x >= 0; x--)
	{
		if(hexString[length-(x+1)] != '-')
		{
			decimalValue += pow(16,x) * SymbolHexInDec(hexString[length-(x+1)]);
		} 
	}
	if(hexString[0] == '-'){decimalValue *= -1;}
	printf("%d\n",decimalValue);
}

int main()
{
	HexToDec("FF");
	HexToDec("10");
	HexToDec("ABC");
	HexToDec("C2");
	HexToDec("-AB");
	
	return 0;
}