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

import com.gmail.socraticphoenix.forge.randore.item.FlexibleItem;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class FlexibleBlockRegistry {
    private static List<FlexibleOre> ores = Collections.synchronizedList(new ArrayList<FlexibleOre>());
    private static List<FlexibleBrick> bricks = Collections.synchronizedList(new ArrayList<FlexibleBrick>());
    private static List<FlexibleTorch> torches = Collections.synchronizedList(new ArrayList<FlexibleTorch>());
    private static List<FlexibleItem> all = Collections.synchronizedList(new ArrayList<FlexibleItem>());

    public static List<FlexibleItem> getAll() {
        return all;
    }

    public static List<FlexibleTorch> getTorches() {
        return torches;
    }

    public static void addTorch(FlexibleTorch torch) {
        torches.add(torch);
        all.add(torch);
    }

    public static List<FlexibleBrick> getBricks() {
        return bricks;
    }

    public static void addBrick(FlexibleBrick brick) {
        bricks.add(brick);
        all.add(brick);
    }

    public static List<FlexibleOre> getOres() {
        return ores;
    }

    public static void addOres(FlexibleOre block) {
        ores.add(block);
        all.add(block);
    }
}
