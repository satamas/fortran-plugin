package org.jetbrains.fortran.lang;

import com.intellij.lang.ASTNode;
import com.intellij.psi.tree.IElementType;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.fortran.FortranLanguage;
import org.jetbrains.fortran.lang.psi.FortranElement;
import org.jetbrains.fortran.lang.psi.impl.FortranElementImpl;

import java.lang.reflect.Constructor;

public class FortranNodeType extends IElementType {
    private Constructor<? extends FortranElement> myPsiFactory;

    public FortranNodeType(@NotNull @NonNls String debugName, Class<? extends FortranElement> psiClass) {
        super(debugName, FortranLanguage.INSTANCE);
        try {
            myPsiFactory = psiClass != null ? psiClass.getConstructor(ASTNode.class) : null;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException("Must have a constructor with ASTNode");
        }
    }

    public FortranElement createPsi(ASTNode node) {
        assert node.getElementType() == this;

        try {
            if (myPsiFactory == null) {
                return new FortranElementImpl(node);
            }
            return myPsiFactory.newInstance(node);
        } catch (Exception e) {
            throw new RuntimeException("Error creating psi element for node", e);
        }
    }
}