package me.lxc.thesieutoc.internal;

import me.lxc.artxeapi.data.ArtxeYAML;

public class Messages extends IConfiguration {
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
        inputSerial = getString("Input-Serial", "§aNhập số Serial");
        inputPin = getString("Input-Pin", "§aNhập mã thẻ");
        serial = getString("Serial", "§aBạn đã nhập serial: §f\"{Serial}\"");
        pin = getString("Pin", "§aBạn đã nhập mã thẻ §f\"{Pin}\"");
        handling = getString("Handling", "§eĐang xử lý, vui lòng đợi...");
        awaitingApproval = getString("Awaiting-Approval", "&eThẻ của bạn hiện đang chờ duyệt...");
        cancelled = getString("Cancelled", "§eĐã hủy nạp thẻ");
        invalidCardType = getString("Invalid-Card-Type", "§cLoại thẻ không hợp lệ");
        success = getString("Success", "§aNạp thẻ thành công với mệnh giá {amount}₫");
        fail = getString("Fail", "§cNạp thẻ không thành công!");
        missingApiInfo = getString("Missing-API-Info", "§cThiếu thông tin API");
        reloaded = getString("Reloaded", "§aCài đặt đã được nạp lại!");
        checked = getString("Checked", "§aCác thẻ trong hàng chờ đã được kiểm tra lại!");
        systemError = getString("System-Error", "§4Có lỗi trong quá trình xử lý!");
        onlyPlayer = getString("Only-Player", "§4Chỉ có người chơi mới có thể dùng lệnh này");
        noPermission = getString("No-Permission", "§4Bạn không có quyền dùng lệnh này");
        invalidCommand = getString("Invalid-Command", "§4Lệnh không xác định");
        notNumber = getString("Not-Number", "§f\"{0}\" §ckhông phải là số");
        tooFewArgs = getString("Too-Few-Args", "§cThiếu dữ liệu");
        tooManyArgs = getString("Too-Many-Agrs", "§cThừa dữ liệu");
        calculating = getString("Calculating", "§aĐang tính toán...");
        emptyTop = getString("Empty-Top", "§cHiện chưa có ai nạp thẻ cả!");
        topMessage = getString("Top-Msg", "§aCó §f§l{Number_Of_Donors} §angười đã nạp và tổng cộng được §6§l{Server_Total}₫§a. Sau đây là top §f{Top} §angười nạp thẻ:");
        yourTop = getString("Your-Top", "§aTop của bạn là §a§l#{Player_Rank} §avới tổng số tiền là §6§l{Player_Total}₫");
        topFormat = getString("Top-Format", "§a§l#{Player_Rank}§f§l: §b§l{Player} §7§l- §6§l{Player_Total}₫");
        cacheCleared = getString("Cache-Cleared", "§aĐã xóa cache");
        given = getString("Given", "§aĐã cho {Player} {Amount}₫ tiền ảo");
    }

}
