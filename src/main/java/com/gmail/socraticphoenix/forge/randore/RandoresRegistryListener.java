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
import com.gmail.socraticphoenix.forge.randore.block.FlexibleBrick;
import com.gmail.socraticphoenix.forge.randore.block.FlexibleOre;
import com.gmail.socraticphoenix.forge.randore.block.FlexibleTorch;
import com.gmail.socraticphoenix.forge.randore.component.Components;
import com.gmail.socraticphoenix.forge.randore.component.CraftableType;
import com.gmail.socraticphoenix.forge.randore.component.ability.EmpoweredEnchantment;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingBlocks;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingItems;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleAxe;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleBattleaxe;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleBow;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleHoe;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemArmor;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemBlock;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemRegistry;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleMaterial;
import com.gmail.socraticphoenix.forge.randore.item.FlexiblePickaxe;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleSledgehammer;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleSpade;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleStick;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleSword;
import net.minecraft.block.Block;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.oredict.OreDictionary;

public class RandoresRegistryListener {

    @SubscribeEvent
    public void onBlockRegister(RegistryEvent.Register<Block> ev) {
        Randores.getInstance().getLogger().info("Initializing Blocks...");
        for (int i = 0; i < Randores.registeredAmount(); i++) {
            FlexibleOre block = new FlexibleOre(i);
            block.setUnlocalizedName(Randores.blockName(i)).setRegistryName(Randores.blockName(i)).setCreativeTab(Randores.TAB_BLOCKS);
            FlexibleBlockRegistry.addOres(block);
            ev.getRegistry().register(block);
        }

        for (int i = 0; i < Randores.registeredAmount(); i++) {
            FlexibleBrick block = new FlexibleBrick(i);
            block.setUnlocalizedName(Randores.blockName(CraftableType.BRICKS.getIndex(i))).setRegistryName(Randores.blockName(CraftableType.BRICKS.getIndex(i))).setCreativeTab(Randores.TAB_BLOCKS);
            FlexibleBlockRegistry.addBrick(block);
            ev.getRegistry().register(block);
        }

        for (int i = 0; i < Randores.registeredAmount(); i++) {
            FlexibleTorch torch = new FlexibleTorch(i);
            torch.setUnlocalizedName(Randores.blockName(CraftableType.TORCH.getIndex(i))).setRegistryName(Randores.blockName(CraftableType.TORCH.getIndex(i))).setCreativeTab(Randores.TAB_TORCHES);
            FlexibleBlockRegistry.addTorch(torch);
            ev.getRegistry().register(torch);
        }

        CraftingBlocks.init();
        ev.getRegistry().register(CraftingBlocks.craftiniumTable);
        ev.getRegistry().register(CraftingBlocks.craftiniumOre);
        ev.getRegistry().register(CraftingBlocks.craftiniumForge);
        ev.getRegistry().register(CraftingBlocks.craftiniumForgeLit);
        RandoresTabBlocks.init();
        ev.getRegistry().register(RandoresTabBlocks.tabOre);
        ev.getRegistry().register(RandoresTabBlocks.tabTorch);
    }

    @SubscribeEvent
    public void onEnchantRegister(RegistryEvent.Register<Enchantment> ev) {
        ev.getRegistry().register(new EmpoweredEnchantment());
    }

