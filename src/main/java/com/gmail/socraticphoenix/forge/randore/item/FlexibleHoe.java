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
import com.gmail.socraticphoenix.forge.randore.component.ability.EmpoweredEnchantment;
import com.google.common.collect.Multimap;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlexibleHoe extends ItemHoe implements FlexibleItem {
    private Map<Long, ItemHoe> backers;
    private int index;

    public FlexibleHoe(int index) {
        super(Randores.MATERIAL_DEFAULT);
        this.index = index;
        this.backers = new HashMap<Long, ItemHoe>();
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, net.minecraft.enchantment.Enchantment enchantment) {
        return enchantment instanceof EmpoweredEnchantment || enchantment.type.canEnchantItem(Items.DIAMOND_SHOVEL);
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        if(Randores.hasRandoresSeed(stack) && this.hasDefinition(Randores.getRandoresSeed(stack))) {
            return this.getDefinition(Randores.getRandoresSeed(stack)).getToolMaterial().getEnchantability();
        }

        return super.getItemEnchantability(stack);
    }

    @Override
    public Item getThis() {
        return this;
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

    public void registerBacker(long seed, ToolMaterial material) {
        this.backers.put(seed, new ItemHoe(material));
    }

    public boolean hasBacker(long seed) {
        return this.backers.containsKey(seed);
    }

    public void removeBacker(long seed) {
        this.backers.remove(seed);
    }

    public ItemHoe getBacker(long seed) {
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

    public ItemHoe getBacker(World world) {
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
    public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        if (this.hasBacker(worldIn)) {
            return this.getBacker(worldIn).onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
        }
        return super.onItemUse(player, worldIn, pos, hand, facing, hitX, hitY, hitZ);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if (this.hasBacker(attacker.world)) {
            return this.getBacker(attacker.world).hitEntity(stack, target, attacker);
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public boolean isFull3D() {
        return true;
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot slot, ItemStack stack) {
        long seed = Randores.getRandoresSeed(stack);
        if (this.hasBacker(seed)) {
            return this.getBacker(seed).getAttributeModifiers(slot, stack);
        }
        return super.getAttributeModifiers(slot, stack);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (this.hasBacker(Randores.getRandoresSeed(toRepair))) {
            return this.getBacker(Randores.getRandoresSeed(toRepair)).getIsRepairable(toRepair, repair);
        }
        return super.getIsRepairable(toRepair, repair);
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
        return Components.HOE;
    }

}
