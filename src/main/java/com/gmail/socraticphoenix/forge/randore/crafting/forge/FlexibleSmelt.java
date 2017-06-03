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
package com.gmail.socraticphoenix.forge.randore.crafting.forge;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.RandoresClientSideRegistry;
import com.gmail.socraticphoenix.forge.randore.component.Components;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.util.IntRange;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class FlexibleSmelt implements CraftiniumSmelt {
    private int index;

    public FlexibleSmelt(int index) {
        this.index = index;
    }

    public int getIndex() {
        return this.index;
    }

    private MaterialDefinition getDefinition(World world) {
        return MaterialDefinitionRegistry.get(Randores.getRandoresSeed(world)).get(this.index);
    }

    @SideOnly(Side.CLIENT)
    public boolean tryClientIsRegistered() {
        long seed = RandoresClientSideRegistry.getCurrentSeed();
        if (MaterialDefinitionRegistry.contains(seed, this.index)) {
            MaterialDefinition definition = MaterialDefinitionRegistry.get(seed).get(this.index);
            if (definition.hasComponent(Components.MATERIAL)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean matches(ItemStack in, World worldIn, BlockPos forge) {
        MaterialDefinition definition = this.getDefinition(worldIn);
        if (definition.getOre().makeItem().equals(in.getItem())) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public ItemStack result(ItemStack in, World worldIn, BlockPos forge) {
        MaterialDefinition definition = this.getDefinition(worldIn);
        Item material = definition.getMaterial().makeItem();
        int amount = new IntRange(definition.getOre().getMinDrops(), definition.getOre().getMaxDrops()).randomElement();
        ItemStack stack = new ItemStack(material, amount);
        Randores.applyData(stack, worldIn);
        return stack;
    }

    @SideOnly(Side.CLIENT)
    public ItemStack tryClientGetInput() {
        if (this.tryClientIsRegistered()) {
            long seed = RandoresClientSideRegistry.getCurrentSeed();
            if (MaterialDefinitionRegistry.contains(seed, this.index)) {
                MaterialDefinition definition = MaterialDefinitionRegistry.get(seed).get(this.index);
                ItemStack stack = new ItemStack(definition.getOre().makeItem());
                Randores.applyData(stack, seed);
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @SideOnly(Side.CLIENT)
    public ItemStack tryClientGetOutput() {
        if (this.tryClientIsRegistered()) {
            long seed = RandoresClientSideRegistry.getCurrentSeed();
            if (MaterialDefinitionRegistry.contains(seed, this.index)) {
                MaterialDefinition definition = MaterialDefinitionRegistry.get(seed).get(this.index);
                ItemStack stack = new ItemStack(definition.getMaterial().makeItem(), 1);
                Randores.applyData(stack, seed);
                return stack;
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean matchesExperience(ItemStack out, World worldIn, BlockPos forge) {
        MaterialDefinition definition = this.getDefinition(worldIn);
        return definition.getMaterial().makeItem().equals(out.getItem());
    }

    @Override
    public float experience(ItemStack in, World worldIn, BlockPos forge) {
        MaterialDefinition definition = this.getDefinition(worldIn);
        return definition.getOre().getSmeltingXp();
    }

    @SideOnly(Side.CLIENT)
    public float tryClientGetXp() {
        if (this.tryClientIsRegistered()) {
            long seed = RandoresClientSideRegistry.getCurrentSeed();
            if (MaterialDefinitionRegistry.contains(seed, this.index)) {
                MaterialDefinition definition = MaterialDefinitionRegistry.get(seed).get(this.index);
                return definition.getOre().getSmeltingXp();
            }
        }
        return 0f;
    }

    @Override
    public int maxResult(ItemStack in, World worldIn, BlockPos forge) {
        MaterialDefinition definition = this.getDefinition(worldIn);
        return definition.getOre().getMaxDrops();
    }

}
