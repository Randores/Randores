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
package com.gmail.socraticphoenix.forge.randore.block;

import com.gmail.socraticphoenix.forge.randore.Randores;
import com.gmail.socraticphoenix.forge.randore.component.Components;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.component.OreComponent;
import com.gmail.socraticphoenix.forge.randore.component.ability.AbilityType;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItem;
import com.gmail.socraticphoenix.forge.randore.util.IntRange;
import net.minecraft.block.Block;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class FlexibleOre extends Block implements FlexibleItem {
    private int index;
    public static final PropertyInteger HARVEST_LEVEL = PropertyInteger.create("harvest_level", 0, 15);
    private Random rand = new Random();

    public FlexibleOre(int index) {
        super(Material.ROCK);
        this.setSoundType(SoundType.STONE);
        this.index = index;
    }

    @Override
    public int index() {
        return this.index;
    }

    @Override
    public List<AbilityType> types() {
        return new ArrayList<AbilityType>();
    }

    @Override
    public boolean hasDefinition(long seed) {
        return MaterialDefinitionRegistry.contains(seed, this.index);
    }

    @Override
    public MaterialDefinition getDefinition(long seed) {
        return MaterialDefinitionRegistry.get(seed).get(this.index);
    }

    @Override
    public MaterialDefinition getDefinition(World world) {
        return this.getDefinition(Randores.getRandoresSeed(world));
    }

    @Override
    public Components getType() {
        return Components.ORE;
    }

    @Override
    public Item getThis() {
        return Item.getItemFromBlock(this);
    }



    public OreComponent getComponent(long seed) {
        return this.getDefinition(seed).getOre();
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
        List<ItemStack> drops = new ArrayList<ItemStack>();
        if (world instanceof World) {
            World worldw = (World) world;
            long seed = Randores.getRandoresSeed(worldw);

            if (this.getComponent(seed).isRequiresSmelting()) {
                ItemStack stack = new ItemStack(Item.getItemFromBlock(this), 1);
                Randores.applyData(stack, worldw);
                drops.add(stack);
            } else {
                int max = this.getComponent(seed).getMaxDrops();
                int min = this.getComponent(seed).getMinDrops();
                IntRange range = new IntRange(min, max);
                int amount = range.randomElement(this.rand);
                if(fortune > 0) {
                    int i = this.rand.nextInt(fortune + 2) - 1;
                    if(i < 0) {
                        i = 0;
                    }
                    amount = amount * (i + 1);
                }
                ItemStack stack = new ItemStack(this.getDefinition(seed).getMaterial().makeItem());
                Randores.applyData(stack, worldw);
                for (int i = 0; i < amount; i++) {
                    drops.add(stack.copy());
                }
            }
        }
        return drops;
    }

    @Override
    public IBlockState getStateForPlacement(World worldIn, BlockPos pos, EnumFacing facing, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer) {
        if(MaterialDefinitionRegistry.contains(Randores.getRandoresSeed(worldIn), this.index)) {
            return this.getDefaultState().withProperty(HARVEST_LEVEL, MaterialDefinitionRegistry.get(Randores.getRandoresSeed(worldIn)).get(this.index).getOre().getHarvestLevel());
        } else {
            return this.getDefaultState();
        }
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        return this.getDefaultState().withProperty(HARVEST_LEVEL, meta);
    }

    @Override
    public int getMetaFromState(IBlockState state) {
        return state.getValue(HARVEST_LEVEL);
    }

    @Override
    protected BlockStateContainer createBlockState() {
        return new BlockStateContainer(this, new IProperty[]{HARVEST_LEVEL});
    }

    @Override
    public float getExplosionResistance(Entity exploder) {
        return this.getComponent(Randores.getRandoresSeed(exploder.getEntityWorld())).getResistance();
    }

    @Override
    public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
        return this.getComponent(Randores.getRandoresSeed(worldIn)).getHardness();
    }

    @Override
    protected boolean canSilkHarvest() {
        return true;
    }

    @Nullable
    @Override
    public String getHarvestTool(IBlockState state) {
        return "pickaxe";
    }

    @Override
    public int getHarvestLevel(IBlockState state) {
        return state.getValue(HARVEST_LEVEL);
    }

}
