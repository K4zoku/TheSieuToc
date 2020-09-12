# TheSieuToc
> A minecraft plugin that allows players donate via TheSieuTocAPI.

[![Quality Gate Status](https://sonarcloud.io/api/project_badges/measure?project=TheSieuToc&metric=alert_status)](https://sonarcloud.io/dashboard?id=TheSieuToc)
[![Maintainability Rating](https://sonarcloud.io/api/project_badges/measure?project=TheSieuToc&metric=sqale_rating)](https://sonarcloud.io/dashboard?id=TheSieuToc)
[![Reliability Rating](https://sonarcloud.io/api/project_badges/measure?project=TheSieuToc&metric=reliability_rating)](https://sonarcloud.io/dashboard?id=TheSieuToc)
[![Security Rating](https://sonarcloud.io/api/project_badges/measure?project=TheSieuToc&metric=security_rating)](https://sonarcloud.io/dashboard?id=TheSieuToc)
[![Lines of Code](https://sonarcloud.io/api/project_badges/measure?project=TheSieuToc&metric=ncloc)](https://sonarcloud.io/dashboard?id=TheSieuToc)

[![Header Image](header.png)](header.png)

## Installation
Download this plugin in [releases][releases] page and put it in `SERVER_ROOT/plugins`

## Configuration
[settings/general.yml][settings-general]
```yaml
# /!\ Không thay đổi giá trị này
Config-Version: 2.0.5

########################################################################
#       ________            _____ _               ______               #
#      /_  __/ /_  ___     / ___/(____  __  __   /_  ______  _____     #
#       / / / __ \/ _ \    \__ \/ / _ \/ / / /    / / / __ \/ ___/     #
#      / / / / / /  __/   ___/ / /  __/ /_/ /    / / / /_/ / /__       #
#     /_/ /_/ /_/\___/   /____/_/\___/\__._/    /_/  \____/\___/       #
#                  | Version: 2.0.5 | Author: LXC |                    #
#               | Copyright (c) 2018-2020 TheSieuToc |                 #
########################################################################

# /!\ Đừng bật nếu bạn không phải là nhà phát triển
Debug: false

# [?] Cài đặt cache
Cache:
  # [?] Thời gian tồn tại của cache, set càng cao thì càng mượt và ngược lại :))
  TTL: 5m

# /!\ Bắt buộc phải điền, không điền thì chạy bằng niềm tin à? (╯°□°）╯︵ ┻━┻
# [?] Bạn có thể lấy API key và API Secret tại https://thesieutoc.net/tich-hop-nap-the.html
TheSieuToc:
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

* ~~1.0.0~~
    * ~~Complete plugin for first time yay~~
* ~~1.0.1~~
    * ~~Not found?~~
* ~~1.0.2~~
    * ~~Not found?~~
* 1.0.3
    * Optimize code
* 2.0.0
    * Recode all?
* 2.0.1
    * Optimized task
* 2.0.2
    * Fixed bugs
    * Added more bugs to fix later
* 2.0.3
    * Optimize
* 2.0.4
    * Fixed logs/top bugs
* 2.0.5
    * Optimize code
    * Add command `napthe.admin.give | /donate give {player} {amount} [{note}..]`
    * Add in-memory log cache
    * Add command `napthe.admin.cache.clear | /donate clear-cache`
    
## Contact me

Takahata Shun – [me.takahatashun][facebook] – [@ztglxc][twitter] – LXC#2324

<!-- Markdown link & img dfn's -->
[releases]: https://github.com/takahatashun/TheSieuToc/releases/latest
[settings-general]: https://github.com/takahatashun/TheSieuToc/blob/master/src/main/resources/settings/general.yml
[languages-messages]: https://github.com/takahatashun/TheSieuToc/blob/master/src/main/resources/languages/messages.yml
[ui-chat]: https://github.com/takahatashun/TheSieuToc/blob/master/src/main/resources/ui/chat.yml
[facebook]: https://www.facebook.com/100022162512692
[twitter]: https://twitter.com/i/user/962282900031877120