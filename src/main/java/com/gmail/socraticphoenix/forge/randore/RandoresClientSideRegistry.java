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
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.resource.RandoresResourceManager;
import com.gmail.socraticphoenix.forge.randore.texture.FlexibleTextureRegistry;
import com.gmail.socraticphoenix.forge.randore.texture.RandoresArmorResourcePack;
import com.gmail.socraticphoenix.forge.randore.texture.TextureData;
import com.gmail.socraticphoenix.forge.randore.texture.TextureTemplate;
import com.google.common.base.Function;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.IResource;
import net.minecraft.crash.CrashReport;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

public class RandoresClientSideRegistry {
    public static final ResourceLocation TEMPLATES_DICT = new ResourceLocation("randores:resources/dictionary/tex_dict.txt");
    public static final ResourceLocation PACK = new ResourceLocation("randores:resources/others/pack.png");
    private static AtomicLong currentSeed = new AtomicLong();
    private static Map<String, TextureTemplate> templates = new HashMap<String, TextureTemplate>();
    private static BufferedImage pack;

    @SideOnly(Side.CLIENT)
    public static Map<String, TextureData> generateTextures(MaterialDefinition definition) {
        Map<String, TextureData> textures = new HashMap<String, TextureData>();
        textures.put(definition.getOre().template(), RandoresClientSideRegistry.getTemplate(definition.getOre().template()).applyWith(definition.getColor(), MaterialDefinition.DEFAULT_HUE_CHOICE));
        textures.put(definition.getMaterial().template(), RandoresClientSideRegistry.getTemplate(definition.getMaterial().template()).applyWith(definition.getColor(), MaterialDefinition.DEFAULT_HUE_CHOICE));
        for (CraftableComponent component : definition.getCraftables()) {
            if (component.getType() == CraftableType.BOW) {
                textures.put("bow_standby", RandoresClientSideRegistry.getTemplate("bow_standby_base").applyWith(definition.getColor(), MaterialDefinition.DEFAULT_HUE_CHOICE));
                textures.put("bow_pulling_0", RandoresClientSideRegistry.getTemplate("bow_pulling_0_base").applyWith(definition.getColor(), MaterialDefinition.DEFAULT_HUE_CHOICE));
                textures.put("bow_pulling_1", RandoresClientSideRegistry.getTemplate("bow_pulling_1_base").applyWith(definition.getColor(), MaterialDefinition.DEFAULT_HUE_CHOICE));
                textures.put("bow_pulling_2", RandoresClientSideRegistry.getTemplate("bow_pulling_2_base").applyWith(definition.getColor(), MaterialDefinition.DEFAULT_HUE_CHOICE));
            } else {
                if (component.getType() == CraftableType.HELMET) {
                    textures.put("armor_1", RandoresClientSideRegistry.getTemplate("armor_over_base").applyWith(definition.getColor(), MaterialDefinition.ARMOR_HUE_CHOICE));
                    textures.put("armor_2", RandoresClientSideRegistry.getTemplate("armor_under_base").applyWith(definition.getColor(), MaterialDefinition.ARMOR_HUE_CHOICE));
                }

                Function<Random, Boolean> hueChoice;
                if (component.getType() == CraftableType.BRICKS) {
                    hueChoice = MaterialDefinition.BRICK_HUE_CHOICE;
                } else {
                    hueChoice = MaterialDefinition.DEFAULT_HUE_CHOICE;
                }
                textures.put(component.template(), RandoresClientSideRegistry.getTemplate(component.template()).applyWith(definition.getColor(), hueChoice));
            }
        }
        return textures;
    }

    @SideOnly(Side.CLIENT)
    public static BufferedImage getPackImage() throws IOException {
        if (pack == null) {
            pack = ImageIO.read(Minecraft.getMinecraft().getResourceManager().getResource(PACK).getInputStream());
        }

        return pack;
    }

    @SideOnly(Side.CLIENT)
    public static IResource getResource(ResourceLocation location) throws IOException {
        IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(location);
        return resource;
    }

    @SideOnly(Side.CLIENT)
    public static TextureTemplate getTemplate(String key) {
        return RandoresClientSideRegistry.templates.get(key);
    }

    @SideOnly(Side.CLIENT)
    public static void registerTemplate(String key, TextureTemplate template) {
        RandoresClientSideRegistry.templates.put(key, template);
    }

