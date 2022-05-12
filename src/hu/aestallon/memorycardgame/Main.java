package hu.aestallon.memorycardgame;

import javax.swing.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/*
 *  TODO:
 *      1) Refactor ugly doAction(),
 *      2) Implement 'NEW GAME' button.
 *      3) After victory -> save attemptCount to a text file.
 *              Alert user, if new record is broken.
 *      4) Find cool pictures,
 *      5) Implement picture loading
 */
public class Main {
    public static final Set<ImageIcon> KRESZ_IMAGES = loadImages("resources/kresz");
    public static final Set<ImageIcon> COL_IMAGES = loadImages("resources/placeholder_images");

    private Main() {}

    public static void main(String[] args) {
        new AppFrame();
    }


    private static Set<ImageIcon> loadImages(String path) {
        try (Stream<Path> paths = Files.walk(Paths.get(path))) {
            return paths.filter(Files::isRegularFile)
                    .map(Path::toString)
                    .map(ImageIcon::new)
                    .collect(Collectors.toSet());
        } catch (IOException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(
                    null,
                    "Graphics files couldn't be loaded!",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }

        return null;
    }
}
