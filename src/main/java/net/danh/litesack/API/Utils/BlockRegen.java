package net.danh.litesack.API.Utils;

import org.bukkit.Material;
import org.bukkit.block.Block;

import java.util.List;
import java.util.Random;

public class BlockRegen {

    public static boolean isEnable() {
        return File.getSetting().getBoolean("BLOCK_REGEN.ENABLE");
    }

    public static boolean isSimpleRegen() {
        return File.getSetting().getBoolean("BLOCK_REGEN.USE_SIMPLE_REGEN");
    }

    public static Material getNextRegen(Block block) {
        if (isEnable() && !isSimpleRegen()) {
            List<String> blocks = File.getSetting().getStringList("BLOCK_REGEN.BLOCKS." + block.getType());
            if (!blocks.isEmpty()) {
                String block_string = blocks.get(new Random().nextInt(blocks.size()));
                if (block_string != null) {
                    Material material = Material.getMaterial(block_string);
                    if (material != null) {
                        if (material.isBlock()) return material;
                    } else {
                        return block.getType();
                    }
                } else {
                    return block.getType();
                }
            } else {
                return block.getType();
            }
            return block.getType();
        } else if (isEnable() && isSimpleRegen()) {
            return block.getType();
        } else return Material.AIR;
    }
}
