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
package com.gmail.socraticphoenix.forge.randore.component.ability.abilities;

import com.gmail.socraticphoenix.forge.randore.component.ability.AbilityContext;
import com.gmail.socraticphoenix.forge.randore.component.ability.AbilityStage;
import com.gmail.socraticphoenix.forge.randore.component.ability.AbilityType;
import com.gmail.socraticphoenix.forge.randore.component.ability.AbstractAbility;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.Vec3d;
import scala.actors.threadpool.Arrays;

public class PotionEffectAbility extends AbstractAbility {
    private PotionEffect effect;
    private boolean passive;

    public PotionEffectAbility(Potion potion, boolean passive) {
        super(Arrays.<AbilityStage>asList(new Object[]{AbilityStage.FIRST, AbilityStage.MIDDLE, AbilityStage.LAST}), Arrays.<AbilityType>asList(passive ? new Object[]{AbilityType.ARMOR_PASSIVE} : new Object[]{AbilityType.PROJECTILE, AbilityType.MELEE, AbilityType.ARMOR_ACTIVE}), 5);
        this.effect = new PotionEffect(potion, 20 * 5, 1);
        this.passive = passive;
    }

    @Override
    public boolean apply(Vec3d location, EntityLivingBase activator, AbilityContext context) {
        if (this.passive && context.getType() == AbilityType.ARMOR_PASSIVE) {
            activator.addPotionEffect(this.effect);
        } else if (context.hasTarget()) {
            context.getTarget().addPotionEffect(this.effect);
        }

        return true;
    }

}
