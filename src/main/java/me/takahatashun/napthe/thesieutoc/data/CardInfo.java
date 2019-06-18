package me.takahatashun.napthe.thesieutoc.data;

public class CardInfo {

    public String transactionID;
    public String type;
    public String serial;
    public String pin;
    public Integer amount;

    public CardInfo(String transactionID, String type, String serial, String pin, Integer amount){
        this.transactionID = transactionID;
        this.type = type;
        this.serial = serial;
        this.pin = pin;
        this.amount = amount;
    }

}
