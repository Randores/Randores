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

public class GenerateBlockModels {

    public static void main(String[] args) throws IOException {
        File states = new File("src/main/resources/assets/randores/blockstates");
        File models = new File("src/main/resources/assets/randores/models/block");
        for (int i = 0; i < 300; i++) {
            String o = "randores.block." + i;
            String b = "randores.block." + CraftableType.BRICKS.getIndex(i);

            String oreContent = "{\n" +
                    "    \"variants\": {\n" +
                    "        \"normal\": { \"model\": \"randores:" + o + "\" },\n";

            for (int j = 0; j < 16; j++) {
                oreContent += "        \"harvest_level=" + j + "\": { \"model\": \"randores:" + o + "\" }";
                if (j < 16 - 1) {
                    oreContent += ",";
                }
                oreContent += "\n";
            }

            oreContent +=
                    "    }\n" +
                            "}";

            write(oreContent, new File(states, "randores.block." + i + ".json"));
            write("{\n" +
                    "    \"variants\": {\n" +
                    "        \"normal\": { \"model\": \"randores:" + b + "\" }\n" +
                    "    }\n" +
                    "}", new File(states, "randores.block." + CraftableType.BRICKS.getIndex(i) + ".json"));
            write("{\n" +
                    "    \"variants\": {\n" +
                    "        \"facing=up\": { \"model\": \"randores:randores.block." + CraftableType.TORCH.getIndex(i) + "\" },\n" +
                    "        \"facing=east\": { \"model\": \"randores:randores.block." + CraftableType.TORCH.getIndex(i) + "_wall" + "\" },\n" +
                    "        \"facing=south\": { \"model\": \"randores:randores.block." + CraftableType.TORCH.getIndex(i) + "_wall" + "\", \"y\": 90 },\n" +
                    "        \"facing=west\": { \"model\": \"randores:randores.block." + CraftableType.TORCH.getIndex(i) + "_wall" + "\", \"y\": 180 },\n" +
                    "        \"facing=north\": { \"model\": \"randores:" + CraftableType.TORCH.getIndex(i) + "_wall" + "\", \"y\": 270 }\n" +
                    "    }\n" +
                    "}\n", new File(states, "randores.block." + CraftableType.TORCH.getIndex(i) + ".json"));
            write("{\n" +
                    "    \"parent\": \"block/cube_all\",\n" +
                    "    \"textures\": {\n" +
                    "        \"all\": \"randores:blocks/randores.block." + i + "\"\n" +
                    "    }\n" +
                    "}", new File(models, "randores.block." + i + ".json"));
            write("{\n" +
                    "    \"parent\": \"block/cube_all\",\n" +
                    "    \"textures\": {\n" +
                    "        \"all\": \"randores:blocks/randores.block." + CraftableType.BRICKS.getIndex(i) + "\"\n" +
                    "    }\n" +
                    "}", new File(models, "randores.block." + CraftableType.BRICKS.getIndex(i) + ".json"));
            write("{\n" +
                    "    \"parent\": \"block/torch\",\n" +
                    "    \"textures\": {\n" +
                    "        \"torch\": \"randores:blocks/" + "randores.block." + CraftableType.TORCH.getIndex(i) + "\"\n" +
                    "    }\n" +
                    "}\n", new File(models, "randores.block." + CraftableType.TORCH.getIndex(i) + ".json"));
            write("{\n" +
                    "    \"parent\": \"block/torch_wall\",\n" +
                    "    \"textures\": {\n" +
                    "        \"torch\": \"randores:blocks/" + "randores.block." + CraftableType.TORCH.getIndex(i) + "\"\n" +
                    "    }\n" +
                    "}\n", new File(models, "randores.block." + CraftableType.TORCH.getIndex(i) + "_wall.json"));
        }
    }

    private static void write(String text, File file) throws IOException {
        FileWriter writer = new FileWriter(file);
        writer.write(text);
        writer.close();
    }

}
