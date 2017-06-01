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
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleBow;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItem;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.entity.projectile.EntityThrowable;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;

public class EmpoweredHitListener {

    @SubscribeEvent
    public void onDamage(LivingHurtEvent ev) {
        Entity source = ev.getSource().getSourceOfDamage();
        EntityLivingBase target = ev.getEntityLiving();
        if (EmpoweredEnchantment.appliedTo(target) && source instanceof EntityThrowable) {
            ItemStack stack1 = EmpoweredEnchantment.isolate(target);
            if (stack1.getItem() instanceof FlexibleItem && ((FlexibleItem) stack1.getItem()).types().contains(AbilityType.ARMOR_ACTIVE)) {
                ((FlexibleItem) stack1.getItem()).getDefinition(target.world).getAbilitySeries().onArmorHit(target, ((EntityThrowable) source).getThrower());
            }
        } else if (EmpoweredEnchantment.appliedTo(target) && source instanceof EntityArrow && ((EntityArrow) source).shootingEntity instanceof EntityLivingBase) {
            ItemStack stack1 = EmpoweredEnchantment.isolate(target);
            if (stack1.getItem() instanceof FlexibleItem && ((FlexibleItem) stack1.getItem()).types().contains(AbilityType.ARMOR_ACTIVE)) {
                ((FlexibleItem) stack1.getItem()).getDefinition(target.world).getAbilitySeries().onArmorHit(target, (EntityLivingBase) ((EntityArrow) source).shootingEntity);
            }
        }

        if (source instanceof EntityLivingBase && !source.world.isRemote) {
            EntityLivingBase attacker = (EntityLivingBase) source;
            ItemStack stack = attacker.getHeldItemMainhand();
            if (stack != null && stack.getItem() instanceof FlexibleItem && EmpoweredEnchantment.appliedTo(stack) && ((FlexibleItem) stack.getItem()).types().contains(AbilityType.MELEE)) {
                ((FlexibleItem) stack.getItem()).getDefinition(target.world).getAbilitySeries().onMeleeHit(attacker, target);
            }
        } else if (source instanceof EntityArrow && ((EntityArrow) source).shootingEntity instanceof EntityLivingBase && !source.world.isRemote && Randores.hasRandoresIndex(source.getEntityData()) && Randores.hasRandoresSeed(source.getEntityData())) {
            int index = Randores.getRandoresIndex(source.getEntityData());
            long seed = Randores.getRandoresSeed(source.getEntityData());
            if (MaterialDefinitionRegistry.contains(seed, index)) {
                MaterialDefinition definition = MaterialDefinitionRegistry.get(seed).get(index);
                definition.getAbilitySeries().onProjectileHitEntity((EntityLivingBase) ((EntityArrow) source).shootingEntity, target);
            }
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ServerTickEvent ev) {
        FlexibleBow.onUpdate();
    }

}
