/*
 * This file was copied and edited from the JEI source code (https://github.com/mezz/JustEnoughItems), which is licensed under the MIT license. Copyright to this file is retained by mezz.
 */
package com.gmail.socraticphoenix.forge.randore.compatability.jei.crafting;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.RandoresTranslations;
import com.gmail.socraticphoenix.forge.randore.compatability.jei.JEIRandoresConfig;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.ICraftingGridHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import mezz.jei.api.recipe.IRecipeWrapper;
import mezz.jei.api.recipe.wrapper.ICustomCraftingRecipeWrapper;
import mezz.jei.api.recipe.wrapper.IShapedCraftingRecipeWrapper;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import java.util.List;

public class CraftingRecipeCategory extends BlankRecipeCategory<IRecipeWrapper> {
    public static final int width = 116;
    public static final int height = 54;
    private static final int craftOutputSlot = 0;
    private static final int craftInputSlot1 = 1;
    private final IDrawable background;
    private final String localizedName;
    private final ICraftingGridHelper craftingGridHelper;

    public CraftingRecipeCategory(IGuiHelper guiHelper) {
        ResourceLocation location = new ResourceLocation("randores:textures/gui/craftinium_table.png");
        background = guiHelper.createDrawable(location, 29, 16, width, height);
        localizedName = RandoresTranslations.get(Randores.PROXY.getCurrentLocale(), RandoresTranslations.Keys.CRAFTINIUM_TABLE);
        craftingGridHelper = guiHelper.createCraftingGridHelper(craftInputSlot1, craftOutputSlot);
    }

    @Override
    public String getUid() {
        return JEIRandoresConfig.CRAFTING_CATEGORY;
    }

    @Override
    public String getTitle() {
        return localizedName;
    }

    @Override
    public IDrawable getBackground() {
        return background;
    }

    @Override
    public void setRecipe(IRecipeLayout recipeLayout, IRecipeWrapper recipeWrapper, IIngredients ingredients) {
        IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

        guiItemStacks.init(craftOutputSlot, false, 94, 18);

        for (int y = 0; y < 3; ++y) {
            for (int x = 0; x < 3; ++x) {
                int index = craftInputSlot1 + x + (y * 3);
                guiItemStacks.init(index, true, x * 18, y * 18);
            }
        }

        if (recipeWrapper instanceof ICustomCraftingRecipeWrapper) {
            ICustomCraftingRecipeWrapper customWrapper = (ICustomCraftingRecipeWrapper) recipeWrapper;
            customWrapper.setRecipe(recipeLayout, ingredients);
            return;
        }

        List<List<ItemStack>> inputs = ingredients.getInputs(ItemStack.class);
        List<List<ItemStack>> outputs = ingredients.getOutputs(ItemStack.class);

        if (recipeWrapper instanceof IShapedCraftingRecipeWrapper) {
            IShapedCraftingRecipeWrapper wrapper = (IShapedCraftingRecipeWrapper) recipeWrapper;
            craftingGridHelper.setInputs(guiItemStacks, inputs, wrapper.getWidth(), wrapper.getHeight());
        } else {
            craftingGridHelper.setInputs(guiItemStacks, inputs);
            recipeLayout.setShapeless();
        }
        guiItemStacks.set(craftOutputSlot, outputs.get(0));
    }

}
