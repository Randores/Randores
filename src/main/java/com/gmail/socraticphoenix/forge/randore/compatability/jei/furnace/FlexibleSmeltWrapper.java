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
package com.gmail.socraticphoenix.forge.randore.compatability.jei.furnace;

import com.gmail.socraticphoenix.forge.randore.crafting.forge.FlexibleSmelt;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeWrapper;
import mezz.jei.util.Translator;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.awt.Color;

public class FlexibleSmeltWrapper extends BlankRecipeWrapper {
    private FlexibleSmelt smelt;
    private ItemStack output;
    private ItemStack input;

    public FlexibleSmeltWrapper(FlexibleSmelt smelt) {
        this.smelt = smelt;
        this.output = smelt.tryClientGetOutput();
        this.input = smelt.tryClientGetInput();
    }

    public FlexibleSmelt getSmelt() {
        return this.smelt;
    }

    public ItemStack getOutput() {
        return this.output;
    }

    public ItemStack getInput() {
        return this.input;
    }

    @Override
    public void getIngredients(IIngredients ingredients) {
        ingredients.setInput(ItemStack.class, this.input);
        ingredients.setOutput(ItemStack.class, this.output);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void drawInfo(Minecraft minecraft, int recipeWidth, int recipeHeight, int mouseX, int mouseY) {
        float ex = this.smelt.tryClientGetXp();
        String exp = String.valueOf(ex);
        while (exp.length() > 5) {
            exp = exp.substring(0, exp.length() - 1);
        }
        float experience = Float.parseFloat(exp);

        if (experience > 0) {
            String experienceString = Translator.translateToLocalFormatted("gui.jei.category.smelting.experience", experience);
            FontRenderer fontRendererObj = minecraft.fontRendererObj;
            int stringWidth = fontRendererObj.getStringWidth(experienceString);
            fontRendererObj.drawString(experienceString, recipeWidth - stringWidth, 0, Color.gray.getRGB());
        }
    }

}
