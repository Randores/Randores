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

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

public class RandoresWorldData extends WorldSavedData {
    private List<Color> ores;

    public RandoresWorldData() {
        super("randores_oreData");
        this.ores = new ArrayList<Color>();
    }

    public void add(Color color) {
        this.ores.add(color);
    }

    public List<Color> getOres() {
        return this.ores;
    }

    @Override
    public void readFromNBT(NBTTagCompound nbt) {
        this.ores.clear();
        NBTTagList ores = (NBTTagList) nbt.getTag("ores");
        for (int i = 0; i < ores.tagCount(); i++) {
            NBTTagCompound colorData = ores.getCompoundTagAt(i);
            int r = colorData.getInteger("r");
            int g = colorData.getInteger("g");
            int b = colorData.getInteger("b");
            int a = colorData.getInteger("a");
            this.ores.add(new Color(r, g, b, a));
        }
    }

    @Override
    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagList list = new NBTTagList();
        for(Color color : this.ores) {
            NBTTagCompound colorData = new NBTTagCompound();
            colorData.setInteger("r", color.getRed());
            colorData.setInteger("g", color.getGreen());
            colorData.setInteger("b", color.getBlue());
            colorData.setInteger("a", color.getAlpha());
            list.appendTag(colorData);
        }
        compound.setTag("ores", list);
        return compound;
    }

    public static RandoresWorldData get(World world) {
        MapStorage storage = world.getMapStorage();
        RandoresWorldData data = (RandoresWorldData) storage.getOrLoadData(RandoresWorldData.class, "randores_oreData");
        if(data == null) {
            data = new RandoresWorldData();
            storage.setData("randores_oreData", data);
        }
        return data;
    }

}
