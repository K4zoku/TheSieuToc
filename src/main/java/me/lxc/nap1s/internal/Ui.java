package me.lxc.nap1s.internal;

import me.lxc.artxeapi.data.ArtxeYAML;

import java.util.Arrays;
import java.util.List;

public class Ui extends IConfiguration {

    public List<String> cancel;

    public String cardTypeText;
    public List<String> cardTypeHover;

    public String cardAmountText;
    public List<String> cardAmountHover;

    public Ui(ArtxeYAML yaml) {
        super(yaml);
    }

    @Override
    public void load() {
        cancel = getStringList("Cancel", Arrays.asList("Cancel", "Exit", "Hủy", "Huỷ", "Huy"));
        cardTypeText = getString("Choose-Card-Type.Text", "§b§l{Card_Type}");
        cardTypeHover = getStringList("Choose-Card-Type.Hover", Arrays.asList("§b§lNạp thẻ {Card_Type}", "§aClick vào để chọn mệnh giá"));
        cardAmountText = getString("Choose-Card-Amount.Text", "§a{Card_Amount}");
        cardAmountHover = getStringList("Choose-Card-Amount.Hover", Arrays.asList("§b§lMệnh giá {Card_Amount}₫", "§aClick vào để bắt đầu nạp"));
    }
}
