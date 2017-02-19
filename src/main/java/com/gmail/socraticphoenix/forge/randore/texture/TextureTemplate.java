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

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TextureTemplate {
    private List<PixelTemplate> pixelTemplates;
    private BufferedImage image;

    public TextureTemplate(List<String> content, BufferedImage image) {
        this.image = image;
        this.pixelTemplates = new ArrayList<PixelTemplate>();
        for (String s : content) {
            if (!s.isEmpty()) {
                this.pixelTemplates.add(new PixelTemplate(s));
            }
        }
    }

    public static boolean percentChance(double percent, Random random) {
        double result = random.nextDouble() * 100;
        return result <= percent;
    }

    private static BufferedImage deepCopy(BufferedImage bi) {
        ColorModel cm = bi.getColorModel();
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        WritableRaster raster = bi.copyData(null);
        return new BufferedImage(cm, raster, isAlphaPremultiplied, null);
    }

    public int getIntegerColor(Color color, boolean varyHue, int tint, int shade, Random random) {
        if (varyHue) {
            if (TextureTemplate.percentChance(33.33, random)) {
                color = color.brighter();
            } else if (TextureTemplate.percentChance(33.33, random)) {
                color = color.darker();
            } //else don't change the color
        }

        if (tint != 0) {
            for (int i = 0; i < tint; i++) {
                color = color.brighter();
            }
        }

        if (shade != 0) {
            for (int i = 0; i < shade; i++) {
                color = color.darker();
            }
        }

        return color.getRGB();
    }

    public BufferedImage applyWith(Color color) {
        Random random = new Random(color.getRGB());
        BufferedImage image = deepCopy(this.image);
        for (PixelTemplate template : this.pixelTemplates) {
            int x = template.getX();
            int y = template.getY();
            int rgb = this.getIntegerColor(color, template.isVaryHue(), template.getTint(), template.getShade(), random);
            image.setRGB(x, y, rgb);
        }
        return image;
    }

}
