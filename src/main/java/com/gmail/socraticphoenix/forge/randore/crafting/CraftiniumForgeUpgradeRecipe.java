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
package com.gmail.socraticphoenix.forge.randore.crafting;

import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumForge;
import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CraftiniumForgeUpgradeRecipe implements IRecipe {
    private ItemStack forge;
    private Map<Item, Float> values;

    public CraftiniumForgeUpgradeRecipe() {
        this.forge = new ItemStack(CraftingBlocks.craftiniumForge);
        this.values = new HashMap<Item, Float>();
    }

    public CraftiniumForgeUpgradeRecipe u(Item item, float f) {
        this.values.put(item, f);
        return this;
    }

    public CraftiniumForgeUpgradeRecipe u(Block item, float f) {
        return this.u(Item.getItemFromBlock(item), f);
    }

    private ItemStack atSpeed(ItemStack stack, int i) {
        ItemStack copy = stack.copy();
        copy.getOrCreateSubCompound("randores").setInteger("furnace_speed", i);
        return copy;
    }

    private ItemStack upgradeSpeed(ItemStack stack, int i) {
        ItemStack copy = stack.copy();
        int z = i + stack.getOrCreateSubCompound("randores").getInteger("furnace_speed");
        copy.getOrCreateSubCompound("randores").setInteger("furnace_speed", z >= 200 ? 200 : z);
        return copy;
    }

    private int speed(ItemStack stack) {
        return stack.getSubCompound("randores") == null ? 0 : stack.getSubCompound("randores").getInteger("furnace_speed");
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        ItemStack middle = inv.getStackInRowAndColumn(1, 1);
        if(middle.getItem() instanceof ItemBlock && ((ItemBlock) middle.getItem()).getBlock() instanceof CraftiniumForge) {
            int speed = this.speed(middle);
            if(speed >= 200) {
                return false;
            } else {
                for(ItemStack stack : this.getSurrounding(inv)) {
                    if(!this.values.containsKey(stack.getItem())) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        ItemStack middle = inv.getStackInRowAndColumn(1, 1);
        float upgradeAmount = 0f;
        for(ItemStack stack : this.getSurrounding(inv)) {
            upgradeAmount += this.values.get(stack.getItem());
        }
        return this.upgradeSpeed(middle, (int) upgradeAmount);
    }

    @Override
    public int getRecipeSize() {
        return 0;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return this.atSpeed(this.forge, 1);
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.withSize(9, ItemStack.EMPTY);
    }

    private List<ItemStack> getSurrounding(InventoryCrafting inv) {
        List<ItemStack> stacks = new ArrayList<ItemStack>();
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if(i != 1 || j != 1) {
                    stacks.add(inv.getStackInRowAndColumn(i, j));
                }
            }
        }
        return stacks;
    }

}
