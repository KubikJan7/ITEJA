package interpreter;

public enum TokenTypeEnum {

    //KEYWORDS
    PUT("put"), //push the value of the variable on the top of the bowl
    INTO("into"), //used to identify the bowl (stack)
    REMOVE("remove"), //remove the value of the variable from the top element of the bowl
    FROM("from"), //used to identify the bowl (stack)
    LIQUEFY("liquefy"), //change the type of the variable to liquid (characters)
    SOLIDIFY("solidify"), //change the type of the variable to dry substance (integer)
    OF("of"),// used to identify the bowl (stack)
    POUR_CONTENTS("pour contents"), // copy the elements from the bowl to the dish, preserving their order and putting them on top of the elements which have already been in the dish.
    CLEAN("clean"), // delete all elements of the bowl
    FOR("for"), // for cycle
    IN("in"), // used to identify the bowl (stack)
    END("end"), // end of for cyclus
    INGREDIENTS("ingredients"), // name of the block where all ingredients (variables) will be declared and initialized
    METHOD("method"), // name of the block where the main logic of the program will be contained
    BAKING_DISH("baking_dish"), // baking dishes are used only to print the result.
    SERVES("serves"), // prints the content of the of the baking dish. The elements in each dish are printed from top of the stack to the bottom

    //DATA TYPES
    ML("ml"), //mililitres => character
    G("g"), //grams => number
    //if not specified it's consider as number

    
    INTEGER(""),
    VAR(""),
    EOL("."); //end of line

    private final String jmeno;

    private TokenTypeEnum(String name) {
        this.jmeno = name;
    }

    public String getName() {
        return jmeno;
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
