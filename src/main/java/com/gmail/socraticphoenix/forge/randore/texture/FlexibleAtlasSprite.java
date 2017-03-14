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
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.util.ResourceLocation;

import java.awt.image.BufferedImage;
import java.io.IOException;

public class FlexibleAtlasSprite extends TextureAtlasSprite {
    public static final int TEST_WIDTH = 32;

    private static int[][] test;
    private int[][] texture;
    private int width;
    private int height;

    public FlexibleAtlasSprite(String spriteName) {
        super(spriteName);
        this.texture = null;
        this.width = TEST_WIDTH;
        this.height = TEST_WIDTH;
    }

    public int[][] getTexture() {
        return this.texture;
    }

    public void setTexture(TextureData texture) {
        if(texture != null) {
            this.texture = texture.getData();
            this.width = texture.getWidth();
            this.height = texture.getHeight();
        } else {
            this.texture = null;
            this.width = TEST_WIDTH;
            this.height = TEST_WIDTH;
        }
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

        this.setIconHeight(this.height);
        this.setIconWidth(this.width);
        this.framesTextureData.clear();
        this.framesTextureData.add(this.texture != null ? this.texture : FlexibleAtlasSprite.test);
        return false;
    }

}
