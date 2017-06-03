package com.gmail.socraticphoenix.forge.randore.compatability.jei.furnace;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.RandoresTranslations;
import com.gmail.socraticphoenix.forge.randore.compatability.jei.JEIRandoresConfig;
import mezz.jei.api.IGuiHelper;
import mezz.jei.api.gui.IDrawable;
import mezz.jei.api.gui.IDrawableAnimated;
import mezz.jei.api.gui.IDrawableStatic;
import mezz.jei.api.gui.IGuiItemStackGroup;
import mezz.jei.api.gui.IRecipeLayout;
import mezz.jei.api.ingredients.IIngredients;
import mezz.jei.api.recipe.BlankRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FurnaceSmeltingCategory extends BlankRecipeCategory<FlexibleSmeltWrapper> {
	private final IDrawable background;
	private final String localizedName;

    protected static final int inputSlot = 0;
    protected static final int fuelSlot = 1;
    protected static final int outputSlot = 2;

    protected final ResourceLocation backgroundLocation;
    protected final IDrawableAnimated flame;
    protected final IDrawableAnimated arrow;

	public FurnaceSmeltingCategory(IGuiHelper guiHelper) {
		ResourceLocation location = new ResourceLocation("minecraft", "textures/gui/container/furnace.png");
		background = guiHelper.createDrawable(location, 55, 16, 82, 54);
		localizedName = RandoresTranslations.get(Randores.PROXY.getCurrentLocale(), RandoresTranslations.Keys.CRAFTINIUM_FORGE);
        backgroundLocation = location;
        IDrawableStatic flameDrawable = guiHelper.createDrawable(backgroundLocation, 176, 0, 14, 14);
        flame = guiHelper.createAnimatedDrawable(flameDrawable, 300, IDrawableAnimated.StartDirection.TOP, true);

        IDrawableStatic arrowDrawable = guiHelper.createDrawable(backgroundLocation, 176, 14, 24, 17);
        this.arrow = guiHelper.createAnimatedDrawable(arrowDrawable, 200, IDrawableAnimated.StartDirection.LEFT, false);
	}

	@Override
	public IDrawable getBackground() {
		return background;
	}

	@Override
	public void drawExtras(Minecraft minecraft) {
		flame.draw(minecraft, 2, 20);
		arrow.draw(minecraft, 24, 18);
	}

	@Override
	public String getTitle() {
		return localizedName;
	}

	@Override
	public String getUid() {
		return JEIRandoresConfig.SMELTING_CATEGORY;
	}

	@Override
	public void setRecipe(IRecipeLayout recipeLayout, FlexibleSmeltWrapper recipeWrapper, IIngredients ingredients) {
		IGuiItemStackGroup guiItemStacks = recipeLayout.getItemStacks();

		guiItemStacks.init(inputSlot, true, 0, 0);
		guiItemStacks.init(outputSlot, false, 60, 18);

		guiItemStacks.set(ingredients);
	}

}
