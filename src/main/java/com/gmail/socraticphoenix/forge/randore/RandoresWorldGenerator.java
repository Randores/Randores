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

import com.gmail.socraticphoenix.forge.randore.component.Dimension;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.component.OreComponent;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingBlocks;
import com.gmail.socraticphoenix.forge.randore.util.IntRange;
import com.google.common.base.Predicate;
import net.minecraft.block.Block;
import net.minecraft.block.state.pattern.BlockMatcher;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.IChunkGenerator;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.feature.WorldGenMinable;
import net.minecraftforge.fml.common.IWorldGenerator;

import java.util.Random;

public class RandoresWorldGenerator implements IWorldGenerator {

    @Override
    public void generate(Random random, int chunkX, int chunkZ, World world, IChunkGenerator chunkGenerator, IChunkProvider chunkProvider) {
        if (!world.isRemote) {
            for (MaterialDefinition definition : MaterialDefinitionRegistry.get(Randores.getRandoresSeed(world))) {
                OreComponent component = definition.getOre();
                if (component.getDimension().getId() == world.provider.getDimension()) {
                    this.generateOre(component.makeBlock(), world, random, chunkX * 16, chunkZ * 16, component.getMaxVein(), component.getMinVein(), component.getMaxY(), component.getMinY(), component.getMaxOccurences(), component.getMinOccurences(), component.getDimension().getGenerateIn());
                }

                if(Dimension.OVERWORLD.getId() == world.provider.getDimension()) {
                    this.generateOre(CraftingBlocks.craftiniumOre, world, random, chunkX * 16, chunkZ * 16, 3, 1, 100, 0, 30, 5, Dimension.OVERWORLD.getGenerateIn());
                }
            }
        }
    }

    private void generateOre(Block block, World world, Random random, int blockX, int blockZ, int maxVein, int minVein, int maxY, int minY, int maxOccurrences, int minOccurrences, Block[] generateIn) {
        IntRange vein = new IntRange(minVein, maxVein);
        IntRange height = new IntRange(minY, maxY);
        IntRange occurrences = new IntRange(minOccurrences, maxOccurrences);

        Predicate predicate;
        if (generateIn.length == 0) {
            predicate = new Predicate() {
                @Override
                public boolean apply(Object input) {
                    return true;
                }
            };
        } else {
            predicate = BlockMatcher.forBlock(generateIn[0]);
            if (generateIn.length > 1) {
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
            gen.generate(world, random, new BlockPos(x, y, z));
            i++;
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
