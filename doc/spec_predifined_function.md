# Tp8 - specs

## Length
**Routine**: length  
**Arguments**:  $a0: start address  
**Return value**: $v0 : size of array (int)  
**Description**:  Return the number of elements inside the given array
## int_of_char
**Routine**: char_to_int  
**Arguments**:  $a0: char element (byte)  
**Return value**: $v0 : int value of given char (int)  
**Description**:  Return the int value of the given char
## char_of_int
**Routine**: int_to_char  
**Arguments**:  $a0: int element (int)  
**Return value**: $v0 : char value of the given int (byte)  
**Description**:  Return the char value of the given int  
## input_char
**Routine**: input_char  
**Arguments**:  None  
**Return value**: $v0 : char value of the input (byte)  
**Description**:  Ask the user for an input char (byte)  
## input_int
**Routine**: input_int  
**Arguments**:  None  
**Return value**: $v0 : int value of the input (int)  
**Description**:  Ask the user for an input int (int)  
## input_string
**Routine**: input_string  
**Arguments**:  None  
**Return value**: $v0 : address of the string input  
**Description**:  Ask the user for an input string (address)  
