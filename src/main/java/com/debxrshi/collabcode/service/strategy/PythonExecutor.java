package com.debxrshi.collabcode.service.strategy;

import com.debxrshi.collabcode.model.Code;
import com.debxrshi.collabcode.model.ExecResult;
import com.debxrshi.collabcode.service.util.ContainerManager;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("python")
public class PythonExecutor implements CodeExecutionStrategy {

    @Override
    public ExecResult execCode(Code code) {
        String containerName = code.getLang() + UUID.randomUUID();
        String dockerCommand = String.format("echo \"%s\" > a.py && timeout -s SIGKILL 10 python3 a.py ; exit", code.getCode().replace("\"", "\\\""));
        ProcessBuilder pb = new ProcessBuilder()
                .command("docker", "run", "--rm", "--name", containerName, "--network", "none",
                        "--memory", "150m", "cc-python:dev", "sh", "-c", dockerCommand)
                .redirectErrorStream(true);
        ExecResult result = new ExecResult();
        return ContainerManager.initContainer(pb, containerName, result);
    }
}

