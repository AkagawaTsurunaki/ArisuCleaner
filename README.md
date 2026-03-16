# Arisu Cleaner

![Static Badge](https://img.shields.io/badge/Minecraft-1.21.5-blue) ![Static Badge](https://img.shields.io/badge/Fabric-0.16.13-blue) ![Static Badge](https://img.shields.io/badge/ver-1.0.0-blue) ![Static Badge](https://img.shields.io/badge/ServerOnly-orange)

定时清理 Minecraft 服务器中的各维度的物品实体（Item Entity）和有生命实体（Living Entity），并在清理前通知所有玩家。

**支持的 Minecraft 版本**：`1.21.4`、`1.21.5`

## 使用方法

### 清理掉落物

```
/arisucleaner clearItems <clearTicks> [<tipsTicks>]
```

**参数：**

`clearTicks`：每多少 Ticks 执行一次清理。必须为大于等于 1 的正整数。默认为 12000 Ticks（10 分钟）。

`tipTicks`：（可选）在执行前多少 Ticks 告知全服的玩家。必须小于 `clearTicks`。如果不填写此参数，默认为 `clearTicks` 的 10%，最小为 1 Tick。

**示例：**

每 600 Ticks 清理各维度的物品实体，清理前 100 Ticks 告知所有玩家。

```
/arisucleaner clearItems 600 100
```

### 清理有生命实体

当各维度中的有生命实体总数达到指定上限，将按指定比例清理占比最高的那一类实体。

```
/arisucleaner clearEntities <executeTicks> [<maxEntities>] [<removeRatio>]
```

**参数：**

`executeTicks`：每多少 Ticks **尝试**一次清理。必须为大于等于 1 的正整数。默认为 6000 Ticks（5分钟）。

`maxEntities`：（可选）当所有维度中的有生命实体达到多少时，执行清理。如果不填写此参数，默认为 3000。

`removeRatio`：（可选）当执行清理时，以多大的比例对该类实体执行清理。如果不填写此参数，默认为 0.8。

**示例：**

每 6000 Ticks 尝试清理各维度中的有生命实体，当有生命实体总数达到 3000 时，找到占比最多的那类有生命实体，对这类实体按 80% 的比例执行清除。

```
/arisucleaner clearEntities 6000 3000 0.8
```

## 注意

定时任务是基于服务器 Ticks 的，因此如果修改了服务器的 TickRate，机器人的执行频率也会受到影响。
默认情况下，服务器的 TickRate 是 20，即每 20 Ticks 为现实世界中的 1 秒。

在 `1.1.0` 以上的版本，你需要使用 `/arisucleaner` 前缀，这是为了防止与其他模组的指令存在冲突。

## Contact with me

Bilibili: [赤川鹤鸣_Channel](https://space.bilibili.com/1076299680)

Email: [AkagawaTsurunaki@outlook.com](mailto:AkagawaTsurunaki@outlook.com)

Github: [AkagawaTsurunaki](https://github.com/AkagawaTsurunaki)