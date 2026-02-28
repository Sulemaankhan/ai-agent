package com.codeagent.backend.service;

import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

@Service
public class AutomationWorkflowService {

    private static final Logger log = LoggerFactory.getLogger(AutomationWorkflowService.class);

    private final GitService gitService;
    private final PomAnalyzerService pomAnalyzerService;
    private final LlmService llmService;
    private final MavenInvokerService mavenInvokerService;

    public AutomationWorkflowService(GitService gitService, PomAnalyzerService pomAnalyzerService,
            LlmService llmService, MavenInvokerService mavenInvokerService) {
        this.gitService = gitService;
        this.pomAnalyzerService = pomAnalyzerService;
        this.llmService = llmService;
        this.mavenInvokerService = mavenInvokerService;
    }

    public void executeAgentRun(String localPath) {
        new Thread(() -> {
            try {
                log.info("Starting automation for local path: {}", localPath);

                File repoDir = new File(localPath);
                if (!repoDir.exists() || !repoDir.isDirectory()) {
                    log.error("Invalid local directory: {}", localPath);
                    return;
                }

                // 1. Read POM and detect outdated
                File pomFile = new File(repoDir, "pom.xml");
                if (!pomFile.exists()) {
                    log.error("pom.xml not found in {}", localPath);
                    return;
                }

                String outdatedDeps = pomAnalyzerService.findOutdatedDependencies(pomFile);
                if (outdatedDeps.isEmpty()) {
                    log.info("No outdated dependencies found.");
                    return;
                }

                // 2. Create branch
                String branchName = "agent-update-deps-" + System.currentTimeMillis();
                gitService.createBranch(repoDir, branchName);

                // 3. Find safe version and update POM
                String safeUpdatePlan = llmService.findSafeVersionAndUpdate(outdatedDeps);
                pomAnalyzerService.applyUpdate(pomFile, safeUpdatePlan);

                // 4. Compile check
                String buildOutput = mavenInvokerService.compile(repoDir);
                if (buildOutput.contains("BUILD FAILURE")) {
                    // 5. Fix compilation error
                    log.info("Build failed, asking LLM to fix compilation errors...");
                    String fixPlan = llmService.fixCompilationError(buildOutput);
                    applySuggestedFixes(repoDir, fixPlan);

                    // Re-compile
                    String retryBuild = mavenInvokerService.compile(repoDir);
                    if (retryBuild.contains("BUILD FAILURE")) {
                        log.error("Failed to fix compile errors.");
                        return; // Stop if failed twice
                    }
                }

                // 6. Commit locally
                gitService.commitChanges(repoDir, "Update outdated dependencies and fix breaking changes");
                log.info("Completed agent workflow successfully! Changes committed to branch: {}", branchName);

            } catch (Exception e) {
                log.error("Agent workflow failed: {}", e.getMessage(), e);
            }
        }).start();
    }

    private void applySuggestedFixes(File repoDir, String fixPlan) {
        log.info("Applying fixes: {}", fixPlan);
        // Add file editing logic here as per LLM response
    }
}
