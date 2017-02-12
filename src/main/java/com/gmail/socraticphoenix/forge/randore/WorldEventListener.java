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

import com.gmail.socraticphoenix.forge.randore.component.CraftableComponent;
import com.gmail.socraticphoenix.forge.randore.component.CraftableType;
import com.gmail.socraticphoenix.forge.randore.component.Dimension;
import com.gmail.socraticphoenix.forge.randore.component.MaterialComponent;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialType;
import com.gmail.socraticphoenix.forge.randore.component.OreComponent;
import net.minecraft.world.World;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

import java.awt.Color;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class WorldEventListener {

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load ev) {
        World world = ev.getWorld();
        if(!world.isRemote && world.provider.getDimension() == 0) {
            Logger logger = Randores.getInstance().getLogger();
            logger.info("Loading Randores world data...");
            RandoreWorldData worldData = RandoreWorldData.get(world);
            if(worldData.getOres().isEmpty()) {
                logger.info("Randores data is empty! Generating new data...");
                for (int i = 0; i < 300; i++) {
                    Color color = new Color(world.rand.nextInt(256), world.rand.nextInt(256), world.rand.nextInt(256));
                    worldData.add(color);
                }
                logger.info("Generated new data.");
            }
            logger.info("Loaded Randores world data.");
            logger.info("Transforming Randores data into MaterialDefinitions...");
            List<MaterialDefinition> definitions = this.makeDefinitions(worldData.getOres());
            Map<Dimension, Integer> dimCount = new LinkedHashMap<Dimension, Integer>();
            for(Dimension dimension : Dimension.values()) {
                dimCount.put(dimension, 0);
            }
            Map<MaterialType, Integer> mCount = new LinkedHashMap<MaterialType, Integer>();
            for(MaterialType type : MaterialType.values()) {
                mCount.put(type, 0);
            }

            for(MaterialDefinition def : definitions) {
                Dimension dim = def.getOre().getDimension();
                MaterialType mat = def.getMaterial().getType();
                dimCount.put(dim, dimCount.get(dim) + 1);
                mCount.put(mat, mCount.get(mat) + 1);
            }
            logger.info("Finished transformations, statistics:");
            logger.info("Definition Count: " + definitions.size());
            logger.info("Ores per Dimension: ");
            for(Map.Entry<Dimension, Integer> entry : dimCount.entrySet()) {
                logger.info("    " + entry.getKey().name() + ": " + entry.getValue() + " ore(s)");
            }
            logger.info("Material Types: ");
            for(Map.Entry<MaterialType, Integer> entry : mCount.entrySet()) {
                logger.info("    " + entry.getKey().name() + ": " + entry.getValue() + " ore(s)");
            }

            //DO STUFF WITH THOSE DEFS MAN!
        }
    }

    public List<MaterialDefinition> makeDefinitions(List<Color> colors) {
        List<MaterialDefinition> definitions = new ArrayList<MaterialDefinition>();
        for(Color color : colors) {
            Random random = new Random(color.getRGB());
            MaterialType type = MaterialType.values()[random.nextInt(MaterialType.values().length)];
            MaterialComponent material = new MaterialComponent(type, random.nextInt(6), random.nextInt(6000) + 500, random.nextFloat() * random.nextInt(5) + 1, random.nextFloat() * random.nextInt(20) + 1, random.nextInt(50) + 1);
            OreComponent ore = new OreComponent(material, Dimension.values()[random.nextInt(Dimension.values().length)], random.nextInt(4), 1, random.nextInt(20), 2, random.nextInt(255), 0, 5, random.nextInt(50) + 5, random.nextBoolean() || material.getType() == MaterialType.INGOT, random.nextFloat() * random.nextInt(10), random.nextFloat() * random.nextInt(8), random.nextFloat() * random.nextInt(20));
            List<CraftableComponent> components = new ArrayList<CraftableComponent>();
            boolean hasComponents = false;
            if(percentChance(75, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.HELMET, 1));
                components.add(new CraftableComponent(CraftableType.CHESTPLATE, 1));
                components.add(new CraftableComponent(CraftableType.LEGGINGS, 1));
                components.add(new CraftableComponent(CraftableType.BOOTS, 1));
            }
            if(percentChance(75, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.PICKAXE, 1));
                components.add(new CraftableComponent(CraftableType.AXE, 1));
                components.add(new CraftableComponent(CraftableType.HOE, 1));
                components.add(new CraftableComponent(CraftableType.SHOVEL, 1));
            }
            if(percentChance(75, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.SWORD, 1));
            }
            if(percentChance(50, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.BRICKS, 4));
            }
            if(percentChance(25, random)) {
                hasComponents = true;
                components.add(new CraftableComponent(CraftableType.STICK, 2));
            }
            if(!hasComponents) { //Basically, very, very rarely, an ore will be able to craft all possible craftable components
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
            MaterialDefinition definition = new MaterialDefinition(color, ore, components);
            definitions.add(definition);
        }
        return definitions;
    }

    public static boolean percentChance(double percent, Random random) {
        double result = random.nextDouble() * 100;
        return result <= percent;
    }

}
