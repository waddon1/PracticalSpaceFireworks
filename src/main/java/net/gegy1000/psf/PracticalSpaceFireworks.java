package net.gegy1000.psf;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModLoadingContext;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.server.ServerStoppedEvent;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import net.gegy1000.psf.server.ServerProxy;
import net.gegy1000.psf.server.init.PSFBlocks;

import java.util.function.Supplier;

@Mod(PracticalSpaceFireworks.MODID)
public class PracticalSpaceFireworks {
    public static final String MODID = "practicalspacefireworks";
    public static final String VERSION = "1.0.0";
    public static final String NAME = "Practical Space Fireworks";
    
    public static final Logger LOGGER = LogManager.getLogger(MODID);
    
    // Use DeferredRegister for registration
    private static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = 
        DeferredRegister.create(BuiltInRegistries.CREATIVE_MODE_TAB, MODID);
        
    // Create creative tab using the new system
    public static final Supplier<CreativeModeTab> TAB = CREATIVE_MODE_TABS.register("main", () ->
        CreativeModeTab.builder()
            .icon(() -> new ItemStack(PSFBlocks.STRUT_CUBE))
            .title(net.minecraft.network.chat.Component.translatable("itemGroup." + MODID))
            .displayItems((parameters, output) -> {
                // Add your items here
            })
            .build()
    );

    private final ServerProxy proxy;
    
    public PracticalSpaceFireworks(IEventBus modEventBus) {
        // Register the mod to the event bus
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, PSFConfig.SPEC);
        
        // Initialize proxy
        this.proxy = new ServerProxy();
        
        // Register deferred registers
        CREATIVE_MODE_TABS.register(modEventBus);
        
        // Register mod event listeners
        modEventBus.addListener(this::setup);
        modEventBus.addListener(this::clientSetup);
        
        // Register server event listeners
        NeoForge.EVENT_BUS.addListener(this::onServerStopped);
        
        // Enable universal bucket if needed
        FluidType.setUniversalBucketEnabled(true);
    }
    
    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            proxy.onPreInit();
            proxy.onInit();
            proxy.onPostInit();
            
            // Register ore dictionary equivalents using tags
            registerTags();
        });
    }
    
    private void clientSetup(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> {
            // Client-side setup code
        });
    }
    
    private void onServerStopped(ServerStoppedEvent event) {
        proxy.getSatellites().flush();
    }
    
    public static ResourceLocation location(String path) {
        return new ResourceLocation(MODID, path);
    }
    
    public static void openGui(int guiId, Player player, Level world, BlockPos pos) {
        // Update with new menu system
        // Will need to be implemented using the new ContainerProvider system
    }
    
    private void registerTags() {
        // Replace old OreDictionary with tags
        // Example:
        // TagKey<Item> ironBarsTag = TagKey.create(Registries.ITEM, new ResourceLocation("forge", "iron_bars"));
        // Add registration here
    }
    
    // Create a new config class
    public static class PSFConfig {
        public static final net.neoforged.neoforge.common.ModConfigSpec SPEC;
        
        static {
            var builder = new net.neoforged.neoforge.common.ModConfigSpec.Builder();
            // Add your config options here
            SPEC = builder.build();
        }
    }
}