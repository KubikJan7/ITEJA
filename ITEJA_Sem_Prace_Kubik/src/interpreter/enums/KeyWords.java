package interpreter.enums;

public enum KeyWords {
    PUT("put"), REMOVE("remove"), LIQUEFY("liquefy"), MIX("mix"), FOR("for"), 
    POUR("pour"), CLEAN("clean"), END("end");

    private final String jmeno;

    private KeyWords(String name) {
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
