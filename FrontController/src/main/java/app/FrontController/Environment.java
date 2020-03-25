package app.FrontController;

import app.interfaces.Executable;
import org.reflections.Reflections;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.*;
import java.util.Set;

public class Environment implements Runnable {

    private URLClassLoader classLoader;
    private WatchService watchService;
    private boolean watchFiles = true;

    public void loadEnvironment()    {
        try {
            File file = new File("D:\\StructuredDesign\\ModuleOne\\build\\libs\\ModuleOne-1.0-SNAPSHOT.jar");
            URL[] urls = new URL[]{file.toURI().toURL()};
            switchClassloader(new URLClassLoader(urls, ClassLoader.getSystemClassLoader()));
        } catch (IOException io) {
            throw new IllegalStateException(io);
        }
    }

    private void switchClassloader(URLClassLoader loaded) throws IOException {
        URLClassLoader tmp = classLoader;
        classLoader = loaded;
        if(tmp != null)
            tmp.close();
    }


    @SuppressWarnings("unchecked")
    public <Type extends Executable> Type create(String classname) throws ClassNotFoundException, NoSuchMethodException, InstantiationException, IllegalAccessException, java.lang.reflect.InvocationTargetException {
        Class<Executable> firstGateway = (Class<Executable>) classLoader.loadClass(classname ); // "app.firstModule.FirstGateway"
        Constructor<Executable> constructor = firstGateway.getConstructor();
         return (Type) constructor.newInstance();
    }


    public void stop() {
        try {
            watchFiles = false;
            watchService.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        try {
            watchService = FileSystems.getDefault().newWatchService();
            Path path = Paths.get("D:\\StructuredDesign\\ModuleOne\\build\\libs\\");
            WatchKey register = path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_CREATE);

            while (watchFiles) {
                WatchKey key = watchService.take();

                key.pollEvents()
                        .forEach(event -> {
                            Path file = (Path) event.context();

                            loadEnvironment();
                        });
                key.reset();
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }

    }
}
