# Prevent Wasted Uses
A small QoL addon for the Better Than Wolves CE 3.0.0 Minecraft mod.

## Overview

Prevent Wasted Uses is a quality-of-life addon that prevents durability damage to an item if it was not helpful.  

## Impetus
Needing to swap between inventory slots constantly to avoid wasting durability is pure tedium.  It adds no meaningful challenge, and it feels bad.  Let's not keep that anti-feature.

## Difference of Opinion
FlowerChild (the original author of Better Than Wolves) purposely made weapons take _more_ damage when used on blocks.  I don't find this necessary, practical, or fun.

## Features

* The addon checks if a tool or weapon is actually effective at breaking, harvesting, or converting the target block.
* Tools only take damage if one of these conditions is true:
  * The tool has higher mining efficiency than bare hands
  * The tool is marked as efficient vs. that block type
  * The tool can harvest that block
  * The tool can convert that block (e.g., loose dirt to tilled soil)
* If none of these apply, no damage is dealt—you can freely use any tool without penalty.
* Does not affect any other item damage situations.  For example, fishing is unchanged.

---

## Installation

1. Install Better Than Worlds: Community Edition 3.0.0 + Legacy Fabric by following the instructions on the [wiki](https://wiki.btwce.com/view/3.0.0_Beta).
2. Download this addon's JAR file from the Releases page.
3. Place the addon JAR file in your `.minecraft/mods` folder.
4. Launch Minecraft. Tools will now only take damage when actually useful.

---

## Compatibility

* **Required**: Better Than Worlds CE 3.0.0
* **Mod Loader**: Fabric/Mixin based (Packaged with the BTW Instance)
* Designed to work with existing tools and potentially those added by addons.
* No known conflicts at this time.

---

## License

This project is released under the [BSD Zero-Clause License](LICENSE).
You're free to use, modify, and share it however you see fit.

---

## Credits

* **Addon author**: Abigail Read
* **Better Than Worlds**: Created by *FlowerChild*, continued by the BTW Community
* Thanks to the **Legacy Fabric team** for keeping classic modding alive.

---

*"Purposes are deduced from behaviour, not from rhetoric or stated goals."* &ensp;– Donella Meadows
</br><small>
[wikiquote](https://en.wikiquote.org/wiki/Donella_Meadows)
</small>