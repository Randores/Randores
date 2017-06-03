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

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.RandoresClientSideRegistry;
import com.gmail.socraticphoenix.forge.randore.component.Component;
import com.gmail.socraticphoenix.forge.randore.component.Components;
import com.gmail.socraticphoenix.forge.randore.component.CraftableType;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.OreDictionary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlexibleRecipe implements CraftiniumRecipe {
    private int index;
    private Components type;
    private char[][] rows;
    private Map<Character, NonNullList<ItemStack>> items;
    private int width;
    private int height;

    private Object[] inputs;

    public FlexibleRecipe(int index, CraftableType type, Object... mappings) {
        this(index, Components.fromCraftable(type), type.getRecipe()[0], type.getRecipe()[1], type.getRecipe()[2], mappings);
    }

    public FlexibleRecipe(int index, Components type, String top, String middle, String bottom, Object... mappings) {
        this.index = index;
        this.type = type;
        this.rows = new char[][]{top.toCharArray(), middle.toCharArray(), bottom.toCharArray()};
        this.items = new HashMap<Character, NonNullList<ItemStack>>();
        for (int i = 0; i < mappings.length; i += 2) {
            NonNullList<ItemStack> stacks = NonNullList.create();
            Character character = (Character) mappings[i];
            Object obj = mappings[i + 1];
            ItemStack stack = ItemStack.EMPTY;
            if (obj instanceof Item) {
                stack = new ItemStack((Item) obj);
            } else if (obj instanceof ItemStack) {
                stack = (ItemStack) obj;
            } else if (obj instanceof Block) {
                stack = new ItemStack((Block) obj);
            } else if (obj instanceof String) {
                stacks.addAll(OreDictionary.getOres((String) obj));
            }

            if (!stack.isEmpty()) {
                stacks.add(stack);
            }

            this.items.put(character, stacks);
        }

        int xMax = 0;
        int yMax = 0;
        for (int y = 0; y < 3; y++) {
            for (int x = 0; x < 3; x++) {
                List<ItemStack> input = this.getInput(x, y);
                if (input != null) {
                    yMax = Math.max(yMax, y);
                    xMax = Math.max(xMax, x);
                }
            }
        }

        this.width = xMax + 1;
        this.height = yMax + 1;

        String shape = top + middle + bottom;
        this.inputs = new Object[shape.length()];
        int x = 0;
        for (char c : shape.toCharArray()) {
            Object input = this.items.get(c);
            this.inputs[x++] = input == null ? ItemStack.EMPTY : input;
        }

        List<Character> unused = new ArrayList<Character>();
        for(Character key : this.items.keySet()) {
            if(shape.indexOf(key) == -1) {
                unused.add(key);
            }
        }

        for (Character key : unused) {
            this.items.remove(key);
        }
    }

    public int getWidth() {
        return this.width;
    }

    public int getHeight() {
        return this.height;
    }

    public Object[] getInputs() {
        return this.inputs;
    }

    private boolean surroundingEmpty(InventoryCrafting crafting, int x, int y, int x2, int y2) {
        for (int x_ = 0; x_ < 3; x_++) {
            for (int y_ = 0; y_ < 3; y_++) {
                if ((x_ < x || x_ > x2 || y_ < y || y_ > y2) && !crafting.getStackInRowAndColumn(x_, y_).isEmpty()) {
                    return false;
                }
            }
        }
        return true;
    }

    private List<ItemStack[][]> inventories(InventoryCrafting crafting) {
        List<ItemStack[][]> inventories = new ArrayList<ItemStack[][]>();
        for (int x = 0; x <= 3 - this.width; x++) {
            for (int y = 0; y <= 3 - this.height; y++) {
                if (this.surroundingEmpty(crafting, x, y, x + this.width - 1, y + this.height - 1)) {
                    ItemStack[][] inventory = new ItemStack[this.height][this.width];
                    for (int y1 = 0; y1 < this.height; y1++) {
                        for (int x1 = 0; x1 < this.width; x1++) {
                            inventory[y1][x1] = crafting.getStackInRowAndColumn(x + x1, y + y1);
                        }
                    }
                    inventories.add(inventory);
                }
            }
        }

        List<ItemStack[][]> retVal = new ArrayList<ItemStack[][]>();
        for (ItemStack[][] inventory : inventories) {
            retVal.add(inventory);
            ItemStack[][] reflection = new ItemStack[inventory.length][];
            for (int y = 0; y < inventory.length; y++) {
                ItemStack[] row = inventory[y];
                ItemStack[] reflect = new ItemStack[row.length];
                for (int x = row.length - 1, i = 0; x >= 0; x--, i++) {
                    reflect[i] = row[x];
                }
                reflection[y] = reflect;
            }
            retVal.add(reflection);
        }
        return retVal;
    }

    @Override
    public boolean matches(InventoryCrafting inv, World worldIn, BlockPos table, EntityPlayer player) {
        long seed = Randores.getRandoresSeed(worldIn);
        MaterialDefinition definition = MaterialDefinitionRegistry.get(seed).get(this.index);
        if (definition.hasComponent(this.type)) {
            for (ItemStack[][] inventories : this.inventories(inv)) {
                if (this.checkMatch(inventories)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public ItemStack result(InventoryCrafting inv, World worldIn, BlockPos table, EntityPlayer player) {
        long seed = Randores.getRandoresSeed(worldIn);
        MaterialDefinition definition = MaterialDefinitionRegistry.get(seed).get(this.index);
        if (definition.hasComponent(this.type)) {
            Component component = definition.getComponent(this.type);
            ItemStack stack = new ItemStack(component.makeItem(), component.quantity());
            Randores.applyData(stack, worldIn);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public NonNullList<ItemStack> remaining(InventoryCrafting inv, World worldIn, BlockPos table, EntityPlayer player) {
        return NonNullList.withSize(inv.getSizeInventory(), ItemStack.EMPTY);
    }

    @SideOnly(Side.CLIENT)
    public boolean tryClientIsRegistered() {
        long seed = RandoresClientSideRegistry.getCurrentSeed();
        if (MaterialDefinitionRegistry.contains(seed, this.index)) {
            MaterialDefinition definition = MaterialDefinitionRegistry.get(seed).get(this.index);
            if (definition.hasComponent(this.type)) {
                return true;
            }
        }
        return false;
    }

    @SideOnly(Side.CLIENT)
    public ItemStack tryClientGetOutput() {
        long seed = RandoresClientSideRegistry.getCurrentSeed();
        if (MaterialDefinitionRegistry.contains(seed, this.index)) {
            MaterialDefinition definition = MaterialDefinitionRegistry.get(seed).get(this.index);
            if (definition.hasComponent(this.type)) {
                Component component = definition.getComponent(this.type);
                ItemStack stack = new ItemStack(component.makeItem(), component.quantity());
                Randores.applyData(stack, seed);
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    private boolean checkMatch(ItemStack[][] inventory) {
        for (int y = 0; y < inventory.length; y++) {
            ItemStack[] row = inventory[y];
            for (int x = 0; x < row.length; x++) {
                ItemStack inInventory = row[x];
                if (!this.matchesThis(x, y, inInventory)) {
                    return false;
                }
            }
        }

        return true;
    }

    private List<ItemStack> getInput(int x, int y) {
        return this.items.get(this.rows[y][x]);
    }

    private boolean matchesThis(int x, int y, ItemStack item) {
        char c = this.rows[y][x];
        if (c == ' ') {
            return item.isEmpty();
        } else {
            return OreDictionary.containsMatch(false, this.items.get(c), item);
        }
    }

}
