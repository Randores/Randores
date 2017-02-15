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

import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.component.OreComponent;
import com.gmail.socraticphoenix.forge.randore.util.IntRange;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.ChunkPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class RandoresWorldGenerator implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if(!world.isRemote) {
            for (MaterialDefinition definition : MaterialDefinitionRegistry.get(Randores.getRandoresSeed(world))) {
                OreComponent component = definition.getOre();
                if (component.getDimension().getId() == world.provider.getDimension()) {
                    this.generateOre(component.makeBlock(), world, random, chunkX * 16, chunkZ * 16, component.getMaxVein(), component.getMinVein(), component.getMaxY(), component.getMinY(), component.getMaxOccurences(), component.getMinOccurences(), component.getDimension().getGenerateIn());
                }
            }
        }
    }

    private void generateOre(Block block, World world, Random random, int blockX, int blockZ, int maxVein, int minVein, int maxY, int minY, int maxOccurrences, int minOccurrences, Block[] generateIn) {
        IntRange vein = new IntRange(minVein, maxVein);
        IntRange height = new IntRange(minY, maxY);
        IntRange occurrences = new IntRange(minOccurrences, maxOccurrences);

        Predicate predicate;
        if(generateIn.length == 0) {
            predicate = new Predicate() {
                @Override
                public boolean apply(Object input) {
                    return true;
                }
            };
        } else {
            predicate =  BlockMatcher.forBlock(generateIn[0]);
            if(generateIn.length > 1) {
                for (int i = 1; i < generateIn.length; i++) {
                    predicate = this.or(predicate, BlockMatcher.forBlock(generateIn[i]));
                }
            }
        }

        WorldGenMinable gen = new WorldGenMinable(block.getDefaultState(), vein.randomElement(random), predicate);

        int i = 0;
        int o = occurrences.randomElement(random);
        while (i < o) {
            int x = blockX + random.nextInt(16);
            int y = height.randomElement(random);
            int z = blockZ + random.nextInt(16);
            if(predicate.apply(world.getBlockState(new BlockPos(x, y, z)))) {
                gen.generate(world, random, new BlockPos(x, y, z));
                i++;
            } else { //Generation for the specific block failed, check to make sure there are actually more blocks in the chunk we can generate in, and if so, try again
                Chunk chunk = world.getChunkFromBlockCoords(new BlockPos(x, y, z));
                ChunkPos pair = chunk.getPos();
                int x1 = Math.min(pair.getXStart(), pair.getXEnd());
                int y1 = minY;
                int z1 = Math.min(pair.getZStart(), pair.getZEnd());
                int x2 = Math.max(pair.getXStart(), pair.getXEnd());
                int y2 = maxY;
                int z2 = Math.max(pair.getZStart(), pair.getZEnd());

                boolean found = false;
                while (x1 < x2) {
                    while (z1 < z2) {
                        while (y1 < y2) {
                            BlockPos blockPos = new BlockPos(x1, y1, z1);
                            if(predicate.apply(world.getBlockState(blockPos))) {
                                found = true;
                                break;
                            }
                            y1++;
                        }
                        if(found) {
                            break;
                        }
                        z1++;
                    }
                    if(found) {
                        break;
                    }
                    x1++;
                }

                if(!found) {
                    return;
                }
            }
        }
    }

    private Predicate or(final Predicate a, final Predicate b) {
        return new Predicate() {
            @Override
            public boolean apply(Object input) {
                return a.apply(input) || b.apply(input);
            }
        };
    }


}
