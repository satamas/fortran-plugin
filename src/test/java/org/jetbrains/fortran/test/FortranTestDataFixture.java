package org.jetbrains.fortran.test;

import com.intellij.openapi.util.io.FileUtil;
import com.intellij.testFramework.TestDataFile;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.fortran.util.PathUtil;

import java.io.File;

public class FortranTestDataFixture {
    public static String navigationMetadata(@TestDataFile String testFile) {
        return testFile;
    }

    public static String getHomeDirectory() {
//        TODO: change this
        File resourceRoot = PathUtil.getResourcePathForClass(FortranTestDataFixture.class);
        File f1 = resourceRoot.getParentFile();
        File f2 = f1.getParentFile();
        String f3 = f2.getParent();
        return FileUtil.toSystemIndependentName(f3);
    }

    @NotNull
    public static File getLangTestData() { return new File(getHomeDirectory(), "src/test/resources/"); }
}