    @SideOnly(Side.CLIENT)
    public static void loadTemplates() throws IOException {
        for (String template : RandoresResourceManager.getLines(Minecraft.getMinecraft().getResourceManager().getResource(TEMPLATES_DICT).getInputStream())) {
            if (template.contains("_base")) {
                IResource image = RandoresClientSideRegistry.getResource(new ResourceLocation("randores:resources/templates/" + template + ".png"));
                IResource config = RandoresClientSideRegistry.getResource(new ResourceLocation("randores:resources/templates/" + template + ".txt"));
                BufferedImage bufferedImage = ImageIO.read(image.getInputStream());
                image.close();

                List<String> configLines = RandoresResourceManager.getLines(config.getInputStream());
                TextureTemplate textureTemplate = new TextureTemplate(configLines, bufferedImage);
                RandoresClientSideRegistry.registerTemplate(template, textureTemplate);
            }
        }
    }

    @SideOnly(Side.CLIENT)
    public static void generateAndSetupTextures(List<MaterialDefinition> definitions, long seed) {
        RandoresArmorResourcePack.clear();
        for (int i = 0; i < definitions.size(); i++) {
            MaterialDefinition def = definitions.get(i);
            Map<String, TextureData> textures = RandoresClientSideRegistry.generateTextures(def);
            FlexibleTextureRegistry.getBlock(i).setTexture(textures.get(def.getOre().template()));
            FlexibleTextureRegistry.getItem(i).setTexture(textures.get(def.getMaterial().template()));
            for (CraftableComponent component : def.getCraftables()) {
                if (component.getType() == CraftableType.BOW) {
                    int k = i * 4;
                    FlexibleTextureRegistry.getBow(k).setTexture(textures.get("bow_standby"));
                    FlexibleTextureRegistry.getBow(k + 1).setTexture(textures.get("bow_pulling_0"));
                    FlexibleTextureRegistry.getBow(k + 2).setTexture(textures.get("bow_pulling_1"));
                    FlexibleTextureRegistry.getBow(k + 3).setTexture(textures.get("bow_pulling_2"));
                } else {
                    if (component.getType() == CraftableType.HELMET) {
                        RandoresArmorResourcePack.setTexture("randores.armor." + i + "_1.png", textures.get("armor_1"));
                        RandoresArmorResourcePack.setTexture("randores.armor." + i + "_2.png", textures.get("armor_2"));
                    }

                    if (component.getType().isBlock()) {
                        FlexibleTextureRegistry.getBlock(component.getType().getIndex(i)).setTexture(textures.get(component.template()));
                    } else {
                        FlexibleTextureRegistry.getItem(component.getType().getIndex(i)).setTexture(textures.get(component.template()));
                    }
                }
            }

        }
    }

    @SideOnly(Side.CLIENT)
    public static void rebindArmorTextures() {
        for (int i = 0; i < Randores.registeredAmount(); i++) {
            Minecraft.getMinecraft().getTextureManager().deleteTexture(new ResourceLocation(RandoresArmorResourcePack.DOMAIN + ":randores.armor." + i + "_1.png"));
            Minecraft.getMinecraft().getTextureManager().deleteTexture(new ResourceLocation(RandoresArmorResourcePack.DOMAIN + ":randores.armor." + i + "_2.png"));

            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(RandoresArmorResourcePack.DOMAIN + ":randores.armor." + i + "_1.png"));
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(RandoresArmorResourcePack.DOMAIN + ":randores.armor." + i + "_2.png"));
        }

    }

    @SideOnly(Side.CLIENT)
    public static void bindAllArmor() {
        for (int i = 0; i < Randores.registeredAmount(); i++) {
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(RandoresArmorResourcePack.DOMAIN + ":randores.armor." + i + "_1.png"));
            Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation(RandoresArmorResourcePack.DOMAIN + ":randores.armor." + i + "_2.png"));
        }
    }

    @SideOnly(Side.CLIENT)
    public static long getCurrentSeed() {
        return RandoresClientSideRegistry.currentSeed.get();
    }

    @SideOnly(Side.CLIENT)
    public static void setCurrentSeed(long currentSeed) {
        RandoresClientSideRegistry.currentSeed.set(currentSeed);
    }

    @SideOnly(Side.CLIENT)
    public static EntityPlayer getClientPlayer() {
        return Minecraft.getMinecraft().player;
    }

    @SideOnly(Side.CLIENT)
    public static void crash(String s, Throwable e) {
        Minecraft.getMinecraft().crashed(new CrashReport(s, e));
    }

    @SideOnly(Side.CLIENT)
    public static boolean isInitialized() {
        return RandoresClientSideRegistry.getCurrentSeed() != 0;
    }
}
