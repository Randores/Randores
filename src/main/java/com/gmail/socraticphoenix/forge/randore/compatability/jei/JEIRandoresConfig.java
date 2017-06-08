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
package com.gmail.socraticphoenix.forge.randore.compatability.jei;

import com.gmail.socraticphoenix.forge.randore.RandoresTabBlocks;
import com.gmail.socraticphoenix.forge.randore.RandoresTabItems;
import com.gmail.socraticphoenix.forge.randore.block.FlexibleBlockRegistry;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingBlocks;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItem;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemRegistry;
import mezz.jei.api.IJeiRuntime;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.IModRegistry;
import mezz.jei.api.ISubtypeRegistry;
import mezz.jei.api.JEIPlugin;
import mezz.jei.api.ingredients.IIngredientBlacklist;
import mezz.jei.api.ingredients.IModIngredientRegistration;
import mezz.jei.api.recipe.VanillaRecipeCategoryUid;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

@JEIPlugin
public class JEIRandoresConfig implements IModPlugin {

    @Override
    public void registerItemSubtypes(ISubtypeRegistry subtypeRegistry) {

    }

    @Override
    public void registerIngredients(IModIngredientRegistration registry) {

    }

    @Override
    public void register(IModRegistry registry) {
        registry.addRecipeCategoryCraftingItem(new ItemStack(CraftingBlocks.craftiniumTable), VanillaRecipeCategoryUid.CRAFTING);
        registry.addRecipeCategoryCraftingItem(new ItemStack(CraftingBlocks.craftiniumForge), VanillaRecipeCategoryUid.SMELTING);
        registry.addRecipeCategoryCraftingItem(new ItemStack(CraftingBlocks.craftiniumForge), VanillaRecipeCategoryUid.FUEL);

        IIngredientBlacklist blacklist = registry.getJeiHelpers().getIngredientBlacklist();
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabItem));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabAxe));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabHelmet));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabHoe));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabPickaxe));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabShovel));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabStick));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabSword));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabBattleaxe));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabSledgehammer));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabItems.tabBow));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabBlocks.tabOre));
        blacklist.addIngredientToBlacklist(new ItemStack(RandoresTabBlocks.tabTorch));

        for(FlexibleItem item : FlexibleItemRegistry.getAll()) {
            blacklist.addIngredientToBlacklist(new ItemStack((Item) item));
        }

        for(FlexibleItem block : FlexibleBlockRegistry.getAll()) {
            blacklist.addIngredientToBlacklist(new ItemStack((Block) block));
        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {

    }

}
