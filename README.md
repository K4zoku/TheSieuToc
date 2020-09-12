# TheCaoFast
> A minecraft plugin that allows players donate via TheCaoFast APIV4.

[![Header Image](https://thecaofast.net/assets/image/logone.png)](https://thecaofast.net/assets/image/logone.png)

## Installation
Download this plugin in [releases][releases] page and put it in `SERVER_ROOT/plugins`

## Configuration
[settings/general.yml][settings-general]
```yaml
# /!\ Không thay đổi giá trị này
Config-Version: 1.0.0

########################################################################
#                  | Version: 1.0.0 | Author: LXC |                    #
#               | Copyright (c) 2020-2020 TheCaoFast |                 #
########################################################################

# /!\ Đừng bật nếu bạn không phải là nhà phát triển
Debug: false

# [?] Cài đặt cache
Cache:
  # [?] Thời gian tồn tại của cache, set càng cao thì càng mượt và ngược lại :))
  TTL: 5m

# /!\ Bắt buộc phải điền, không điền thì chạy bằng niềm tin à? (╯°□°）╯︵ ┻━┻
# [?] Bạn có thể lấy API key và API Secret tại https://thesieutoc.net/tich-hop-nap-the.html
TheCaoFast:
  API-Key: ''
  API-Secret: ''

# [?] Chu kỳ kiểm tra card, những card nào khi gửi đi mà ko nhận dc response ngay thì vào hàng chờ
# hàng chờ này sẽ được kiểm tra vào mỗi 10s (mặc định)
# Fact: Bạn có thể dùng các loại đơn vị như tick (không cần ghi gì sau số), milisecond (ms),
#       second (s), minute (m), hour (h), day (d)
Card-Check-Period: 1m

# /!\ Phải nhập đúng tên thẻ có trên https://thesieutoc.net, còn không thì cứ để mặc định
# Fact: Không thích loại thẻ nào thì có thể bỏ bớt
Card-Enabled:
  - 'Viettel'
  - 'Vinaphone'
  - 'Mobifone'
  - 'Vietnamobile'
  - 'Vcoin'
  - 'Zing'
  - 'Gate'

# [?] Placeholder có thể dùng:
#         ● {Player}: Tên người chơi
#         ● {Amount}: Số tiền người chơi nạp
#         ● {Player_Rank}: Thứ hạng nạp thẻ của người chơi
#         ● {Player_Total}: Tổng cộng số tiền đã nạp của người chơi
#         ● {Total}: Tổng cộng số tiền mà server đã nhận được
# [?] Có 3 loại đối tượng chạy command:
#         ● player: chạy dưới quyền có sẵn của người chơi đó
#         ● op: vẫn chạy dưới dạng người chơi nhưng có quyền của op
#         ● console: chạy command trên console
Card-Reward:
  10000:
    - 'player:me vừa cống hiến 10k cho server!!'
    - 'op:p give {player} 10';
    - 'console:broadcast {player} vừa donate 10k'
  ...
```

## Release History

* 1.0.0
    * Copy all feature from TheSieuToc (2.0.5)
    
## Contact me

LXC – [me.takahatashun][facebook] – [@ztglxc][twitter] – LXC#1848

<!-- Markdown link & img dfn's -->
[releases]: https://github.com/takahatashun/TheSieuToc/releases/latest
[settings-general]: https://github.com/takahatashun/TheSieuToc/blob/master/src/main/resources/settings/general.yml
[languages-messages]: https://github.com/takahatashun/TheSieuToc/blob/master/src/main/resources/languages/messages.yml
[ui-chat]: https://github.com/takahatashun/TheSieuToc/blob/master/src/main/resources/ui/chat.yml
[facebook]: https://www.facebook.com/100022162512692
[twitter]: https://twitter.com/i/user/962282900031877120
