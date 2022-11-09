package jdt.visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

public class TypeDeclarationVisitor extends ASTVisitor {
    List<TypeDeclaration> typeDeclarations = new ArrayList<>();
    TypeDeclaration typeDeclaration;
    @Override
    public boolean visit(TypeDeclaration node) {
        if (!node.isInterface()){
            if(!node.isMemberTypeDeclaration()) typeDeclaration = node;
            typeDeclarations.add(node);
        }

        return super.visit(node);
    }

    public TypeDeclaration getTypeDeclaration() {
        return typeDeclaration;
    }

    public List<TypeDeclaration> getTypeDeclarations() {
        return typeDeclarations;
    }
}
