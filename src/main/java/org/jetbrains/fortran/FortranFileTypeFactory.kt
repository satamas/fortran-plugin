package org.jetbrains.fortran

import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory

class FortranFileTypeFactory : FileTypeFactory() {
    override fun createFileTypes(fileTypeConsumer: FileTypeConsumer) {
        for (i in FortranFileType.DEFAULT_EXTENSIONS.indices) {
            fileTypeConsumer.consume(FortranFileType, FortranFileType.DEFAULT_EXTENSIONS[i])
        }
    }
}
