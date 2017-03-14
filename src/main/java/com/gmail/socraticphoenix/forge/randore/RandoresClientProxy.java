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
import com.gmail.socraticphoenix.forge.randore.texture.RandoresArmorResourcePack;
import com.gmail.socraticphoenix.forge.randore.texture.RandoresLazyResourcePack;
import com.gmail.socraticphoenix.forge.randore.texture.TextureTemplate;
import com.google.common.io.Files;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.crash.CrashReport;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class RandoresClientProxy extends RandoresProxy {

    @Override
    @SideOnly(Side.CLIENT)
    public void preInitSided() {
        Logger logger = Randores.getInstance().getLogger();
        logger.info("Randores is running client-side.");
        Configuration configuration = Randores.getInstance().getConfiguration();
        configuration.load();
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

            logger.info("Searching for template packs...");
            File templates = new File(Randores.getInstance().getConfDir(), "templates");
            if (templates.exists()) {
                File[] packs = templates.listFiles();
                if (packs != null) {
                    for (File dir : packs) {
                        logger.info("Attempting to load pack: " + dir.getName());
                        for (String dict : dictionary) {
                            File tex = new File(dir, dict + ".png");
                            File temp = new File(dir, dict + ".txt");
                            boolean success = true;
                            if (!tex.exists()) {
                                logger.info("No texture for template: " + dict);
                                success = false;
                            }

                            if (!temp.exists()) {
                                logger.info("No config for template: " + dict);
                                success = false;
                            }

                            if (success) {
                                TextureTemplate textureTemplate = new TextureTemplate(Files.readLines(temp, Charset.forName("UTF8")), ImageIO.read(tex));
                                RandoresClientSideRegistry.putTemplate(dir.getName() + ":" + dict, textureTemplate);
                            }
                        }
                        logger.info("Finished loading pack: " + dir.getName());
                    }
                } else {
                    logger.info("No template packs found.");
                }
            } else {
                logger.info("No template packs found.");
            }
        } catch (IOException e) {
            Minecraft.getMinecraft().crashed(new CrashReport("Unable to load texture templates.", e));
            return;
        }

        logger.info("Hacking resource packs...");
        List<Field> candidates = new ArrayList<Field>();
        for (Field field : Minecraft.class.getDeclaredFields()) {
            if (field.getType() == List.class && field.getGenericType() instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) field.getGenericType();
                if (parameterizedType.getActualTypeArguments().length == 1 && parameterizedType.getActualTypeArguments()[0] == IResourcePack.class) {
                    candidates.add(field);
                }
            }
        }
        logger.info("Number of candidates: " + candidates.size());
        if (candidates.size() == 1) {
            Field field = candidates.get(0);
            boolean accessible = field.isAccessible();
            field.setAccessible(true);
            try {
                List<IResourcePack> packs = (List<IResourcePack>) field.get(Minecraft.getMinecraft());
                packs.add(1, new RandoresArmorResourcePack());
                packs.add(1, new RandoresLazyResourcePack());
                logger.info("Successfully added packs to list, refreshing list");
                Minecraft.getMinecraft().refreshResources();
                logger.info("Successfully hacked default resource packs.");
            } catch (IllegalAccessException e) {
                Minecraft.getMinecraft().crashed(new CrashReport("Fatal error, candidate not accessible", e));
                return;
            } finally {
                field.setAccessible(accessible);
            }
        }  else {
            Minecraft.getMinecraft().crashed(new CrashReport("Fatal error, expected 1 candidate, but found " + candidates.size(), new IllegalStateException()));
            return;
        }
    }

    @Override
    public void initSided() {

    }


}
