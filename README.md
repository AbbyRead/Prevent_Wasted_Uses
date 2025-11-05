# Prevent Wasted Uses

A quality-of-life addon for Better Than Wolves Community Edition 3.0.0 that prevents tools from taking durability damage when they're not actually useful.

## Overview

Prevent Wasted Uses is a quality-of-life addon that prevents durability damage to an item if it was not helpful in breaking, harvesting, or converting a block. It tracks tool effectiveness in real-time and only applies damage when the tool provides a meaningful advantage over bare hands.

## Impetus

Needing to swap between inventory slots constantly to avoid wasting durability is pure tedium. It adds no meaningful challenge, and it feels bad. Let's not keep that anti-feature.

## Difference of Opinion

FlowerChild (the original author of Better Than Wolves) purposely made weapons take more damage when used on blocks. I don't find this necessary, practical, or fun. This addon removes that penalty.

## Features

### Smart Damage Prevention

The addon checks if a tool or weapon is actually effective at breaking, harvesting, or converting the target block before applying durability damage.

**Tools only take damage if one of these conditions is true:**
- The tool has higher mining efficiency than bare hands
- The tool is marked as efficient versus that block type
- The tool can harvest that block normally
- The tool can convert that block (e.g., loose dirt to firm dirt, grass to sparse grass)
- The block was just converted by the tool (tracking conversion success within 100ms)

**If none of these apply, no damage is dealt**—you can freely hold the tool, to no benefit, but without penalty.

---

## Installation

1. Install Better Than Wolves: Community Edition 3.0.0 + Legacy Fabric by following the instructions on the [wiki](https://wiki.btwce.com/view/3.0.0_Beta)
2. Download this addon's JAR file from the Releases page
3. Place the addon JAR file in your `.minecraft/mods` folder
4. Launch Minecraft. Tools will now only take damage when actually useful

## Compatibility

- **Required**: Better Than Wolves CE 3.0.0
- **Mod Loader**: Fabric/Mixin based (Packaged with the BTW Instance)
- Designed to work with existing tools and potentially those added by addons
- Works well with "Slightly Better Terrible Tools" and **should** work well with other tool-enhancement addons
- No known conflicts at this time

## Technical Details

### Damage Decision Logic

The addon evaluates five criteria to determine whether a tool is genuinely useful:

1. **Efficiency comparison**: Tool speed vs. bare hands (1.0x baseline)
2. **Efficient flag**: Whether the tool is marked as efficient for that block type
3. **Harvest capability**: Whether the tool can harvest the block
4. **Convert capability**: Whether the tool can perform a block conversion
5. **Recent conversion**: Whether the tool just successfully converted a block

False positives are filtered out—rock and log materials never trigger conversion flags, preventing stone tools from dealing damage for "converting" stone or axes from dealing damage for "converting" logs.

### Conversion Flag System

Conversion success is tracked via a weak hash map with a 100ms time-to-live. This prevents the edge case where a tool might take damage for breaking a block immediately after converting it, while still allowing legitimate damage application for most scenarios.

### Debug Mode

For addon developers making forks or troubleshooting, set `DEBUG = true` in `UsefulnessHelper.java` to log detailed efficiency calculations and damage decisions to the console.

---

## License

This project is released under the [BSD Zero-Clause License](LICENSE). You're free to use, modify, and share it however you see fit.

---

## Credits

- **Addon author**: Abigail Read
- **Better Than Wolves**: Created by *FlowerChild*, continued by the BTW Community
- Thanks to the **Legacy Fabric team** for keeping classic modding alive

---

*"Purposes are deduced from behaviour, not from rhetoric or stated goals."* &ensp;– Donella Meadows
</br><small>
[wikiquote](https://en.wikiquote.org/wiki/Donella_Meadows)
</small>