    @SubscribeEvent
    public void onItemRegister(RegistryEvent.Register<Item> ev) {
        Randores.getInstance().getLogger().info("Initializing Items...");

        for (int i = 0; i < Randores.registeredAmount(); i++) {
            FlexibleMaterial material = new FlexibleMaterial(i);
            material.setUnlocalizedName(Randores.itemName(i)).setRegistryName(Randores.itemName(i)).setCreativeTab(Randores.TAB_MATERIALS);
            FlexibleItemRegistry.addMaterial(material);
            ev.getRegistry().register(material);

            OreDictionary.registerOre("randoresMaterial", material);
        }

        for (int i = 0; i < Randores.registeredAmount(); i++) {
            FlexibleStick stick = new FlexibleStick(i);
            int loc = CraftableType.STICK.getIndex(i);
            stick.setUnlocalizedName(Randores.itemName(loc)).setRegistryName(Randores.itemName(loc)).setCreativeTab(Randores.TAB_STICKS);
            FlexibleItemRegistry.addStick(stick);
            ev.getRegistry().register(stick);
            OreDictionary.registerOre("stickWood", stick);
        }

        for(FlexibleTorch torch : FlexibleBlockRegistry.getTorches()) {
            Item item = new FlexibleItemBlock(torch, torch.index(), Components.TORCH).setUnlocalizedName(torch.getUnlocalizedName()).setRegistryName(torch.getRegistryName());
            ev.getRegistry().register(item);
            OreDictionary.registerOre("torch", torch);
        }

        for (int i = 0; i < Randores.registeredAmount(); i++) {
            FlexibleSword sword = new FlexibleSword(i);
            int loc = CraftableType.SWORD.getIndex(i);
            sword.setUnlocalizedName(Randores.itemName(loc)).setRegistryName(Randores.itemName(loc)).setCreativeTab(Randores.TAB_SWORDS);
            FlexibleItemRegistry.addSword(sword);
            ev.getRegistry().register(sword);
        }

        for (int i = 0; i < Randores.registeredAmount(); i++) {
            FlexibleHoe hoe = new FlexibleHoe(i);
            int loc = CraftableType.HOE.getIndex(i);
            hoe.setUnlocalizedName(Randores.itemName(loc)).setRegistryName(Randores.itemName(loc)).setCreativeTab(Randores.TAB_HOES);
            FlexibleItemRegistry.addHoe(hoe);
            ev.getRegistry().register(hoe);
        }

        for (int i = 0; i < Randores.registeredAmount(); i++) {
            FlexibleAxe axe = new FlexibleAxe(i);
            int loc = CraftableType.AXE.getIndex(i);
            axe.setUnlocalizedName(Randores.itemName(loc)).setRegistryName(Randores.itemName(loc)).setCreativeTab(Randores.TAB_AXES);
            FlexibleItemRegistry.addAxe(axe);
            ev.getRegistry().register(axe);
        }

        for (int i = 0; i < Randores.registeredAmount(); i++) {
            FlexibleSpade spade = new FlexibleSpade(i);
            int loc = CraftableType.SHOVEL.getIndex(i);
            spade.setUnlocalizedName(Randores.itemName(loc)).setRegistryName(Randores.itemName(loc)).setCreativeTab(Randores.TAB_SPADES);
            FlexibleItemRegistry.addSpade(spade);
            ev.getRegistry().register(spade);
        }

        for (int i = 0; i < Randores.registeredAmount(); i++) {
            FlexibleBow bow = new FlexibleBow(i);
            bow.setUnlocalizedName(Randores.bowName(i)).setRegistryName(Randores.bowName(i)).setCreativeTab(Randores.TAB_BOW);
            FlexibleItemRegistry.addBow(bow);
            ev.getRegistry().register(bow);
        }

        for (int i = 0; i < Randores.registeredAmount(); i++) {
            FlexiblePickaxe pickaxe = new FlexiblePickaxe(i);
            int loc = CraftableType.PICKAXE.getIndex(i);
            pickaxe.setUnlocalizedName(Randores.itemName(loc)).setRegistryName(Randores.itemName(loc)).setCreativeTab(Randores.TAB_PICKAXES);
            FlexibleItemRegistry.addPickaxe(pickaxe);
            ev.getRegistry().register(pickaxe);
        }

        for (int i = 0; i < Randores.registeredAmount(); i++) {
            FlexibleBattleaxe battleaxe = new FlexibleBattleaxe(i);
            int loc = CraftableType.BATTLEAXE.getIndex(i);
            battleaxe.setUnlocalizedName(Randores.itemName(loc)).setRegistryName(Randores.itemName(loc)).setCreativeTab(Randores.TAB_BATTLEAXES);
            FlexibleItemRegistry.addBattleaxe(battleaxe);
            ev.getRegistry().register(battleaxe);
        }

        for (int i = 0; i < Randores.registeredAmount(); i++) {
            FlexibleSledgehammer sledgehammer = new FlexibleSledgehammer(i);
            int loc = CraftableType.SLEDGEHAMMER.getIndex(i);
            sledgehammer.setUnlocalizedName(Randores.itemName(loc)).setRegistryName(Randores.itemName(loc)).setCreativeTab(Randores.TAB_SLEDGEHAMMERS);
            FlexibleItemRegistry.addSledgehammer(sledgehammer);
            ev.getRegistry().register(sledgehammer);
        }

        for (int i = 0; i < Randores.registeredAmount(); i++) {
            FlexibleItemArmor helmet = new FlexibleItemArmor(i, Components.HELMET);
            int loc = CraftableType.HELMET.getIndex(i);
            helmet.setUnlocalizedName(Randores.itemName(loc)).setRegistryName(Randores.itemName(loc)).setCreativeTab(Randores.TAB_ARMOR);
            FlexibleItemRegistry.addHelmet(helmet);
            ev.getRegistry().register(helmet);

            FlexibleItemArmor chestplate = new FlexibleItemArmor(i, Components.CHESTPLATE);
            loc = CraftableType.CHESTPLATE.getIndex(i);
            chestplate.setUnlocalizedName(Randores.itemName(loc)).setRegistryName(Randores.itemName(loc)).setCreativeTab(Randores.TAB_ARMOR);
            FlexibleItemRegistry.addChestplate(chestplate);
            ev.getRegistry().register(chestplate);

            FlexibleItemArmor leggings = new FlexibleItemArmor(i, Components.LEGGINGS);
            loc = CraftableType.LEGGINGS.getIndex(i);
            leggings.setUnlocalizedName(Randores.itemName(loc)).setRegistryName(Randores.itemName(loc)).setCreativeTab(Randores.TAB_ARMOR);
            FlexibleItemRegistry.addLeggings(leggings);
            ev.getRegistry().register(leggings);

            FlexibleItemArmor boots = new FlexibleItemArmor(i, Components.BOOTS);
            loc = CraftableType.BOOTS.getIndex(i);
            boots.setUnlocalizedName(Randores.itemName(loc)).setRegistryName(Randores.itemName(loc)).setCreativeTab(Randores.TAB_ARMOR);
            FlexibleItemRegistry.addBoots(boots);
            ev.getRegistry().register(boots);
        }

        for (FlexibleOre block : FlexibleBlockRegistry.getOres()) {
            Item item = new FlexibleItemBlock(block, block.index(), Components.ORE).setUnlocalizedName(block.getUnlocalizedName()).setRegistryName(block.getRegistryName());
            ev.getRegistry().register(item);
        }

        for (FlexibleBrick brick : FlexibleBlockRegistry.getBricks()) {
            Item item = new FlexibleItemBlock(brick, brick.index(), Components.BRICKS).setUnlocalizedName(brick.getUnlocalizedName()).setRegistryName(brick.getRegistryName());
            ev.getRegistry().register(item);
        }


        ev.getRegistry().register(new ItemBlock(CraftingBlocks.craftiniumTable).setUnlocalizedName(CraftingBlocks.craftiniumTable.getUnlocalizedName()).setRegistryName(CraftingBlocks.craftiniumTable.getRegistryName()));
        ev.getRegistry().register(new ItemBlock(CraftingBlocks.craftiniumForge).setUnlocalizedName(CraftingBlocks.craftiniumForge.getUnlocalizedName()).setRegistryName(CraftingBlocks.craftiniumForge.getRegistryName()));
        ev.getRegistry().register(new ItemBlock(CraftingBlocks.craftiniumForgeLit).setUnlocalizedName(CraftingBlocks.craftiniumForgeLit.getUnlocalizedName()).setRegistryName(CraftingBlocks.craftiniumForgeLit.getRegistryName()));
        ev.getRegistry().register(new ItemBlock(CraftingBlocks.craftiniumOre).setUnlocalizedName(CraftingBlocks.craftiniumOre.getUnlocalizedName()).setRegistryName(CraftingBlocks.craftiniumOre.getRegistryName()));
        CraftingItems.init();
        ev.getRegistry().register(CraftingItems.craftiniumLump);
        ev.getRegistry().register(CraftingItems.tome);

        RandoresTabItems.init();
        ev.getRegistry().register(RandoresTabItems.tabAxe);
        ev.getRegistry().register(RandoresTabItems.tabHoe);
        ev.getRegistry().register(RandoresTabItems.tabShovel);
        ev.getRegistry().register(RandoresTabItems.tabPickaxe);
        ev.getRegistry().register(RandoresTabItems.tabSword);
        ev.getRegistry().register(RandoresTabItems.tabHelmet);
        ev.getRegistry().register(RandoresTabItems.tabItem);
        ev.getRegistry().register(RandoresTabItems.tabStick);
        ev.getRegistry().register(RandoresTabItems.tabBattleaxe);
        ev.getRegistry().register(RandoresTabItems.tabSledgehammer);
        ev.getRegistry().register(RandoresTabItems.tabBow);
        ev.getRegistry().register(new ItemBlock(RandoresTabBlocks.tabOre).setUnlocalizedName(RandoresTabBlocks.tabOre.getUnlocalizedName()).setRegistryName(RandoresTabBlocks.tabOre.getRegistryName()));
        ev.getRegistry().register(new ItemBlock(RandoresTabBlocks.tabTorch).setUnlocalizedName(RandoresTabBlocks.tabTorch.getUnlocalizedName()).setRegistryName(RandoresTabBlocks.tabTorch.getRegistryName()));
    }

}
