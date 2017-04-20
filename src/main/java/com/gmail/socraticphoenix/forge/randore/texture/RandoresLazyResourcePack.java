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
package com.gmail.socraticphoenix.forge.randore.texture;

import com.gmail.socraticphoenix.forge.randore.RandoresClientSideRegistry;
import com.gmail.socraticphoenix.forge.randore.component.Components;
import com.google.common.collect.Sets;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Set;
import java.util.regex.Pattern;

public class RandoresLazyResourcePack implements IResourcePack {
    public static final String DOMAIN = "randores";

    private static String generateModel(String path) {
        if (path.contains("models/item")) {
            return generateItemModel(path);
        } else if (path.contains("models/block")) {
            return generateBlockModel(path);
        } else {
            throw new IllegalStateException("Unreachable code");
        }
    }

    private static String generateBlockstate(String path) {
        String name = path.replaceFirst(Pattern.quote("blockstates/"), "").replace(".json", "");
        String[] pieces = name.split(Pattern.quote("."));
        return generateBlockstate(Integer.parseInt(pieces[2]));
    }

    private static String generateItemModel(String path) {
        if(path.contains("bow")) {
            return generateBowModel(path);
        } else {
            String name = path.replaceFirst(Pattern.quote("models/item/"), "").replace(".json", "");
            String[] pieces = name.split(Pattern.quote("."));
            return generateItemModel(Integer.parseInt(pieces[2]), pieces[1].equals("block"));
        }
    }

    private static String generateBowModel(String path) {
        if(path.contains("pulling")) {
            String name = path.replaceFirst(Pattern.quote("models/item/"), "").replace(".json", "");
            String[] pieces = name.replace("_pulling_0", "").replace("_pulling_1", "").replace("_pulling_2", "").split(Pattern.quote("."));
            String[] pullingPieces = name.split("_");
            return RandoresModelTemplates.make(RandoresModelTemplates.bowPulling(Integer.parseInt(pullingPieces[pullingPieces.length - 1])), Integer.parseInt(pieces[3]));
        } else {
            String name = path.replaceFirst(Pattern.quote("models/item/"), "").replace(".json", "");
            String[] pieces = name.split(Pattern.quote("."));
            return RandoresModelTemplates.make(RandoresModelTemplates.bowNormal(), Integer.parseInt(pieces[3]));
        }
    }

    private static String generateBlockModel(String path) {
        String name = path.replaceFirst(Pattern.quote("models/block/"), "").replace("_wall", "").replace(".json", "");
        String[] pieces = name.split(Pattern.quote("."));
        return generateBlockModel(Integer.parseInt(pieces[2]), path.contains("_wall"));
    }

    private static String generateBlockstate(int index) {
        Components component = Components.fromIndex(index, true);
        switch (component) {
            case ORE:
                return RandoresModelTemplates.make(RandoresModelTemplates.oreBlockState(), index);
            case BRICKS:
                return RandoresModelTemplates.make(RandoresModelTemplates.blockState(), index);
            case TORCH:
                return RandoresModelTemplates.make(RandoresModelTemplates.torchBlockState(), index);
        }
        throw new IllegalArgumentException("No block state for component: " + component);
    }

    private static String generateItemModel(int index, boolean block) {
        Components component = Components.fromIndex(index, block);
        switch (component) {
            case AXE:
            case HOE:
            case PICKAXE:
            case SHOVEL:
            case SWORD:
            case BATTLEAXE:
            case SLEDGEHAMMER:
                return RandoresModelTemplates.make(RandoresModelTemplates.toolItem(), index);
            case BOOTS:
            case CHESTPLATE:
            case HELMET:
            case LEGGINGS:
                return RandoresModelTemplates.make(RandoresModelTemplates.armorItem(), index);
            case BRICKS:
            case ORE:
                return RandoresModelTemplates.make(RandoresModelTemplates.blockItem(), index);
            case TORCH:
                return RandoresModelTemplates.make(RandoresModelTemplates.torchItem(), index);
            case STICK:
                return RandoresModelTemplates.make(RandoresModelTemplates.stickItem(), index);
            case MATERIAL:
                return RandoresModelTemplates.make(RandoresModelTemplates.materialItem(), index);
        }
        throw new IllegalArgumentException("No item model for component: " + component);
    }

    private static String generateBlockModel(int index, boolean wall) {
        Components component = Components.fromIndex(index, true);
        switch (component) {
            case ORE:
            case BRICKS:
                return RandoresModelTemplates.make(RandoresModelTemplates.blockModel(), index);
            case TORCH:
                if (wall) {
                    return RandoresModelTemplates.make(RandoresModelTemplates.torchWallModel(), index);
                } else {
                    return RandoresModelTemplates.make(RandoresModelTemplates.torchModel(), index);
                }
        }
        throw new IllegalArgumentException("No block model for component: " + component);
    }

    @Override
    public InputStream getInputStream(ResourceLocation location) throws IOException {
        String json;
        String path = location.getResourcePath();
        if (path.startsWith("blockstates")) {
            json = generateBlockstate(path);
        } else if (path.startsWith("models")) {
            json = generateModel(path);
        } else {
            throw new IllegalStateException("Unreachable code");
        }
        return new ByteArrayInputStream(json.getBytes("UTF8"));
    }

    @Override
    public boolean resourceExists(ResourceLocation location) {
        return location.getResourceDomain().equals(DOMAIN) && location.getResourcePath().endsWith(".json") && location.getResourcePath().contains("randores.") && (location.getResourcePath().contains("blockstates") || location.getResourcePath().contains("models"));
    }

    @Override
    public Set<String> getResourceDomains() {
        return Sets.newHashSet(DOMAIN);
    }

    @Nullable
    @Override
    public <T extends IMetadataSection> T getPackMetadata(MetadataSerializer metadataSerializer, String metadataSectionName) throws IOException {
        return null;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public BufferedImage getPackImage() throws IOException {
        return RandoresClientSideRegistry.getPackImage();
    }

    @Override
    public String getPackName() {
        return "RandoresCustomPack:LazyModels";
    }

}
