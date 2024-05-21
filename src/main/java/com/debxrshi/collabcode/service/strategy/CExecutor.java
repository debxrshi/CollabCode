package com.debxrshi.collabcode.service.strategy;

import java.util.UUID;


import com.debxrshi.collabcode.model.Code;
import com.debxrshi.collabcode.model.ExecResult;
import com.debxrshi.collabcode.service.util.ProcessOutputReader;
import org.springframework.stereotype.Component;

@Component("c")
public class CExecutor implements CodeExecutionStrategy {

    @Override
    public ExecResult execCode(Code code){
        try {
            String containerName = code.getLang()+UUID.randomUUID();
            String dockerCommand = String.format("echo \"%s\" > main.c && gcc main.c -o main && timeout -s SIGKILL 10 ./main ; exit", code.getCode().replace("\"", "\\\""));
            ProcessBuilder pb = new ProcessBuilder()
                    .command("docker", "run", "--rm", "--name" , containerName,"--network", "none",
                            "--memory", "150m", "cc-gxx:dev", "sh", "-c", dockerCommand)
                    .redirectErrorStream(true);
            ExecResult result = new ExecResult();
            Process p = pb.start();
            long startTime = System.currentTimeMillis();
            String output = ProcessOutputReader.readOut(p, containerName);
            long endTime = System.currentTimeMillis();
            float time = (float) (endTime - startTime - 1) / 1000;
            if (output.contains("Killed")) {
                result.setOut("Your code took too long to execute!");
                result.setTte(time);
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
    
