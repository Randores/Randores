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

import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumForgeContainer;
import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumForgeGui;
import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumForgeTileEntity;
import com.gmail.socraticphoenix.forge.randore.crafting.table.CraftiniumTableContainer;
import com.gmail.socraticphoenix.forge.randore.crafting.table.CraftiniumTableGui;
import com.gmail.socraticphoenix.forge.randore.tome.TomeGui;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import scala.actors.threadpool.Arrays;

import javax.annotation.Nullable;

public class RandoresGuiHandler implements IGuiHandler {

    @Nullable
    @Override
    public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (ID == RandoresGuiType.FORGE.ordinal()) {
            TileEntity entity = world.getTileEntity(pos);
            if (entity instanceof CraftiniumForgeTileEntity) {
                return new CraftiniumForgeContainer((CraftiniumForgeTileEntity) entity, player.inventory);
            }
        } else if (ID == RandoresGuiType.TABLE.ordinal()) {
            return new CraftiniumTableContainer(player.inventory, world, pos);
        }
        return null;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
        BlockPos pos = new BlockPos(x, y, z);
        if (ID == RandoresGuiType.FORGE.ordinal()) {
            TileEntity entity = world.getTileEntity(pos);
            if (entity instanceof CraftiniumForgeTileEntity) {
                return new CraftiniumForgeGui((CraftiniumForgeTileEntity) entity, player.inventory);
            }
        } else if (ID == RandoresGuiType.TABLE.ordinal()) {
            return new CraftiniumTableGui(player.inventory, world, new BlockPos(x, y, z));
        } else if (ID == RandoresGuiType.TOME.ordinal()) {
            return new TomeGui(Arrays.asList(new String[]{
                    "This is a page",
                    "this is another page",
                    "this is a " + TextFormatting.RED + "red" + TextFormatting.RESET + " page",
                    "this is a very long page that is long and probably takes too long to draw, it exists for the purpose of testing the algorithm that splits the text up onto multiple pages and hopefully this test is long enough. Let's see just how well this thing works, let's just go ahead and test it you know."
            }));
            /*ItemStack[] stacks = new ItemStack[] {player.getHeldItemMainhand(), player.getHeldItemOffhand()};
            for(ItemStack stack : stacks) {
                if(stack.getItem() instanceof ItemTome && Randores.hasRandoresSeed(stack) && Randores.hasRandoresIndex(stack)) {
                    int index = Randores.getRandoresIndex(stack);
                    long seed = Randores.getRandoresSeed(stack);
                    if(MaterialDefinitionRegistry.contains(seed, index)) {
                        return new TomeGui(MaterialDefinitionRegistry.get(seed).get(index).buildPages());
                    }
                }
            }*/
        }
        return null;
    }

}
