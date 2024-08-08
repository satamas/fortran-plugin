package org.jetbrains.fortran.test

import com.intellij.openapi.util.io.FileUtil
import com.intellij.testFramework.TestDataFile
import org.jetbrains.fortran.util.PathUtil

import java.io.File

class FortranTestDataFixture {
    fun navigationMetadata(@TestDataFile testFile : String ) = testFile

    fun getHomeDirectory() : String {
//        TODO: change this
        val resourceRoot = PathUtil.getResourcePathForClass(FortranTestDataFixture::class.java)
        val f1 = resourceRoot.parentFile
        val f2 = f1.parentFile
        val f3 = f2.parent
        return FileUtil.toSystemIndependentName(f3)
    }

    fun getLangTestData() = File(getHomeDirectory(), "src/test/resources/")
}
