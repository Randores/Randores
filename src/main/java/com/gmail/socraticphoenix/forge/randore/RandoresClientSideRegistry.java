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

import com.gmail.socraticphoenix.forge.randore.texture.TextureTemplate;
import net.minecraft.client.Minecraft;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class RandoresClientSideRegistry {
    private static AtomicLong currentSeed = new AtomicLong();
    private static AtomicInteger oreNumber = new AtomicInteger();
    private static Map<String, Map<String, TextureTemplate>> templates = new HashMap<String, Map<String, TextureTemplate>>();

    public static int getOreCount() {
        return RandoresClientSideRegistry.oreNumber.get();
    }

    public static void setOreCount(int count) {
        RandoresClientSideRegistry.oreNumber.set(count);
    }

    public static boolean containsTemplate(String pack, String name) {
        return templates.containsKey(pack) && templates.get(pack).containsKey(name);
    }

    public static TextureTemplate getTemplate(String name) {
        String pack = Randores.getTexturePack();
        if(RandoresClientSideRegistry.containsTemplate(pack, name)) {
            return RandoresClientSideRegistry.templates.get(pack).get(name);
        } else {
            return RandoresClientSideRegistry.templates.get("vanilla").get(name);
        }
    }

    public static void putTemplate(String pack, String name, TextureTemplate textureTemplate) {
        if(!RandoresClientSideRegistry.templates.containsKey(pack)) {
            RandoresClientSideRegistry.templates.put(pack, new HashMap<String, TextureTemplate>());
        }
        RandoresClientSideRegistry.templates.get(pack).put(name, textureTemplate);
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

    @SideOnly(Side.CLIENT)
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
