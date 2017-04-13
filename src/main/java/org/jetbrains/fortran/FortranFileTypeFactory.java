package org.jetbrains.fortran;

import com.intellij.openapi.fileTypes.FileTypeConsumer;
import com.intellij.openapi.fileTypes.FileTypeFactory;
import org.jetbrains.annotations.NotNull;

public class FortranFileTypeFactory extends FileTypeFactory {
    @Override
    public void createFileTypes(@NotNull FileTypeConsumer fileTypeConsumer) {
        for (int i = 0; i < FortranFileType.DEFAULT_EXTENSIONS.length; i++)
        {
            fileTypeConsumer.consume(FortranFileType.INSTANCE, FortranFileType.DEFAULT_EXTENSIONS[i]);
        }
    }
}
