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
import com.gmail.socraticphoenix.forge.randore.RandoresClientSideRegistry;
import com.gmail.socraticphoenix.forge.randore.component.Components;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.item.constructable.ConstructablePickaxe;
import com.google.common.collect.Multimap;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemPickaxe;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FlexiblePickaxe extends ItemPickaxe implements FlexibleItem {
    private Map<Long, ItemPickaxe> backers;
    private int index;

    public FlexiblePickaxe(int index) {
        super(Randores.MATERIAL_DEFAULT);
        this.index = index;
        this.backers = new HashMap<Long, ItemPickaxe>();
    }

    @Override
    public void addInformation(ItemStack stack, EntityPlayer playerIn, List<String> tooltip, boolean advanced) {
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT && this.getDefinition(playerIn.world).hasComponent(this.getType())) {
            tooltip.remove(0);
            MaterialDefinition definition = this.getDefinition(playerIn.world);
            tooltip.add(0, definition.getName() + " " + definition.getComponent(this.getType()).getLocalName(RandoresClientSideRegistry.getCurrentLocale()));
            tooltip.addAll(definition.generateLore(RandoresClientSideRegistry.getCurrentLocale()));
        }
        super.addInformation(stack, playerIn, tooltip, advanced);
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
    public boolean canHarvestBlock(IBlockState state, ItemStack stack) {
        if(this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).canHarvestBlock(state, stack);
        }
        return super.canHarvestBlock(state, stack);
    }

    @Override
    public float getStrVsBlock(ItemStack stack, IBlockState state) {
        if(this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).getStrVsBlock(stack, state);
        }
        return super.getStrVsBlock(stack, state);
    }

    @Override
    public boolean hitEntity(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if(this.hasBacker(attacker.world)) {
            return this.getBacker(attacker.world).hitEntity(stack, target, attacker);
        }
        return super.hitEntity(stack, target, attacker);
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        if(this.hasBacker(worldIn)) {
            return this.getBacker(worldIn).onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
        }
        return super.onBlockDestroyed(stack, worldIn, state, pos, entityLiving);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if(this.hasBacker(Randores.getRandoresSeed(toRepair))) {
            return this.getBacker(Randores.getRandoresSeed(toRepair)).getIsRepairable(toRepair, repair);
        }
        return super.getIsRepairable(toRepair, repair);
    }

    @Override
    public Multimap<String, AttributeModifier> getAttributeModifiers(EntityEquipmentSlot equipmentSlot, ItemStack stack) {
        if(this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).getAttributeModifiers(equipmentSlot, stack);
        }
        return super.getItemAttributeModifiers(equipmentSlot);
    }

    @Override
    public int getHarvestLevel(ItemStack stack, String toolClass, @Nullable EntityPlayer player, @Nullable IBlockState blockState) {
        if(this.hasBacker(player.world)) {
            return this.getBacker(player.world).getHarvestLevel(stack, toolClass, player, blockState);
        }
        return super.getHarvestLevel(stack, toolClass, player, blockState);
    }

    @Override
    public int getItemEnchantability(ItemStack stack) {
        if(this.hasBacker(Randores.getRandoresSeed(stack))) {
            return this.getBacker(Randores.getRandoresSeed(stack)).getItemEnchantability(stack);
        }
        return super.getItemEnchantability(stack);
    }


    public void registerBacker(long seed, ToolMaterial material) {
        this.backers.put(seed, new ConstructablePickaxe(material));
    }

    public boolean hasBacker(long seed) {
        return this.backers.containsKey(seed);
    }

    public void removeBacker(long seed) {
        this.backers.remove(seed);
    }

    public ItemPickaxe getBacker(long seed) {
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

    public ItemPickaxe getBacker(World world) {
        return this.getBacker(Randores.getRandoresSeed(world));
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
    public Components getType() {
        return Components.PICKAXE;
    }

}
