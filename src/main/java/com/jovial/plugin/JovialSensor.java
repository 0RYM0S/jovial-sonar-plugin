package com.jovial.plugin;

import com.jovial.AST.ASTModel;
import com.jovial.plugin.lsp.LSPRunner;
import com.jovial.rules.base.RuleRegistry;
import org.sonar.api.batch.fs.FileSystem;
import org.sonar.api.batch.fs.InputFile;
import org.sonar.api.batch.sensor.Sensor;
import org.sonar.api.batch.sensor.SensorContext;
import org.sonar.api.batch.sensor.SensorDescriptor;
import org.sonar.api.batch.fs.FileSystem;


public class JovialSensor implements Sensor {

    private final FileSystem fs;
    private final RuleRegistry registry = new RuleRegistry();

    public JovialSensor(FileSystem fs) {
        this.fs = fs;
    }

    @Override
    public void describe(SensorDescriptor descriptor) {
        descriptor.name("Jovial Sensor").onlyOnLanguage(JovialLanguage.KEY);
    }

    @Override
    public void execute(SensorContext context) {
        // ASTModel ast = LSPRunner.parseAST(file);
        // if (ast != null) {
        //      NoGotoRule.apply(ast, file, context);
        // // Add more rule calls here...
        // }
        fs.inputFiles(fs.predicates().hasLanguage(JovialLanguage.KEY)).forEach(file -> {
            if (!file.filename().endsWith(".jov")) return;

            ASTModel ast = LSPRunner.parseAST(file.file());
            registry.execute(ast, file, context);
        });
    }
}