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
import com.gmail.socraticphoenix.forge.randore.texture.TextureTemplate;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.crash.CrashReport;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import org.apache.logging.log4j.Logger;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class RandoresClientProxy extends RandoresProxy {

    @Override
    public void preInitSided() {
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
                    long time = entry.getValue().getLong(System.currentTimeMillis() - TimeUnit.DAYS.toMillis(14));
                    Date date = new Date(time + TimeUnit.DAYS.toMillis(14));
                    Date now = new Date();
                    if (date.before(now)) {
                        logger.info("Texture cache for seed: " + seed + " expired, deleting textures...");
                        cache.remove(entry.getKey());
                        if(!delete(texDir)) {
                            return;
                        }
                        logger.info("Deleted textures for seed: " + seed);
                    }
                }
                configuration.save();
            } catch (NumberFormatException e) {
                logger.info("Cache entry determined invalid, " + entry.getKey() + " is not a long... removing.");
                cache.remove(entry.getKey());
            }
        }

        File[] dirs = Randores.getInstance().getTextureDir().listFiles();
        if(dirs != null) {
            for(File file : dirs) {
                String seed = file.getName().replace("_", "-");
                if(!cache.containsKey(seed)) {
                    logger.info("Texture directory determined invalid, " + seed + " is not registered in the texture cache... removing.");
                    if(!this.delete(file)) {
                        return;
                    }
                    logger.info("Deleted texture directory: " + seed);
                }
            }
        }

        ConfigCategory conf = configuration.getCategory("config");
        if(!conf.containsKey("invalidatecache")) {
            conf.put("invalidatecache", new Property("invalidatecache", "false", Property.Type.BOOLEAN));
        }

        boolean invalidate = conf.get("invalidatecache").getBoolean();
        if(invalidate) {
            logger.info("Texture cache invalidated, removing all textures...");
            cache.clear();
            conf.put("invalidatecache", new Property("invalidatecache", "false", Property.Type.BOOLEAN));
            configuration.save();
            File[] files = Randores.getInstance().getTextureDir().listFiles();
            if(files != null) {
                for (File file : files) {
                    if(!this.delete(file)) {
                        return;
                    }
                }
            }
            logger.info("Removed all textures.");
        }

        logger.info("Loading texture templates...");
        try {
            List<String> dictionary = RandoresResourceManager.getResourceLines("aa_dict.txt");
            for (String entry : dictionary) {
                List<String> config = RandoresResourceManager.getResourceLines(entry + ".txt");
                BufferedImage texture = RandoresResourceManager.getImageResource(entry + ".png");
                TextureTemplate template = new TextureTemplate(config, texture);
                RandoresClientSideRegistry.putTemplate(entry, template);
                logger.info("Successfully loaded texture template \"" + entry + "\"");
            }
        } catch (IOException e) {
            Minecraft.getMinecraft().crashed(new CrashReport("Unable to load texture templates.", e));
            return;
        }

        logger.info("Hacking default resource packs...");
        List<Field> candidates = new ArrayList<Field>();
        for(Field field : Minecraft.class.getDeclaredFields()) {
            if(field.getType() == List.class && field.getGenericType() instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                if(parameterizedType.getActualTypeArguments().length == 1 && parameterizedType.getActualTypeArguments()[0] == IResourcePack.class) {
                    candidates.add(field);
                }
            }
        }
        logger.info("Number of candidates: " + candidates.size());
        if(candidates.size() == 1) {
            Field field = candidates.get(0);
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            try {
                List<IResourcePack> packs = (List<IResourcePack>) field.get(Minecraft.getMinecraft());
                packs.add(new RandoresArmorResourcePack());
                logger.info("Succesfully hacked default resource packs.");
            } catch (IllegalAccessException e) {
                Minecraft.getMinecraft().crashed(new CrashReport("Fatal error, candidate not accessible", e));
                return;
            } finally {
                field.setAccessible(accessible);
            }
        } else {
            Minecraft.getMinecraft().crashed(new CrashReport("Fatal error, expected 1 candidate, but found " + candidates.size(), new IllegalStateException()));
            return;
        }
    }

    @Override
    public void initSided() {
    }

    private boolean delete(File texDir) {
        Logger logger = Randores.getInstance().getLogger();
        for (File tex : texDir.listFiles()) {
            boolean succesful = tex.delete();
            if (!succesful) {
                logger.info("Failed to delete file " + tex.getAbsolutePath() + ", trying 3 more times...");
            }
            for (int i = 0; i < 3 && !succesful; i++) {
                succesful = tex.delete();
                if (!succesful) {
                    logger.info("Failed to delete file " + tex.getAbsolutePath() + ", trying " + (3 - (i + 1)) + " more times...");
                }
            }
            if (!succesful) {
                Minecraft.getMinecraft().crashed(new CrashReport("Failed to remove Randores cache file " + tex.getAbsolutePath(), new IOException("Failed to delete file")));
                return false;
            }
        }
        boolean succesful = texDir.delete();
        if (!succesful) {
            logger.info("Failed to delete file " + texDir.getAbsolutePath() + ", trying 3 more times...");
        }
        for (int i = 0; i < 3 && !succesful; i++) {
            succesful = texDir.delete();
            if (!succesful) {
                logger.info("Failed to delete file " + texDir.getAbsolutePath() + ", trying " + (3 - (i + 1)) + " more times...");
            }
        }
        if (!succesful) {
            Minecraft.getMinecraft().crashed(new CrashReport("Failed to remove Randores cache file " + texDir.getAbsolutePath(), new IOException("Failed to delete file")));
            return false;
        }

        return true;
    }

}
