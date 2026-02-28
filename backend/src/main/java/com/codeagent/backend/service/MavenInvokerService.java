package com.codeagent.backend.service;

import org.apache.maven.shared.invoker.DefaultInvocationRequest;
import org.apache.maven.shared.invoker.DefaultInvoker;
import org.apache.maven.shared.invoker.InvocationRequest;
import org.apache.maven.shared.invoker.InvocationResult;
import org.apache.maven.shared.invoker.Invoker;
import org.springframework.stereotype.Service;
import java.io.File;
import java.util.Collections;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

@Service
public class MavenInvokerService {

    public String compile(File baseDir) {
        InvocationRequest request = new DefaultInvocationRequest();
        request.setPomFile(new File(baseDir, "pom.xml"));
        request.setGoals(Collections.singletonList("compile"));

        Invoker invoker = new DefaultInvoker();
        
        // Ensure maven is set up, in production you'd need System.setProperty("maven.home", "...");
        // Or expect it in the environment path
        
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        request.setOutputHandler(line -> {
            try {
                outputStream.write((line + "\n").getBytes());
            } catch (Exception ignored) {}
        });

        request.setErrorHandler(line -> {
            try {
                outputStream.write((line + "\n").getBytes());
            } catch (Exception ignored) {}
        });

        try {
            InvocationResult result = invoker.execute(request);
            if (result.getExitCode() != 0) {
                return "BUILD FAILURE\n" + outputStream.toString();
            }
            return "BUILD SUCCESS\n" + outputStream.toString();
        } catch (Exception e) {
            return "BUILD FAILURE\n" + e.getMessage();
        }
    }
}
