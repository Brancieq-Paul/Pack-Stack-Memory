# Pack List Features: A smart pack list

<a href="https://discord.gg/KTGfgTGgQX"><img alt="Discord" height="40" src="https://cdn.jsdelivr.net/npm/@intergrav/devins-badges@3/assets/compact/social/discord-plural_vector.svg"></a>

[![Modrinth](https://img.shields.io/modrinth/dt/packlistfeatures?logo=modrinth&color=darkgreen&label=Download%20from%20Modrinth&style=for-the-badge)](https://modrinth.com/mod/packlistfeatures)

IMPORTANT:
- If you find any bugs or want a feature implemented, please make an issue or ask on discord

## Two main features

### Unlocked server packs
Feature inspired by [Server Pack Unlocker](https://modrinth.com/mod/server-pack-unlocker).
Makes the server packs movable in the selected pack list.

### Pack position memory
The mod remember the relative position of your packs for you.
Thus, if you reconnect to a server after several months,
the packs from this server will be replaced correctly between your packs.
#### How does that work ?
The mod does not remember a specific position for each pack, it remembers an index
(an ordered list) that contain all the pack you ever used. This index is updated dynamically
when you update your selected pack list order.

<iframe width="560" height="315" src="https://www.youtube-nocookie.com/embed/whe831Dl6xg" title="YouTube video player" frameborder="0" allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture; web-share" allowfullscreen></iframe>

## Secondary features
The features that are deriving from the main one:
- The packs (server or client) will automatically find their way back to their
  registered position (when joining a server or when enabling a pack from the pack screen)

Independent features:
- Newly incompatible mods (when you update your game and some mods are not compatible anymore) will not be disabled on launch anymore.
- In case of resource loading failure, the mod will try again without incompatible packs before deactivating all other packs if it fails again.
- Fix minecraft vanilla bug [MC-267868](https://bugs.mojang.com/browse/MC-267868)

**Not yet** implemented features:
- Better resource pack identification
- Configuration

## Dev status

- The mod is actually a proof of concept. It should already be stable enough,
  but breaking changes may come in the future.
- The identification is actually pretty bad, it actually uses the pack description to
  differentiate them. I plan to change that in the future.