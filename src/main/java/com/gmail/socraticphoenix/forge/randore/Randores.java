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

import com.gmail.socraticphoenix.forge.randore.component.CraftableType;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinition;
import com.gmail.socraticphoenix.forge.randore.component.MaterialDefinitionRegistry;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingBlocks;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingGuiHandler;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingItems;
import com.gmail.socraticphoenix.forge.randore.crafting.FlexibleRecipe;
import com.gmail.socraticphoenix.forge.randore.crafting.FlexibleSmelt;
import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumDelegateSmelt;
import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumForgeTileEntity;
import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumSmeltRegistry;
import com.gmail.socraticphoenix.forge.randore.crafting.table.CraftiniumDelegateRecipe;
import com.gmail.socraticphoenix.forge.randore.crafting.table.CraftiniumRecipeRegistry;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItem;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemRegistry;
import com.gmail.socraticphoenix.forge.randore.packet.RandoresNetworking;
import com.gmail.socraticphoenix.forge.randore.resource.RandoresResourceManager;
import com.google.common.base.Supplier;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Mod(modid = "randores", name = "Socratic_Phoenix's Randores", version = "1.5")
public class Randores {
    public static final Item.ToolMaterial MATERIAL_DEFAULT = EnumHelper.addToolMaterial("MATERIAL_DEFAULT", 1, 100, 1, 1, 1);
    public static final ItemArmor.ArmorMaterial ARMOR_DEFAULT = EnumHelper.addArmorMaterial("ARMOR_DEFAULT", "no_texture", 100, new int[]{1, 1, 1, 1}, 1, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1);
    public static final CreativeTabs TAB_CRAFTING = new RandoresTab("randores_crafting", new Supplier<Item>() {
        @Override
        public Item get() {
            return Item.getItemFromBlock(CraftingBlocks.craftiniumTable);
        }
    });
    public static final CreativeTabs TAB_BLOCKS = new RandoresTab("randores_blocks", new Supplier<Item>() {
        @Override
        public Item get() {
            return Item.getItemFromBlock(TabBlocks.tabOre);
        }
    });
    public static final CreativeTabs TAB_MATERIALS = new RandoresTab("randores_materials", new Supplier<Item>() {
        @Override
        public Item get() {
            return TabItems.tabItem;
        }
    });
    public static final CreativeTabs TAB_STICKS = new RandoresTab("randores_sticks", new Supplier<Item>() {
        @Override
        public Item get() {
            return TabItems.tabStick;
        }
    });
    public static final CreativeTabs TAB_HOES = new RandoresTab("randores_hoes", new Supplier<Item>() {
        @Override
        public Item get() {
            return TabItems.tabHoe;
        }
    });
    public static final CreativeTabs TAB_SWORDS = new RandoresTab("randores_swords", new Supplier<Item>() {
        @Override
        public Item get() {
            return TabItems.tabSword;
        }
    });
    public static final CreativeTabs TAB_AXES = new RandoresTab("randores_axes", new Supplier<Item>() {
        @Override
        public Item get() {
            return TabItems.tabAxe;
        }
    });
    public static final CreativeTabs TAB_PICKAXES = new RandoresTab("randores_pickaxes", new Supplier<Item>() {
        @Override
        public Item get() {
            return TabItems.tabPickaxe;
        }
    });
    public static final CreativeTabs TAB_SPADES = new RandoresTab("randores_spades", new Supplier<Item>() {
        @Override
        public Item get() {
            return TabItems.tabShovel;
        }
    });
    public static final CreativeTabs TAB_ARMOR = new RandoresTab("randores_armor", new Supplier<Item>() {
        @Override
        public Item get() {
            return TabItems.tabHelmet;
        }
    });


    private static Randores instance;
    private static Map<Long, Long> worldSeeds = new HashMap<Long, Long>();
    @SidedProxy(modId = "randores", clientSide = "com.gmail.socraticphoenix.forge.randore.RandoresClientProxy", serverSide = "com.gmail.socraticphoenix.forge.randore.RandoresProxy")
    private static RandoresProxy proxy;

    private Logger logger;
    private File confDir;
    private File conf;
    private File tex;
    private Configuration configuration;

