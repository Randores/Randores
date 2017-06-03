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
import com.gmail.socraticphoenix.forge.randore.component.ability.AbilityRegistry;
import com.gmail.socraticphoenix.forge.randore.component.ability.EmpoweredArmorListener;
import com.gmail.socraticphoenix.forge.randore.component.ability.ScheduleListener;
import com.gmail.socraticphoenix.forge.randore.component.ability.abilities.PotionEffectAbility;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingBlocks;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftingItems;
import com.gmail.socraticphoenix.forge.randore.crafting.CraftiniumForgeUpgradeRecipe;
import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumDelegateSmelt;
import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumForge;
import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumForgeTileEntity;
import com.gmail.socraticphoenix.forge.randore.crafting.forge.CraftiniumSmeltRegistry;
import com.gmail.socraticphoenix.forge.randore.crafting.forge.FlexibleSmelt;
import com.gmail.socraticphoenix.forge.randore.crafting.table.CraftiniumDelegateRecipe;
import com.gmail.socraticphoenix.forge.randore.crafting.table.CraftiniumRecipeRegistry;
import com.gmail.socraticphoenix.forge.randore.crafting.table.FlexibleCraftingRecipe;
import com.gmail.socraticphoenix.forge.randore.crafting.table.FlexibleRecipe;
import com.gmail.socraticphoenix.forge.randore.entity.RandoresArrow;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItem;
import com.gmail.socraticphoenix.forge.randore.item.FlexibleItemRegistry;
import com.gmail.socraticphoenix.forge.randore.module.altar.RandoresAltarGenerator;
import com.gmail.socraticphoenix.forge.randore.module.equip.RandoresMobEquip;
import com.gmail.socraticphoenix.forge.randore.module.loot.RandoresLoot;
import com.gmail.socraticphoenix.forge.randore.module.starter.RandoresStarterKit;
import com.gmail.socraticphoenix.forge.randore.packet.RandoresNetworking;
import com.gmail.socraticphoenix.forge.randore.proxy.RandoresProxy;
import com.gmail.socraticphoenix.forge.randore.resource.RandoresResourceManager;
import com.gmail.socraticphoenix.forge.randore.texture.RandoresLazyResourcePack;
import com.gmail.socraticphoenix.forge.randore.tome.TomeCraftingRecipe;
import com.google.common.base.Supplier;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLInterModComms;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.oredict.RecipeSorter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.Color;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Random;

