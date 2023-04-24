package mineript;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;

public class config {

    private static File file;
    private static FileConfiguration configFile;

    public static void setup(){
        file = new File(Mineript.getPlugin().getDataFolder(), "config.yml");

        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException ex) {
                System.err.println("CREATE FILE ERROR");
                System.err.println(ex);
            }
        }
        configFile = YamlConfiguration.loadConfiguration(file);
    }

    public static FileConfiguration get() {
        return configFile;
    }

    public static void edit(String what, Object to) {
        configFile.set(what, to);
        save();
    }

    public static void save() {
        try {
            configFile.save(file);
        } catch (IOException ex) {
            System.out.println("SAVE FILE ERROR");
        }
    }

    public static void reload() {
        configFile = YamlConfiguration.loadConfiguration(file);
    }
}
