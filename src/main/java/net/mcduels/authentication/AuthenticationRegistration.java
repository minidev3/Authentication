package net.mcduels.authentication;

import net.mcduels.authentication.exceptions.LackOfPermissionException;
import net.mcduels.authentication.exceptions.PasswordBoundsException;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class AuthenticationRegistration<T , V> {

    private UUID identification;

    private T username;
    private V password;

    private int autehntication_reset = 0; //default password reset integer

    public AuthenticationRegistration(final UUID id, T user, V pass, int reset) {
        this.identification = id;
        this.password = pass;
        this.username = user;
        this.autehntication_reset = reset;
    }

    @SuppressWarnings("unchecked")
    public void initialize() throws IOException, PasswordBoundsException {
        JSONObject object = new JSONObject();

        object.put("identification", this.identification);
        object.put("username", this.username);

        int[] passArray = (int[]) password;

        if (passArray.length > 10) {

            for (Player all : Authentication.getInstance().getServer().getOnlinePlayers()) {
                if (all.getUniqueId().equals(identification)) {

                    throw new PasswordBoundsException(); // TODO: customize it
                }
            }
        } else {

            object.put("password", this.password);
            object.put("authentication_reset", this.autehntication_reset);
            object.put("timestamp", getTimeStamp());
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(Authentication.getInstance().getAuthenticationFile()))) {

            writer.write(object.toJSONString());
            writer.flush();

        } finally {
            Authentication.getInstance().getLogger().info(this.toString());
        }
    }

    @Override
    public String toString() {
        return getTimeStamp() + ":" + identification + ":" + username + ":" + password;
    }

    private String getTimeStamp() {
        return new SimpleDateFormat("yyyy.MM.dd.HH.mm.ss").format(new Date());
    }

    public static class AuthenticationManager {

        /**
         * GLOBAL AUTHENTICATION GETTER
         */

        public static String getAuthenticationPlayer(Player player, CommandSender sender) throws LackOfPermissionException {
            if (!sender.hasPermission("authentication.check")) { throw new LackOfPermissionException(); } //TODO: customize it

            JSONParser parser = new JSONParser();

            try {

                Object obj = parser.parse(new FileReader(Authentication.getInstance().getAuthenticationFile()));

                JSONObject object = (JSONObject) obj;

                sender.sendMessage("&a&lFetching Authentication data (GLOBAL)...".replace('&', '§'));

                if (object.containsKey(player.getUniqueId())) {

                    String id = (String) object.get("identification");
                    String user = (String) object.get("username");
                    String pass = (String) object.get("password");
                    String time = (String) object.get("timestamp");
                    int reset = (int) object.get("authentication_reset");

                    return"&eIdentification: &f".replace('&', '§') + id + "\n"
                            + "&eUsername: &f".replace('&', '§') + user + "\n"
                            + "&ePassword: &f".replace('&', '§') + pass + "\n"
                            + "&eReset: &f".replace('&', '§') + reset + "\n"
                            + "&eTimeStamp: &f".replace('&', '§') + time;
                } else {

                    sender.sendMessage(player.getName() + " wasn't found in the database! :(");

                }
            } catch (IOException | ParseException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
