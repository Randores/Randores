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
package com.gmail.socraticphoenix.forge.randore.component;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.block.FlexibleBlockRegistry;
import com.gmail.socraticphoenix.forge.randore.component.ability.AbilityRegistry;
import com.gmail.socraticphoenix.forge.randore.component.plugin.RandoresPlugin;
import com.gmail.socraticphoenix.forge.randore.component.plugin.RandoresPluginRegistry;
import com.gmail.socraticphoenix.forge.randore.component.property.MaterialProperty;
import com.gmail.socraticphoenix.forge.randore.component.property.properties.FlammableProperty;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemRegistry;
import com.gmail.socraticphoenix.forge.randore.probability.RandomNumberBuilder;
import com.gmail.socraticphoenix.forge.randore.probability.RandoresProbability;
import com.google.common.base.Function;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import org.apache.logging.log4j.Logger;

import javax.annotation.Nullable;
import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MaterialDefinitionGenerator {

    public static List<Color> generateColors(Random random) {
        Randores.getInstance().getConfiguration().load();
        List<Color> colors = new ArrayList<Color>();
        if (Randores.getOreCount() > Randores.registeredAmount()) {
            throw new IllegalArgumentException("Ore count is greater than registered amount");
        }
        for (int i = 0; i < Randores.getOreCount(); i++) {
            Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            while (colors.contains(color)) {
                color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
            }
            colors.add(color);
        }
        return colors;
    }

    public static List<MaterialDefinition> makeDefinitions(List<Color> colors, long seed) {
        List<MaterialDefinition> definitions = new ArrayList<MaterialDefinition>();
        for (int c = 0; c < colors.size(); c++) {
            Color color = colors.get(c);
            Random random = new Random(color.getRGB());
            RandomNumberBuilder b = new RandomNumberBuilder(random);
            b.expRand("material", 2.5, 0, MaterialType.values().length)
                    .rand("dimension", Dimension.values().length)
                    .oneSidedInflectedNormalRand("uses", 20, 6000, 2000)
                    .op(new Function<Number, Number>() {
                        @Nullable
                        @Override
                        public Number apply(@Nullable Number input) {
                            return input.intValue() - input.intValue() % 10;
                        }
                    })
                    .inflectedNormalRand("commonality", 10, 70, 40, 25)
                    .copy("commonality", "commonalityInt")
                    .op(new Function<Number, Number>() {
                        @Nullable
                        @Override
                        public Number apply(@Nullable Number input) {
                            return input.intValue();
                        }
                    })
                    .copy("commonality", "rarity")
                    .op(new Function<Number, Number>() {
                        @Nullable
                        @Override
                        public Number apply(@Nullable Number input) {
                            return 80 - input.doubleValue();
                        }
                    })
                    .copy("commonalityInt", "rarityInt")
                    .op(new Function<Number, Number>() {
                        @Nullable
                        @Override
                        public Number apply(@Nullable Number input) {
                            return 80 - input.intValue();
                        }
                    })
                    .oneSidedInflectedNormalRand("efficiencyVar", 0, 10, 2)
                    .oneSidedInflectedNormalRand("damageVar", 0, 10, 3)
                    .oneSidedInflectedNormalRand("enchantVar", 0, 20, 5)
                    .rand("toughness", 3)
                    .oneSidedInflectedNormalRand("maxYVar", 20, 100, 20)
                    .expRand("minYVar", 2, 0, b.getDouble("commonality"))
                    .op(new Function<Number, Number>() {
                        @Nullable
                        @Override
                        public Number apply(@Nullable Number input) {
                            return Math.floor(input.doubleValue());
                        }
                    })
                    .put("maxOccurrences", b.getInt("commonalityInt") / 20 + 1)
                    .rand("occurrencesVar", b.getInt("maxOccurrences"))
                    .oneSidedInflectedNormalRand("maxDrops", 1, 6, 3)
                    .op(new Function<Number, Number>() {
                        @Nullable
                        @Override
                        public Number apply(@Nullable Number input) {
                            return Math.floor(input.doubleValue());
                        }
                    })
                    .oneSidedInflectedNormalRand("minDrops", 0, b.getInt("maxDrops"), 1)
                    .clamp(1, b.getInt("maxDrops"))
                    .put("maxVein", (int) (b.getDouble("commonality") / 8) + 2)
                    .rand("minVein", b.getInt("maxVein"))
                    .clamp(1, b.getInt("maxVein") - 1)
                    .inflectedNormalRand("smeltingXp", 0, 1.25, 0.75, 0.25)
                    .percentChance("stick", 30)
                    .percentChance("armor", 60)
                    .percentChance("tools", 60)
                    .percentChance("sword", 60)
                    .percentChance("bow", 30)
                    .percentChance("flammable", 15)
                    .rand("flammability", 1200)
                    .clamp(200, 1200)
                    .percentChance("battleaxe", 30)
                    .percentChance("sledgehammer", 30)
                    .randLong("abilitySeed")
                    .rand("oreHarvest", Blocks.DIAMOND_ORE.getHarvestLevel(Blocks.DIAMOND_ORE.getDefaultState()))
                    .clamp(1, Blocks.DIAMOND_ORE.getHarvestLevel(Blocks.DIAMOND_ORE.getDefaultState()))
            ;

            MaterialType type = MaterialType.values()[b.getInt("material")];

            int commonalityInt = b.getInt("commonalityInt");
            double rarity = b.getDouble("rarity");
            int rarityInt = b.getInt("rarityInt");

            int maxY = RandoresProbability.clamp((int) (commonalityInt + b.getDouble("maxYVar")), 10, 65);
            int maxOccurrences = b.getInt("maxOccurrences");

            MaterialComponent material = new MaterialComponent(type,
                    RandoresProbability.clamp(rarityInt / 22 + 1, Item.ToolMaterial.WOOD.getHarvestLevel(), Item.ToolMaterial.DIAMOND.getHarvestLevel()),
                    b.getInt("uses"),
                    (int) (rarity / 6 + b.getDouble("efficiencyVar")),
                    (int) (rarity / 13 + b.getDouble("damageVar")),
                    (int) (rarity / 3 + b.getDouble("enchantVar")),
                    b.getInt("toughness"),
                    MaterialDefinitionGenerator.clampArmor(new int[]{rarityInt / 20, rarityInt / 8, rarityInt / 6, rarityInt / 20}),
                    FlexibleItemRegistry.getMaterial(c));

            OreComponent ore = new OreComponent(material,
                    Dimension.values()[b.getInt("dimension")],
                    b.getInt("maxDrops"),
                    b.getInt("minDrops"),
                    b.getInt("maxVein"),
                    b.getInt("minVein"),
                    maxY,
                    RandoresProbability.clamp(commonalityInt - (commonalityInt - b.getInt("minYVar")) + 1, 1, maxY - 5),
                    maxOccurrences - b.getInt("occurrencesVar"),
                    maxOccurrences,
                    type == MaterialType.INGOT,
                    (float) b.getInt("smeltingXp"),
                    (float) Math.round(rarity / 14),
                    (float) Math.round(rarity / 7),
                    b.getInt("oreHarvest"),
                    Item.getItemFromBlock(FlexibleBlockRegistry.getOres().get(c)));

            List<CraftableComponent> components = new ArrayList<CraftableComponent>();
            components.add(new CraftableComponent(CraftableType.BRICKS, Item.getItemFromBlock(FlexibleBlockRegistry.getBricks().get(c))));
            components.add(new CraftableComponent(CraftableType.TORCH, Item.getItemFromBlock(FlexibleBlockRegistry.getTorches().get(c))));

            if (b.getBoolean("stick")) {
                components.add(new CraftableComponent(CraftableType.STICK, FlexibleItemRegistry.getStick(c)));
            }
            if (b.getBoolean("armor")) {
                components.add(new CraftableComponent(CraftableType.HELMET, FlexibleItemRegistry.getHelmet(c)));
                components.add(new CraftableComponent(CraftableType.CHESTPLATE, FlexibleItemRegistry.getChestplate(c)));
                components.add(new CraftableComponent(CraftableType.LEGGINGS, FlexibleItemRegistry.getLeggings(c)));
                components.add(new CraftableComponent(CraftableType.BOOTS, FlexibleItemRegistry.getBoots(c)));
            }
            if (b.getBoolean("tools")) {
                components.add(new CraftableComponent(CraftableType.PICKAXE, FlexibleItemRegistry.getPickaxe(c)));
                components.add(new CraftableComponent(CraftableType.AXE, FlexibleItemRegistry.getAxe(c)));
                components.add(new CraftableComponent(CraftableType.HOE, FlexibleItemRegistry.getHoe(c)));
                components.add(new CraftableComponent(CraftableType.SHOVEL, FlexibleItemRegistry.getSpade(c)));
            }
            if (b.getBoolean("bow")) {
                components.add(new CraftableComponent(CraftableType.BOW, FlexibleItemRegistry.getBow(c)));
            }
            if (b.getBoolean("sword")) {
                components.add(new CraftableComponent(CraftableType.SWORD, FlexibleItemRegistry.getSword(c)));
            }
            if (b.getBoolean("battleaxe")) {
                components.add(new CraftableComponent(CraftableType.BATTLEAXE, FlexibleItemRegistry.getBattleaxe(c)));
            }
            if (b.getBoolean("sledgehammer")) {
                components.add(new CraftableComponent(CraftableType.SLEDGEHAMMER, FlexibleItemRegistry.getSledgehammer(c)));
            }

            List<MaterialProperty> properties = new ArrayList<MaterialProperty>();
            if (b.getBoolean("flammable")) {
                properties.add(new FlammableProperty(b.getInt("flammability")));
            }


            MaterialDefinition definition = new MaterialDefinition(color, ore, components, properties, AbilityRegistry.buildSeries(new Random(b.getLong("abilitySeed"))), seed, c);
            for (RandoresPlugin plugin : RandoresPluginRegistry.get()) {
                plugin.modify(definition, b);
            }
            definitions.add(definition);
        }
        return definitions;
    }

    public static void logStatistics(List<MaterialDefinition> definitions) {
        Logger logger = Randores.getInstance().getLogger();
        Map<Dimension, Integer> dimCount = new LinkedHashMap<Dimension, Integer>();
        for (Dimension dimension : Dimension.values()) {
            dimCount.put(dimension, 0);
        }
        Map<MaterialType, Integer> mCount = new LinkedHashMap<MaterialType, Integer>();
        for (MaterialType type : MaterialType.values()) {
            mCount.put(type, 0);
        }

        for (MaterialDefinition def : definitions) {
            Dimension dim = def.getOre().getDimension();
            MaterialType mat = def.getMaterial().getType();
            dimCount.put(dim, dimCount.get(dim) + 1);
            mCount.put(mat, mCount.get(mat) + 1);
        }
        logger.info("Definition Count: " + definitions.size());
        logger.info("Ores per Dimension: ");
        for (Map.Entry<Dimension, Integer> entry : dimCount.entrySet()) {
            logger.info("    " + entry.getKey().name() + ": " + entry.getValue() + " ore(s)");
        }
        logger.info("Material Types: ");
        for (Map.Entry<MaterialType, Integer> entry : mCount.entrySet()) {
            logger.info("    " + entry.getKey().name() + ": " + entry.getValue() + " ore(s)");
        }
    }

    public static void unregisterDefinitions(long seed) {
        MaterialDefinitionRegistry.remove(seed);
        for (int i = 0; i < Randores.registeredAmount(); i++) {
            FlexibleItemRegistry.getHoe(i).removeBacker(seed);
            FlexibleItemRegistry.getSword(i).removeBacker(seed);
            FlexibleItemRegistry.getAxe(i).removeBacker(seed);
            FlexibleItemRegistry.getSpade(i).removeBacker(seed);
            FlexibleItemRegistry.getPickaxe(i).removeBacker(seed);
            FlexibleItemRegistry.getHelmet(i).removeBacker(seed);
            FlexibleItemRegistry.getChestplate(i).removeBacker(seed);
            FlexibleItemRegistry.getLeggings(i).removeBacker(seed);
            FlexibleItemRegistry.getBoots(i).removeBacker(seed);
        }
    }

    public static void registerDefinitionsIfNeeded(long seed) {
        Logger logger = Randores.getInstance().getLogger();
        if (!MaterialDefinitionRegistry.contains(seed)) {
            logger.info("Generating definitions for Randores seed: " + seed);
            List<MaterialDefinition> definitions = MaterialDefinitionGenerator.makeDefinitions(MaterialDefinitionGenerator.generateColors(new Random(seed)), seed);
            MaterialDefinitionGenerator.logStatistics(definitions);
            MaterialDefinitionRegistry.put(seed, definitions);
            for (int i = 0; i < Randores.getOreCount(); i++) {
                Item.ToolMaterial toolMaterial = definitions.get(i).getToolMaterial();
                ItemArmor.ArmorMaterial armorMaterial = definitions.get(i).getArmorMaterial();
                FlexibleItemRegistry.getHoe(i).registerBacker(seed, toolMaterial);
                FlexibleItemRegistry.getSword(i).registerBacker(seed, toolMaterial);
                FlexibleItemRegistry.getAxe(i).registerBacker(seed, toolMaterial);
                FlexibleItemRegistry.getSpade(i).registerBacker(seed, toolMaterial);
                FlexibleItemRegistry.getPickaxe(i).registerBacker(seed, toolMaterial);
                FlexibleItemRegistry.getBattleaxe(i).registerBacker(seed, toolMaterial);
                FlexibleItemRegistry.getHelmet(i).registerBacker(seed, armorMaterial);
                FlexibleItemRegistry.getChestplate(i).registerBacker(seed, armorMaterial);
                FlexibleItemRegistry.getLeggings(i).registerBacker(seed, armorMaterial);
                FlexibleItemRegistry.getBoots(i).registerBacker(seed, armorMaterial);
            }
        }
    }

    private static int[] clampArmor(int[] reduc) {
        for (int i = 0; i < reduc.length; i++) {
            if (reduc[i] < 1) {
                reduc[i] = 1;
            }
        }

        while (sum(reduc) > 20) {
            reduce(reduc, 0);
            reduce(reduc, 1);
            reduce(reduc, 1);
            reduce(reduc, 2);
            reduce(reduc, 2);
            reduce(reduc, 3);
        }

        return reduc;
    }

    private static void reduce(int[] arr, int slot) {
        if (arr[slot] > 2) {
            arr[slot] = arr[slot] - 1;
        }
    }

    private static int sum(int[] arr) {
        int a = 0;
        for (int i : arr) {
            a += i;
        }
        return a;
    }

}
