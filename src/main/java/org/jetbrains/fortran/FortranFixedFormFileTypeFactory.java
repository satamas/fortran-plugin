package org.jetbrains.fortran;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class FortranFixedFormFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        for (int i = 0; i < FortranFixedFormFileType.DEFAULT_EXTENSIONS.length; i++)
        {
            fileTypeConsumer.consume(FortranFixedFormFileType.INSTANCE, FortranFixedFormFileType.DEFAULT_EXTENSIONS[i]);
        }
    }
}
