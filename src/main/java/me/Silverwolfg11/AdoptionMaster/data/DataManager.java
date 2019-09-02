package me.Silverwolfg11.AdoptionMaster.data;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import me.Silverwolfg11.AdoptionMaster.objects.Adoption;
import me.Silverwolfg11.AdoptionMaster.AdoptionMaster;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.UUID;

public class DataManager {

    private static final File FILE = new File("plugins/AdoptionMaster", "adoptions.json");

    private static final Gson gson = new Gson();

    public void loadData() {
        if (!FILE.getParentFile().isDirectory()) {
            FILE.getParentFile().mkdir();
        }

        if(FILE.exists()) {
            loadAdoptions();
            return;
        }

        if (!FILE.exists()) {
            try {
                FILE.createNewFile();
            } catch (IOException ex) {
                ex.printStackTrace();
            }

            if(AdoptionMaster.adoptions == null)
                AdoptionMaster.adoptions = new ArrayList<>();
        }
    }

    public void loadAdoptions() {
        try {
            Scanner scanner = new Scanner(FILE);

            Type type = new TypeToken<ArrayList<Adoption>>(){}.getType();

            AdoptionMaster.adoptions = gson.fromJson(scanner.nextLine(), type);

        } catch(Exception e) {
            if (!(e instanceof NoSuchElementException)) {
                e.printStackTrace();
            }
        }
        if(AdoptionMaster.adoptions == null)
            AdoptionMaster.adoptions = new ArrayList<>();
    }


    public static void saveAdoptions() {
        try {
            PrintWriter writer = new PrintWriter(FILE);

            writer.write(gson.toJson(AdoptionMaster.adoptions));

            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static Adoption locateAdoptionByParent(UUID parent) {
        if(AdoptionMaster.adoptions.isEmpty())
            return null;

        for(Adoption adopt : AdoptionMaster.adoptions) {
            if(adopt.isParent(parent))
                return adopt;
        }

        return null;
    }

    public static Adoption locateAdoptionByChild(UUID child) {
        if(AdoptionMaster.adoptions.isEmpty())
            return null;

        for(Adoption adopt : AdoptionMaster.adoptions) {
            if(adopt.containsChild(child))
                return adopt;
        }

        return null;
    }

    public static Adoption createAdoption(UUID parent1, UUID parent2) {
        Adoption adopt = new Adoption(parent1, parent2);

        AdoptionMaster.adoptions.add(adopt);

        return adopt;
    }

}
