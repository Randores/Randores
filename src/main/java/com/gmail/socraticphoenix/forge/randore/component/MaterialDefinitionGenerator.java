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
import com.gmail.socraticphoenix.forge.randore.RandoresProbability;
import com.gmail.socraticphoenix.forge.randore.block.FlexibleBlockRegistry;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemRegistry;
import com.gmail.socraticphoenix.forge.randore.texture.FlexibleTextureRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class MaterialDefinitionGenerator {

    public static List<Color> generateColors(Random random) {
        Randores.getInstance().getConfiguration().load();
        List<Color> colors = new ArrayList<Color>();
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
            MaterialType type = MaterialType.values()[(int) RandoresProbability.expRand(2.5, 0, MaterialType.values().length, random)];
            Dimension dimension = Dimension.values()[random.nextInt(Dimension.values().length)];

            int uses = (int) Math.ceil(RandoresProbability.oneSidedInflectedNormalRand(20, 6000, 2000, random));
            uses -= uses % 10;

            double commonality = RandoresProbability.inflectedNormalRand(10, 70, 40, 25, random);
            int commonalityInt = (int) Math.round(commonality);
            double rarity = 80 - commonality;
            int rarityInt = 80 - commonalityInt;

            int toolHarvestLevel = RandoresProbability.clamp(rarityInt / 22 + 1, Item.ToolMaterial.WOOD.getHarvestLevel(), Item.ToolMaterial.DIAMOND.getHarvestLevel());
            int efficiency = (int) (rarity / 6 + RandoresProbability.oneSidedInflectedNormalRand(0, 10, 2, random));
            int damage = (int) (rarity / 13 + RandoresProbability.oneSidedInflectedNormalRand(0, 10, 3, random));
            int enchantability = (int) (rarity / 3 + RandoresProbability.oneSidedInflectedNormalRand(0, 20, 5, random));
            int toughness = random.nextInt(3);

            int maxY = RandoresProbability.clamp((int) (commonalityInt + RandoresProbability.oneSidedInflectedNormalRand(20, 100, 20, random)), 10, 65);
            int minY = RandoresProbability.clamp((int) (commonalityInt - (commonalityInt - Math.floor(RandoresProbability.expRand(2, 0, commonality, random))) + 1), 1, maxY - 5);
            int maxOccurrences = commonalityInt / 20 + 1;
            int minOccurrences = maxOccurrences - random.nextInt(maxOccurrences);
            int oreHarvestLevel = RandoresProbability.clamp(rarityInt / 10, Blocks.IRON_ORE.getHarvestLevel(Blocks.IRON_ORE.getDefaultState()), Blocks.DIAMOND_ORE.getHarvestLevel(Blocks.DIAMOND_ORE.getDefaultState()));
            int maxDrops = (int) Math.floor(RandoresProbability.oneSidedInflectedNormalRand(2, 6, 3, random));
            int minDrops = (int) RandoresProbability.oneSidedInflectedNormalRand(1, maxDrops, 2, random);
            int maxVein = (int) (commonality / 10) + 2;
            int minVein = (int) Math.ceil(RandoresProbability.expRand(1.2, 2, maxVein, random));
            float smeltingXp = (float) RandoresProbability.inflectedNormalRand(0, 1.25, 0.75, 0.25, random);
            float hardness = (float) Math.round(rarity / 14);
            float resistance = (float) Math.round(rarity / 7);
            int[] armor = new int[]{rarityInt / 20, rarityInt / 8, rarityInt / 6, rarityInt / 20};
            MaterialDefinitionGenerator.clampArmor(armor);

            MaterialComponent material = new MaterialComponent(type, toolHarvestLevel, uses, efficiency, damage, enchantability, toughness, armor, FlexibleItemRegistry.getMaterial(c));
            OreComponent ore = new OreComponent(material, dimension, maxDrops, minDrops, maxVein, minVein, maxY, minY, minOccurrences, maxOccurrences, type == MaterialType.INGOT, smeltingXp, hardness, resistance, oreHarvestLevel, Item.getItemFromBlock(FlexibleBlockRegistry.getOres().get(c)));

            List<CraftableComponent> components = new ArrayList<CraftableComponent>();
            boolean hasComponents = false;
            if (RandoresProbability.percentChance(60, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.HELMET, 1, FlexibleItemRegistry.getHelmet(c)));
                components.add(new CraftableComponent(CraftableType.CHESTPLATE, 1, FlexibleItemRegistry.getChestplate(c)));
                components.add(new CraftableComponent(CraftableType.LEGGINGS, 1, FlexibleItemRegistry.getLeggings(c)));
                components.add(new CraftableComponent(CraftableType.BOOTS, 1, FlexibleItemRegistry.getBoots(c)));
            }
            if (RandoresProbability.percentChance(60, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.PICKAXE, 1, FlexibleItemRegistry.getPickaxe(c)));
                components.add(new CraftableComponent(CraftableType.AXE, 1, FlexibleItemRegistry.getAxe(c)));
                components.add(new CraftableComponent(CraftableType.HOE, 1, FlexibleItemRegistry.getHoe(c)));
                components.add(new CraftableComponent(CraftableType.SHOVEL, 1, FlexibleItemRegistry.getSpade(c)));
            }
            if (RandoresProbability.percentChance(60, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.SWORD, 1, FlexibleItemRegistry.getSword(c)));
            }
            if (RandoresProbability.percentChance(90, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.BRICKS, 4, Item.getItemFromBlock(FlexibleBlockRegistry.getBricks().get(c))));
            }
            if (RandoresProbability.percentChance(50, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.STICK, 2, FlexibleItemRegistry.getStick(c)));
            }
            if (!hasComponents) {
                components.add(new CraftableComponent(CraftableType.HELMET, 1, FlexibleItemRegistry.getHelmet(c)));
                components.add(new CraftableComponent(CraftableType.CHESTPLATE, 1, FlexibleItemRegistry.getChestplate(c)));
                components.add(new CraftableComponent(CraftableType.LEGGINGS, 1, FlexibleItemRegistry.getLeggings(c)));
                components.add(new CraftableComponent(CraftableType.BOOTS, 1, FlexibleItemRegistry.getBoots(c)));
                components.add(new CraftableComponent(CraftableType.PICKAXE, 1, FlexibleItemRegistry.getPickaxe(c)));
                components.add(new CraftableComponent(CraftableType.AXE, 1, FlexibleItemRegistry.getAxe(c)));
                components.add(new CraftableComponent(CraftableType.HOE, 1, FlexibleItemRegistry.getHoe(c)));
                components.add(new CraftableComponent(CraftableType.SHOVEL, 1, FlexibleItemRegistry.getPickaxe(c)));
                components.add(new CraftableComponent(CraftableType.SWORD, 1, FlexibleItemRegistry.getSword(c)));
                components.add(new CraftableComponent(CraftableType.BRICKS, 4, Item.getItemFromBlock(FlexibleBlockRegistry.getBricks().get(c))));
                components.add(new CraftableComponent(CraftableType.STICK, 2, FlexibleItemRegistry.getStick(c)));
            }
            MaterialDefinition definition = new MaterialDefinition(color, ore, components, seed, c);
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

    @SideOnly(Side.CLIENT)
    public static void setupTextures(List<MaterialDefinition> definitions, long seed) {
        for (int i = 0; i < definitions.size(); i++) {
            FlexibleTextureRegistry.getBlock(i).setTexture("block." + i, seed);
            FlexibleTextureRegistry.getItem(i).setTexture("item." + i + ".png", seed);
            for (CraftableType type : CraftableType.values()) {
                if (type == CraftableType.BRICKS) {
                    FlexibleTextureRegistry.getBlock(type.getIndex(i)).setTexture("bricks." + i + ".png", seed);
                } else {
                    FlexibleTextureRegistry.getItem(type.getIndex(i)).setTexture(type.getTemplate().replaceAll("_base", "") + "." + i + ".png", seed);
                }
            }
        }

        for (int i = definitions.size(); i < 300; i++) {
            FlexibleTextureRegistry.getBlock(i).setTexture("test", seed);
            FlexibleTextureRegistry.getItem(i).setTexture("test", seed);
            for (CraftableType type : CraftableType.values()) {
                if (type == CraftableType.BRICKS) {
                    FlexibleTextureRegistry.getBlock(type.getIndex(i)).setTexture("test", seed);
                } else {
                    FlexibleTextureRegistry.getItem(type.getIndex(i)).setTexture("test", seed);
                }
            }
        }
    }

    public static void unregisterDefinitions(long seed) {
        MaterialDefinitionRegistry.remove(seed);
        for (int i = 0; i < 300; i++) {
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
                FlexibleItemRegistry.getHelmet(i).registerBacker(seed, armorMaterial);
                FlexibleItemRegistry.getChestplate(i).registerBacker(seed, armorMaterial);
                FlexibleItemRegistry.getLeggings(i).registerBacker(seed, armorMaterial);
                FlexibleItemRegistry.getBoots(i).registerBacker(seed, armorMaterial);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void generateAndSetupTextures(List<MaterialDefinition> definitions, long seed) throws IOException {
        for (int i = 0; i < definitions.size(); i++) {
            MaterialDefinition def = definitions.get(i);
            Map<String, BufferedImage> textures = def.generateTextures();
            Randores.getInstance().getTextureFile(seed).mkdirs();
            File target = new File(Randores.getInstance().getTextureFile(seed), "block." + i + ".png");
            ImageIO.write(textures.get(def.getOre().template()), "png", target);
            File itarg = new File(Randores.getInstance().getTextureFile(seed), "item." + i + ".png");
            ImageIO.write(textures.get(def.getMaterial().template()), "png", itarg);
            for (CraftableComponent component : def.getCraftables()) {
                if (component.getType() == CraftableType.HELMET) {
                    File armor1 = new File(Randores.getInstance().getTextureFile(seed), "armor." + i + "_1.png");
                    File armor2 = new File(Randores.getInstance().getTextureFile(seed), "armor." + i + "_2.png");
                    ImageIO.write(textures.get("armor_1"), "png", armor1);
                    ImageIO.write(textures.get("armor_2"), "png", armor2);
                    File ttarg = new File(Randores.getInstance().getTextureFile(seed), component.template().replaceAll("_base", "") + "." + i + ".png");
                    ImageIO.write(textures.get(component.template()), "png", ttarg);
                } else if (component.getType() == CraftableType.BRICKS) {
                    File btarg = new File(Randores.getInstance().getTextureFile(seed), "bricks." + i + ".png");
                    ImageIO.write(textures.get(component.template()), "png", btarg);
                } else {
                    File ttarg = new File(Randores.getInstance().getTextureFile(seed), component.template().replaceAll("_base", "") + "." + i + ".png");
                    ImageIO.write(textures.get(component.template()), "png", ttarg);
                }
            }
        }
        MaterialDefinitionGenerator.setupTextures(definitions, seed);
    }

    @SideOnly(Side.CLIENT)
    public static void setupArmorTextures(List<MaterialDefinition> definitions) {
        for (int i = 0; i < definitions.size(); i++) {
            MaterialDefinition def = definitions.get(i);
            Minecraft.getMinecraft().getTextureManager().deleteTexture(new ResourceLocation("randores_armor", "armor." + i + "_1.png"));
            Minecraft.getMinecraft().getTextureManager().deleteTexture(new ResourceLocation("randores_armor", "armor." + i + "_2.png"));

            if (def.hasComponent(Components.HELMET)) {
                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("randores_armor", "armor." + i + "_1.png"));
                Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("randores_armor", "armor." + i + "_2.png"));
            }
        }

        for (int i = definitions.size(); i < 300; i++) {
            Minecraft.getMinecraft().getTextureManager().deleteTexture(new ResourceLocation("randores_armor", "armor." + i + "_1.png"));
            Minecraft.getMinecraft().getTextureManager().deleteTexture(new ResourceLocation("randores_armor", "armor." + i + "_2.png"));

        }
    }

    private static void clampArmor(int[] reduc) {
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
