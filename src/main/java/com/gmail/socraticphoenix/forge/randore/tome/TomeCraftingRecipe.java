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

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingItems;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItem;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleMaterial;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemBook;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

public class TomeCraftingRecipe implements IRecipe {

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        ItemStack book = null;
        ItemStack material = null;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (stack.getItem() instanceof ItemBook) {
                if (book != null) {
                    return false;
                } else {
                    book = stack;
                }
            } else if (stack.getItem() instanceof FlexibleMaterial) {
                if (material != null) {
                    return false;
                } else {
                    material = stack;
                }
            }
        }

        return material != null && book != null;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        int index = 0;
        long seed = 0;
        for (int i = 0; i < inv.getSizeInventory(); i++) {
            ItemStack stack = inv.getStackInSlot(i);
            if (!stack.isEmpty() && stack.getItem() instanceof FlexibleItem && Randores.hasRandoresSeed(stack)) {
                seed = Randores.getRandoresSeed(stack);
                index = ((FlexibleItem) stack.getItem()).index();
                break;
            }
        }
        ItemStack base = this.getRecipeOutput();
        Randores.applyRandoresSeed(base, seed);
        Randores.applyRandoresIndex(base, index);
        return base;
    }

    @Override
    public int getRecipeSize() {
        return 1;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return new ItemStack(CraftingItems.tome);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

}
