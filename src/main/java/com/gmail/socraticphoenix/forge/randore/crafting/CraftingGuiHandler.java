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
package com.gmail.socraticphoenix.forge.randore.crafting;

import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumForgeContainer;
import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumForgeGui;
import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumForgeTileEntity;
import com.gmail.socraticphoenix.forge.randore.crafting.table.CraftiniumTableContainer;
import com.gmail.socraticphoenix.forge.randore.crafting.table.CraftiniumTableGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

import javax.annotation.Nullable;

public class CraftingGuiHandler implements IGuiHandler {

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (ID == CraftingGuiType.FORGE.ordinal()) {
            TileEntity entity = world.getTileEntity(pos);
            if (entity instanceof CraftiniumForgeTileEntity) {
                return new CraftiniumForgeContainer((CraftiniumForgeTileEntity) entity, player.inventory);
            }
        } else if (ID == CraftingGuiType.TABLE.ordinal()) {
            return new CraftiniumTableContainer(player.inventory, world, pos);
        }
        return null;
    }

    @Nullable
    @Override
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (ID == CraftingGuiType.FORGE.ordinal()) {
            TileEntity entity = world.getTileEntity(pos);
            if (entity instanceof CraftiniumForgeTileEntity) {
                return new CraftiniumForgeGui((CraftiniumForgeTileEntity) entity, player.inventory);
            }
        } else if (ID == CraftingGuiType.TABLE.ordinal()) {
            return new CraftiniumTableGui(player.inventory, world, new BlockPos(x, y, z));
        }
        return null;
    }

}
