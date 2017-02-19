/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2016 socraticphoenix@gmail.com
 * Copyright (c) 2016 contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software and
 * associated documentation files (the "Software"), to deal in the Software without restriction,
 * including without limitation the rights to use, copy, modify, merge, publish, distribute,
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT
 * NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND
 * NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM,
 * DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package com.gmail.socraticphoenix.forge.randore.util;

import com.gmail.socraticphoenix.forge.randore.component.CraftableType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TranslationGenerator {

    public static void main(String[] args) throws IOException {
        final String lang = "en_US"; //The language and country the translations are for
        final String craftingTab = "Randores Crafting"; //The name of the randores crafting tab
        final String blocksTab = "Randores Blocks"; //The name of the randores blocks tab
        final String materialsTab = "Randores Materials"; //The name of the randores materials tab
        final String sticksTab = "Randores Sticks"; //The name of the randores sticks tab
        final String swordsTab = "Randores Swords"; //The name of the randores swords tab
        final String hoesTab = "Randores Hoes"; //The name of the randores hoes tab
        final String axesTab = "Randores Axes"; //The name of the randores axes tab
        final String spadesTab = "Randores Shovels"; //The name of the randores shovels tab
        final String pickaxeTab = "Randores Pickaxes"; //The name of the randores pickaxes tab
        final String armorTab = "Randores Armor"; //The name of the randores armor tab
        final String forge = "Craftinium Forge"; //The name of the craftinium forge
        final String table = "Craftinium Table"; //The name of the craftinium table
        final String cOre = "Craftinium Ore"; //The name of the craftinium ore
        final String cLump = "Craftinium Lump"; //The name of the craftinium lump
        final String ore = "Ore";
        final String bricks = "Bricks";
        final String material = "Material";
        final String axe = "Axe";
        final String hoe = "Hoe";
        final String pick = "Pickaxe";
        final String shovel = "Shovel";
        final String sword = "Sword";
        final String stick = "Stick";
        final String boots = "Boots";
        final String chest = "Chestplate";
        final String helm = "Helmet";
        final String pants = "Leggings";


        Map<CraftableType, String> typeMap = new HashMap<CraftableType, String>() {{
            put(CraftableType.AXE, axe);
            put(CraftableType.HOE, hoe);
            put(CraftableType.PICKAXE, pick);
            put(CraftableType.SHOVEL, shovel);
            put(CraftableType.SWORD, sword);
            put(CraftableType.STICK, stick);
            put(CraftableType.BOOTS, boots);
            put(CraftableType.CHESTPLATE, chest);
            put(CraftableType.HELMET, helm);
            put(CraftableType.LEGGINGS, pants);
        }};

        File translations = new File(lang + ".lang");
        FileWriter writer = new FileWriter(translations);
        String[] tabKey = new String[]{"crafting", "blocks", "materials", "sticks", "swords", "hoes", "axes", "spades", "pickaxes", "armor"};
        String[] tabs = new String[]{craftingTab, blocksTab, materialsTab, sticksTab, swordsTab, hoesTab, axesTab, spadesTab, pickaxeTab, armorTab};
        String[] defTileKey = new String[]{"forge", "table", "ore"};
        String[] defTile = new String[]{forge, table, cOre};
        for (int i = 0; i < tabKey.length; i++) {
            wl("itemGroup.randores_" + tabKey[i] + "=" + tabs[i], writer);
        }
        for (int i = 0; i < defTileKey.length; i++) {
            wl("tile.craftinium_" + defTileKey[i] + ".name=" + defTile[i], writer);
        }
        wl("item.craftinium_lump.name=" + cLump, writer);
        for (int i = 0; i < 300; i++) {
            wl("tile.randores.block." + i + ".name=" + ore + " #" + (i + 1), writer);
            wl("tile.randores.block." + (i + 300) + ".name=" + bricks + " #" + (i + 1), writer);
            wl("item.randores.item." + i + ".name=" + material + " #" + (i + 1), writer);
            for (CraftableType type : CraftableType.values()) {
                if (type != CraftableType.BRICKS) {
                    wl("items.randores.items." + type.getIndex(i) + ".name=" + typeMap.get(type) + " #" + (i + 1), writer);
                }
            }
        }
        writer.close();
    }

    private static void wl(String s, FileWriter w) throws IOException {
        w.write(s + "\n");
    }

}
