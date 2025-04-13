package dev.gllody.meteorcapes.mixin;

import com.mojang.authlib.GameProfile;
import meteordevelopment.meteorclient.systems.modules.Modules;
import meteordevelopment.meteorclient.systems.modules.misc.NameProtect;
import meteordevelopment.meteorclient.utils.network.Capes;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.DefaultSkinHelper;
import net.minecraft.client.util.SkinTextures;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import dev.gllody.meteorcapes.modules.CapesModule;

import net.minecraft.client.network.PlayerListEntry;



import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Objects;

import static meteordevelopment.meteorclient.MeteorClient.mc;


@Mixin(PlayerListEntry.class)
public abstract class PlayerListEntryMixin {
    @Unique
    private boolean loadedCapeTexture;

    public String name;
    public String cape;

    @Unique
    private Identifier customCapeTexture;

    @Inject(method = "<init>(Lcom/mojang/authlib/GameProfile;Z)V", at = @At("TAIL"))
    private void initHook(GameProfile profile, boolean secureChatEnforced, CallbackInfo ci) {
        getTexture(profile);
    }

    @Inject(method = "getSkinTextures", at = @At("TAIL"), cancellable = true)
    private void getCapeTexture(CallbackInfoReturnable<SkinTextures> cir) {
        if (customCapeTexture != null) {
            SkinTextures prev = cir.getReturnValue();
            SkinTextures newTextures = new SkinTextures(prev.texture(), prev.textureUrl(), customCapeTexture, customCapeTexture, prev.model(), prev.secure());
            cir.setReturnValue(newTextures);
        }
    }

    @Unique
    private void getTexture(GameProfile profile) {
        if (loadedCapeTexture) return;
        loadedCapeTexture = true;

        Util.getMainWorkerExecutor().execute(() -> {
            try {
                URL capesList = new URL("https://raw.githubusercontent.com/Gllody/nhcapes/refs/heads/main/capeBase.txt");
                BufferedReader in = new BufferedReader(new InputStreamReader(capesList.openStream()));
                String inputLine;
                while ((inputLine = in.readLine()) != null) {
                    String colune = inputLine.trim();
                    name = colune.split(":")[0];
                    cape = colune.split(":")[1];

                    if (Objects.equals(profile.getName(), name)) {
                        customCapeTexture = Identifier.of("meteorcapes", "textures/capes/" + cape + ".png");
                        return;
                    }
                    Modules modules = Modules.get();
                    if (CapesModule.capeed == null) { return; } else {
                        name = mc.getSession().getUsername();
                        cape = CapesModule.capeed;
                        if (Objects.equals(profile.getName(), name)) {
                            customCapeTexture = Identifier.of("meteorcapes", "textures/capes/" + cape + ".png");
                            return;
                        }
                    }
                }
            } catch (Exception ignored) {
            }
        });
    }

    @Shadow
    public abstract GameProfile getProfile();

    @Inject(method = "getSkinTextures", at = @At("HEAD"), cancellable = true)
    private void onGetTexture(CallbackInfoReturnable<SkinTextures> info) {
        if (getProfile().getName().equals(MinecraftClient.getInstance().getSession().getUsername())) {
            if (Modules.get().get(NameProtect.class).skinProtect()) {
                info.setReturnValue(DefaultSkinHelper.getSkinTextures(getProfile()));
            }
        }
    }
}

