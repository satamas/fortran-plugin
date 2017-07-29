package org.jetbrains.fortran

import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory

class FortranFixedFormFileTypeFactory : FileTypeFactory() {
    override fun createFileTypes(fileTypeConsumer: FileTypeConsumer) {
        for (i in FortranFixedFormFileType.DEFAULT_EXTENSIONS.indices) {
            fileTypeConsumer.consume(FortranFixedFormFileType, FortranFixedFormFileType.DEFAULT_EXTENSIONS[i])
        }
    }
}
