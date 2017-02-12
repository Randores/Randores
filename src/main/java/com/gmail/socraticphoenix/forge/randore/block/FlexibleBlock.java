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
package com.gmail.socraticphoenix.forge.randore.block;

import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.OreComponent;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlexibleBlock extends Block {
    private MaterialDefinition definition;
    private OreComponent component;

    public FlexibleBlock() {
        super(Material.ROCK);
    }

    public void setDefinition(MaterialDefinition definition) {
        this.definition = definition;
        this.setComponent(definition.getOre());
    }

    public void setComponent(OreComponent component) {
        this.component = component;
        this.setProperties(component.getHardness(), component.getResistance());

    }

    public void setProperties(float hardness, float resistance) {
        this.blockHardness = hardness;
        this.blockResistance = resistance;
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        Random random = world instanceof World ? ((World)world).rand : RANDOM;

        List<ItemStack> drops = new ArrayList<ItemStack>();
        if(this.component.isRequiresSmelting()) {
            ItemStack stack = new ItemStack(this.component.makeItem(), 1);
            stack.setStackDisplayName(this.definition.getName() + " Ore");
            drops.add(stack);
        } else {
            int amount = (random.nextInt(this.component.getMaxDrops() - this.component.getMinDrops()) + this.component.getMinDrops()) + random.nextInt(fortune) * random.nextInt(fortune);
            ItemStack stack = new ItemStack(this.definition.getMaterial().makeItem(), amount);
            stack.setStackDisplayName(this.definition.getName() + " " + this.definition.getMaterial().getType().getName());
            drops.add(stack);
        }
        return drops;
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return this.canSilkHarvest();
    }
}
