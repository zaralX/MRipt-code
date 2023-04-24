package mineript;

import mineript.code.LoadCode;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.logging.Logger;

public final class Mineript extends JavaPlugin {

    public static Plugin plugin;

    @Override
    public void onEnable() {
        Logger logger = Logger.getLogger("");
        plugin = Bukkit.getServer().getPluginManager().getPlugin("MineRipt");
        // Generate folders and files
        FileWorker fileWorker = new FileWorker();
        try { fileWorker.generate(); } catch (IOException e) { throw new RuntimeException(e); }
        fileWorker.load();

        // Load code
        try { LoadCode.load(fileWorker.codes); } catch (IOException e) { throw new RuntimeException(e); }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Plugin getPlugin() {
        return plugin;
    }
}
