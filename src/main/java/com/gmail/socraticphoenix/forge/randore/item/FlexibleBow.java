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
import com.gmail.socraticphoenix.forge.randore.entity.RandoresArrow;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.init.Enchantments;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.IItemPropertyGetter;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemBow;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.StatList;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundCategory;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;

public class FlexibleBow extends ItemBow implements FlexibleItem {
    private int index;

    public FlexibleBow(int index) {
        this.maxStackSize = 1;
        final FlexibleBow dis = this;
        this.addPropertyOverride(new ResourceLocation("pull"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return entityIn == null ? 0.0F : (!entityIn.getActiveItemStack().getItem().equals(dis) ? 0.0F : (float) (stack.getMaxItemUseDuration() - entityIn.getItemInUseCount()) / 20.0F);
            }
        });
        this.addPropertyOverride(new ResourceLocation("pulling"), new IItemPropertyGetter() {
            @SideOnly(Side.CLIENT)
            public float apply(ItemStack stack, @Nullable World worldIn, @Nullable EntityLivingBase entityIn) {
                return entityIn != null && entityIn.isHandActive() && entityIn.getActiveItemStack().equals(stack) ? 1.0F : 0.0F;
            }
        });
        this.index = index;
    }

    @Override
    public boolean isDamageable() {
        return true;
    }

    private ItemStack findAmmo(EntityPlayer player) {
        if (this.isArrow(player.getHeldItem(EnumHand.OFF_HAND))) {
            return player.getHeldItem(EnumHand.OFF_HAND);
        } else if (this.isArrow(player.getHeldItem(EnumHand.MAIN_HAND))) {
            return player.getHeldItem(EnumHand.MAIN_HAND);
        } else {
            for (int i = 0; i < player.inventory.getSizeInventory(); ++i) {
                ItemStack itemstack = player.inventory.getStackInSlot(i);

                if (this.isArrow(itemstack)) {
                    return itemstack;
                }
            }

            return ItemStack.EMPTY;
        }
    }

    public void onPlayerStoppedUsing(ItemStack stack, World worldIn, EntityLivingBase entityLiving, int timeLeft) {
        if (entityLiving instanceof EntityPlayer) {
            EntityPlayer entityplayer = (EntityPlayer) entityLiving;
            boolean flag = entityplayer.capabilities.isCreativeMode || EnchantmentHelper.getEnchantmentLevel(Enchantments.INFINITY, stack) > 0;
            ItemStack itemstack = this.findAmmo(entityplayer);

            int i = this.getMaxItemUseDuration(stack) - timeLeft;
            i = net.minecraftforge.event.ForgeEventFactory.onArrowLoose(stack, worldIn, entityplayer, i, !itemstack.isEmpty() || flag);
            if (i < 0) return;

            if (!itemstack.isEmpty() || flag) {
                if (itemstack.isEmpty()) {
                    itemstack = new ItemStack(Items.ARROW);
                }

                float f = getArrowVelocity(i);

                if ((double) f >= 0.1D) {
                    boolean flag1 = entityplayer.capabilities.isCreativeMode || (itemstack.getItem() instanceof ItemArrow && ((ItemArrow) itemstack.getItem()).isInfinite(itemstack, stack, entityplayer));

                    if (!worldIn.isRemote) {
                        RandoresArrow entityarrow = new RandoresArrow(worldIn, entityplayer);
                        entityarrow.seed = Randores.getRandoresSeed(worldIn);
                        entityarrow.index = this.index;
                        entityarrow.setPotionEffect(itemstack);
                        entityarrow.setAim(entityplayer, entityplayer.rotationPitch, entityplayer.rotationYaw, 0.0F, f * 3.0F, 1.0F);

                        if (f == 1.0F) {
                            entityarrow.setIsCritical(true);
                        }

                        int j = EnchantmentHelper.getEnchantmentLevel(Enchantments.POWER, stack);

                        if (j > 0) {
                            entityarrow.setDamage(entityarrow.getDamage() + (double) j * 0.5D + 0.5D);
                        }

                        int k = EnchantmentHelper.getEnchantmentLevel(Enchantments.PUNCH, stack);

                        if (k > 0) {
                            entityarrow.setKnockbackStrength(k);
                        }

                        if (EnchantmentHelper.getEnchantmentLevel(Enchantments.FLAME, stack) > 0) {
                            entityarrow.setFire(100);
                        }

                        stack.damageItem(1, entityplayer);

                        if (flag1 || entityplayer.capabilities.isCreativeMode && (itemstack.getItem() == Items.SPECTRAL_ARROW || itemstack.getItem() == Items.TIPPED_ARROW)) {
                            entityarrow.pickupStatus = EntityArrow.PickupStatus.CREATIVE_ONLY;
                        }

                        worldIn.spawnEntity(entityarrow);
                    }

                    worldIn.playSound(null, entityplayer.posX, entityplayer.posY, entityplayer.posZ, SoundEvents.ENTITY_ARROW_SHOOT, SoundCategory.PLAYERS, 1.0F, 1.0F / (itemRand.nextFloat() * 0.4F + 1.2F) + f * 0.5F);

                    if (!flag1 && !entityplayer.capabilities.isCreativeMode) {
                        itemstack.shrink(1);

                        if (itemstack.isEmpty()) {
                            entityplayer.inventory.deleteStack(itemstack);
                        }
                    }

                    entityplayer.addStat(StatList.getObjectUseStats(this));
                }
            }
        }
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        if (Randores.hasRandoresSeed(stack)) {
            long seed = Randores.getRandoresSeed(stack);
            if (this.hasDefinition(seed)) {
                return this.getDefinition(seed).getMaterial().getMaxUses();
            }
        }

        return super.getMaxDamage(stack);
    }

    @Override
    public boolean getIsRepairable(ItemStack toRepair, ItemStack repair) {
        if (Randores.hasRandoresSeed(toRepair)) {
            long seed = Randores.getRandoresSeed(toRepair);
            if (this.hasDefinition(seed)) {
                return this.getDefinition(seed).getMaterial().makeItem().equals(repair.getItem());
            }
        }

        return super.getIsRepairable(toRepair, repair);
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
    public int index() {
        return this.index;
    }

    @Override
    public boolean hasDefinition(long seed) {
        return MaterialDefinitionRegistry.contains(seed, this.index);
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
        return Components.BOW;
    }

    @Override
    public Item getThis() {
        return this;
    }

    @Override
    public List<AbilityType> types() {
        return Arrays.asList(AbilityType.PROJECTILE);
    }

}
