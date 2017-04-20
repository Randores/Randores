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

import com.gmail.socraticphoenix.forge.randore.texture.RandoresArmorResourcePack;
import com.gmail.socraticphoenix.forge.randore.texture.RandoresLazyResourcePack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResourcePack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;

public class RandoresClientProxy extends RandoresProxy {

    @Override
    @SideOnly(Side.CLIENT)
    public void preInitSided() throws IOException, IllegalAccessException {
        Logger logger = Randores.getInstance().getLogger();
        logger.info("Randores is running client-side.");
        Configuration configuration = Randores.getInstance().getConfiguration();
        configuration.load();
        logger.info("Loading languages...");
        RandoresTranslations.registerFromResources();
        logger.info("Loaded languages.");
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
                throw e;
            } finally {
                field.setAccessible(accessible);
            }
        } else {
            throw new IllegalStateException("Fatal error, expected 1 candidate, but found " + candidates.size());
        }
    }

    @Override
    public void initSided() {

    }


}
