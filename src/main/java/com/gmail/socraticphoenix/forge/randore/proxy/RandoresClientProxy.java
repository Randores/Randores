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
package com.gmail.socraticphoenix.forge.randore.proxy;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.RandoresClientSideRegistry;
import com.gmail.socraticphoenix.forge.randore.RandoresTranslations;
import com.gmail.socraticphoenix.forge.randore.entity.RandoresArrow;
import com.gmail.socraticphoenix.forge.randore.resource.RandoresResourceManager;
import com.gmail.socraticphoenix.forge.randore.texture.FlexibleTextureRegistry;
import com.gmail.socraticphoenix.forge.randore.texture.RandoresArmorResourcePack;
import com.gmail.socraticphoenix.forge.randore.texture.RandoresLazyResourcePack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderTippedArrow;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class RandoresClientProxy implements RandoresProxy {
    public static final ResourceLocation LANG_DICT = new ResourceLocation("randores:resources/dictionary/lang_dict.txt");
    private AtomicInteger oreCount = new AtomicInteger();

    @Override
    @SideOnly(Side.CLIENT)
    public void preInit(FMLPreInitializationEvent ev) throws IOException, IllegalAccessException {
        FlexibleTextureRegistry.setTextureSeed(Randores.getInstance().getPreviousSeed());
        Logger logger = Randores.getInstance().getLogger();
        logger.info("Randores is running client-side.");
        Configuration configuration = Randores.getInstance().getConfiguration();
        configuration.load();
        logger.info("Loading languages...");
        for (String langFile : RandoresResourceManager.getLines(Minecraft.getMinecraft().getResourceManager().getResource(LANG_DICT).getInputStream())) {
            ResourceLocation location = new ResourceLocation("randores:resources/lang/" + langFile + ".lang");
            IResource resource = RandoresClientSideRegistry.getResource(location);
            List<String> lines = RandoresResourceManager.getLines(resource.getInputStream());
            String lang = RandoresResourceManager.getFileName(resource.getResourceLocation()).replace(".lang", "");
            for (String line : lines) {
                if (line.contains("=")) {
                    String[] pieces = line.split("=", 2);
                    RandoresTranslations.register(lang, pieces[0], pieces[1]);
                }
            }
        }
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
                packs.add(new RandoresLazyResourcePack());
                packs.add(new RandoresArmorResourcePack());
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
        logger.info("Registering entity renders...");
        RenderingRegistry.registerEntityRenderingHandler(RandoresArrow.class, new IRenderFactory<RandoresArrow>() {
            @Override
            public Render<? super RandoresArrow> createRenderFor(RenderManager manager) {
                return new RenderTippedArrow(manager);
            }
        });
        logger.info("Finished registering entity renders.");
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void init(FMLInitializationEvent ev) throws IOException {
        Logger logger = Randores.getInstance().getLogger();
        logger.info("Setting up armor textures...");
        RandoresArmorResourcePack.setupTest();
        RandoresClientSideRegistry.bindAllArmor();
        logger.info("Setup armor textures.");
    }

    @Override
    public void postInit(FMLPostInitializationEvent ev) {

    }

    @Override
    public String getCurrentLocale() {
        return Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
    }

    @Override
    public int oreCount() {
        return this.oreCount.get();
    }

    @Override
    public void setOreCount(int count) {
        this.oreCount.set(count);
    }

    @Override
    public long seed() {
        return RandoresClientSideRegistry.getCurrentSeed();
    }

}
