package me.Silverwolfg11.AdoptionMaster;

import org.bukkit.ChatColor;

import java.text.MessageFormat;
import java.util.ResourceBundle;

public class Messages {
    private static Messages instance = new Messages();

    private ResourceBundle bundle;

    private Messages() {
        loadBundle();
    }

    public static String getString(String key, Object... args) {
        try {
            return instance.format(instance.bundle.getString(key), args);
        } catch (Exception e) {
            return key;
        }
    }

    private void loadBundle() {
        bundle = ResourceBundle.getBundle("messages");
    }

    private String format(String message, Object... args) {
        message = message.replace("{prefix}", bundle.getString("prefix"));

        if (args != null) {
            message = MessageFormat.format(message, args);
        }

        message = ChatColor.translateAlternateColorCodes('&', message);

        return message;
    }


}