@Mod(modid = "randores", name = "Socratic_Phoenix's Randores", version = "1.12")
public class Randores {
    public static final Item.ToolMaterial MATERIAL_DEFAULT = EnumHelper.addToolMaterial("MATERIAL_DEFAULT", 1, 100, 1, 1, 1);
    public static final ItemArmor.ArmorMaterial ARMOR_DEFAULT = EnumHelper.addArmorMaterial("ARMOR_DEFAULT", "no_texture", 100, new int[]{1, 1, 1, 1}, 1, SoundEvents.ITEM_ARMOR_EQUIP_IRON, 1);
    public static final CreativeTabs TAB_CRAFTING = new RandoresTab("randores_crafting", new Supplier<Item>() {
        @Override
        public Item get() {
            return Item.getItemFromBlock(CraftingBlocks.craftiniumTable);
        }
    }, false);
    public static final CreativeTabs TAB_BLOCKS = new RandoresTab("randores_blocks", new Supplier<Item>() {
        @Override
        public Item get() {
            return Item.getItemFromBlock(RandoresTabBlocks.tabOre);
        }
    }, true);
    public static final CreativeTabs TAB_MATERIALS = new RandoresTab("randores_materials", new Supplier<Item>() {
        @Override
        public Item get() {
            return RandoresTabItems.tabItem;
        }
    }, true);
    public static final CreativeTabs TAB_STICKS = new RandoresTab("randores_sticks", new Supplier<Item>() {
        @Override
        public Item get() {
            return RandoresTabItems.tabStick;
        }
    }, true);
    public static final CreativeTabs TAB_HOES = new RandoresTab("randores_hoes", new Supplier<Item>() {
        @Override
        public Item get() {
            return RandoresTabItems.tabHoe;
        }
    }, true);
    public static final CreativeTabs TAB_SWORDS = new RandoresTab("randores_swords", new Supplier<Item>() {
        @Override
        public Item get() {
            return RandoresTabItems.tabSword;
        }
    }, true);
    public static final CreativeTabs TAB_AXES = new RandoresTab("randores_axes", new Supplier<Item>() {
        @Override
        public Item get() {
            return RandoresTabItems.tabAxe;
        }
    }, true);
    public static final CreativeTabs TAB_PICKAXES = new RandoresTab("randores_pickaxes", new Supplier<Item>() {
        @Override
        public Item get() {
            return RandoresTabItems.tabPickaxe;
        }
    }, true);
    public static final CreativeTabs TAB_SPADES = new RandoresTab("randores_spades", new Supplier<Item>() {
        @Override
        public Item get() {
            return RandoresTabItems.tabShovel;
        }
    }, true);
    public static final CreativeTabs TAB_BOW = new RandoresTab("randores_bows", new Supplier<Item>() {
        @Override
        public Item get() {
            return RandoresTabItems.tabBow;
        }
    }, true);
    public static final CreativeTabs TAB_SLEDGEHAMMERS = new RandoresTab("randores_sledgehammers", new Supplier<Item>() {
        @Override
        public Item get() {
            return RandoresTabItems.tabSledgehammer;
        }
    }, true);
    public static final CreativeTabs TAB_BATTLEAXES = new RandoresTab("randores_battleaxes", new Supplier<Item>() {
        @Override
        public Item get() {
            return RandoresTabItems.tabBattleaxe;
        }
    }, true);
    public static final CreativeTabs TAB_ARMOR = new RandoresTab("randores_armor", new Supplier<Item>() {
        @Override
        public Item get() {
            return RandoresTabItems.tabHelmet;
        }
    }, true);
    public static final CreativeTabs TAB_TORCHES = new RandoresTab("randores_torches", new Supplier<Item>() {
        @Override
        public Item get() {
            return Item.getItemFromBlock(RandoresTabBlocks.tabTorch);
        }
    }, true);
    public static final CreativeTabs TAB_TOMES = new RandoresTomeTab("randores_tomes");
    @SidedProxy(modId = "randores", clientSide = "com.gmail.socraticphoenix.forge.randore.proxy.RandoresClientProxy", serverSide = "com.gmail.socraticphoenix.forge.randore.proxy.RandoresServerProxy")
    public static RandoresProxy PROXY = null;
    private static Randores instance;
    private static Map<Long, Long> worldSeeds = new HashMap<Long, Long>();
    private static List<String> offensiveWords = new ArrayList<String>();
    private static int registeredamount;
    private Logger logger;
    private File confDir;
    private File packs;
    private File conf;
    private Configuration configuration;

