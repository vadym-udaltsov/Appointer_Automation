package com.bot;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Set;

@SupportedAnnotationTypes("com.bot.LambdaFunction")
@SupportedSourceVersion(SourceVersion.RELEASE_11)
public class BotAnnotationProcessor extends AbstractProcessor {

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (Element element : roundEnv.getElementsAnnotatedWith(LambdaFunction.class)) {
            String className = element.getSimpleName().toString();
            String packageName = processingEnv.getElementUtils().getPackageOf(element).toString();
            String annotationValue = element.getAnnotation(LambdaFunction.class).value();

            String generatedClassName = className + "Generated";
            String generatedClassSource = "package " + packageName + ";\n\n" +
                    "public class " + generatedClassName + " {\n" +
                    "    public " + generatedClassName + "() {\n" +
                    "        System.out.println(\"@MyAnnotation value: " + annotationValue + "\");\n" +
                    "    }\n" +
                    "}";

            try {
                JavaFileObject sourceFile = processingEnv.getFiler().createSourceFile(
                        packageName + "." + generatedClassName, element);
                PrintWriter out = new PrintWriter(sourceFile.openWriter());
                out.print(generatedClassSource);
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }
//    @Override
//    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
//        System.out.println("------------------");
//        for (TypeElement annotation : annotations) {
//            Set<? extends Element> annotatedElements = roundEnv.getElementsAnnotatedWith(annotation);
//            for (Element element : annotatedElements) {
//                LambdaFunction ann = element.getAnnotation(LambdaFunction.class);
//                String value = ann.value();
//                System.out.println(value + " ------------------ " + value);
//                try {
//                    Files.createFile(Paths.get("testFile.txt"));
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//
//
//        }
//        return false;
//    }
}
