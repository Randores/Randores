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
package com.gmail.socraticphoenix.forge.randore;

import com.gmail.socraticphoenix.forge.randore.item.FlexibleItem;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

import java.util.HashSet;
import java.util.Set;

public class RandoresLivingUpdate {
    private Set seen = new HashSet();

    @SubscribeEvent(priority = EventPriority.LOW)
    public void onUpdate(LivingEvent.LivingUpdateEvent ev) {
        EntityLivingBase entityLivingBase = ev.getEntityLiving();
        if(!entityLivingBase.world.isRemote && !(entityLivingBase instanceof EntityPlayer)) {
            for (EnumFacing facing : EnumFacing.values()) {
                if (entityLivingBase.hasCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing)) {
                    IItemHandler handler = entityLivingBase.getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, facing);
                    if (!seen.contains(handler)) {
                        seen.add(handler);
                        for (int i = 0; i < handler.getSlots(); i++) {
                            ItemStack stack = handler.getStackInSlot(i);
                            if(stack.getItem() instanceof FlexibleItem) {
                                stack.getItem().onUpdate(stack, entityLivingBase.world, entityLivingBase, i, false);
                            }
                        }
                    }
                }
            }

            seen.clear();
        }
    }

}
