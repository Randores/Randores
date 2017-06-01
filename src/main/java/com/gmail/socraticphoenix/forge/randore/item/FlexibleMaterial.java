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
import com.gmail.socraticphoenix.forge.randore.component.Components;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.component.ability.AbilityType;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.List;

public class FlexibleMaterial extends Item implements FlexibleItem {
    private int index;

    public FlexibleMaterial(int index) {
        this.index = index;
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        FlexibleItemHelper.addInformation(this, stack, playerIn, tooltip, advanced);
    }

    @Override
    public String getItemStackDisplayName(ItemStack stack) {
        String name = FlexibleItemHelper.getItemStackDisplayName(this, stack);
        return name == null ? super.getItemStackDisplayName(stack) : name;
    }

    @Override
    public void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        FlexibleItemHelper.onUpdate(stack, worldIn, entityIn, itemSlot, isSelected);
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public MaterialDefinition getDefinition(long seed) {
        return MaterialDefinitionRegistry.get(seed).get(this.index);
    }

    @Override
    public MaterialDefinition getDefinition(World world) {
        return this.getDefinition(Randores.getRandoresSeed(world));
    }

    @Override
    public boolean hasDefinition(long seed) {
        return MaterialDefinitionRegistry.contains(seed, this.index);
    }

    @Override
    public Components getType() {
        return Components.MATERIAL;
    }

    @Override
    public Item getThis() {
        return this;
    }

    @Override
    public List<AbilityType> types() {
        return Arrays.asList(AbilityType.MELEE);
    }

    @Override
    public int index() {
        return this.index;
    }

}
