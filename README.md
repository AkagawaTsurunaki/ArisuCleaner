# Arisu Cleaner

![Static Badge](https://img.shields.io/badge/Minecraft-1.21.5-gren) ![Static Badge](https://img.shields.io/badge/Fabric-blue) ![Static Badge](https://img.shields.io/badge/ServerOnly-orange)

定时清理 Minecraft 服务器中的各维度的物品实体（Item Entity），并在清理前通知所有玩家。

## 使用方法

```
/clearItems <clearTicks> [<tipsTicks>]
```

`clearTicks`：每多少 Ticks 执行一次清理。必须为大于等于 1 的正整数。

`tipTicks`：（可选）在执行前多少 Ticks 告知全服的玩家。必须小于 `clearTicks`。如果不填写此参数，默认为 `clearTicks` 的 10%，最小为 1 Tick。

## 示例

每 600 Ticks 清理各维度的物品实体，清理前 100 Ticks 告知所有玩家。
```
/clearItems 600 100
```

## 注意

定时任务是基于服务器 Ticks 的，因此如果修改了服务器的 TickRate，机器人的执行频率也会受到影响。
默认情况下，服务器的 TickRate 是 20，即每 20 Ticks 为现实世界中的 1 秒。

## Contact with me

Email: AkagawaTsurunaki@outlook.com

Github: AkagawaTsurunaki