package com.debxrshi.collabcode.service.strategy;

import com.debxrshi.collabcode.model.Code;
import com.debxrshi.collabcode.model.ExecResult;
import com.debxrshi.collabcode.service.util.ContainerManager;
import org.springframework.stereotype.Component;

import java.util.UUID;


@Component("javascript")
public class JavaScriptExecutor implements CodeExecutionStrategy {

    @Override
    public ExecResult execCode(Code code) {

        String containerName = code.getLang() + UUID.randomUUID();
        String dockerCommand = String.format("echo \"%s\" > main.js && timeout -s SIGKILL 10 node main.js ; exit", code.getCode().replace("\"", "\\\""));
        ProcessBuilder pb = new ProcessBuilder()
                .command("docker", "run", "--rm", "--name", containerName, "--network", "none", "--memory", "150m", "cc-node:dev", "sh", "-c", dockerCommand)
                .redirectErrorStream(true);
        ExecResult result = new ExecResult();
        return ContainerManager.initContainer(pb, containerName, result);
    }
}
