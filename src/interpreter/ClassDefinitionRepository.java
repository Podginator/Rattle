package interpreter;

import interpreter.ClassDefinition.ClassInstance;

public interface ClassDefinitionRepository {
    /**
     * Add a function to the current level.
     */
    void addClassDefintion(ClassDefinition definition);

    boolean classDefinitionExists(ClassDefinition def);

    ClassDefinition getClassDefinition(String def);

    ClassInstance createClassInstance(String def);
}
