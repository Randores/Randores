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
import com.gmail.socraticphoenix.forge.randore.RandoresTranslations;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.ability.EmpoweredEnchantment;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FlexibleItemHelper {

    public static String getItemStackDisplayName(FlexibleItem item, ItemStack stack) {
        if (Randores.hasRandoresSeed(stack) && item.hasDefinition(Randores.getRandoresSeed(stack)) && item.getDefinition(Randores.getRandoresSeed(stack)).hasComponent(item.getType())) {
            MaterialDefinition definition = item.getDefinition(Randores.getRandoresSeed(stack));
            String format = RandoresTranslations.get(Randores.PROXY.getCurrentLocale(), RandoresTranslations.Keys.FORMAT);
            String name = definition.getName();
            String itemName = definition.getComponent(item.getType()).getLocalName(Randores.PROXY.getCurrentLocale());
            return format.replace("${name}", name).replace("${item}", itemName);
        }
        return null;
    }

    public static void onUpdate(ItemStack stack, World worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (!Randores.hasRandoresSeed(stack) || Randores.getRandoresSeed(worldIn) != Randores.getRandoresSeed(stack)) {
            Randores.applyData(stack, worldIn);
        }
    }

    public static void doEmpowered(ItemStack stack, EntityLivingBase target, EntityLivingBase attacker) {
        if(stack.getItem() instanceof FlexibleItem && EmpoweredEnchantment.appliedTo(stack)) {
            MaterialDefinition definition = ((FlexibleItem) stack.getItem()).getDefinition(Randores.getRandoresSeed(target.world));
            definition.getAbilitySeries().onMeleeHit(attacker, target);
        }
    }
}
