package com.github.blackjack200.ouranos;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import org.yaml.snakeyaml.Yaml;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.Map;

@Log4j2
public class PacketConfig {
    public static final Path PACKET_CONFIG_FILE = Path.of("packet_config.yml");
    @Getter
    private static final Map<String, Boolean> packetSettings = new HashMap<>();

    public static void init() {
        load();
    }

    @SneakyThrows
    public static void load() {
        if (!Files.exists(PACKET_CONFIG_FILE)) {
            try (InputStream in = PacketConfig.class.getClassLoader().getResourceAsStream("packet_config.yml")) {
                if (in != null) {
                    Files.copy(in, PACKET_CONFIG_FILE, StandardCopyOption.REPLACE_EXISTING);
                    log.info("Created default packet_config.yml");
                } else {
                    log.error("Default packet_config.yml not found in resources!");
                    return;
                }
            }
        }
        
        try (FileInputStream inputStream = new FileInputStream(PACKET_CONFIG_FILE.toFile())) {
            Yaml yaml = new Yaml();
            Map<String, Boolean> loaded = yaml.load(inputStream);
            if (loaded != null) {
                packetSettings.clear();
                packetSettings.putAll(loaded);
            }
        } catch (Exception e) {
            log.error("Failed to load packet_config.yml", e);
        }
    }
    
    public static boolean isEnabled(String key) {
        return packetSettings.getOrDefault(key, true);
    }
}
