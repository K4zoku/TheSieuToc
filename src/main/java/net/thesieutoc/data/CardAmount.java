package net.thesieutoc.data;

public enum CardAmount {
    _10K(10000, 1),
    _20K(20000, 2),
    _30K(30000, 3),
    _50K(50000, 4),
    _100K(100000, 5),
    _200K(200000, 6),
    _300K(300000, 7),
    _500K(500000, 8),
    _1M(1000000, 9),
    UNKNOWN(0, -1);

    private final int amount;
    private final int id;

    CardAmount (int amount, int id) {
        this.amount = amount;
        this.id = id;
    }

    public static CardAmount getAmount(int amount) {
        for (CardAmount a : values()) {
            if (a.amount == amount) {
                return a;
            }
        }
        return UNKNOWN;
    }

    public int getAmount() {
        return amount;
    }

    public int getId() {
        return id;
    }
}
