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
package com.gmail.socraticphoenix.forge.randore.dev;

import com.gmail.socraticphoenix.forge.randore.component.CraftableType;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class GenerateItemModels {

    public static void main(String[] args) throws IOException {
        File item = new File("src/main/resources/assets/randores/models/item");
        item.mkdirs();
        for (int i = 0; i < 300; i++) {
            String material = "{\n" +
                    "    \"parent\": \"item/generated\",\n" +
                    "    \"textures\": {\n" +
                    "        \"layer0\": \"" + textureName(i) + "\"\n" +
                    "    }\n" +
                    "}\n";

            String hoe = "{\n" +
                    "    \"parent\": \"item/handheld\",\n" +
                    "    \"textures\": {\n" +
                    "        \"layer0\": \"" + textureName(i, CraftableType.HOE) + "\"\n" +
                    "    }\n" +
                    "}\n";
            String pickaxe = "{\n" +
                    "    \"parent\": \"item/handheld\",\n" +
                    "    \"textures\": {\n" +
                    "        \"layer0\": \"" + textureName(i, CraftableType.PICKAXE) + "\"\n" +
                    "    }\n" +
                    "}\n";
            String shovel = "{\n" +
                    "    \"parent\": \"item/handheld\",\n" +
                    "    \"textures\": {\n" +
                    "        \"layer0\": \"" + textureName(i, CraftableType.SHOVEL) + "\"\n" +
                    "    }\n" +
                    "}\n";
            String axe = "{\n" +
                    "    \"parent\": \"item/handheld\",\n" +
                    "    \"textures\": {\n" +
                    "        \"layer0\": \"" + textureName(i, CraftableType.AXE) + "\"\n" +
                    "    }\n" +
                    "}\n";
            String sword = "{\n" +
                    "    \"parent\": \"item/handheld\",\n" +
                    "    \"textures\": {\n" +
                    "        \"layer0\": \"" + textureName(i, CraftableType.SWORD) + "\"\n" +
                    "    }\n" +
                    "}\n";

            String helmet = "{\n" +
                    "    \"parent\": \"item/generated\",\n" +
                    "    \"textures\": {\n" +
                    "        \"layer0\": \"" + textureName(i, CraftableType.HELMET) + "\"\n" +
                    "    }\n" +
                    "}\n";
            String chest = "{\n" +
                    "    \"parent\": \"item/generated\",\n" +
                    "    \"textures\": {\n" +
                    "        \"layer0\": \"" + textureName(i, CraftableType.CHESTPLATE) + "\"\n" +
                    "    }\n" +
                    "}\n";
            String leggings = "{\n" +
                    "    \"parent\": \"item/generated\",\n" +
                    "    \"textures\": {\n" +
                    "        \"layer0\": \"" + textureName(i, CraftableType.LEGGINGS) + "\"\n" +
                    "    }\n" +
                    "}\n";
            String boots = "{\n" +
                    "    \"parent\": \"item/generated\",\n" +
                    "    \"textures\": {\n" +
                    "        \"layer0\": \"" + textureName(i, CraftableType.BOOTS) + "\"\n" +
                    "    }\n" +
                    "}\n";
            String stick = "{\n" +
                    "    \"parent\": \"item/handheld\",\n" +
                    "    \"textures\": {\n" +
                    "        \"layer0\": \"" + textureName(i, CraftableType.STICK) + "\"\n" +
                    "    }\n" +
                    "}\n";
            String brick = "{\n" +
                    "    \"parent\": \"randores:block/" + blockName(i, CraftableType.BRICKS) + "\",\n" +
                    "    \"display\": {\n" +
                    "        \"thirdperson\": {\n" +
                    "            \"rotation\": [ 10, -45, 170 ],\n" +
                    "            \"translation\": [ 0, 1.5, -2.75 ],\n" +
                    "            \"scale\": [ 0.375, 0.375, 0.375 ]\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
            String ore = "{\n" +
                    "    \"parent\": \"randores:block/" + blockName(i) + "\",\n" +
                    "    \"display\": {\n" +
                    "        \"thirdperson\": {\n" +
                    "            \"rotation\": [ 10, -45, 170 ],\n" +
                    "            \"translation\": [ 0, 1.5, -2.75 ],\n" +
                    "            \"scale\": [ 0.375, 0.375, 0.375 ]\n" +
                    "        }\n" +
                    "    }\n" +
                    "}";
            String torch = "{\n" +
                    "    \"parent\": \"item/generated\",\n" +
                    "    \"textures\": {\n" +
                    "        \"layer0\": \"randores:blocks/" + blockName(i, CraftableType.TORCH) + "\"\n" +
                    "    }\n" +
                    "}\n";

            write(material, new File(item, itemName(i)));
            write(hoe, new File(item, itemName(i, CraftableType.HOE)));
            write(shovel, new File(item, itemName(i, CraftableType.SHOVEL)));
            write(pickaxe, new File(item, itemName(i, CraftableType.PICKAXE)));
            write(axe, new File(item, itemName(i, CraftableType.AXE)));
            write(sword, new File(item, itemName(i, CraftableType.SWORD)));
            write(helmet, new File(item, itemName(i, CraftableType.HELMET)));
            write(chest, new File(item, itemName(i, CraftableType.CHESTPLATE)));
            write(leggings, new File(item, itemName(i, CraftableType.LEGGINGS)));
            write(boots, new File(item, itemName(i, CraftableType.BOOTS)));
            write(stick, new File(item, itemName(i, CraftableType.STICK)));

            write(ore, new File(item, blockName(i) + ".json"));
            write(brick, new File(item, blockName(i, CraftableType.BRICKS) + ".json"));
            write(torch, new File(item, blockName(i, CraftableType.TORCH) + ".json"));
        }
    }

    private static void write(String text, File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(text);
        writer.close();
    }

    private static String itemName(int i) {
        return "randores.item." + i + ".json";
    }

    private static String itemName(int i, CraftableType type) {
        return "randores.item." + type.getIndex(i) + ".json";
    }

    public static String blockName(int i) {
        return "randores.block." + i;
    }

    public static String blockName(int i, CraftableType type) {
        return "randores.block." + type.getIndex(i);
    }

    private static String textureName(int i, CraftableType type) {
        return "randores:items/randores.item." + (i + 300 * (type.ordinal() + 1));
    }

    private static String textureName(int i) {
        return "randores:items/randores.item." + i;
    }

}
