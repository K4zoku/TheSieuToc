package me.lxc.thecaofast.internal;

import me.lxc.artxeapi.data.ArtxeYAML;

public class Messages extends IConfiguration {
    public String prefix;
    public String inputSerial;
    public String inputPin;
    public String serial;
    public String pin;
    public String handling;
    public String awaitingApproval;
    public String cancelled;
    public String invalidCardType;
    public String success;
    public String fail;
    public String missingApiInfo;
    public String reloaded;
    public String checked;
    public String systemError;
    public String onlyPlayer;
    public String noPermission;
    public String invalidCommand;
    public String notNumber;
    public String tooFewArgs;
    public String tooManyArgs;
    public String calculating;
    public String emptyTop;
    public String topMessage;
    public String yourTop;
    public String topFormat;
    public String cacheCleared;
    public String given;


    public Messages(ArtxeYAML messagesYml) {
        super(messagesYml);
        load();
    }

    @Override
    public void load() {
        prefix = getString("Prefix", "§a[§cTheCao§bFast§a] &r");
        inputSerial = prefix + getString("Input-Serial", "§aNhập số Serial");
        inputPin = prefix + getString("Input-Pin", "§aNhập mã thẻ");
        serial = prefix + getString("Serial", "§aBạn đã nhập serial: §f\"{Serial}\"");
        pin = prefix + getString("Pin", "§aBạn đã nhập mã thẻ §f\"{Pin}\"");
        handling = prefix + getString("Handling", "§eĐang xử lý, vui lòng đợi...");
        awaitingApproval = prefix + getString("Awaiting-Approval", "&eThẻ của bạn hiện đang chờ duyệt...");
        cancelled = prefix + getString("Cancelled", "§eĐã hủy nạp thẻ");
        invalidCardType = prefix + getString("Invalid-Card-Type", "§cLoại thẻ không hợp lệ");
        success = prefix + getString("Success", "§aNạp thẻ thành công với mệnh giá {amount}₫");
        fail = prefix + getString("Fail", "§cNạp thẻ không thành công!");
        missingApiInfo = prefix + getString("Missing-API-Info", "§cThiếu thông tin API");
        reloaded = prefix + getString("Reloaded", "§aCài đặt đã được nạp lại!");
        checked = prefix + getString("Checked", "§aCác thẻ trong hàng chờ đã được kiểm tra lại!");
        systemError = prefix + getString("System-Error", "§4Có lỗi trong quá trình xử lý!");
        onlyPlayer = prefix + getString("Only-Player", "§4Chỉ có người chơi mới có thể dùng lệnh này");
        noPermission = prefix + getString("No-Permission", "§4Bạn không có quyền dùng lệnh này");
        invalidCommand = prefix + getString("Invalid-Command", "§4Lệnh không xác định");
        notNumber = prefix + getString("Not-Number", "§f\"{0}\" §ckhông phải là số");
        tooFewArgs = prefix + getString("Too-Few-Args", "§cThiếu dữ liệu");
        tooManyArgs = prefix + getString("Too-Many-Agrs", "§cThừa dữ liệu");
        calculating = prefix + getString("Calculating", "§aĐang tính toán...");
        emptyTop = prefix + getString("Empty-Top", "§cHiện chưa có ai nạp thẻ cả!");
        topMessage = prefix + getString("Top-Msg", "§aCó §f§l{Number_Of_Donors} §angười đã nạp và tổng cộng được §6§l{Server_Total}₫§a. Sau đây là top §f{Top} §angười nạp thẻ:");
        yourTop = prefix + getString("Your-Top", "§aTop của bạn là §a§l#{Player_Rank} §avới tổng số tiền là §6§l{Player_Total}₫");
        topFormat = prefix + getString("Top-Format", "§a§l#{Player_Rank}§f§l: §b§l{Player} §7§l- §6§l{Player_Total}₫");
        cacheCleared = prefix + getString("Cache-Cleared", "§aĐã xóa cache");
        given = prefix + getString("Given", "§aĐã cho {Player} {Amount}₫ tiền ảo");
    }

}
