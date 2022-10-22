package methods;

import org.eclipse.jdt.core.JavaCore;
import org.eclipse.jdt.core.dom.AST;
import org.eclipse.jdt.core.dom.ASTParser;
import org.eclipse.jdt.core.dom.CompilationUnit;

import java.util.Map;

public class ASTCreator {
    private final String projectSourcePath;
    private final String jrePath;

    public ASTCreator(String projectSourcePath, String jrePath) {
        this.projectSourcePath = projectSourcePath;
        this.jrePath = jrePath;
    }

    // create AST
    public CompilationUnit parse(char[] classSource) {
        ASTParser parser = ASTParser.newParser(AST.JLS4);
        parser.setResolveBindings(true);
        parser.setKind(ASTParser.K_COMPILATION_UNIT);

        parser.setBindingsRecovery(true);

        Map options = JavaCore.getOptions();
        parser.setCompilerOptions(options);

        parser.setUnitName("");

        String[] sources = { projectSourcePath };
        String[] classpath = {jrePath};

        parser.setEnvironment(classpath, sources, new String[] { "UTF-8"}, true);
        parser.setSource(classSource);

        return (CompilationUnit) parser.createAST(null); // create and parse
    }
}
