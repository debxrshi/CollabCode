package com.debxrshi.collabcode.controller;

import com.debxrshi.collabcode.model.Code;
import com.debxrshi.collabcode.model.ExecResult;
import com.debxrshi.collabcode.service.Impl.CodeExecutorImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@CrossOrigin
@RestController
@RequestMapping("/api")
public class ExecutionController {

    @Autowired
    private CodeExecutorImpl codeExecutor ;

    @PostMapping(value = "/exec", produces = "application/json", consumes = "application/json")
    public ExecResult executeCode(@RequestBody Code code) {
        return codeExecutor.codeExecutor(code);
    }
}