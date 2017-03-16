package org.jetbrains.fortran.lang.psi;

import com.intellij.psi.tree.IElementType;
import org.jetbrains.fortran.FortranLanguage;

public class FortranCompositeElementType extends IElementType {
    public FortranCompositeElementType(String debug) {
       super(debug, FortranLanguage.INSTANCE);
    }
}

