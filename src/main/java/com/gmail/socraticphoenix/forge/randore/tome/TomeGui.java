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
package com.gmail.socraticphoenix.forge.randore.tome;

import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class TomeGui extends GuiScreen {
    public static final ResourceLocation BACKGROUND = new ResourceLocation("randores:textures/gui/tome.png");
    public static final ResourceLocation ICONS = new ResourceLocation("randores:textures/gui/tomeButtons.png");

    private int ySize = 166;
    private int xSize = 247;

    private List<String> pages;

    private int currentPage;

    public TomeGui(List<String> pages) {
        this.pages = this.clean(pages);
        this.currentPage = 0;
    }

    public String getPage() {
        return this.pages.get(this.currentPage);
    }

    public boolean hasNext() {
        return this.currentPage < this.pages.size() - 1;
    }

    public boolean hasPrevious() {
        return this.currentPage - 1 >= 0;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        super.drawScreen(mouseX, mouseY, partialTicks);
        this.drawDefaultBackground();

        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(BACKGROUND);
        int i = (this.width - this.xSize) / 2;
        int j = (this.height - this.ySize) / 2;
        this.drawTexturedModalRect(i, j, 0, 0, this.xSize, this.ySize);

        this.mc.getTextureManager().bindTexture(ICONS);
        if (this.hasPrevious()) {
            int bx = 5;
            int by = 139;
            int width = 14;
            int height = 22;
            int tx;
            int ty;

            if (this.previousButtonContains(bx, by, mouseX, mouseY)) {
                tx = 34;
                ty = 37;
            } else {
                tx = 34;
                ty = 5;
            }

            this.drawTexturedModalRect(bx, by, tx, ty, width, height);
        }

        if (this.hasNext()) {
            int bx = 229;
            int by = 139;
            int width = 14;
            int height = 22;
            int tx;
            int ty;

            if (this.nextButtonContains(bx, by, mouseX, mouseY)) {
                tx = 16;
                ty = 37;
            } else {
                tx = 16;
                ty = 5;
            }

            this.drawTexturedModalRect(bx, by, tx, ty, width, height);
        }

        this.fontRendererObj.drawSplitString(this.getPage(), 5, 5, 237, Color.BLACK.getRGB());
    }

    @Override
    protected void mouseReleased(int mouseX, int mouseY, int state) {
        super.mouseReleased(mouseX, mouseY, state);
        if(this.hasPrevious() && this.previousButtonContains(5, 139, mouseX, mouseY)) {
            this.currentPage--;
        } else if (this.hasNext() && this.nextButtonContains(229, 139, mouseX, mouseY)) {
            this.currentPage++;
        }
    }

    public boolean nextButtonContains(int bx, int by, int x, int y) {
        if (y >= by && y <= y + 21) {
            if (x >= bx && x <= bx + 4) {
                return true;
            }

            int y1 = by + 2;
            int y2 = by + 19;
            for (int x1 = bx + 4; x1 <= bx + 13; x1 += 2) {
                if ((x == x1 || x == x1 + 1) && y >= y1 && y <= y2) {
                    return true;
                }
            }
        }

        return false;
    }

    public boolean previousButtonContains(int bx, int by, int x, int y) {
        if (y >= by && y <= y + 21) {
            if (x >= bx + 10 && x <= bx + 13) {
                return true;
            }

            int y1 = by + 2;
            int y2 = by + 19;
            for (int x1 = bx + 9; x1 >= bx; x1 -= 2) {
                if ((x == x1 || x == x1 - 1) && y >= y1 && y <= y2) {
                    return true;
                }
            }
        }

        return false;
    }

    public List<String> clean(List<String> pages) {
        List<String> list = new ArrayList<String>();
        for (String k : pages) {
            if (this.fontRendererObj.getWordWrappedHeight(k, 237) >= 133) {
                StringBuilder n = new StringBuilder();
                String[] pieces = k.split(" ");
                for (String p : pieces) {
                    if (this.fontRendererObj.getWordWrappedHeight(n + p, 237) >= 133) {
                        list.add(n.toString());
                        n = new StringBuilder();
                    } else {
                        n.append(p);
                    }
                }

                if (n.length() > 0) {
                    list.add(n.toString());
                }
            } else {
                list.add(k);
            }
        }
        return list;
    }


}
