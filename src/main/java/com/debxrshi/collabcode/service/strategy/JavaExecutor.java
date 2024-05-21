package com.debxrshi.collabcode.service.strategy;

import com.debxrshi.collabcode.model.Code;
import com.debxrshi.collabcode.model.ExecResult;
import com.debxrshi.collabcode.service.util.ProcessOutputReader;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component("java")
public class JavaExecutor implements CodeExecutionStrategy {

    @Override
    public ExecResult execCode(Code code) {
        try {
            String containerName = code.getLang() + UUID.randomUUID();
            String dockerCommand = String.format("echo \"%s\" > Main.java && javac Main.java && timeout -s SIGKILL 10 java Main ; exit", code.getCode().replace("\"", "\\\""));
            ProcessBuilder pb = new ProcessBuilder()
                    .command("docker", "run", "--rm", "--network", "none",
                            "--memory", "150m", "cc-java:dev", "sh", "-c", dockerCommand)
                    .redirectErrorStream(true);
            ExecResult result = new ExecResult();
            Process p = pb.start();
            long startTime = System.currentTimeMillis();
            String output = ProcessOutputReader.readOut(p, containerName);
            long endTime = System.currentTimeMillis();
            float time = (float) (endTime - startTime - 1) / 1000;
            if (output.contains("Killed")) {
                result.setOut("Your code took too long to execute or took up too many resources!");
                result.setTte(10F);
            } else {
                result.setOut(output.trim());
                result.setTte(time);
            }
            return result;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }
}

