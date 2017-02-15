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
package com.gmail.socraticphoenix.forge.randore.item;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.component.CraftableComponent;
import com.gmail.socraticphoenix.forge.randore.component.CraftableType;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.Map;

public class FlexibleRecipe implements IRecipe {
    private int index;
    private CraftableType type;
    private char[][] rows;
    private Map<Character, Item> items;
    private int width;
    private int height;

    public FlexibleRecipe(int index, CraftableType type, String top, String middle, String bottom, Object... mappings) {
        this.index = index;
        this.type = type;
        this.rows = new char[][]{top.toCharArray(), middle.toCharArray(), bottom.toCharArray()};
        this.items = new HashMap<Character, Item>();
        for (int i = 0; i < mappings.length; i += 2) {
            Character character = (Character) mappings[i];
            Item item = (Item) mappings[i + 1];
            this.items.put(character, item);
        }

        int xMax = 0;
        int yMax = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                Item input = this.getInput(x, y);
                if (input != null) {
                    yMax = Math.max(yMax, y);
                    xMax = Math.max(xMax, x);
                }
            }
        }

        this.width = xMax;
        this.height = yMax;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn) {
        long seed = Randores.getRandoresSeed(worldIn);
        MaterialDefinition definition = MaterialDefinitionRegistry.get(seed).get(this.index);
        boolean hasType = false;
        for (CraftableComponent craftable : definition.getCraftables()) {
            if (craftable.getType() == this.type) {
                hasType = true;
                break;
            }
        }

        if (hasType) {
            for (int i = 0; i <= 3 - this.width; i++) {
                for (int j = 0; j <= 3 - this.height; j++) {
                    if (this.checkMatch(inv, i, j)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    private boolean checkMatch(InventoryCrafting inventory, int x, int y) {
        for (int i = 0; i < 3 - x; i++) {
            for (int j = 0; j < 3 - y; j++) {
                Item in = this.getInput(i, j);
                ItemStack stack = inventory.getStackInRowAndColumn(j + y, i + x);
                if (stack.isEmpty() && in != null) {
                    return false;
                } else if (in != null && !stack.getItem().equals(in)) {
                    return false;
                }
            }
        }
        return true;
    }

    private Item getInput(int x, int y) {
        return this.items.get(this.rows[y][x]);
    }

    @Override
    public ItemStack getCraftingResult(InventoryCrafting inv) {
        return this.getRecipeOutput();
    }

    @Override
    public int getRecipeSize() {
        return this.width * this.height;
    }

    @Override
    public ItemStack getRecipeOutput() {
        return null;
    }

    @Override
    public NonNullList<ItemStack> getRemainingItems(InventoryCrafting inv) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

}
