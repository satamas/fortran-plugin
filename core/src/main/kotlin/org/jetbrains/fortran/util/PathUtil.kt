package org.jetbrains.fortran.util

import com.intellij.openapi.application.PathManager
import java.io.File

object PathUtil {
    fun getResourcePathForClass(aClass: Class<*> ) : File {
        val path = "/" + aClass.name.replace('.', '/') + ".class"
        val resourceRoot = PathManager.getResourceRoot(aClass, path) ?:
                throw IllegalStateException("Resource not found: " + path)
        return File(resourceRoot).absoluteFile
    }
}
