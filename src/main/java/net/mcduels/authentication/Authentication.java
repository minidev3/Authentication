package net.mcduels.authentication;

import jdk.nashorn.internal.objects.annotations.Getter;
import org.bukkit.plugin.java.JavaPlugin;

public final class Authentication extends JavaPlugin {

    private static Authentication instance;

    public static Authentication getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        Authentication.instance = this;
    }

    @Override
    public void onDisable() {
        Authentication.instance = null;
    }

    @Getter
    String getAuthenticationFile() {
        return "authentication.json";
    }
}
