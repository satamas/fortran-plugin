package org.jetbrains.fortran.util;

import com.intellij.openapi.application.PathManager;
import org.jetbrains.annotations.NotNull;

import java.io.File;

public class PathUtil {
    @NotNull
    public static File getResourcePathForClass(@NotNull Class aClass) {
        String path = "/" + aClass.getName().replace('.', '/') + ".class";
        String resourceRoot = PathManager.getResourceRoot(aClass, path);
        if (resourceRoot == null) {
            throw new IllegalStateException("Resource not found: " + path);
        }
        return new File(resourceRoot).getAbsoluteFile();
    }
}
