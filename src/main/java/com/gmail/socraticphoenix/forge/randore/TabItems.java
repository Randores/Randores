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

import net.minecraft.item.Item;

public class TabItems {

    public static Item tabAxe;
    public static Item tabHoe;
    public static Item tabShovel;
    public static Item tabPickaxe;
    public static Item tabSword;
    public static Item tabHelmet;
    public static Item tabItem;
    public static Item tabStick;

    public static void init() {
        tabAxe = new Item().setUnlocalizedName("tab_axe").setRegistryName("tab_axe");
        tabHoe = new Item().setUnlocalizedName("tab_hoe").setRegistryName("tab_hoe");
        tabShovel = new Item().setUnlocalizedName("tab_shovel").setRegistryName("tab_shovel");
        tabPickaxe = new Item().setUnlocalizedName("tab_pickaxe").setRegistryName("tab_pickaxe");
        tabSword = new Item().setUnlocalizedName("tab_sword").setRegistryName("tab_sword");
        tabHelmet = new Item().setUnlocalizedName("tab_helmet").setRegistryName("tab_helmet");
        tabItem = new Item().setUnlocalizedName("tab_item").setRegistryName("tab_item");
        tabStick = new Item().setUnlocalizedName("tab_stick").setRegistryName("tab_stick");

    }

}
