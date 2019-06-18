package net.thesieutoc;

public enum CardType {
    VIETTEL("Viettel"),
    VINAPHONE("Vinaphone"),
    MOBIFONE("Mobifone");

    private String name;
    CardType(String name){
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
