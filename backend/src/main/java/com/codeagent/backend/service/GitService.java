package com.codeagent.backend.service;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.transport.UsernamePasswordCredentialsProvider;
import org.springframework.stereotype.Service;
import java.io.File;

@Service
public class GitService {

    public void cloneRepository(String repoUrl, File directory, String token) throws Exception {
        Git.cloneRepository()
                .setURI(repoUrl)
                .setDirectory(directory)
                .setCredentialsProvider(new UsernamePasswordCredentialsProvider("token", token))
                .call();
    }

    public void createBranch(File directory, String branchName) throws Exception {
        try (Git git = Git.open(directory)) {
            git.branchCreate().setName(branchName).call();
            git.checkout().setName(branchName).call();
        }
    }

    public void commitChanges(File directory, String message) throws Exception {
        try (Git git = Git.open(directory)) {
            git.add().addFilepattern(".").call();
            git.commit().setMessage(message).call();
        }
    }

    public void raisePullRequest(String repoUrl, String branchName, String title, String body, String token) {
        // Here we would call the GitHub remote API (e.g. using RestTemplate or
        // WebClient)
        // POST https://api.github.com/repos/{owner}/{repo}/pulls
        System.out.println("Mock: Raised PR on " + repoUrl + " for branch " + branchName);
    }
}
