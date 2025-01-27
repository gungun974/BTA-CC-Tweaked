package dan200.computercraft;

import net.minecraft.client.Minecraft;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ResourceManager {


    public Identifier[] findResources(String namespace, String subPath) {
        final String path = "/assets/" + namespace + "/" + subPath;

        List<Identifier> identifiers = new ArrayList<>();

        try {
            URI uri = Minecraft.getMinecraft().getClass().getResource(path).toURI();
            Path dirPath;

            try {
                dirPath = Paths.get(uri);
            } catch (FileSystemNotFoundException e) {
                Map<String, String> env = new HashMap<>();
                FileSystem fileSystem = FileSystems.newFileSystem(uri, env);
                dirPath = fileSystem.getPath(path);
            }

            Files.list(dirPath).forEach(found -> {
                identifiers.add(new Identifier(namespace, found.toString().replaceFirst("/assets/" + namespace + "/", "")));
            });
        } catch (Exception e) {
        }

        return identifiers.toArray(new Identifier[0]);
    }

    public Resource getResource(Identifier identifier) {
        return new Resource(identifier);
    }

    public static class Identifier {
        private final String namespace;
        private final String subPath;

        public Identifier(String namespace, String subPath) {
            this.namespace = namespace;
            this.subPath = subPath;
        }

        public String getNamespace() {
            return namespace;
        }

        public String getPath() {
            return subPath;
        }
    }

    public static class Resource {
        private final Identifier identifier;

        public Resource(Identifier identifier) {
            this.identifier = identifier;
        }

        public InputStream getInputStream() {
            return Minecraft.getMinecraft().getClass().getResourceAsStream( "/assets/" + identifier.namespace + "/" + identifier.subPath);
        }
    }
}
