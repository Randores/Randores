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
package com.gmail.socraticphoenix.forge.randore;

import com.gmail.socraticphoenix.forge.randore.resource.RandoresResourceManager;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class RandoresTranslations {
    private static Map<Locale, Map<String, String>> translations = new HashMap<Locale, Map<String, String>>();

    public static void register(Locale locale, String key, String value) {
        RandoresTranslations.ensureExistence(locale);
        RandoresTranslations.translations.get(locale).put(key, value);
    }

    public static String get(Locale locale, String key) {
        if(RandoresTranslations.translations.containsKey(locale)) {
            return RandoresTranslations.translations.get(locale).get(key);
        } else {
            return RandoresTranslations.get(Locale.US, key);
        }
    }

    public static void registerFromResources(Locale locale, String fileName) throws IOException {
        if(RandoresResourceManager.resourceExists("lang" + File.separator + fileName)) {
            List<String> lines = RandoresResourceManager.getResourceLines("lang" + File.separator + fileName);
            for(String line : lines) {
                if(line.contains("=")) {
                    String[] pieces = line.split("=", 2);
                    RandoresTranslations.register(locale, pieces[0], pieces[1]);
                }
            }
        }
    }

    private static void ensureExistence(Locale locale) {
        if(!RandoresTranslations.translations.containsKey(locale)) {
            RandoresTranslations.translations.put(locale, new HashMap<String, String>());
        }
    }

    public interface Keys {
        String TEXTURES_MESSAGE = "randores.texture.setup";
        String RESOURCES_RELOADING = "randores.texture.reloading";
        String INFORMATION = "randores.material.information";
        String EFFICIENCY = "randores.material.efficiency";
        String FULL_ARMOR = "randores.materials.full_armor";
        String DAMAGE = "randores.materials.base_damage";
        String DURABILITY = "randores.materials.durability";
        String ENCHANTABILITY = "randores.materials.enchantability";
        String RECIPES = "randores.materials.recipes";
        String TOOLS_RECIPE = "randores.recipes.tools";
        String ARMOR_RECIPE = "randores.recipes.armor";
        String SWORD_RECIPE = "randores.recipes.sword";
        String BRICKS_RECIPE = "randores.recipes.bricks";
        String STICK_RECIPE = "randores.recipes.stick";
        String CRAFTINIUM_FORGE = "randores.blocks.forge";
        String CRAFTINIUM_TABLE = "randores.blocks.table";
        String ORE = "randores.items.ore";
        String STICK = "randores.recipes.stick";
        String AXE = "randores.items.axe";
        String HOE = "randores.items.hoe";
        String SHOVEL = "randores.items.shovel";
        String PICKAXE = "randores.items.pickaxe";
        String SWORD = "randores.items.sword";
        String BRICKS = "randores.items.bricks";
        String HELMET = "randores.items.helmet";
        String CHESTPLATE = "randores.items.chestplate";
        String LEGGINGS = "randores.items.leggings";
        String BOOTS = "randores.items.boots";
        String INGOT = "randores.items.ingot";
        String DUST = "randores.items.dust";
        String GEM = "randores.items.gem";
        String EMERALD = "randores.items.emerald";
        String CIRCLE_GEM = "randores.items.circle_gem";
        String SHARD = "randores.items.shard";
    }

}