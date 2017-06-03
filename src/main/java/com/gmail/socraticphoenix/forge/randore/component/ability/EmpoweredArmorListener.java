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
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemArmor;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class EmpoweredArmorListener {

    @SubscribeEvent
    public void onHurt(LivingHurtEvent ev) {
        DamageSource source = ev.getSource();
        Entity root = source.getEntity();
        if (root instanceof EntityLivingBase) {
            EntityLivingBase cause = (EntityLivingBase) root;
            EntityLivingBase hurt = ev.getEntityLiving();
            if (EmpoweredEnchantment.appliedTo(hurt)) {
                ItemStack armor = EmpoweredEnchantment.isolate(hurt);
                if (!armor.isEmpty() && armor.getItem() instanceof FlexibleItemArmor) {
                    MaterialDefinition definition = ((FlexibleItemArmor) armor.getItem()).getDefinition(Randores.getRandoresSeed(hurt.world));
                    definition.getAbilitySeries().onArmorHit(hurt, cause);
                }
            }
        }
    }

}
