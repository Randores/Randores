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
package com.gmail.socraticphoenix.forge.randore.module.altar;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.probability.RandoresProbability;
import com.gmail.socraticphoenix.forge.randore.component.Component;
import com.gmail.socraticphoenix.forge.randore.component.Components;
import com.gmail.socraticphoenix.forge.randore.component.Dimension;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.util.IntRange;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockChest;
import net.minecraft.block.material.MaterialLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntityChest;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraftforge.fml.common.IWorldGenerator;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

public class RandoresAltarGenerator implements IWorldGenerator {
    private static IntRange levels = new IntRange(10, 20);

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        long seed = Randores.getRandoresSeed(world);
        if (!world.isRemote && world.provider.getDimension() == Dimension.OVERWORLD.getId()) {
            if (Randores.getInstance().getConfiguration().getCategory("modules").get("altar").getBoolean() && RandoresProbability.percentChance(Randores.getInstance().getConfiguration().getCategory("modules").get("youtubemode").getBoolean() ? 10 : 0.5, random)) {
                List<MaterialDefinition> definitions = MaterialDefinitionRegistry.get(seed);
                MaterialDefinition definition;
                int tries = 0;
                do {
                    definition = definitions.get(random.nextInt(definitions.size()));
                    tries++;
                } while (!definition.hasComponent(Components.BRICKS) && tries < 500);


                if (definition.hasComponent(Components.BRICKS)) {
                    int blockX = chunkX * 16 + 8;
                    int blockZ = chunkZ * 16 + 8;
                    blockX = random.nextBoolean() ? blockX - random.nextInt(5) : blockX + random.nextInt(5);
                    blockZ = random.nextBoolean() ? blockZ - random.nextInt(5) : blockZ + random.nextInt(5);
                    int blockY = world.getHeight();

                    boolean valid = false;
                    for (int i = blockY; i >= 0; i--) {
                        BlockPos pos = new BlockPos(blockX, i, blockZ);
                        if (world.getBlockState(pos).getBlock() != Blocks.AIR && !world.getBlockState(pos).getBlock().isReplaceable(world, pos)) {
                            blockY = i + 1;
                            if (world.getHeight() - 10 > blockY && world.getBlockState(pos).isOpaqueCube() && !(world.getBlockState(pos).getMaterial() instanceof MaterialLiquid) && !world.getBlockState(pos).getBlock().isLeaves(world.getBlockState(pos), world, pos)) {
                                int numBelow = 0;
                                BlockPos down = pos;
                                while (this.hasAir(world, down, 2, 2)) {
                                    numBelow++;
                                    down = down.add(0, -1, 0);
                                }
                                valid = numBelow < 10;
                            }
                            break;
                        }
                    }

                    if (valid) {
                        BlockPos center = new BlockPos(blockX, blockY, blockZ);
                        IBlockState state = Block.getBlockFromItem(definition.getComponent(Components.BRICKS).makeItem()).getDefaultState();
                        this.fill(world, center, null, 2, 2, random, true);
                        this.fill(world, center, state, 2, 2, random, false);
                        world.setBlockState(center, state);
                        for (int i = 1; i <= 4; i++) {
                            this.fill(world, center.add(0, i, 0), null, 2, 2, random, false);
                            this.fillPerimeter(world, center.add(0, i, 0), state, 2, 2, random);
                        }

                        BlockPos down = center.add(0, -1, 0);
                        while (this.hasAir(world, down, 2, 2)) {
                            this.fill(world, down, state, 2, 2, random, true);
                            down = down.add(0, -1, 0);
                        }

                        BlockPos valuables = center.add(0, 1, 0);
                        world.setBlockState(valuables, Blocks.CHEST.getDefaultState());
                        TileEntityChest chest = new TileEntityChest(BlockChest.Type.BASIC);
                        for (int i = 0; i < chest.getSizeInventory(); i++) {
                            if (RandoresProbability.percentChance(30, random)) {
                                List<Component> components = definition.getComponents(new Predicate<Components>() {
                                    @Override
                                    public boolean apply(@Nullable Components input) {
                                        return input != Components.ORE;
                                    }
                                });
                                Component component = components.get(random.nextInt(components.size()));
                                int size = component.quantity() == 1 ? 1 : random.nextInt(4) + component.quantity();
                                ItemStack stack = new ItemStack(component.makeItem(), size);
                                if(RandoresProbability.percentChance(10, random)) {
                                    stack = EnchantmentHelper.addRandomEnchantment(random, stack, levels.randomElement(random), true);
                                }
                                Randores.applyData(stack, seed);
                                chest.setInventorySlotContents(i, stack);
                            }
                        }
                        world.setTileEntity(valuables, chest);
                    }
                }
            }
        }
    }

    private boolean isAir(World world, BlockPos pos, int width, int length) {
        int minWidth = Math.min(width, -width);
        int minLength = Math.min(length, -length);
        int maxWidth = Math.max(width, -width);
        int maxLength = Math.max(length, -length);
        for (int x = minWidth; x <= maxWidth; x++) {
            for (int z = minLength; z <= maxLength; z++) {
                BlockPos loc = pos.add(x, 0, z);
                if (world.getBlockState(loc).getBlock() != Blocks.AIR) {
                    return false;
                }
            }
        }
        return true;
    }

    private boolean hasAir(World world, BlockPos pos, int width, int length) {
        int minWidth = Math.min(width, -width);
        int minLength = Math.min(length, -length);
        int maxWidth = Math.max(width, -width);
        int maxLength = Math.max(length, -length);
        for (int x = minWidth; x <= maxWidth; x++) {
            for (int z = minLength; z <= maxLength; z++) {
                BlockPos loc = pos.add(x, 0, z);
                if (world.getBlockState(loc).getBlock() == Blocks.AIR) {
                    return true;
                }
            }
        }
        return false;
    }

    private void fillPerimeter(World world, BlockPos pos, IBlockState state, int width, int length, Random random) {
        int minWidth = Math.min(width, -width);
        int minLength = Math.min(length, -length);
        int maxWidth = Math.max(width, -width);
        int maxLength = Math.max(length, -length);
        for (int x = minWidth; x <= maxWidth; x++) {
            for (int z = minLength; z <= maxLength; z++) {
                BlockPos loc = pos.add(x, 0, z);
                if (RandoresProbability.percentChance(70, random) && (x == minWidth || x == maxWidth || z == minLength || z == maxLength)) {
                    world.setBlockState(loc, state);
                }
            }
        }
    }

    private void fill(World world, BlockPos pos, IBlockState state, int width, int length, Random random, boolean override) {
        int minWidth = Math.min(width, -width);
        int minLength = Math.min(length, -length);
        int maxWidth = Math.max(width, -width);
        int maxLength = Math.max(length, -length);
        for (int x = minWidth; x <= maxWidth; x++) {
            for (int z = minLength; z <= maxLength; z++) {
                BlockPos loc = pos.add(x, 0, z);
                if(state == null) {
                    world.setBlockToAir(loc);
                } else {
                    if (override || RandoresProbability.percentChance(70, random)) {
                        world.setBlockState(loc, state);
                    }
                }
            }
        }
    }

}
