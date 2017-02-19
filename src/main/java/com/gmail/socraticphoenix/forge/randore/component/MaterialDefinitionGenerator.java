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
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemRegistry;
import com.gmail.socraticphoenix.forge.randore.texture.FlexibleTextureRegistry;
import net.minecraft.client.Minecraft;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
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
        List<Color> colors = new ArrayList<Color>();
        for (int i = 0; i < 300; i++) {
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
            MaterialType type;
            if (percentChance(50, random)) {
                type = MaterialType.INGOT;
            } else if (percentChance(30, random)) {
                if (random.nextBoolean()) {
                    type = MaterialType.GEM;
                } else {
                    type = MaterialType.DUST;
                }
            } else if (percentChance(5, random)) {
                type = MaterialType.EMERALD;
            } else if (percentChance(5, random)) {
                type = MaterialType.SHARD;
            } else if (percentChance(5, random)) {
                type = MaterialType.CIRCLE_GEM;
            } else {
                type = MaterialType.values()[random.nextInt(MaterialType.values().length)];
            }
            int uses = random.nextInt(6000) + 200;
            uses = uses - (uses % 100);
            MaterialComponent material = new MaterialComponent(type, random.nextInt(6) + 1, uses, MathHelper.ceil(random.nextFloat() * random.nextInt(5) + random.nextInt(10) + 2), MathHelper.ceil(random.nextFloat() * random.nextInt(20) + 1), random.nextInt(50) + 1, MathHelper.floor(random.nextFloat() * 3), FlexibleItemRegistry.getMaterials().get(c));
            OreComponent ore = new OreComponent(material, Dimension.values()[random.nextInt(Dimension.values().length)], random.nextInt(4) + 2, 1, random.nextInt(15) + 2, 2, random.nextInt(200) + 5, 0, 5, random.nextInt(15) + 5, random.nextBoolean() || material.getType() == MaterialType.INGOT, random.nextFloat() * 2, random.nextFloat() * random.nextInt(10) + 0.5f, random.nextFloat() * random.nextInt(50) + 2f, Item.getItemFromBlock(FlexibleBlockRegistry.getOres().get(c)));
            List<CraftableComponent> components = new ArrayList<CraftableComponent>();
            boolean hasComponents = false;
            if (percentChance(75, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.HELMET, 1, FlexibleItemRegistry.getHelmet(c)));
                components.add(new CraftableComponent(CraftableType.CHESTPLATE, 1, FlexibleItemRegistry.getChestplate(c)));
                components.add(new CraftableComponent(CraftableType.LEGGINGS, 1, FlexibleItemRegistry.getLeggings(c)));
                components.add(new CraftableComponent(CraftableType.BOOTS, 1, FlexibleItemRegistry.getBoots(c)));
            }
            if (percentChance(75, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.PICKAXE, 1, FlexibleItemRegistry.getPickaxe(c)));
                components.add(new CraftableComponent(CraftableType.AXE, 1, FlexibleItemRegistry.getAxe(c)));
                components.add(new CraftableComponent(CraftableType.HOE, 1, FlexibleItemRegistry.getHoe(c)));
                components.add(new CraftableComponent(CraftableType.SHOVEL, 1, FlexibleItemRegistry.getSpade(c)));
            }
            if (percentChance(75, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.SWORD, 1, FlexibleItemRegistry.getSword(c)));
            }
            if (percentChance(50, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.BRICKS, 4, Item.getItemFromBlock(FlexibleBlockRegistry.getBricks().get(c))));
            }
            if (percentChance(25, random)) {
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

    public static boolean percentChance(double percent, Random random) {
        double result = random.nextDouble() * 100;
        return result <= percent;
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

    public static void setupTextures(List<MaterialDefinition> definitions, long seed) {
        for (int i = 0; i < definitions.size(); i++) {
            MaterialDefinition def = definitions.get(i);
            FlexibleTextureRegistry.getBlock(i).setTexture("block." + i, seed);
            FlexibleTextureRegistry.getItem(i).setTexture("item." + i + ".png", seed);
            for (CraftableComponent component : def.getCraftables()) {
                if (component.getType() == CraftableType.BRICKS) {
                    FlexibleTextureRegistry.getBlock(component.getType().getIndex(i)).setTexture("bricks." + i + ".png", seed);
                } else {
                    FlexibleTextureRegistry.getItem(component.getType().getIndex(i)).setTexture(component.template().replaceAll("_base", "") + "." + i + ".png", seed);
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
            for (int i = 0; i < 300; i++) {
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

    public static void generateAndSetupTextures(List<MaterialDefinition> definitions, long seed) throws IOException {
        for (int i = 0; i < definitions.size(); i++) {
            MaterialDefinition def = definitions.get(i);
            Map<String, BufferedImage> textures = def.generateTextures();
            Randores.getInstance().getTextureFile(seed).mkdirs();
            File target = new File(Randores.getInstance().getTextureFile(seed), "block." + i + ".png");
            ImageIO.write(textures.get(def.getOre().template()), "png", target);
            FlexibleTextureRegistry.getBlock(i).setTexture("block." + i, seed);
            File itarg = new File(Randores.getInstance().getTextureFile(seed), "item." + i + ".png");
            ImageIO.write(textures.get(def.getMaterial().template()), "png", itarg);
            FlexibleTextureRegistry.getItem(i).setTexture("item." + i + ".png", seed);
            for (CraftableComponent component : def.getCraftables()) {
                if (component.getType() == CraftableType.HELMET) {
                    File armor1 = new File(Randores.getInstance().getTextureFile(seed), "armor." + i + "_1.png");
                    File armor2 = new File(Randores.getInstance().getTextureFile(seed), "armor." + i + "_2.png");
                    ImageIO.write(textures.get("armor_1"), "png", armor1);
                    ImageIO.write(textures.get("armor_2"), "png", armor2);
                    File ttarg = new File(Randores.getInstance().getTextureFile(seed), component.template().replaceAll("_base", "") + "." + i + ".png");
                    ImageIO.write(textures.get(component.template()), "png", ttarg);
                    FlexibleTextureRegistry.getItem(i + 300 * (component.getType().ordinal() + 1)).setTexture(component.template().replaceAll("_base", "") + "." + i + ".png", seed);
                } else if (component.getType() == CraftableType.BRICKS) {
                    File btarg = new File(Randores.getInstance().getTextureFile(seed), "bricks." + i + ".png");
                    ImageIO.write(textures.get(component.template()), "png", btarg);
                    FlexibleTextureRegistry.getBlock(i + 300).setTexture("bricks." + i + ".png", seed);
                } else {
                    File ttarg = new File(Randores.getInstance().getTextureFile(seed), component.template().replaceAll("_base", "") + "." + i + ".png");
                    ImageIO.write(textures.get(component.template()), "png", ttarg);
                    FlexibleTextureRegistry.getItem(i + 300 * (component.getType().ordinal() + 1)).setTexture(component.template().replaceAll("_base", "") + "." + i + ".png", seed);
                }
            }

        }
    }

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
    }

}
