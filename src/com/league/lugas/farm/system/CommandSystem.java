package com.league.lugas.farm.system;

import com.league.lugas.farm.Main;
import com.league.lugas.farm.commands.Farm;
import com.league.lugas.farm.commands.FarmLeave;
import com.league.lugas.farm.utils.ClassFinder;
import org.bukkit.command.CommandExecutor;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class CommandSystem {

    private static final String commandPackage = "com.league.lugas.farm.commands";

    public CommandSystem(File pluginFile) {
        Main plugin = Main.getPlugin(Main.class);
        boolean hasCommandExecutor = false;
        ZipInputStream zis;
        ZipEntry entry;
        List<Class> classes = new ArrayList<>();
        try {
            zis = new ZipInputStream(new FileInputStream(pluginFile));
            while ((entry = zis.getNextEntry()) != null) {
                String fileName = entry.getName().replace("/", ".");
                if (!fileName.startsWith(commandPackage) || !fileName.endsWith(".class")) continue;
                fileName = fileName.replace(".class", "");

                classes.add(Class.forName(fileName));
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        for (Class c : classes) {
            try {
                for (Class interfaceClass : c.getInterfaces()) {
                    if (interfaceClass.equals(CommandExecutor.class)) {
                        hasCommandExecutor = true;
                        break;
                    }
                }
                if (!hasCommandExecutor) continue;
                Annotation cmdName = c.getAnnotation(CommandName.class);
                plugin.getCommand(((CommandName)cmdName).value()).setExecutor((CommandExecutor) c.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                e.printStackTrace();
            }
        }
    }
}
