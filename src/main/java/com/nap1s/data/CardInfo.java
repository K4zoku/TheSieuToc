package com.nap1s.data;

public class CardInfo {
    public String transactionID;
    public String type;
    public String serial;
    public String pin;
    public int amount;

    public CardInfo(String transactionID, String type, int amount, String serial, String pin){
        this.transactionID = transactionID;
        this.type = type;
        this.serial = serial;
        this.pin = pin;
        this.amount = amount;
    }
}
