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

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.resource.RandoresResourceManager;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FlexibleAtlasSprite extends TextureAtlasSprite {
    private static int[][] test;
    private String texture;
    private long seed;

    public FlexibleAtlasSprite(String spriteName, String texture) {
        super(spriteName);
        this.texture = texture;
        this.seed = 0;
    }

    public String getTexture() {
        return this.texture;
    }

    public long getSeed() {
        return this.seed;
    }

    public void setTexture(String texture, long seed) {
        this.texture = texture;
        this.seed = seed;
    }

    @Override
    public void updateAnimation() {
        TextureUtil.uploadTextureMipmap(this.framesTextureData.get(0), this.width, this.height, this.originX, this.originY, false, false);
    }

    @Override
    public boolean hasCustomLoader(IResourceManager manager, ResourceLocation location) {
        return true;
    }

    @Override
    public boolean load(IResourceManager manager, ResourceLocation location) {
        try {
            if (test == null) {
                BufferedImage texImg = RandoresResourceManager.getImageResource("test.png");
                int[][] texData = new int[(int) (1 + (Math.log10(texImg.getWidth()) / Math.log10(2)))][];
                int[] buffer = new int[texImg.getHeight() * texImg.getWidth()];
                texImg.getRGB(0, 0, texImg.getWidth(), texImg.getHeight(), buffer, 0, texImg.getWidth());
                for (int i = 0; i < texData.length; i++) {
                    texData[i] = buffer;
                }
                test = texData;
            }
        } catch (IOException e) {
            Minecraft.getMinecraft().crashed(new CrashReport("\"Fatal error: Unable to load texture \"test\"", e));

        }

        String name = this.texture.endsWith(".png") ? this.texture : this.texture + ".png";
        File texture = new File(Randores.getInstance().getTextureFile(this.seed), name);
        BufferedImage texImg;
        try {
            if (texture.exists()) {
                texImg = ImageIO.read(texture);
            } else if (RandoresResourceManager.resourceExists(name)) {
                texImg = RandoresResourceManager.getImageResource(name);
            } else {
                this.framesTextureData.clear();
                this.framesTextureData.add(test.clone());
                return false;
            }
        } catch (IOException e) {
            Randores.getInstance().getLogger().error("Fatal Error: Unable to load texture \"" + this.texture + ",\" reverting to test texture.", e);
            this.framesTextureData.clear();
            this.framesTextureData.add(test.clone());
            return false;
        }
        this.setIconHeight(texImg.getHeight());
        this.setIconWidth(texImg.getWidth());
        int[][] texData = new int[(int) (1 + (Math.log10(texImg.getWidth()) / Math.log10(2)))][];
        int[] buffer = new int[texImg.getHeight() * texImg.getWidth()];
        texImg.getRGB(0, 0, texImg.getWidth(), texImg.getHeight(), buffer, 0, texImg.getWidth());
        for (int i = 0; i < texData.length; i++) {
            texData[i] = buffer;
        }
        this.framesTextureData.clear();
        this.framesTextureData.add(texData);
        return false;
    }

}
