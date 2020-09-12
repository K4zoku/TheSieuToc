package net.thecaofast.data;

public class CardInfo {
    public String transactionID;
    public String type;
    public String serial;
    public String pin;
    public int amount;

    public CardInfo(String transactionID, String type, int amount, String serial, String pin) {
        this.transactionID = transactionID;
        this.type = type;
        this.serial = serial;
        this.pin = pin;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return String.format("{\"transactionID\": \"%s\", \"type\": \"%s\", \"serial\": \"%s\", \"pin\": \"%s\", \"amount\": %d}", transactionID, type, serial, pin, amount);
    }
}
