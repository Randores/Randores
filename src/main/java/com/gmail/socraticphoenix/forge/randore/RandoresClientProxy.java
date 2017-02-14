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
import com.gmail.socraticphoenix.forge.randore.block.FlexibleOre;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.crash.CrashReport;
import net.minecraft.item.Item;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RandoresClientProxy extends RandoresProxy {

    @Override
    public void preInit() {
        Logger logger = Randores.getInstance().getLogger();
        logger.info("Randores is running client-side.");
        logger.info("Loading configuration and checking caches...");
        Configuration configuration = Randores.getInstance().getConfiguration();
        configuration.load();
        ConfigCategory cache = configuration.getCategory("TextureCache");
        for (Map.Entry<String, Property> entry : cache.entrySet()) {
            try {
                long seed = Long.valueOf(entry.getKey());
                File texDir = Randores.getInstance().getTextureFile(seed);
                if (!texDir.exists()) {
                    logger.info("Cache entry determined invalid, " + entry.getKey() + " does not have a texture directory... removing.");
                    cache.remove(entry.getKey());
                } else {
                    long time = entry.getValue().getLong(TimeUnit.DAYS.toMillis(15));
                    Date date = new Date(time + TimeUnit.DAYS.toMillis(10));
                    Date now = new Date();
                    if (date.before(now)) {
                        logger.info("Texture cache for seed: " + seed + " expired, deleting textures...");
                        cache.remove(entry.getKey());
                        for (File tex : texDir.listFiles()) {
                            boolean succesful = tex.delete();
                            if (!succesful) {
                                logger.info("Failed to delete file " + tex.getAbsolutePath() + ", trying 3 more times...");
                            }
                            for (int i = 0; i < 3 && succesful; i++) {
                                succesful = tex.delete();
                                if (!succesful) {
                                    logger.info("Failed to delete file " + tex.getAbsolutePath() + ", trying" + (3 - (i + 1)) + "more times...");
                                }
                            }
                            if (!succesful) {
                                Minecraft.getMinecraft().crashed(new CrashReport("Failed to remove Randores cache file " + tex.getAbsolutePath(), new IOException("Failed to delete file")));
                            }
                        }
                        boolean succesful = texDir.delete();
                        if (!succesful) {
                            logger.info("Failed to delete file " + texDir.getAbsolutePath() + ", trying 3 more times...");
                        }
                        for (int i = 0; i < 3 && succesful; i++) {
                            succesful = texDir.delete();
                            if (!succesful) {
                                logger.info("Failed to delete file " + texDir.getAbsolutePath() + ", trying" + (3 - (i + 1)) + "more times...");
                            }
                        }
                        if (!succesful) {
                            Minecraft.getMinecraft().crashed(new CrashReport("Failed to remove Randores cache file " + texDir.getAbsolutePath(), new IOException("Failed to delete file")));
                        }
                    }
                    logger.info("Finished refreshing cache entries.");
                }
                configuration.save();
            } catch (NumberFormatException e) {
                logger.info("Cache entry determined invalid, " + entry.getKey() + " is not a long... removing.");
                cache.remove(entry.getKey());
            }
        }
    }

    @Override
    public void init() {
        Logger logger = Randores.getInstance().getLogger();
        for (Item item : FlexibleItemRegistry.getMaterials()) {
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation("randores:" + item.getUnlocalizedName().substring(5), "inventory"));
        }

        for (FlexibleOre block : FlexibleBlockRegistry.getOres()) {
            Item item = Item.getItemFromBlock(block);
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation("randores:" + item.getUnlocalizedName().substring(5)));
        }

        for (Block brick : FlexibleBlockRegistry.getBricks()) {
            Item item = Item.getItemFromBlock(brick);
            Minecraft.getMinecraft().getRenderItem().getItemModelMesher().register(item, 0, new ModelResourceLocation("randores:" + item.getUnlocalizedName().substring(5)));
        }
    }

}
