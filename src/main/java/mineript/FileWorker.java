package mineript;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FileWorker {

    public List<File> codes;

    public final void generate() throws IOException {
        final String default_folder = String.valueOf(Mineript.getPlugin().getDataFolder());


        new File(default_folder).mkdir();

        if (!new File(default_folder+"\\code").mkdir()) {
            //Files.copy(Paths.get("examples\\example.mr").toFile(), new File(default_folder+"\\code"));
            //Mineript.getPlugin().saveResource("examples\\example.mr", false);
        }

        config.setup();
        config.get().addDefault("debug", false);
        config.get().options().copyDefaults(true);
        config.save();
    }

    public final void load() {
        codes = new ArrayList<>(Arrays.asList(new File(Mineript.getPlugin().getDataFolder() +"\\code").listFiles()));
        List<Integer> delete = new ArrayList<>();
        for (int i = 0; i < codes.size(); i++) {
            if (!codes.get(i).isFile()) {
                delete.add(i);
            } else if (!codes.get(i).toString().substring(codes.get(i).toString().length() - 2).equals("mr")) {
                delete.add(i);
            }
        }
        for (int i = 0; i < delete.size(); i++) {
            codes.remove(delete.get(i)-i);
        }
    }
}
