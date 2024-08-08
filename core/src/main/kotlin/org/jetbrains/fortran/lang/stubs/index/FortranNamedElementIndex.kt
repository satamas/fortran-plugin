package org.jetbrains.fortran.lang.stubs.index

import com.intellij.psi.stubs.StringStubIndexExtension
import com.intellij.psi.stubs.StubIndexKey
import org.jetbrains.fortran.lang.psi.ext.FortranNamedElement
import org.jetbrains.fortran.lang.stubs.FortranFileStub

class FortranNamedElementIndex : StringStubIndexExtension<FortranNamedElement>() {
    override fun getVersion(): Int = FortranFileStub.Type.stubVersion
    override fun getKey(): StubIndexKey<String, FortranNamedElement> = KEY

    companion object {
        val KEY: StubIndexKey<String, FortranNamedElement> =
                StubIndexKey.createIndexKey("org.jetbrains.fortran.lang.core.stubs.index.RustNamedElementIndex")
    }
}