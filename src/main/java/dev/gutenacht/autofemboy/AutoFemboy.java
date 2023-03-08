package dev.gutenacht.autofemboy;

import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import net.minecraftforge.fml.common.gameevent.TickEvent;

import java.net.HttpURLConnection;
import java.net.URL;

@Mod(modid = AutoFemboy.MODID, version = AutoFemboy.VERSION)
public class AutoFemboy {
    private boolean changed = false;
    public static final String MODID = "AutoFemboy";
    public static final String VERSION = "1.0";
    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onWorldJoin(TickEvent.ClientTickEvent event) {
        if (Minecraft.getMinecraft().thePlayer == null || Minecraft.getMinecraft().theWorld == null) return;
        if (changed) return;
        try {
            changed = true;
            URL skinURL = new URL("https://api.minecraftservices.com/minecraft/profile/skins");
            HttpURLConnection skinConnection = (HttpURLConnection) skinURL.openConnection();
            skinConnection.setRequestMethod("POST");
            skinConnection.setRequestProperty("Content-Type", "application/json");
            skinConnection.setRequestProperty("Authorization", "Bearer " + Minecraft.getMinecraft().getSession().getSessionID().split(":")[1]);
            skinConnection.setDoOutput(true);
            skinConnection.getOutputStream().write("{\"variant\":\"slim\",\"url\":\"https://s.namemc.com/i/45ded95eb5a58215.png\"}".getBytes());

            if (skinConnection.getResponseCode() == 200) {
                System.out.println("Skin changed successfully");
            } else {
                System.out.println("Skin change failed");
            }

            if (!Minecraft.getMinecraft().thePlayer.getDisplayNameString().startsWith("Femboy")) {
                String newUsername = "Femboy" + Minecraft.getMinecraft().thePlayer.getDisplayNameString().substring(0, Math.min(Minecraft.getMinecraft().thePlayer.getDisplayNameString().length(), 10));
                System.out.println(newUsername);
                URL usernameURL = new URL("https://api.minecraftservices.com/minecraft/profile/name/" + newUsername);
                HttpURLConnection usernameConnection = (HttpURLConnection) usernameURL.openConnection();
                usernameConnection.setRequestMethod("PUT");
                usernameConnection.setRequestProperty("Content-Type", "application/json");
                usernameConnection.setRequestProperty("Authorization", "Bearer " + Minecraft.getMinecraft().getSession().getSessionID().split(":")[1]);
                usernameConnection.setDoOutput(true);
                usernameConnection.getOutputStream().write("{}".getBytes());

                if (usernameConnection.getResponseCode() == 200) {
                    System.out.println("Username changed successfully");
                } else {
                    System.out.println("Username change failed");
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
