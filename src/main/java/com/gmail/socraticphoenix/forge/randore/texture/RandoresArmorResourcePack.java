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

import com.gmail.socraticphoenix.forge.randore.resource.RandoresResourceManager;
import com.google.common.collect.Sets;
import net.minecraft.client.resources.IResourcePack;
import net.minecraft.client.resources.data.IMetadataSection;
import net.minecraft.client.resources.data.MetadataSerializer;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nullable;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class RandoresArmorResourcePack implements IResourcePack {
    public static final String DOMAIN = "randores_armor";
    private static Map<String, BufferedImage> textures;

    public RandoresArmorResourcePack() {
        textures = new HashMap<String, BufferedImage>();
    }

    public static void setTexture(String name, TextureData data) {
        BufferedImage image = new BufferedImage(data.getWidth(), data.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();
        g.setBackground(new Color(0, true));
        g.clearRect(0, 0, image.getWidth(), image.getHeight());
        g.dispose();
        image.setRGB(0, 0, data.getWidth(), data.getHeight(), data.getData()[0], 0, data.getWidth());
        textures.put(name, image);
    }

    public static void clear() {
        textures.clear();
    }

    @Override
    public InputStream getInputStream(ResourceLocation location) throws IOException {
        BufferedImage image = this.textures.get(location.getResourcePath());
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        ImageIO.write(image,"png", os);
        return new ByteArrayInputStream(os.toByteArray());

    }

    @Override
    public boolean resourceExists(ResourceLocation location) {
        return location.getResourceDomain().equals(DOMAIN) && textures.containsKey(location.getResourcePath());
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
    public BufferedImage getPackImage() throws IOException {
        return RandoresResourceManager.getImageResource("pack.png");
    }

    @Override
    public String getPackName() {
        return "RandoresCustomPack:ArmorModels";
    }

}
