package net.thesieutoc;

public enum CardAmount {
    A10k(1, 10000),
    A20k(2, 20000),
    A30k(3, 30000),
    A50K(4, 50000),
    A100k(5, 100000),
    A200k(6, 200000),
    A300k(7, 300000),
    A500k(8, 500000),
    A1000k(9, 1000000);

    private int code;
    private int amount;
    CardAmount(int code, int amount){
        this.code = code;
        this.amount = amount;
    }

    public int getCode() {
        return this.code;
    }

    public int getAmount() {
        return this.amount;
    }

    public static CardAmount getCardAmount(int amount){
        for (CardAmount ca : CardAmount.values()) {
            if(amount == ca.getAmount()){
                return ca;
            }
        }
        return null;
    }
}
