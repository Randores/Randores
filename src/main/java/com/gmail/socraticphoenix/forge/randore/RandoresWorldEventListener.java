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

import com.gmail.socraticphoenix.forge.randore.block.FlexibleBlockRegistry;
import com.gmail.socraticphoenix.forge.randore.component.CraftableComponent;
import com.gmail.socraticphoenix.forge.randore.component.CraftableType;
import com.gmail.socraticphoenix.forge.randore.component.Dimension;
import com.gmail.socraticphoenix.forge.randore.component.MaterialComponent;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.component.MaterialType;
import com.gmail.socraticphoenix.forge.randore.component.OreComponent;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemRegistry;
import com.gmail.socraticphoenix.forge.randore.texture.FlexibleTextureRegistry;
import net.minecraft.item.Item;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
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

public class RandoresWorldEventListener {
    private int dimension = 0;
    private int loads = 0;
    private int remoteLoads = 0;

    public static List<MaterialDefinition> makeDefinitions(List<Color> colors, long seed) {
        List<MaterialDefinition> definitions = new ArrayList<MaterialDefinition>();
        for (int c = 0; c < colors.size(); c++) {
            Color color = colors.get(c);
            Random random = new Random(color.getRGB());
            MaterialType type;
            if(percentChance(50, random)) {
                type = MaterialType.INGOT;
            } else if (percentChance(15, random)) {
                type = MaterialType.DUST;
            } else if (percentChance(15, random)) {
                type = MaterialType.GEM;
            } else if (percentChance(10, random)) {
                type = MaterialType.EMERALD;
            } else if (percentChance(5, random)) {
                type = MaterialType.CIRCLE_GEM;
            } else if (percentChance(5, random)) {
                type = MaterialType.SHARD;
            } else {
                type = MaterialType.values()[random.nextInt(MaterialType.values().length)];
            }
            MaterialComponent material = new MaterialComponent(type, random.nextInt(6), random.nextInt(6000) + 500, random.nextFloat() * random.nextInt(5) + 1, random.nextFloat() * random.nextInt(20) + 1, random.nextInt(50) + 1, FlexibleItemRegistry.getMaterials().get(c));
            OreComponent ore = new OreComponent(material, Dimension.values()[random.nextInt(Dimension.values().length)], random.nextInt(4) + 2, 1, random.nextInt(15) + 2, 2, random.nextInt(255), 0, 5, random.nextInt(25) + 5, random.nextBoolean() || material.getType() == MaterialType.INGOT, random.nextInt(500), random.nextFloat() * random.nextInt(8), random.nextFloat() * random.nextInt(20), Item.getItemFromBlock(FlexibleBlockRegistry.getOres().get(c)));
            List<CraftableComponent> components = new ArrayList<CraftableComponent>();
            boolean hasComponents = false;
            if (percentChance(75, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.HELMET, 1));
                components.add(new CraftableComponent(CraftableType.CHESTPLATE, 1));
                components.add(new CraftableComponent(CraftableType.LEGGINGS, 1));
                components.add(new CraftableComponent(CraftableType.BOOTS, 1));
            }
            if (percentChance(75, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.PICKAXE, 1));
                components.add(new CraftableComponent(CraftableType.AXE, 1));
                components.add(new CraftableComponent(CraftableType.HOE, 1));
                components.add(new CraftableComponent(CraftableType.SHOVEL, 1));
            }
            if (percentChance(75, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.SWORD, 1));
            }
            if (percentChance(50, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.BRICKS, 4));
            }
            if (percentChance(25, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.STICK, 2));
            }
            if (!hasComponents) { //Basically, very, very rarely, an ore will be able to craft all possible craftable components
                components.add(new CraftableComponent(CraftableType.HELMET, 1));
                components.add(new CraftableComponent(CraftableType.CHESTPLATE, 1));
                components.add(new CraftableComponent(CraftableType.LEGGINGS, 1));
                components.add(new CraftableComponent(CraftableType.BOOTS, 1));
                components.add(new CraftableComponent(CraftableType.PICKAXE, 1));
                components.add(new CraftableComponent(CraftableType.AXE, 1));
                components.add(new CraftableComponent(CraftableType.HOE, 1));
                components.add(new CraftableComponent(CraftableType.SHOVEL, 1));
                components.add(new CraftableComponent(CraftableType.SWORD, 1));
                components.add(new CraftableComponent(CraftableType.BRICKS, 4));
                components.add(new CraftableComponent(CraftableType.STICK, 2));
            }
            MaterialDefinition definition = new MaterialDefinition(color, ore, components, seed);
            definitions.add(definition);
        }
        return definitions;
    }

    public static boolean percentChance(double percent, Random random) {
        double result = random.nextDouble() * 100;
        return result <= percent;
    }

    /*@SubscribeEvent
    public void onWorldUnload(WorldEvent.Unload ev) {
        World world = ev.getWorld();
        if(!world.isRemote && this.loads > 0) {
            this.loads--;
        } else if (world.isRemote && this.remoteLoads > 0) {
            this.remoteLoads--;
        }

    }*/

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load ev) throws IOException {
        World world = ev.getWorld();

        if (!world.isRemote && this.loads == 0) {
            this.loads++;
            Random random = new Random(world.getSeed());
            Logger logger = Randores.getInstance().getLogger();
            logger.info("Loading Randores world data...");
            RandoresWorldData worldData = RandoresWorldData.get(world);
            if (worldData.getOres().isEmpty()) {
                logger.info("Randores data is empty! Generating new data...");
                for (int i = 0; i < 300; i++) {
                    Color color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                    while (worldData.getOres().contains(color)) {
                        color = new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256));
                    }
                    worldData.add(color);
                }
                logger.info("Generated new data.");
            }
            logger.info("Loaded Randores world data.");
            logger.info("Transforming Randores data into MaterialDefinitions...");
            List<MaterialDefinition> definitions = makeDefinitions(worldData.getOres(), world.getSeed());
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
            logger.info("Finished transformations, statistics:");
            logger.info("Definition Count: " + definitions.size());
            logger.info("Ores per Dimension: ");
            for (Map.Entry<Dimension, Integer> entry : dimCount.entrySet()) {
                logger.info("    " + entry.getKey().name() + ": " + entry.getValue() + " ore(s)");
            }
            logger.info("Material Types: ");
            for (Map.Entry<MaterialType, Integer> entry : mCount.entrySet()) {
                logger.info("    " + entry.getKey().name() + ": " + entry.getValue() + " ore(s)");
            }

            MaterialDefinitionRegistry.put(world.getSeed(), definitions);

            //SERVER SIDE SETUP ONLY

            //DO STUFF WITH THOSE DEFS MAN!
            for (File texture : Randores.getInstance().getTexFile().listFiles()) {
                texture.delete();
            }

            for (int i = 0; i < definitions.size(); i++) {
                MaterialDefinition def = definitions.get(i);
                Map<String, BufferedImage> textures = def.generateTextures(random);
                File target = new File(Randores.getInstance().getTexFile(), "block." + i + ".png");
                ImageIO.write(textures.get(def.getOre().template()), "png", target);
                FlexibleTextureRegistry.getBlock(i).setTexture("block." + i);

                File itarg = new File(Randores.getInstance().getTexFile(), "item." + i + ".png");
                ImageIO.write(textures.get(def.getMaterial().template()), "png", itarg);
                FlexibleTextureRegistry.getItem(i).setTexture("item." + i + ".png");


                for (CraftableComponent component : def.getCraftables()) {
                    if (component.getType() == CraftableType.HELMET) {
                        File armor1 = new File(Randores.getInstance().getTexFile(), "armor." + i + "_1.png");
                        File armor2 = new File(Randores.getInstance().getTexFile(), "armor." + i + "_2.png");
                        ImageIO.write(textures.get("armor_1"), "png", armor1);
                        ImageIO.write(textures.get("armor_2"), "png", armor2);
                        File ttarg = new File(Randores.getInstance().getTexFile(), component.template().replaceAll("_base", "") + "." + i + ".png");
                        ImageIO.write(textures.get(component.template()), "png", ttarg);
                        FlexibleTextureRegistry.getItem(i + 300 * (component.getType().ordinal() + 1)).setTexture(component.template().replaceAll("_base", "") + "." + i + ".png");
                    } else if (component.getType() == CraftableType.BRICKS) {
                        File btarg = new File(Randores.getInstance().getTexFile(), "bricks." + i + ".png");
                        ImageIO.write(textures.get(component.template()), "png", btarg);
                        FlexibleTextureRegistry.getBlock(i + 300).setTexture("bricks." + i + ".png");
                    } else {
                        File ttarg = new File(Randores.getInstance().getTexFile(), component.template().replaceAll("_base", "") + "." + i + ".png");
                        ImageIO.write(textures.get(component.template()), "png", ttarg);
                        FlexibleTextureRegistry.getItem(i + 300 * (component.getType().ordinal() + 1)).setTexture(component.template().replaceAll("_base", "") + "." + i + ".png");
                    }
                }

                def.registerRecipes();
            }
        } else if (world.isRemote && this.remoteLoads == 0) {
            this.remoteLoads++;
        }
    }

}
