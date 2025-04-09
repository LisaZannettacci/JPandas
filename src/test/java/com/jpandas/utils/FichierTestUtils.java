package com.jpandas.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

public class FichierTestUtils {

    public static Path creerFichierCSVTemporaire() throws IOException {
        Path fichierTemp = Files.createTempFile("exemple", ".csv");

        Files.write(fichierTemp, Arrays.asList(
                "Nom,Age,Ville",
                "Alice,25,Grenoble",
                "Bob,30,Lyon",
                "Charlie,22,Paris"
        ));
        
        return fichierTemp;
    }
}
