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
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

public class FlexibleTextureRegistry {
    private static List<FlexibleAtlasSprite> block = Collections.synchronizedList(new ArrayList<FlexibleAtlasSprite>());
    private static List<FlexibleAtlasSprite> item = Collections.synchronizedList(new ArrayList<FlexibleAtlasSprite>());
    private static List<FlexibleAtlasSprite> bow = Collections.synchronizedList(new ArrayList<FlexibleAtlasSprite>());
    private static AtomicLong textureSeed = new AtomicLong(0);
    private static AtomicBoolean initialized = new AtomicBoolean(false);

    public static long getTextureSeed() {
        return FlexibleTextureRegistry.textureSeed.get();
    }

    public static void setTextureSeed(long textureSeed) {
        FlexibleTextureRegistry.textureSeed.set(textureSeed);
    }

    public static boolean isInitialized() {
        return FlexibleTextureRegistry.initialized.get();
    }

    public static void setInitialized(boolean initialized) {
        FlexibleTextureRegistry.initialized.set(initialized);
    }

    public static void registerBow(FlexibleAtlasSprite sprite) {
        FlexibleTextureRegistry.bow.add(sprite);
    }

    public static FlexibleAtlasSprite getBow(int index) {
        return FlexibleTextureRegistry.bow.get(index);
    }

    public static List<FlexibleAtlasSprite> getBowSprites() {
        return FlexibleTextureRegistry.bow;
    }

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

    public static int bowQuantity() {
        return FlexibleTextureRegistry.bow.size();
    }
}
