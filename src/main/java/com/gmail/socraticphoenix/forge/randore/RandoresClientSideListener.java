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
import com.gmail.socraticphoenix.forge.randore.texture.RandoresLazyResourcePack;
import net.minecraft.block.Block;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.item.Item;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class RandoresClientSideListener {

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onModelLoad(ModelRegistryEvent ev) {
        ModelLoader.setCustomModelResourceLocation(CraftingItems.craftiniumLump, 0, new ModelResourceLocation("randores:" + CraftingItems.craftiniumLump.getUnlocalizedName().substring(5), "inventory"));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(CraftingBlocks.craftiniumOre), 0, new ModelResourceLocation("randores:" + Item.getItemFromBlock(CraftingBlocks.craftiniumOre).getUnlocalizedName().substring(5)));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(CraftingBlocks.craftiniumTable), 0, new ModelResourceLocation("randores:" + Item.getItemFromBlock(CraftingBlocks.craftiniumTable).getUnlocalizedName().substring(5)));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(CraftingBlocks.craftiniumForge), 0, new ModelResourceLocation("randores:" + Item.getItemFromBlock(CraftingBlocks.craftiniumForge).getUnlocalizedName().substring(5)));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(CraftingBlocks.craftiniumForgeLit), 0, new ModelResourceLocation("randores:" + Item.getItemFromBlock(CraftingBlocks.craftiniumForgeLit).getUnlocalizedName().substring(5)));

        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(RandoresTabBlocks.tabOre), 0, new ModelResourceLocation("randores:" + Item.getItemFromBlock(RandoresTabBlocks.tabOre).getUnlocalizedName().substring(5)));
        ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(RandoresTabBlocks.tabTorch), 0, new ModelResourceLocation("randores:" + Item.getItemFromBlock(RandoresTabBlocks.tabTorch).getUnlocalizedName().substring(5)));
        ModelLoader.setCustomModelResourceLocation(RandoresTabItems.tabItem, 0, new ModelResourceLocation("randores:" + RandoresTabItems.tabItem.getUnlocalizedName().substring(5), "inventory"));
        ModelLoader.setCustomModelResourceLocation(RandoresTabItems.tabHoe, 0, new ModelResourceLocation("randores:" + RandoresTabItems.tabHoe.getUnlocalizedName().substring(5), "inventory"));
        ModelLoader.setCustomModelResourceLocation(RandoresTabItems.tabAxe, 0, new ModelResourceLocation("randores:" + RandoresTabItems.tabAxe.getUnlocalizedName().substring(5), "inventory"));
        ModelLoader.setCustomModelResourceLocation(RandoresTabItems.tabShovel, 0, new ModelResourceLocation("randores:" + RandoresTabItems.tabShovel.getUnlocalizedName().substring(5), "inventory"));
        ModelLoader.setCustomModelResourceLocation(RandoresTabItems.tabPickaxe, 0, new ModelResourceLocation("randores:" + RandoresTabItems.tabPickaxe.getUnlocalizedName().substring(5), "inventory"));
        ModelLoader.setCustomModelResourceLocation(RandoresTabItems.tabSword, 0, new ModelResourceLocation("randores:" + RandoresTabItems.tabSword.getUnlocalizedName().substring(5), "inventory"));
        ModelLoader.setCustomModelResourceLocation(RandoresTabItems.tabHelmet, 0, new ModelResourceLocation("randores:" + RandoresTabItems.tabHelmet.getUnlocalizedName().substring(5), "inventory"));
        ModelLoader.setCustomModelResourceLocation(RandoresTabItems.tabStick, 0, new ModelResourceLocation("randores:" + RandoresTabItems.tabStick.getUnlocalizedName().substring(5), "inventory"));



        for (Item item : FlexibleItemRegistry.getMaterials()) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(RandoresLazyResourcePack.DOMAIN + ":" + item.getUnlocalizedName().substring(5), "inventory"));
        }

        for (Item item : FlexibleItemRegistry.getSticks()) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(RandoresLazyResourcePack.DOMAIN + ":" + item.getUnlocalizedName().substring(5), "inventory"));
        }

        for (Item item : FlexibleItemRegistry.getSwords()) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(RandoresLazyResourcePack.DOMAIN + ":" + item.getUnlocalizedName().substring(5), "inventory"));
        }

        for (Item item : FlexibleItemRegistry.getHoes()) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(RandoresLazyResourcePack.DOMAIN + ":" + item.getUnlocalizedName().substring(5), "inventory"));
        }

        for (Item item : FlexibleItemRegistry.getAxes()) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(RandoresLazyResourcePack.DOMAIN + ":" + item.getUnlocalizedName().substring(5), "inventory"));
        }

        for (Item item : FlexibleItemRegistry.getSpades()) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(RandoresLazyResourcePack.DOMAIN + ":" + item.getUnlocalizedName().substring(5), "inventory"));
        }

        for (Item item : FlexibleItemRegistry.getPickaxes()) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(RandoresLazyResourcePack.DOMAIN + ":" + item.getUnlocalizedName().substring(5), "inventory"));
        }

        for (Item item : FlexibleItemRegistry.getHelmets()) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(RandoresLazyResourcePack.DOMAIN + ":" + item.getUnlocalizedName().substring(5), "inventory"));
        }

        for (Item item : FlexibleItemRegistry.getChestplates()) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(RandoresLazyResourcePack.DOMAIN + ":" + item.getUnlocalizedName().substring(5), "inventory"));
        }

        for (Item item : FlexibleItemRegistry.getLeggings()) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(RandoresLazyResourcePack.DOMAIN + ":" + item.getUnlocalizedName().substring(5), "inventory"));
        }

        for (Item item : FlexibleItemRegistry.getBoots()) {
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(RandoresLazyResourcePack.DOMAIN + ":" + item.getUnlocalizedName().substring(5), "inventory"));
        }

        for (FlexibleOre block : FlexibleBlockRegistry.getOres()) {
            Item item = Item.getItemFromBlock(block);
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(RandoresLazyResourcePack.DOMAIN + ":" + item.getUnlocalizedName().substring(5)));
        }

        for (Block brick : FlexibleBlockRegistry.getBricks()) {
            Item item = Item.getItemFromBlock(brick);
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(RandoresLazyResourcePack.DOMAIN + ":" + item.getUnlocalizedName().substring(5)));
        }

        for (Block torch : FlexibleBlockRegistry.getTorches()) {
            Item item = Item.getItemFromBlock(torch);
            ModelLoader.setCustomModelResourceLocation(item, 0, new ModelResourceLocation(RandoresLazyResourcePack.DOMAIN + ":" + item.getUnlocalizedName().substring(5)));
        }
    }

    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onStitch(TextureStitchEvent.Pre ev) {
        if (FlexibleTextureRegistry.itemQuantity() == 0) {
            for (int i = 0; i < Randores.registeredAmount() * 11; i++) {
                FlexibleAtlasSprite sprite = new FlexibleAtlasSprite(Randores.itemTextureName(i));
                FlexibleTextureRegistry.registerItem(sprite);
                ev.getMap().setTextureEntry(sprite);
            }
        } else {
            for (FlexibleAtlasSprite sprite : FlexibleTextureRegistry.getItemSprites()) {
                ev.getMap().setTextureEntry(sprite);
            }
        }

        if (FlexibleTextureRegistry.blockQuantity() == 0) {
            for (int i = 0; i < Randores.registeredAmount() * 3; i++) {
                FlexibleAtlasSprite sprite = new FlexibleAtlasSprite(Randores.textureName(i));
                FlexibleTextureRegistry.registerBlock(sprite);
                ev.getMap().setTextureEntry(sprite);
            }
        } else {
            for (FlexibleAtlasSprite sprite : FlexibleTextureRegistry.getBlockSprites()) {
                ev.getMap().setTextureEntry(sprite);
            }
        }

    }


}
