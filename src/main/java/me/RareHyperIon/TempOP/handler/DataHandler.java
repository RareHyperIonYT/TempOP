package me.RareHyperIon.TempOP.handler;

import java.io.File;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

public class DataHandler {

    private final Logger logger;

    public DataHandler(final Logger logger) {
        this.logger = logger;
    }

    public final Map<UUID, Long> load(final File dataFolder) {
        final File dataFile = new File(dataFolder, "data.bin");

        if(!dataFile.exists()) return new HashMap<>(); // Ignore, there is no operators to load.

        this.logger.info("Loading data...");

        try(final ObjectInputStream stream = new ObjectInputStream(Files.newInputStream(dataFile.toPath()))) {
            final Object object = stream.readObject();

            if(object instanceof Map) {
                @SuppressWarnings("unchecked")
                final Map<UUID, Long> operators = (Map<UUID, Long>) object;
                this.logger.info("Successfully loaded!");
                return operators;
            }
        } catch (final IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return new HashMap<>();
    }

    public void save(final File dataFolder, final Map<UUID, Long> operators) {
        this.logger.info("Saving data...");

        final File dataFile = new File(dataFolder, "data.bin");

        try(final ObjectOutputStream stream = new ObjectOutputStream(Files.newOutputStream(dataFile.toPath()))) {
            if(!dataFile.exists() && dataFile.createNewFile()) this.logger.info("Created data binary.");
            stream.writeObject(operators);
        } catch (final IOException e) {
            e.printStackTrace();
        } finally {
            this.logger.info("Successfully saved!");
        }
    }

}
