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

import com.gmail.socraticphoenix.forge.randore.block.FlexibleBlockRegistry;
import com.gmail.socraticphoenix.forge.randore.block.FlexibleOre;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingBlocks;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingItems;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemRegistry;
import com.gmail.socraticphoenix.forge.randore.texture.FlexibleAtlasSprite;
import com.gmail.socraticphoenix.forge.randore.texture.FlexibleTextureRegistry;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.logging.log4j.Logger;

public class RandoresClientSideListener {

    @SubscribeEvent
    public void onModelLoad(ModelRegistryEvent ev) {
        ModelLoader.setCustomModelResourceLocation(CraftingItems.CRAFTINIUM_LUMP, 0, new ModelResourceLocation("randores:" + CraftingItems.CRAFTINIUM_LUMP.getUnlocalizedName().substring(5), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(CraftingBlocks.CRAFTINIUM_ORE), 0, new ModelResourceLocation("randores:" + Item.getItemFromBlock(CraftingBlocks.CRAFTINIUM_ORE).getUnlocalizedName().substring(5)));

        for (Item item : FlexibleItemRegistry.getMaterials()) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation("randores:" + item.getUnlocalizedName().substring(5), "inventory"));
        }

        for (FlexibleOre block : FlexibleBlockRegistry.getOres()) {
            Item item = Item.getItemFromBlock(block);
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation("randores:" + item.getUnlocalizedName().substring(5)));
        }

        for (Block brick : FlexibleBlockRegistry.getBricks()) {
            Item item = Item.getItemFromBlock(brick);
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation("randores:" + item.getUnlocalizedName().substring(5)));
        }
    }

    @SubscribeEvent
    public void onStitch(TextureStitchEvent.Pre ev) {
        Logger logger = Randores.getInstance().getLogger();

        if (FlexibleTextureRegistry.itemQuantity() == 0) {
            for (int i = 0; i < 3300; i++) {
                FlexibleAtlasSprite sprite = new FlexibleAtlasSprite(Randores.itemTextureName(i), "test");
                FlexibleTextureRegistry.registerItem(sprite);
                ev.getMap().setTextureEntry(sprite);
            }
        } else {
            for (FlexibleAtlasSprite sprite : FlexibleTextureRegistry.getItemSprites()) {
                ev.getMap().setTextureEntry(sprite);
            }
        }

        if (FlexibleTextureRegistry.blockQuantity() == 0) {
            for (int i = 0; i < 600; i++) {
                FlexibleAtlasSprite sprite = new FlexibleAtlasSprite(Randores.textureName(i), "test");
                FlexibleTextureRegistry.registerBlock(sprite);
                ev.getMap().setTextureEntry(sprite);
            }
        } else {
            for (FlexibleAtlasSprite sprite : FlexibleTextureRegistry.getBlockSprites()) {
                ev.getMap().setTextureEntry(sprite);
            }
        }

        if (FlexibleTextureRegistry.specificQuantity() == 0) {
            r(new FlexibleAtlasSprite("randores:blocks/craftinium_ore", "craftinium_ore"), ev);
            r(new FlexibleAtlasSprite("randores:items/craftinium_lump", "craftinium_lump"), ev);
        } else {
            for (FlexibleAtlasSprite sprite : FlexibleTextureRegistry.getSpecific()) {
                ev.getMap().setTextureEntry(sprite);
            }
        }
    }

    private void r(FlexibleAtlasSprite sprite, TextureStitchEvent.Pre ev) {
        FlexibleTextureRegistry.registerSpecific(sprite);
        ev.getMap().setTextureEntry(sprite);
    }

}
