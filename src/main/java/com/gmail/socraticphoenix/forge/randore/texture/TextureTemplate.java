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

import com.google.common.base.Function;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TextureTemplate {
    private List<PixelTemplate> pixelTemplates;
    private PixelTemplate[][] templatesMap;
    private int[] image;
    private int width;
    private int height;

    public TextureTemplate(List<String> content, BufferedImage image) {
        this.width = image.getWidth();
        this.height = image.getHeight();
        this.image = image.getRGB(0, 0, image.getWidth(), image.getHeight(), new int[this.width * this.height], 0, image.getWidth());

        this.pixelTemplates = new ArrayList<PixelTemplate>();
        for (String s : content) {
            if (!s.isEmpty()) {
                this.pixelTemplates.add(new PixelTemplate(s));
            }
        }

        this.templatesMap = new PixelTemplate[this.height][this.width];
        for (PixelTemplate template : this.pixelTemplates) {
            this.templatesMap[template.getY()][template.getX()] = template;
        }
    }

    public List<PixelTemplate> getPixelTemplates() {
        return this.pixelTemplates;
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public int getIntegerColor(Color color, boolean varyHue, int tint, int shade, Random random, Function<Random, Boolean> hueChoice) {
        if (varyHue) {
            if (hueChoice.apply(random)) {
                if (random.nextBoolean()) {
                    color = color.brighter();
                } else {
                    color = color.darker();
                }
            }
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

    public TextureData applyWith(Color color, Function<Random, Boolean> hueChoice) {
        Random random = new Random(color.getRGB());
        int[] image = this.image.clone();
        int yoff = 0;
        int off = 0;
        for (int y = 0; y < this.height; y++, yoff += this.width) {
            off = yoff;
            for (int x = 0; x < this.width; x++) {
                PixelTemplate template = this.templatesMap[y][x];
                if(template != null) {
                    image[off] = this.getIntegerColor(color, template.isVaryHue(), template.getTint(), template.getShade(), random, hueChoice);
                }
                off++;
            }

        }
        int[][] texData = new int[(int) (1 + (Math.log10(this.width) / Math.log10(2)))][];
        for (int i = 0; i < texData.length; i++) {
            texData[i] = image;
        }
        return new TextureData(texData, this.width, this.height);
    }

}
