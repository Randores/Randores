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
import com.gmail.socraticphoenix.forge.randore.resource.ResourceManager;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;

import javax.imageio.ImageIO;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class FlexibleAtlasSprite extends TextureAtlasSprite {
    private String texture;

    public FlexibleAtlasSprite(String spriteName, String texture) {
        super(spriteName);
        this.texture = texture;
    }

    private static BufferedImage scale(BufferedImage sbi, int imageType, int dWidth, int dHeight, double fWidth, double fHeight) {
        BufferedImage dbi = null;
        if (sbi != null) {
            dbi = new BufferedImage(dWidth, dHeight, imageType);
            Graphics2D g = dbi.createGraphics();
            AffineTransform at = AffineTransform.getScaleInstance(fWidth, fHeight);
            g.drawRenderedImage(sbi, at);
        }
        return dbi;
    }

    public String getTexture() {
        return this.texture;
    }

    public void setTexture(String texture) {
        this.texture = texture;
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
        File texture = new File(Randores.getInstance().getTexFile(), this.texture.endsWith(".png") ? this.texture : this.texture + ".png");
        try {
            BufferedImage texImg;
            try {
                texImg = this.texture.equals("test") ? ResourceManager.getImageResource("test.png") : ImageIO.read(texture);
            } catch (IOException e) {
                Randores.getInstance().getLogger().error("Fatal Error: Unable to load texture \"" + this.texture + ",\" reverting to test texture.", e);
                texImg = ResourceManager.getImageResource("test.png");
            }
            int[][] texData = new int[(int) (1 + (Math.log10(texImg.getWidth()) / Math.log10(2)))][];
            int[] buffer = new int[texImg.getHeight() * texImg.getWidth()];
            texImg.getRGB(0, 0, texImg.getWidth(), texImg.getHeight(), buffer, 0, texImg.getWidth());
            for (int i = 0; i < texData.length; i++) {
                texData[i] = buffer;
                texImg = scale(texImg, texImg.getType(), texImg.getWidth(), texImg.getHeight(), 0.5, 0.5);
                buffer = new int[texImg.getHeight() * texImg.getWidth()];
                texImg.getRGB(0, 0, texImg.getWidth(), texImg.getHeight(), buffer, 0, texImg.getWidth());
            }
            this.framesTextureData.clear();
            this.framesTextureData.add(texData);
        } catch (IOException e) {
            Randores.getInstance().getLogger().error("Fatal error: Unable to load texture \"" + this.texture + ",\" and unable to revert to test texture.");
            throw new RuntimeException(e);
        }
        return false;
    }

}