    public Randores() {
        Randores.instance = this;
        this.confDir = new File("config", "randores");
        this.conf = new File(this.confDir, "config.cfg");
        this.configuration = new Configuration(this.conf);
        this.tex = new File(this.confDir, "textures");
        this.logger = LogManager.getLogger("Randores");
        if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            MinecraftForge.EVENT_BUS.register(new RandoresClientSideListener());
        }
        MinecraftForge.EVENT_BUS.register(new RandoresRegistryListener());
        MinecraftForge.EVENT_BUS.register(new RandoresPlayerListener());
        MinecraftForge.EVENT_BUS.register(new RandoresWorldEventListener());
    }

    private static long getRandoresSeedFromWorld(long worldSeed) {
        Long seed = Randores.worldSeeds.get(worldSeed);
        if (seed == null) {
            Random random = new Random(worldSeed);
            seed = random.nextLong();
            while (seed == 0) {
                seed = random.nextLong();
            }
            Randores.worldSeeds.put(worldSeed, seed);
        }
        return seed;
    }

    public static long getRandoresSeed(World world) {
        if (!world.isRemote) {
            return Randores.getRandoresSeedFromWorld(world.getSeed());
        } else if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            return RandoresClientSideRegistry.getCurrentSeed();
        } else {
            throw new IllegalArgumentException("World is remote and we're not on the client!");
        }
    }

    public static Randores getInstance() {
        return instance;
    }

    public static RandoresProxy getProxy() {
        return proxy;
    }

    public static String textureName(int num) {
        return "randores:blocks/randores.block." + num;
    }

    public static String blockName(int num) {
        return "randores.block." + num;
    }

    public static String itemTextureName(int num) {
        return "randores:items/randores.item." + num;
    }

    public static String itemName(int num) {
        return "randores.item." + num;
    }

    public static ItemStack applyData(ItemStack stack, long seed) {
        if (stack.getItem() instanceof FlexibleItem) {
            FlexibleItem item = (FlexibleItem) stack.getItem();
            if (item.getDefinition(seed).hasComponent(item.getType())) {
                NBTTagCompound randores = stack.getOrCreateSubCompound("randores");
                randores.setLong("seed", seed);
                stack.setTagInfo("randores", randores);
            }
        }
        return stack;
    }

    private static void applyLore(List<String> lore, ItemStack stack) {
        NBTTagCompound compound = stack.getOrCreateSubCompound("display");
        NBTTagList list;
        if (compound.hasKey("Lore")) {
            list = compound.getTagList("Lore", 8);
        } else {
            list = new NBTTagList();
        }
        for (String s : lore) {
            list.appendTag(new NBTTagString(TextFormatting.RESET + "" + TextFormatting.GREEN + s));
        }
        compound.setTag("Lore", list);
    }

    private static String makeName(String name) {
        return TextFormatting.RESET + name;
    }

    public static ItemStack applyData(ItemStack stack, World world) {
        return Randores.applyData(stack, Randores.getRandoresSeed(world));
    }

    public static long getRandoresSeed(ItemStack stack) {
        if (Randores.hasRandoresSeed(stack)) {
            return stack.getSubCompound("randores").getLong("seed");
        } else {
            return 0;
        }
    }

    public static MaterialDefinition getDefinition(int index, ItemStack stack) {
        return MaterialDefinitionRegistry.get(Randores.getRandoresSeed(stack)).get(index);
    }

    public static boolean hasRandoresSeed(ItemStack stack) {
        return stack.getSubCompound("randores") != null && stack.getSubCompound("randores").hasKey("seed");
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent ev) {
        try {
            this.tex.mkdirs();
            if (!this.conf.exists()) {
                this.conf.createNewFile();
            }

            RandoresTranslations.registerFallback();

            List<String> languages = RandoresResourceManager.getResourceLines("ab_dict.txt");
            for (String lang : languages) {
                this.logger.info("Loading randores translations from file: " + lang);
                RandoresTranslations.registerFromResources(lang, lang + ".lang");

            }

            GameRegistry.registerTileEntityWithAlternatives(CraftiniumForgeTileEntity.class, "craftinium_forge", "craftinium_forge_lit");

            CraftingManager.getInstance().addRecipe(new ItemStack(CraftingBlocks.craftiniumTable), "XX ", "XX ", 'X', CraftingItems.craftiniumLump);
            CraftingManager.getInstance().addRecipe(new ItemStack(CraftingBlocks.craftiniumForge), "XXX", "X X", "XXX", 'X', CraftingItems.craftiniumLump);

            for (int i = 0; i < 300; i++) {
                Item material = FlexibleItemRegistry.getMaterial(i);
                for (CraftableType type : CraftableType.values()) {
                    CraftiniumRecipeRegistry.register(new FlexibleRecipe(i, type, 'X', material, 'S', "stickWood"));
                }
                CraftiniumSmeltRegistry.register(new FlexibleSmelt(i));
            }

            this.logger.info("Testing the names algorithm...");
            Random random = new Random();
            for (int i = 0; i < 10; i++) {
                this.logger.info(RandoresNameAlgorithm.name(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))));
            }
            this.logger.info("Finished testing names algorithm");
            RandoresNetworking.initNetwork();
            this.logger.info("Running proxy pre-initialization...");
            Randores.proxy.preInitSided();
            this.logger.info("Proxy pre-initialized.");
        } catch (IOException e) {
            if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
                RandoresClientSideRegistry.crash("Encountered IOException while randores was loading", e);
            } else {
                e.printStackTrace();
            }
        }
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent ev) {
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new CraftingGuiHandler());
        GameRegistry.registerWorldGenerator(new RandoresWorldGenerator(), 0);
        this.logger.info("Running proxy initialization...");
        Randores.proxy.initSided();
        this.logger.info("Proxy initialized.");
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent ev) {
        this.logger.info("Adding crafting table recipes to craftinium table and furnace recipes to craftinium forge...");
        for (IRecipe recipe : CraftingManager.getInstance().getRecipeList()) {
            CraftiniumRecipeRegistry.register(new CraftiniumDelegateRecipe(recipe));
        }
        for (Map.Entry<ItemStack, ItemStack> smelt : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
            CraftiniumSmeltRegistry.register(new CraftiniumDelegateSmelt(smelt.getKey(), smelt.getValue(), FurnaceRecipes.instance().getSmeltingExperience(smelt.getKey())));
        }
        this.logger.info("Recipes added.");
    }

    public Logger getLogger() {
        return this.logger;
    }

    public File getConfDir() {
        return this.confDir;
    }

    public File getConf() {
        return this.conf;
    }

    public File getTextureFile(long seed) {
        return new File(this.tex, String.valueOf(seed).replaceAll("-", "_"));
    }
}
