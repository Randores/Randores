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
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlexibleSword extends ItemSword implements FlexibleItem {
    private Map<Long, ItemSword> backers;
    private int index;

    public FlexibleSword(int index) {
        super(Randores.MATERIAL_DEFAULT);
        this.index = index;
        this.backers = new HashMap<Long, ItemSword>();
    }

    @Override
    public List<AbilityType> types() {
        return Arrays.asList(AbilityType.MELEE);
    }

    @Override
    public Item getThis() {
        return this;
    }

    @Override
    public boolean isFull3D() {
        return true;
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

    public void registerBacker(long seed, ToolMaterial material) {
        this.backers.put(seed, new ItemSword(material));
    }

    public boolean hasBacker(long seed) {
        return this.backers.containsKey(seed);
    }

    public void removeBacker(long seed) {
        this.backers.remove(seed);
    }

    public ItemSword getBacker(long seed) {
        return this.backers.get(seed);
    }

    public void registerBacker(World world, ToolMaterial material) {
        this.registerBacker(Randores.getRandoresSeed(world), material);
    }

    public boolean hasBacker(World world) {
        return this.hasBacker(Randores.getRandoresSeed(world));
    }

    public void removeBacker(World world) {
        this.removeBacker(Randores.getRandoresSeed(world));
    }

    public ItemSword getBacker(World world) {
        return this.getBacker(Randores.getRandoresSeed(world));
    }

    @Override
    public int getRGBDurabilityForDisplay(ItemStack stack) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).getRGBDurabilityForDisplay(stack);
        }
        return super.getRGBDurabilityForDisplay(stack);
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).getMaxDamage(stack);
        }
        return super.getMaxDamage(stack);
    }

    @Override
    public boolean isDamaged(ItemStack stack) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).isDamaged(stack);
        }
        return super.isDamaged(stack);
    }

    @Override
    public void setDamage(ItemStack stack, int damage) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            this.getBacker(Randores.getRandoresSeed(stack)).setDamage(stack, damage);
            return;
        }
        super.setDamage(stack, damage);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).getStrVsBlock(stack, state);
        }
        return super.getStrVsBlock(stack, state);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (this.hasBacker(attacker.world)) {
            return this.getBacker(attacker.world).hitEntity(stack, target, attacker);
        }
        FlexibleItemHelper.doEmpowered(stack, target, attacker);
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if (this.hasBacker(worldIn)) {
            return this.getBacker(worldIn).onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
        }
        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (this.hasBacker(Randores.getRandoresSeed(toRepair))) {
            return this.getBacker(Randores.getRandoresSeed(toRepair)).getIsRepairable(toRepair, repair);
        }
        return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).getAttributeModifiers(slot, stack);
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        if (this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).getItemEnchantability(stack);
        }
        return super.getItemEnchantability(stack);
    }

    @Override
    public float getDamageVsEntity() {
        return 0.0F;
    }

    @Override
    public int index() {
        return this.index;
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
        return Components.SWORD;
    }

}
