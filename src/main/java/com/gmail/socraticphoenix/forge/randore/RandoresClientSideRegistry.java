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
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.client.resources.SimpleReloadableResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RandoresClientSideRegistry {
    public static final ResourceLocation TEMPLATES_DICT = new ResourceLocation("randores:resources/dictionary/tex_dict.txt");
    public static final ResourceLocation PACK = new ResourceLocation("randores:resources/others/pack.png");
    private static AtomicLong currentSeed = new AtomicLong();
    private static AtomicInteger oreNumber = new AtomicInteger();
    private static Map<String, TextureTemplate> templates = new HashMap<String, TextureTemplate>();
    private static BufferedImage pack;

    @SideOnly(Side.CLIENT)
    public static BufferedImage getPackImage() throws IOException {
        if (pack == null) {
            pack = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(PACK).getInputStream());
        }

        return pack;
    }

    public static TextureTemplate getTemplate(String key) {
        return RandoresClientSideRegistry.templates.get(key);
    }

    public static void registerTemplate(String key, TextureTemplate template) {
        RandoresClientSideRegistry.templates.put(key, template);
    }

    @SideOnly(Side.CLIENT)
    public static void loadTemplates() throws IOException {
        for (String template : RandoresResourceManager.getLines(Minecraft.getMinecraft().getResourceManager().getResource(TEMPLATES_DICT).getInputStream())) {
            if(template.contains("_base")) {
                IResource image = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("randores:resources/templates/" + template + ".png"));
                IResource config = Minecraft.getMinecraft().getResourceManager().getResource(new ResourceLocation("randores:resources/templates/" + template + ".txt"));
                BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
                image.close();

                List<String> configLines = RandoresResourceManager.getLines(config.getInputStream());
                TextureTemplate textureTemplate = new TextureTemplate(configLines, bufferedImage);
                RandoresClientSideRegistry.registerTemplate(template, textureTemplate);
            }
        }
    }

    public static int getOreCount() {
        return RandoresClientSideRegistry.oreNumber.get();
    }

    public static void setOreCount(int count) {
        RandoresClientSideRegistry.oreNumber.set(count);
    }

    public static long getCurrentSeed() {
        return RandoresClientSideRegistry.currentSeed.get();
    }

    public static void setCurrentSeed(long currentSeed) {
        RandoresClientSideRegistry.currentSeed.set(currentSeed);
    }

    @SideOnly(Side.CLIENT)
    public static EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }

    public static String getCurrentLocale() {
        return FMLCommonHandler.instance().getSide() == Side.CLIENT ? RandoresClientSideRegistry.clientSideLocale() : RandoresClientSideRegistry.serverSideLocale();
    }

    private static String clientSideLocale() {
        return Minecraft.getMinecraft().getLanguageManager().getCurrentLanguage().getLanguageCode();
    }

    private static String serverSideLocale() {
        return "en_us";
    }

    public static void crash(String s, Throwable e) {
        Minecraft.getMinecraft().crashed(new CrashReport(s, e));
    }

    public static boolean isInitialized() {
        return RandoresClientSideRegistry.getCurrentSeed() != 0;
    }
}
