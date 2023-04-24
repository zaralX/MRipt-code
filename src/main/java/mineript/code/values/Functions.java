package mineript.code.values;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;

import java.util.HashMap;
import java.util.Map;

public class Functions {

    private static Map<String, Function> functions;

    static  {
        functions = new HashMap<>();

        functions.put("sin", args -> {
            if (args.length != 1) throw new RuntimeException("Expected 1 argument, received "+args.length);
            return new NumberValue(Math.sin(args[0].asDouble()));
        });

        functions.put("cos", args -> {
            if (args.length != 1) throw new RuntimeException("Expected 1 argument, received "+args.length);
            return new NumberValue(Math.cos(args[0].asDouble()));
        });

        functions.put("round", args -> {
            if (args.length != 1) throw new RuntimeException("Expected 1 argument, received "+args.length);
            return new NumberValue(Math.round(args[0].asDouble()));
        });

        functions.put("ceil", args -> {
            if (args.length != 1) throw new RuntimeException("Expected 1 argument, received "+args.length);
            return new NumberValue(Math.ceil(args[0].asDouble()));
        });

        functions.put("floor", args -> {
            if (args.length != 1) throw new RuntimeException("Expected 1 argument, received "+args.length);
            return new NumberValue(Math.floor(args[0].asDouble()));
        });

        functions.put("array", ArrayValue::new);

        functions.put("setblock", args -> {
            System.err.println(args.toString());
            if (args.length != 5) throw new RuntimeException("Expected 5 argument, received "+args.length);
            if (Material.getMaterial(args[4].asString()) == null) throw new RuntimeException("Unknown Block "+args[4]);
            if (Bukkit.getWorld(args[0].asString()) == null) throw new RuntimeException("Unknown World "+args[0]);
            Bukkit.getWorld(args[0].asString()).getBlockAt(new Location(Bukkit.getWorld(args[0].asString()), args[1].asNumber(), args[2].asNumber(), args[3].asNumber())).setType(Material.getMaterial(args[4].asString()));
            return new NumberValue(1);
        });

        functions.put("fill", args -> {
            System.err.println(args.toString());
            if (args.length != 8) throw new RuntimeException("Expected 8 argument, received "+args.length);
            if (Material.getMaterial(args[7].asString()) == null) throw new RuntimeException("Unknown Block "+args[4]);
            if (Bukkit.getWorld(args[0].asString()) == null) throw new RuntimeException("Unknown World "+args[0]);

            final int start_x = Math.min(args[1].asNumber(), args[4].asNumber());
            final int start_y = Math.min(args[2].asNumber(), args[5].asNumber());
            final int start_z = Math.min(args[3].asNumber(), args[6].asNumber());

            final int size_x = args[1].asNumber() > args[4].asNumber() ? args[1].asNumber() - args[4].asNumber() + 1 : args[4].asNumber() - args[1].asNumber() + 1;
            final int size_y = args[2].asNumber() > args[5].asNumber() ? args[2].asNumber() - args[5].asNumber() + 1 : args[5].asNumber() - args[2].asNumber() + 1;
            final int size_z = args[3].asNumber() > args[6].asNumber() ? args[3].asNumber() - args[6].asNumber() + 1 : args[6].asNumber() - args[3].asNumber() + 1;

            for (int x = 0; x < size_x; x++) {
                for (int y = 0; y < size_y; y++) {
                    for (int z = 0; z < size_z; z++) {
                        Bukkit.getWorld(args[0].asString()).getBlockAt(new Location(Bukkit.getWorld(args[0].asString()), start_x+x, start_y+y, start_z+z)).setType(Material.getMaterial(args[7].asString()));
                    }
                }
            }
            return new NumberValue(1);
        });
    }

    public static boolean isExists(String key) {
        return functions.containsKey(key);
    }

    public static Function get(String key) {
        if (!isExists(key)) throw new RuntimeException("Unknown Function" + key);
        return functions.get(key);
    }

    public static void set(String key, Function function) {
        functions.put(key, function);
    }
}
