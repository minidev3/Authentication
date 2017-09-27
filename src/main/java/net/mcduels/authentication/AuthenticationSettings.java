package net.mcduels.authentication;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class AuthenticationSettings {

    private static Long registerResetRunnable(Object obj) {
        final Map<Object, Long> runnable = new HashMap<>();

        if (runnable.containsKey(obj)) {

            long elapsedTimeLong = runnable.get(obj) - System.currentTimeMillis();

            return elapsedTimeLong/1000;
        } else {
            runnable.put(obj, System.currentTimeMillis() + (10 * 1000));
        }

        return null;
    }

    private static BaseComponent[] clickable(Player player) {

        return new ComponentBuilder("are you sure you want to reset?").color(ChatColor.AQUA).append("[CANCEL]").color(ChatColor.RED).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/authentication admin cancel " + player)).append("[CONFIRM]").color(ChatColor.GREEN).event(new ClickEvent(ClickEvent.Action.RUN_COMMAND, "/authentication admin reset " + player)).create();
    }
}
