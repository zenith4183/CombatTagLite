# CombatTagLite

*CombatTagLite is the last plugin you'll ever need to stop those dirty combat loggers!*

## Features

* Instantly kill player when they log off in combat
* Safe Logout that optionally lets tagged player wait a timer without moving to safely log off
* Disallow flying while player is in combat
* Disallow block editing while player is in combat
* Disallow enderpearls while player is in combat
* Disallow teleporting while player is in combat
* Broadcast a PVP kill/death message
* Disable combat tagging for specific worlds
* Deny specific commands while player is in combat
* Disallow running back to safety while in combat (can also render a forcefield)
* Supports WorldGuard PVP deny flag (WorldGuard 7.0 supported)
* Works with BarAPI to show a boss bar counting down with the player's remaining combat timer

## Installation

1. Obtain the latest version of CombatTagLite from the [build server](https://drone.io/github.com/zenith4183/CombatTagLite/files).
2. Copy or move **CombatTagLite.jar** into your server's *plugins* folder.
3. Start the server. _This creates a new file **plugins/CombatTagLite/config.yml**_
4. Edit the newly created configuration file with desired behavior.
5. If you made any changes, run the command **/ctreload** from console or as an operator.

## Permissions

| **Permission**           | **Description**                            | **Default** |
| -------------------------| ------------------------------------------ | ----------- |
| ctlite.reload            | Reload CombatTagLite configuration         | operator    |
| ctlite.bypass.tag        | Bypass combat tagging                      | operator    |
| ctlite.bypass.command    | Bypass command restrictions in combat      | operator    |
| ctlite.bypass.blockedit  | Bypass block edit restrictions in combat   | operator    |
| ctlite.bypass.enderpearl | Bypass enderpearl restrictions in combat   | operator    |
| ctlite.bypass.flying     | Bypass flying restriction in combat        | operator    |
| ctlite.bypass.teleport   | Bypass teleportation restriction in combat | operator    |
| ctlite.check             | Check remaining combat timer               | everyone    |
| ctlite.logout            | Initiate a safe logout                     | everyone    |
| ctlite.notify.kill       | Receive kill/death message broadcast       | everyone    |

## Contributing

* 4-space indentation
* UNIX line endings
* Braces on the same line

Thanks :)

## License

CombatTagLite is licensed to you freely under the terms of the [LGPL license](https://www.gnu.org/licenses/lgpl.html).

