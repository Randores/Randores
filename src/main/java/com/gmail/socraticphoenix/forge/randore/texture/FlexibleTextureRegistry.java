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
package com.gmail.socraticphoenix.forge.randore.texture;

import java.util.ArrayList;
import java.util.List;

public class FlexibleTextureRegistry {
    private static List<FlexibleAtlasSprite> block = new ArrayList<FlexibleAtlasSprite>();
    private static List<FlexibleAtlasSprite> item = new ArrayList<FlexibleAtlasSprite>();

    public static void registerBlock(FlexibleAtlasSprite sprite) {
        FlexibleTextureRegistry.block.add(sprite);
    }

    public static FlexibleAtlasSprite getBlock(int index) {
        return FlexibleTextureRegistry.block.get(index);
    }

    public static int blockQuantity() {
        return FlexibleTextureRegistry.block.size();
    }

    public static List<FlexibleAtlasSprite> getBlockSprites() {
        return FlexibleTextureRegistry.block;
    }

    public static void registerItem(FlexibleAtlasSprite sprite) {
        FlexibleTextureRegistry.item.add(sprite);
    }

    public static FlexibleAtlasSprite getItem(int index) {
        return FlexibleTextureRegistry.item.get(index);
    }

    public static int itemQuantity() {
        return FlexibleTextureRegistry.item.size();
    }

    public static List<FlexibleAtlasSprite> getItemSprites() {
        return FlexibleTextureRegistry.item;
    }

}
