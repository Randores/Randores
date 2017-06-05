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
package com.gmail.socraticphoenix.forge.randore.component.ability;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.component.Components;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItem;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemArmor;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnumEnchantmentType;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.inventory.EntityEquipmentSlot;
import net.minecraft.item.ItemStack;

public class EmpoweredEnchantment extends Enchantment {
    public static final EmpoweredEnchantment INSTANCE = new EmpoweredEnchantment();

    private static EntityEquipmentSlot[] slots = {EntityEquipmentSlot.CHEST, EntityEquipmentSlot.LEGS, EntityEquipmentSlot.HEAD, EntityEquipmentSlot.FEET};

    public EmpoweredEnchantment() {
        super(Rarity.COMMON, EnumEnchantmentType.ALL, EntityEquipmentSlot.values());
        this.setName("randores.empowered");
        this.setRegistryName("randores.empowered");
    }

    public static boolean appliedTo(ItemStack stack) {
        for (Enchantment en : EnchantmentHelper.getEnchantments(stack).keySet()) {
            if (en instanceof EmpoweredEnchantment) {
                return true;
            }
        }

        return false;
    }

    public static boolean appliedTo(EntityLivingBase entity) {
        for (EntityEquipmentSlot slot : slots) {
            if (entity.getItemStackFromSlot(slot) != null && EmpoweredEnchantment.appliedTo(entity.getItemStackFromSlot(slot))) {
                return true;
            }
        }
        return false;
    }

    public static void doArmor(EntityLivingBase hurt, EntityLivingBase cause) {
        for (EntityEquipmentSlot slot : slots) {
            if (!hurt.getItemStackFromSlot(slot).isEmpty() && EmpoweredEnchantment.appliedTo(hurt.getItemStackFromSlot(slot))) {
                ItemStack stack = hurt.getItemStackFromSlot(slot);
                if (stack.getItem() instanceof FlexibleItemArmor) {
                    MaterialDefinition definition = ((FlexibleItemArmor) stack.getItem()).getDefinition(Randores.getRandoresSeed(hurt.world));
                    definition.getAbilitySeries().onArmorHit(hurt, cause);
                }
            }
        }
    }

    public static void doPassiveArmor(EntityLivingBase entity) {
        for (EntityEquipmentSlot slot : slots) {
            if (!entity.getItemStackFromSlot(slot).isEmpty() && EmpoweredEnchantment.appliedTo(entity.getItemStackFromSlot(slot))) {
                ItemStack stack = entity.getItemStackFromSlot(slot);
                if (stack.getItem() instanceof FlexibleItemArmor) {
                    MaterialDefinition definition = ((FlexibleItemArmor) stack.getItem()).getDefinition(Randores.getRandoresSeed(entity.world));
                    definition.getAbilitySeries().onArmorUpdate(entity);
                }
            }
        }
    }

    @Override
    public boolean isTreasureEnchantment() {
        return true;
    }

    @Override
    public boolean canApply(ItemStack stack) {
        if (stack.getItem() instanceof FlexibleItem) {
            Components type = ((FlexibleItem) stack.getItem()).getType();
            if (!type.isBlock() && type.isCraftable()) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return this.canApply(stack);
    }

}