    public Randores() {
        Randores.instance = this;
        this.confDir = new File("config", "randores");
        this.conf = new File(this.confDir, "config.cfg");
        this.packs = new File(this.confDir, "packs");
        this.configuration = new Configuration(this.conf);
        this.logger = LogManager.getLogger("Randores");
        ConfigCategory config = this.getConfiguration().getCategory("config");
        if (!config.containsKey("registeredamount")) {
            registeredamount = 300;
            config.put("registeredamount", new Property("registeredamount", "300", Property.Type.INTEGER));
        } else {
            registeredamount = config.get("registeredamount").getInt();
        }

        if(FMLCommonHandler.instance().getSide().isClient()) {
            MinecraftForge.EVENT_BUS.register(new RandoresClientSideListener());
        }
        MinecraftForge.EVENT_BUS.register(new RandoresInvulnerabilityListener());

        MinecraftForge.EVENT_BUS.register(new RandoresRegistryListener());
        MinecraftForge.EVENT_BUS.register(new RandoresPlayerListener());
        MinecraftForge.EVENT_BUS.register(new RandoresLivingUpdate());
        MinecraftForge.EVENT_BUS.register(new RandoresWorldEventListener());

        //Modules
        MinecraftForge.EVENT_BUS.register(new RandoresMobEquip());
        MinecraftForge.EVENT_BUS.register(new RandoresLoot());
        MinecraftForge.EVENT_BUS.register(new RandoresStarterKit());

        //Items
        MinecraftForge.EVENT_BUS.register(new ScheduleListener());
        MinecraftForge.EVENT_BUS.register(new EmpoweredArmorListener());
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

    public static int registeredAmount() {
        return registeredamount;
    }

    public static long getRandoresSeed(World world) {
        if (world == null) {
            return 0;
        } else if (!world.isRemote) {
            return Randores.getRandoresSeedFromWorld(world.getSeed());
        } else if (FMLCommonHandler.instance().getSide() == Side.CLIENT) {
            return Randores.PROXY.seed();
        } else {
            throw new IllegalArgumentException("World is remote and we're not on the client!");
        }
    }

    public static Randores getInstance() {
        return instance;
    }

    public static String textureName(int num) {
        return RandoresLazyResourcePack.DOMAIN + ":blocks/randores.block." + num;
    }

    public static String blockName(int num) {
        return "randores.block." + num;
    }

    public static String itemTextureName(int num) {
        return RandoresLazyResourcePack.DOMAIN + ":items/randores.item." + num;
    }

    public static String itemName(int num) {
        return "randores.item." + num;
    }

    public static String bowName(int num) {
        return "randores.item.bow." + num;
    }

    public static String bowTexturePre(int num) {
        return RandoresLazyResourcePack.DOMAIN + ":items/randores.item.bow." + num;
    }

    public static ItemStack applyData(ItemStack stack, long seed) {
        if (stack.getItem() instanceof FlexibleItem) {
            FlexibleItem item = (FlexibleItem) stack.getItem();
            if (item.hasDefinition(seed) && item.getDefinition(seed).hasComponent(item.getType())) {
                NBTTagCompound randores = stack.getOrCreateSubCompound("randores");
                randores.setLong("seed", seed);
                stack.setTagInfo("randores", randores);
            }
        } else if (stack.getItem() instanceof ItemBlock && ((ItemBlock) stack.getItem()).getBlock() instanceof CraftiniumForge) {
            if (stack.getSubCompound("randores") == null || !stack.getSubCompound("randores").hasKey("furnace_speed")) {
                stack.getOrCreateSubCompound("randores").setInteger("furnace_speed", 1);
            }
        }
        return stack;
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

    public static long getRandoresSeed(NBTTagCompound baseData) {
        return baseData.getCompoundTag("randores").getLong("seed");
    }

    public static boolean hasRandoresSeed(NBTTagCompound baseData) {
        return baseData.hasKey("randores") && baseData.getCompoundTag("randores").hasKey("seed");
    }

    public static void applyRandoresSeed(NBTTagCompound baseData, long seed) {
        if (!baseData.hasKey("randores")) {
            baseData.setTag("randores", new NBTTagCompound());
        }
        baseData.getCompoundTag("randores").setLong("seed", seed);
    }

    public static void applyRandoresSeed(ItemStack stack, long seed) {
        stack.getOrCreateSubCompound("randores").setLong("seed", seed);
    }

    public static int getRandoresIndex(NBTTagCompound baseData) {
        return baseData.getCompoundTag("randores").getInteger("index");
    }

    public static boolean hasRandoresIndex(NBTTagCompound baseData) {
        return baseData.hasKey("randores") && baseData.getCompoundTag("randores").hasKey("index");
    }

    public static void applyRandoresIndex(NBTTagCompound baseData, int index) {
        if (!baseData.hasKey("randores")) {
            baseData.setTag("randores", new NBTTagCompound());
        }
        baseData.getCompoundTag("randores").setInteger("index", index);
    }

    public static void applyRandoresIndex(ItemStack stack, int index) {
        stack.getOrCreateSubCompound("randores").setInteger("index", index);
    }

    public static MaterialDefinition getDefinition(int index, ItemStack stack) {
        return MaterialDefinitionRegistry.get(Randores.getRandoresSeed(stack)).get(index);
    }

    public static boolean hasRandoresSeed(ItemStack stack) {
        return stack.getSubCompound("randores") != null && stack.getSubCompound("randores").hasKey("seed");
    }

    public static String getTexturePack() {
        String pack = "vanilla";
        Configuration config = Randores.getInstance().getConfiguration();
        ConfigCategory category = config.getCategory("config");
        if (!category.containsKey("pack")) {
            Property property = new Property("pack", pack, Property.Type.STRING);
            category.put("pack", property);
            config.save();
        } else {
            pack = category.get("pack").getString();
        }
        return pack;
    }

    public static int getOreCountConfigProperty() {
        int res;
        Configuration config = Randores.getInstance().getConfiguration();
        ConfigCategory category = config.getCategory("config");
        if (!category.containsKey("orecount")) {
            Property property = new Property("orecount", String.valueOf(Randores.registeredAmount()), Property.Type.INTEGER);
            property.setComment("The number of ores to generate in each world.");
            category.put("orecount", property);
            config.save();
            res = Randores.registeredAmount();
        } else {
            res = category.get("orecount").getInt();
        }
        if (res > Randores.registeredAmount()) {
            res = Randores.registeredAmount();
            category.put("orecount", new Property("orecount", String.valueOf(Randores.registeredAmount()), Property.Type.INTEGER));
            config.save();
        }
        PROXY.setOreCount(res);
        return res;
    }

    public static int getOreCount() {
        return PROXY.oreCount();
    }

    public static boolean containsOffensiveWord(String string) {
        for (String s : Randores.offensiveWords) {
            if (string.toLowerCase().contains(s.toLowerCase())) {
                return true;
            }
        }

        return false;
    }

    public static boolean hasRandoresIndex(ItemStack stack) {
        return Randores.hasRandoresIndex(stack.getTagCompound());
    }

    public static int getRandoresIndex(ItemStack stack) {
        return Randores.getRandoresIndex(stack.getTagCompound());
    }

    public long getPreviousSeed() {
        ConfigCategory config = this.getConfiguration().getCategory("config");
        if (config.containsKey("previousSeed")) {
            return config.get("previousSeed").getLong();
        }

        return 0;
    }

    public Configuration getConfiguration() {
        return this.configuration;
    }

    @Mod.EventHandler
    public void onPreInit(FMLPreInitializationEvent ev) throws IOException, IllegalAccessException {
        Randores.offensiveWords.addAll(RandoresResourceManager.getResourceLines("offensive_words.txt"));

        this.confDir.mkdirs();
        if (!this.conf.exists()) {
            this.conf.createNewFile();
        }

        this.getConfiguration().load();

        RandoresTranslations.registerFallback();

        Randores.getOreCountConfigProperty();

        GameRegistry.registerFuelHandler(new RandoresFuelHandler());

        ConfigCategory modules = this.getConfiguration().getCategory("modules");
        if (!modules.containsKey("mobequip")) {
            modules.put("mobequip", new Property("mobequip", "true", Property.Type.BOOLEAN));
        }

        if (!modules.containsKey("dungeonloot")) {
            modules.put("dungeonloot", new Property("dungeonloot", "true", Property.Type.BOOLEAN));
        }

        if (!modules.containsKey("dimensionless")) {
            modules.put("dimensionless", new Property("dimensionless", "true", Property.Type.BOOLEAN));
        }

        if (!modules.containsKey("starterkit")) {
            modules.put("starterkit", new Property("starterkit", "true", Property.Type.BOOLEAN));
        }

        if (!modules.containsKey("altar")) {
            modules.put("altar", new Property("altar", "true", Property.Type.BOOLEAN));
        }

        if (!modules.containsKey("youtubemode")) {
            modules.put("youtubemode", new Property("youtubemode", "false", Property.Type.BOOLEAN));
        }

        this.getConfiguration().save();

        GameRegistry.registerTileEntityWithAlternatives(CraftiniumForgeTileEntity.class, "craftinium_forge", "craftinium_forge_lit");

        ItemStack forge = new ItemStack(CraftingBlocks.craftiniumForge);
        forge.getOrCreateSubCompound("randores").setInteger("furnace_speed", 1);
        CraftingManager.getInstance().addRecipe(new ItemStack(CraftingBlocks.craftiniumTable), "XX ", "XX ", 'X', CraftingItems.craftiniumLump);
        CraftingManager.getInstance().addRecipe(forge, "XXX", "X X", "XXX", 'X', CraftingItems.craftiniumLump);

        RecipeSorter.register("randores:forge_upgrade", CraftiniumForgeUpgradeRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
        RecipeSorter.register("randores:flexible_recipe", FlexibleCraftingRecipe.class, RecipeSorter.Category.SHAPED, "after:minecraft:shaped");
        RecipeSorter.register("randores:tome_recipe", TomeCraftingRecipe.class, RecipeSorter.Category.SHAPELESS, "after:minecraft:shapeless");

        CraftingManager.getInstance().addRecipe(new TomeCraftingRecipe());

        CraftiniumForgeUpgradeRecipe upgradeRecipe = new CraftiniumForgeUpgradeRecipe();
        upgradeRecipe.u(CraftingItems.craftiniumLump, 1 / 8f);
        CraftingManager.getInstance().addRecipe(upgradeRecipe);

        for (int i = 0; i < Randores.registeredAmount(); i++) {
            Item material = FlexibleItemRegistry.getMaterial(i);
            for (CraftableType type : CraftableType.values()) {
                CraftiniumRecipeRegistry.register(new FlexibleRecipe(i, type, 'X', material, 'S', "stickWood", 'T', "torch", 'R', "string"));
                CraftingManager.getInstance().addRecipe(new FlexibleCraftingRecipe(i, type, 'X', material, 'S', "stickWood", 'T', "torch", 'R', "string"));
            }
            CraftiniumSmeltRegistry.register(new FlexibleSmelt(i));
        }
        MaterialDefinition.CRAFTING_MAPPINGS.put('S', Items.STICK);
        MaterialDefinition.CRAFTING_MAPPINGS.put('T', Item.getItemFromBlock(Blocks.TORCH));
        MaterialDefinition.CRAFTING_MAPPINGS.put('R', Items.STRING);


        this.logger.info("Testing the names algorithm...");
        Random random = new Random();
        for (int i = 0; i < 10; i++) {
            this.logger.info(RandoresNameAlgorithm.name(new Color(random.nextInt(256), random.nextInt(256), random.nextInt(256))));
        }
        this.logger.info("Finished testing names algorithm");
        RandoresNetworking.initNetwork();
        this.logger.info("Registering entities...");
        EntityRegistry.registerModEntity(new ResourceLocation("randores:randores_arrow"), RandoresArrow.class, "randores:randores_arrow", 0, this, 20, 20, true);
        this.logger.info("Finished registering entities.");
        this.logger.info("Running proxy pre-initialization...");
        Randores.PROXY.preInit(ev);
        this.logger.info("Proxy pre-initialized.");
    }

    @Mod.EventHandler
    public void onInit(FMLInitializationEvent ev) throws IOException {
        FMLInterModComms.sendMessage("waila", "register", "com.gmail.socraticphoenix.forge.randore.compatability.waila.RandoresWailaHandler.callbackRegister");
        NetworkRegistry.INSTANCE.registerGuiHandler(this, new RandoresGuiHandler());
        GameRegistry.registerWorldGenerator(new RandoresWorldGenerator(), 10);
        GameRegistry.registerWorldGenerator(new RandoresAltarGenerator(), 0);
        this.logger.info("Running proxy initialization...");
        Randores.PROXY.init(ev);
        this.logger.info("Proxy initialized.");
    }

    @Mod.EventHandler
    public void onPostInit(FMLPostInitializationEvent ev) {
        this.logger.info("Adding crafting table recipes to craftinium table and furnace recipes to craftinium forge...");
        for (IRecipe recipe : CraftingManager.getInstance().getRecipeList()) {
            if (!(recipe instanceof FlexibleCraftingRecipe)) {
                CraftiniumRecipeRegistry.register(new CraftiniumDelegateRecipe(recipe));
            }
        }
        for (Map.Entry<ItemStack, ItemStack> smelt : FurnaceRecipes.instance().getSmeltingList().entrySet()) {
            CraftiniumSmeltRegistry.register(new CraftiniumDelegateSmelt(smelt.getKey(), smelt.getValue(), FurnaceRecipes.instance().getSmeltingExperience(smelt.getKey())));
        }
        this.logger.info("Recipes added.");

        this.logger.info("Registering abilities.");
        Iterator<Potion> iterator = Potion.REGISTRY.iterator();
        while (iterator.hasNext()) {
            Potion next = iterator.next();
            AbilityRegistry.register(new PotionEffectAbility(next));
        }
        this.logger.info("Finished registering.");
        this.logger.info("Running proxy post-initialization...");
        Randores.PROXY.postInit(ev);
        this.logger.info("Proxy post-initialized.");
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

    public File getPackDir() {
        return this.packs;
    }

}
