package net.shotatevdorashvili.fastblockplacement;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.common.util.Lazy;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = FastBlockPlacement.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientSideHandling {
    private static int ticker = 20;

    @SubscribeEvent
    public static void registerKeyBindings(RegisterKeyMappingsEvent event)
    {
        event.register(TOGGLE_INSTAPLACE_BIND.get());
    }

    public static final Lazy<KeyMapping> TOGGLE_INSTAPLACE_BIND = Lazy.of(() -> new KeyMapping(
            "key." + FastBlockPlacement.MODID + ".enableinstaplace",  // The translation key for the button's label
            KeyConflictContext.IN_GAME,
            InputConstants.getKey(InputConstants.KEY_U, -1),
            "key.categories." + FastBlockPlacement.MODID
    ));

    @Mod.EventBusSubscriber(modid = FastBlockPlacement.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
    public class ClientForgeEvents
    {
        @SubscribeEvent
        public static void onClientTick(TickEvent.ClientTickEvent event)
        {
            if(ticker > 0)
            {
                ticker--;
            }

            if(TOGGLE_INSTAPLACE_BIND.get().isDown() && Minecraft.getInstance().player != null && ticker <= 0)
            {
                ticker = 20;
                TOGGLE_INSTAPLACE_BIND.get().consumeClick();

                if(Config.isInstantPlacing) {
                    Minecraft.getInstance().player.displayClientMessage(Component.translatable("message." + FastBlockPlacement.MODID + ".disabledinstaplace"), true);
                    Minecraft.getInstance().player.playSound(SoundEvents.NOTE_BLOCK_PLING.get());
                    Config.isInstantPlacing = false;
                } else {
                    Minecraft.getInstance().player.displayClientMessage(Component.translatable("message." + FastBlockPlacement.MODID + ".enabledinstaplace"), true);
                    Minecraft.getInstance().player.playSound(SoundEvents.NOTE_BLOCK_CHIME.get());
                    Config.isInstantPlacing = true;
                }
            }
        }
    }
}

