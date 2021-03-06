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

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RandoresTranslations {
    private static Map<String, Map<String, String>> translations = new HashMap<String, Map<String, String>>();
    private static Map<String, String> fallback = new HashMap<String, String>();

    public static void register(String locale, String key, String value) {
        RandoresTranslations.ensureExistence(locale);
        RandoresTranslations.translations.get(locale.toLowerCase()).put(key, value);
    }

    public static String get(String key) {
        return RandoresTranslations.get(Randores.PROXY.getCurrentLocale(), key);
    }

    public static String get(String locale, String key) {
        if (RandoresTranslations.translations.containsKey(locale.toLowerCase()) && RandoresTranslations.translations.get(locale.toLowerCase()).containsKey(key)) {
            return RandoresTranslations.translations.get(locale.toLowerCase()).get(key);
        } else if (RandoresTranslations.fallback.containsKey(key)) {
            return RandoresTranslations.fallback.get(key);
        } else {
            return "$" + key + "$";
        }
    }

    public static void registerFallback() throws IOException {
        List<String> lines = RandoresResourceManager.getResourceLines("en_US.lang");
        for (String line : lines) {
            if (line.contains("=")) {
                String[] pieces = line.split("=", 2);
                RandoresTranslations.fallback.put(pieces[0], pieces[1]);
                RandoresTranslations.register("en_US", pieces[0], pieces[1]);
            }
        }
    }

    private static void ensureExistence(String locale) {
        if (!RandoresTranslations.translations.containsKey(locale.toLowerCase())) {
            RandoresTranslations.translations.put(locale.toLowerCase(), new HashMap<String, String>());
        }
    }

    public interface Keys {
        String RESOURCES_RELOADING = "randores.texture.reloading";
        String RESOURCES_LOADED = "randores.texture.loaded";
        String EFFICIENCY = "randores.material.efficiency";
        String FULL_ARMOR = "randores.materials.full_armor";
        String DAMAGE = "randores.materials.base_damage";
        String DURABILITY = "randores.materials.durability";
        String ENCHANTABILITY = "randores.materials.enchantability";
        String HARVEST_LEVEL = "randores.materials.harvest";
        String ORE_HARVEST_LEVEL = "randores.materials.ore_harvest";
        String FORGE_POWER = "randores.crafting.forge_power";
        String CRAFTINIUM_FORGE = "randores.blocks.forge";
        String CRAFTINIUM_TABLE = "randores.blocks.table";
        String TOME = "randores.items.tome";
        String ORE = "randores.items.ore";
        String STICK = "randores.items.stick";
        String AXE = "randores.items.axe";
        String HOE = "randores.items.hoe";
        String SHOVEL = "randores.items.shovel";
        String PICKAXE = "randores.items.pickaxe";
        String SWORD = "randores.items.sword";
        String BRICKS = "randores.items.bricks";
        String HELMET = "randores.items.helmet";
        String CHESTPLATE = "randores.items.chestplate";
        String LEGGINGS = "randores.items.leggings";
        String BOW = "randores.items.bow";
        String BOOTS = "randores.items.boots";
        String INGOT = "randores.items.ingot";
        String DUST = "randores.items.dust";
        String GEM = "randores.items.gem";
        String EMERALD = "randores.items.emerald";
        String CIRCLE_GEM = "randores.items.circle_gem";
        String SHARD = "randores.items.shard";
        String TORCH = "randores.items.torch";
        String FORMAT = "randores.name.format";
        String BATTLEAXE = "randores.items.battleaxe";
        String SLEDGEHAMMER = "randores.items.sledgehammer";
        String TYPE = "randores.materials.type";
        String ABILITIES = "randores.materials.abilities";
        String MELEE = "randores.materials.melee";
        String PROJECTILE = "randores.materials.projectile";
        String ARMOR_PASSIVE = "randores.materials.armor.passive";
        String ARMOR_ACTIVE = "randores.materials.armor.active";
        String ERROR = "randores.materials.error";
        String CONTENTS = "randores.materials.contents";
        String OBTAINING = "randores.materials.obtaining";
        String RECIPES = "randores.materials.recipes";
        String INFORMATION = "randores.materials.information";
        String PROPERTIES = "randores.materials.properties";
    }

}
