package net.danh.litesack.Stats;

import io.lumine.mythic.lib.version.VersionMaterial;
import net.Indyuce.mmoitems.stat.type.DoubleStat;

public class Multi extends DoubleStat {
    public Multi() {
        super("MULTI"
                , VersionMaterial.LAPIS_LAZULI.toMaterial()
                , "Multi", new String[]{"Increase item when mine"});
    }
}
