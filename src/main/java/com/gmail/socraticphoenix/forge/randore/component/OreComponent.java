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
package com.gmail.socraticphoenix.forge.randore.component;

import net.minecraft.block.Block;
import net.minecraft.item.Item;

public class OreComponent implements Component {
    private MaterialComponent material;
    private Dimension dimension;

    private int maxDrops;
    private int minDrops;

    private int maxVein;
    private int minVein;

    private int maxY;
    private int minY;

    private int minOccurences;
    private int maxOccurences;

    private float smeltingXp;

    private float hardness;
    private float resistance;

    private boolean requiresSmelting;

    private Item item;

    public OreComponent(MaterialComponent material, Dimension dimension, int maxDrops, int minDrops, int maxVein, int minVein, int maxY, int minY, int minOccurences, int maxOccurences, boolean requiresSmelting, float smeltingXp, float hardness, float resistance, Item item) {
        this.material = material;
        this.dimension = dimension;
        this.maxDrops = maxDrops;
        this.minDrops = minDrops;
        this.maxVein = maxVein;
        this.minVein = minVein;
        this.maxY = maxY;
        this.minY = minY;
        this.minOccurences = minOccurences;
        this.maxOccurences = maxOccurences;
        this.requiresSmelting = requiresSmelting;
        this.smeltingXp = smeltingXp;
        this.hardness = hardness;
        this.resistance = resistance;
        this.item = item;
    }

    public Block makeBlock() {
        return Block.getBlockFromItem(this.makeItem());
    }

    @Override
    public Item makeItem() {
        return this.item;
    }

    @Override
    public int quantity() {
        return 1;
    }

    @Override
    public String getName() {
        return "Ore";
    }

    public float getHardness() {
        return this.hardness;
    }

    public float getResistance() {
        return this.resistance;
    }

    public float getSmeltingXp() {
        return this.smeltingXp;
    }

    public int getMaxVein() {
        return this.maxVein;
    }

    public int getMinVein() {
        return this.minVein;
    }

    public int getMaxY() {
        return this.maxY;
    }

    public int getMinY() {
        return this.minY;
    }

    public int getMinOccurences() {
        return this.minOccurences;
    }

    public int getMaxOccurences() {
        return this.maxOccurences;
    }

    public int getMaxDrops() {
        return this.maxDrops;
    }

    public int getMinDrops() {
        return this.minDrops;
    }

    public boolean isRequiresSmelting() {
        return this.requiresSmelting;
    }

    public MaterialType getType() {
        return this.material.getType();
    }

    public MaterialComponent getMaterial() {
        return this.material;
    }

    public Dimension getDimension() {
        return this.dimension;
    }

    public String template() {
        String ore = getType() == MaterialType.INGOT || getType() == MaterialType.DUST ? "ore_base" : "gem_ore_base";
        String dimension = this.dimension == Dimension.END ? "end_" : this.dimension == Dimension.NETHER ? "nether_" : "";
        return dimension + ore;
    }


}
