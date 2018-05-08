package org.jetbrains.fortran.lang.types.ty

import com.intellij.xml.util.XmlStringUtil

open class FortranType

val FortranType.escaped: String
 get() = XmlStringUtil.escapeString(this.toString())