package org.jetbrains.fortran

import com.intellij.openapi.fileTypes.FileTypeConsumer
import com.intellij.openapi.fileTypes.FileTypeFactory

class FortranFixedFormFileTypeFactory : FileTypeFactory() {
    companion object {
        private val fixedFormExtensions = "f;for"
    }

    override fun createFileTypes(fileTypeConsumer: FileTypeConsumer) {
        fileTypeConsumer.consume(FortranFileType, fixedFormExtensions)
    }
}
