# ITEJA
## Term work description
This project is focused on creating an interpreter of a self-made language called Gourmet.

## Gourmet
Gourmet is a programming language in which programs look like recipes. 

### Program structure
* Title
* Comments
* Ingredients
  * Value, type and name of variables that will be used
  * Type 'g' stands for 'char' and 'ml' or nothing for 'int'
* Equipment
  * Name of the stack that will store values
* Method
  * Program body
* Serves
  * Prints the content of used stacks
  * Contains a number of how many times the result will be printed

### Instructions
  * Put ingredient into the [equipment].
    * Push the value of the variable on the top of the bowl (stack)
  * For each [ingredient] in the [equipment].
    * Start of loop for variables inside the specific bowl
  * For each ingredient end.
    * End of loop
  * Remove ingredient from the [equipment].
    * Pop the value of the top ingredient in the bowl
  * Liquefy contents of the [equipment].
    * Change the type of all variables in the specified bowl to integer
  * Solidify contents of the [equipment].
    * Change the type of all variables in the specified bowl to character
  * Combine contents of the [equipment].
    * Sum the values of all variables in the specified bowl
  * Pour contents of the [equipment] into the baking dish.
    * Copy the elements from the bowl to the baking dish
    * Baking dish represents a stack that will be printed