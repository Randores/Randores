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
package com.gmail.socraticphoenix.forge.randore.crafting.table;

import mezz.jei.api.IJeiHelpers;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CraftiniumRecipeRegistry {
    private static List<CraftiniumRecipe> recipes = Collections.synchronizedList(new ArrayList<CraftiniumRecipe>());

    public static List<CraftiniumRecipe> getRecipes() {
        return CraftiniumRecipeRegistry.recipes;
    }

    public static List<FlexibleRecipe> getFlexible() {
        List<FlexibleRecipe> recipes = new ArrayList<FlexibleRecipe>();
        for(CraftiniumRecipe recipe : CraftiniumRecipeRegistry.recipes) {
            if(recipe instanceof FlexibleRecipe) {
                recipes.add((FlexibleRecipe) recipe);
            }
        }

        return recipes;
    }

    public static void register(CraftiniumRecipe recipe) {
        CraftiniumRecipeRegistry.recipes.add(recipe);
    }

    public static CraftiniumRecipe findMatching(InventoryCrafting inv, World worldIn, BlockPos table, EntityPlayer player) {
        for(CraftiniumRecipe recipe : CraftiniumRecipeRegistry.recipes) {
            if(recipe.matches(inv, worldIn, table, player)) {
                return recipe;
            }
        }

        return null;
    }

}
