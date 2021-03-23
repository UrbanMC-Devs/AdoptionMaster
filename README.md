# AdoptionMaster
An unofficial extension plugin of MarriageMaster that allows you to adopt players!

## Dependencies
Requires **Spigot 1.16** (or a fork) and [MarriageMaster](https://github.com/GeorgH93/MarriageMaster) v2.2.14 or higher.

## Installing
This plugin can be installed from the [releases](https://github.com/UrbanMC-Devs/AdoptionMaster/releases/latest) page.

## Building
This is a simple maven project and can be built via `mvn clean package`.

## Commands and Permissions
| Command | Description | Permission |
| :---: | :--- | :---: |
| `adopt` | Adopt a child. Right click on a player to adopt them! | `adoption.adopt` |
| `family` | Display your family (parents and children). | `adoption.family` |
| `disown [player]` | Disown a child! | `adoption.disown` |
| `runaway` | Runaway from your family (parents)! | `adoption.runaway` |
| `adoptreload` | Reload AdoptionMaster data. Meant for admins. | `adoption.reload` |

### Permission Groups:
| Permission | Description | Child Perms |
| :---: | :--- | :-- |
| `adoption.default` | Default permissions for AdoptionMaster | `adoption.adopt`, `adoption.family`, `adoption.disown`, `adoption.runaway` |
| `adoption.*` | All permissions for AdoptionMaster | `adoption.default`, `adoption.reload`  |

### Developed by Silverwolfg11 
