package visitors;

import org.eclipse.jdt.core.dom.ASTVisitor;
import org.eclipse.jdt.core.dom.TypeDeclaration;

import java.util.ArrayList;
import java.util.List;

public class TypeDeclarationVisitor extends ASTVisitor {
    List<TypeDeclaration> typeDeclarations = new ArrayList<>();

    @Override
    public boolean visit(TypeDeclaration node) {
        if (!node.isInterface()){
            typeDeclarations.add(node);
        }

        return super.visit(node);
    }

    public List<TypeDeclaration> getTypeDeclarations() {
        return typeDeclarations;
    }
}
