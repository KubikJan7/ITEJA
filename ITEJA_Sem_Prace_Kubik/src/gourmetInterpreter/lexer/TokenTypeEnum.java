package gourmetInterpreter.lexer;

public enum TokenTypeEnum {

    //KEYWORDS
    PUT("put"), //push the value of the variable on the top of the bowl
    INTO("into"), //used to identify the bowl (stack)
    REMOVE("remove"), //remove the value of the variable from the top element of the bowl
    FROM("from"), //used to identify the bowl (stack)
    LIQUEFY("liquefy"), //change the type of the variable to liquid (integer)
    SOLIDIFY("solidify"), //change the type of the variable to dry substance (character)
    OF("of"),// used to identify the bowl (stack)
    POUR("pour"), // copy the elements from the bowl to the dish, preserving their order and putting them on top of the elements which have already been in the dish
    CONTENTS("contents"),
    COMBINE("combine"), //will sum all the ingredient of the specified bowl
    CLEAN("clean"), // delete all elements of the bowl
    FOR("for"), // for cycle
    EACH("each"),
    IN("in"), // used to identify the bowl (stack)
    END("end"), // end of for cyclus
    THE("the"),
    INGREDIENTS("ingredients"), // name of the block where all ingredients (variables) will be declared and initialized
    EQUIPMENT("equipment"), //name of the block where all bowls (stacks) will be declared
    METHOD("method"), // name of the block where the main logic of the program will be contained
    BAKING("baking"), // baking dishes are used only to print the result.
    DISH("dish"),
    SERVES("serves"), // prints the content of the of the baking dish. The elements in each dish are printed from top of the stack to the bottom
    EOL("."), //end of line

    ML("ml"), //mililitres => integer
    G("g"), //grams => character
    //if not specified it's consider as number

    TITLE("title"), //name of the recipe
    COMMENT("comment"), //comment after the title
    INTEGER("int"),
    CHAR("char"),
    VARIABLE("variable"), //names of ingredients and bowls

    NONE(""),
    ASSIGNMENT(""),
    DECLARATION(""),
    OPERATION(""),
    BODY("");
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

    public static boolean isKeyword(String string) {
        return string.equals(PUT.toString()) || string.equals(INTO.toString())
                || string.equals(REMOVE.toString()) || string.equals(FROM.toString())
                || string.equals(LIQUEFY.toString()) || string.equals(SOLIDIFY.toString())
                || string.equals(OF.toString()) || string.equals(POUR.toString())
                || string.equals(CONTENTS.toString()) || string.equals(COMBINE.toString())
                || string.equals(CLEAN.toString()) || string.equals(FOR.toString())
                || string.equals(EACH.toString()) || string.equals(IN.toString())
                || string.equals(END.toString()) || string.equals(THE.toString())
                || string.equals(INGREDIENTS.toString()) || string.equals(METHOD.toString())
                || string.equals(EQUIPMENT.toString()) || string.equals(BAKING.toString())
                || string.equals(DISH.toString()) || string.equals(SERVES.toString())
                || string.equals(ML.toString()) || string.equals(G.toString())
                || string.equals(EOL.toString());
    }

    public static TokenTypeEnum getKeyword(String string) {
        switch (string) {
            case "put":
                return PUT;
            case "into":
                return INTO;
            case "remove":
                return REMOVE;
            case "from":
                return FROM;
            case "liquefy":
                return LIQUEFY;
            case "solidify":
                return SOLIDIFY;
            case "of":
                return OF;
            case "pour":
                return POUR;
            case "contents":
                return CONTENTS;
            case "combine":
                return COMBINE;
            case "clean":
                return CLEAN;
            case "for":
                return FOR;
            case "each":
                return EACH;
            case "in":
                return IN;
            case "end":
                return END;
            case "the":
                return THE;
            case "ingredients":
                return INGREDIENTS;
            case "method":
                return METHOD;
            case "equipment":
                return EQUIPMENT;
            case "baking":
                return BAKING;
            case "dish":
                return DISH;
            case "serves":
                return SERVES;
            case "ml":
                return ML;
            case "g":
                return G;
            case ".":
                return EOL;
            default:
                return null;
        }
    }
}
