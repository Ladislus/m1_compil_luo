plugins {
    java
    antlr
    application
}

version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

sourceSets {
    main {
        java {
            exclude("ast/VisitorBase.java", "ast/VisitorDefault.java", "ast/VisitorCopy.java")
        }
    }
}

dependencies {
    testImplementation("junit", "junit", "4.12")
    antlr("org.antlr:antlr4:4.9.1")
}

tasks.generateGrammarSource {
    arguments = arguments + listOf("-visitor", "-long-messages", "-package", "parser")
    outputDirectory = file("src/main/java/parser")
}

val run: JavaExec by tasks
run.standardInput = System.`in`

application {
    mainClass.set("luo.Main")
}