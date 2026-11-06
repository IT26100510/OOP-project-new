
package lk.student.pizza.repository;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;

@Component
public class FileDataStore {

    private final Path dataDirectory = Paths.get("data");

    // Constructor
    public FileDataStore() {

        try {
            Files.createDirectories(dataDirectory);

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Cannot create data folder", e
            );
        }
    }

    // Read data from a file
    public synchronized List<String> read(String fileName) {

        Path path = dataDirectory.resolve(fileName);

        try {

            if (!Files.exists(path)) {
                return new ArrayList<>();
            }

            return new ArrayList<>(
                    Files.readAllLines(path, StandardCharsets.UTF_8)
            );

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Could not read " + fileName, e
            );
        }
    }

    // Write data to a file
    public synchronized void write(
            String fileName,
            List<String> lines) {

        Path path = dataDirectory.resolve(fileName);

        try {

            Files.write(
                    path,
                    lines,
                    StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE,
                    StandardOpenOption.TRUNCATE_EXISTING
            );

        } catch (IOException e) {

            throw new IllegalStateException(
                    "Could not write " + fileName, e
            );
        }
    }
}
