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

public class RandoresModelTemplates {

    public static String materialItem() {
        return "{\n" +
                "    \"parent\": \"item/generated\",\n" +
                "    \"textures\": {\n" +
                "        \"layer0\": \"randores:items/randores.item.{#}\"\n" +
                "    }\n" +
                "}\n";
    }

    public static String toolItem() {
        return "{\n" +
                "    \"parent\": \"item/handheld\",\n" +
                "    \"textures\": {\n" +
                "        \"layer0\": \"randores:items/randores.item.{#}\"\n" +
                "    }\n" +
                "}\n";
    }
    
    public static String armorItem() {
        return "{\n" +
                "    \"parent\": \"item/generated\",\n" +
                "    \"textures\": {\n" +
                "        \"layer0\": \"randores:items/randores.item.{#}\"\n" +
                "    }\n" +
                "}\n";
    }
    
    public static String stickItem() {
        return "{\n" +
                "    \"parent\": \"item/handheld\",\n" +
                "    \"textures\": {\n" +
                "        \"layer0\": \"randores:items/randores.item.{#}\"\n" +
                "    }\n" +
                "}\n";
    }
    
    public static String torchItem() {
        return "{\n" +
                "    \"parent\": \"item/generated\",\n" +
                "    \"textures\": {\n" +
                "        \"layer0\": \"randores:blocks/randores.block.{#}\"\n" +
                "    }\n" +
                "}\n";
    }
    
    public static String blockItem() {
        return "{\n" +
                "    \"parent\": \"" + RandoresLazyResourcePack.DOMAIN + ":block/randores.block.{#}\",\n" +
                "    \"display\": {\n" +
                "        \"thirdperson\": {\n" +
                "            \"rotation\": [ 10, -45, 170 ],\n" +
                "            \"translation\": [ 0, 1.5, -2.75 ],\n" +
                "            \"scale\": [ 0.375, 0.375, 0.375 ]\n" +
                "        }\n" +
                "    }\n" +
                "}";
    }

    public static String blockState() {
        return "{\n" +
                "    \"variants\": {\n" +
                "        \"normal\": { \"model\": \"" + RandoresLazyResourcePack.DOMAIN + ":randores.block.{#}\" }\n" +
                "    }\n" +
                "}";
    }

    public static String oreBlockState() {
        String o = "randores.block.{#}";
        String oreContent = "{\n" +
                "    \"variants\": {\n" +
                "        \"normal\": { \"model\": \"" + RandoresLazyResourcePack.DOMAIN + ":" + o + "\" },\n";

        for (int j = 0; j < 16; j++) {
            oreContent += "        \"harvest_level=" + j + "\": { \"model\": \"" + RandoresLazyResourcePack.DOMAIN + ":" + o + "\" }";
            if (j < 16 - 1) {
                oreContent += ",";
            }
            oreContent += "\n";
        }

        oreContent +=
                "    }\n" +
                        "}";
        return oreContent;
    }
    
    public static String torchBlockState() {
        return "{\n" +
                "    \"variants\": {\n" +
                "        \"facing=up\": { \"model\": \"" + RandoresLazyResourcePack.DOMAIN + ":randores.block.{#}\" },\n" +
                "        \"facing=east\": { \"model\": \"" + RandoresLazyResourcePack.DOMAIN + ":randores.block.{#}_wall" + "\" },\n" +
                "        \"facing=south\": { \"model\": \"" + RandoresLazyResourcePack.DOMAIN + ":randores.block.{#}_wall" + "\", \"y\": 90 },\n" +
                "        \"facing=west\": { \"model\": \"" + RandoresLazyResourcePack.DOMAIN + ":randores.block.{#}_wall" + "\", \"y\": 180 },\n" +
                "        \"facing=north\": { \"model\": \"" + RandoresLazyResourcePack.DOMAIN + ":randores.block.{#}_wall" + "\", \"y\": 270 }\n" +
                "    }\n" +
                "}\n";
    }
    
    public static String blockModel() {
        return "{\n" +
                "    \"parent\": \"block/cube_all\",\n" +
                "    \"textures\": {\n" +
                "        \"all\": \"randores:blocks/randores.block.{#}\"\n" +
                "    }\n" +
                "}";
    }

    public static String torchModel() {
        return "{\n" +
                "    \"parent\": \"block/torch\",\n" +
                "    \"textures\": {\n" +
                "        \"torch\": \"randores:blocks/randores.block.{#}\"\n" +
                "    }\n" +
                "}\n";
    }

    public static String torchWallModel() {
        return "{\n" +
                "    \"parent\": \"block/torch_wall\",\n" +
                "    \"textures\": {\n" +
                "        \"torch\": \"randores:blocks/randores.block.{#}\"\n" +
                "    }\n" +
                "}\n";
    }

    public static String make(String template, int index) {
        return template.replace("{#}", String.valueOf(index));
    }
    
}
