# Arisu Cleaner

![Static Badge](https://img.shields.io/badge/Minecraft-1.21.5-blue) ![Static Badge](https://img.shields.io/badge/Fabric-0.16.13-blue) ![Static Badge](https://img.shields.io/badge/ver-1.0.0-blue) ![Static Badge](https://img.shields.io/badge/ServerOnly-orange)

Periodically clear item entities in all dimensions of the Minecraft Server and notify all players beforehand.

## Usage

```plaintext
/clearItems <clearTicks> [<tipsTicks>]
```

`clearTicks`: The interval in Ticks at which the cleanup will be executed. It must be a positive integer greater than or equal to 1.

`tipTicks`: (Optional) The number of Ticks before the cleanup to notify all players on the server. It must be less than `clearTicks`. If this parameter is not specified, it defaults to 10% of `clearTicks`, with a minimum of 1 Tick.

## Example

Clear item entities in all dimensions every 600 Ticks, and notify all players 100 Ticks before the cleanup.
```plaintext
/clearItems 600 100
```

## Notes

The scheduled task is based on server Ticks. Therefore, if the server's TickRate is modified, the execution frequency of the task will also be affected.
By default, the server's TickRate is 20, meaning that 20 Ticks are equivalent to 1 second in real time.