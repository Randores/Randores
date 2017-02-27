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
package com.gmail.socraticphoenix.forge.randore.compatability.waila;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.RandoresClientSideRegistry;
import com.gmail.socraticphoenix.forge.randore.RandoresTranslations;
import com.gmail.socraticphoenix.forge.randore.block.FlexibleOre;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItem;
import mcp.mobius.waila.api.IWailaConfigHandler;
import mcp.mobius.waila.api.IWailaDataAccessor;
import mcp.mobius.waila.api.IWailaDataProvider;
import mcp.mobius.waila.api.IWailaRegistrar;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;

import java.util.List;

public class RandoresWailaHandler implements IWailaDataProvider {

    @Override
    public ItemStack getWailaStack(IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return null;
    }

    @Override
    public List<String> getWailaHead(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return list;
    }

    @Override
    public List<String> getWailaBody(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        Block block = iWailaDataAccessor.getBlock();
        Item item = Item.getItemFromBlock(block);
        String locale = RandoresClientSideRegistry.getCurrentLocale();
        if(item instanceof FlexibleItem) {
            MaterialDefinition definition = ((FlexibleItem) item).getDefinition(Randores.getRandoresSeed(iWailaDataAccessor.getWorld()));
            list.addAll(definition.generateBlockLore(locale));
            if(block instanceof FlexibleOre) {
                String canHarvest = TextFormatting.GREEN + "  " + t(RandoresTranslations.Keys.CAN_HARVEST, locale) + ": ";
                if (block.canHarvestBlock(iWailaDataAccessor.getWorld(), iWailaDataAccessor.getPosition(), iWailaDataAccessor.getPlayer())) {
                    canHarvest += TextFormatting.DARK_GREEN + "\u2714";
                } else {
                    canHarvest += TextFormatting.RED + "\u2718";
                }
                list.add(canHarvest);
            }
        }
        return list;
    }

    @Override
    public List<String> getWailaTail(ItemStack itemStack, List<String> list, IWailaDataAccessor iWailaDataAccessor, IWailaConfigHandler iWailaConfigHandler) {
        return list;
    }

    @Override
    public NBTTagCompound getNBTData(EntityPlayerMP entityPlayerMP, TileEntity tileEntity, NBTTagCompound nbtTagCompound, World world, BlockPos blockPos) {
        return null;
    }

    public static void callbackRegister(IWailaRegistrar registrar) {
        registrar.registerBodyProvider(new RandoresWailaHandler(), Block.class);
    }

    private String t(String s, String locale) {
        return RandoresTranslations.get(locale, s);
    }


}
