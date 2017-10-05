package org.jetbrains.fortran

import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory

class FortranFileTypeFactory : FileTypeFactory() {
    companion object {
        val freeFormExtensions = "f90;f95;f03;f08"
    }

    override fun createFileTypes(fileTypeConsumer: FileTypeConsumer) {
        fileTypeConsumer.consume(FortranFileType, freeFormExtensions)
    }
}
