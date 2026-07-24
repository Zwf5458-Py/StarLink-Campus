package com.starlink.campus.module.kindergarten.controller;

import com.starlink.campus.common.R;
import org.flowable.engine.RuntimeService;
import org.flowable.engine.TaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cn.dev33.satoken.annotation.SaCheckLogin;
import jakarta.validation.Valid;

import java.time.format.DateTimeFormatter;
import java.util.*;

@SaCheckLogin
@RestController
@RequestMapping("/kindergarten/flowable")
@CrossOrigin
public class FlowableProcessController {

    @Autowired
    private RuntimeService runtimeService;

    @Autowired
    private TaskService taskService;

    @PostMapping("/process/start")
    public R<Map<String, Object>> startProcess(@Valid @RequestBody Map<String, Object> variables) {
        String processDefinitionKey = (String) variables.getOrDefault("processDefinitionKey", "kindergarten_oa_leave");
        
        // Start process instance
        ProcessInstance processInstance = runtimeService.startProcessInstanceByKey(processDefinitionKey, variables);
        
        Map<String, Object> res = new HashMap<>();
        res.put("processInstanceId", processInstance.getId());
        res.put("processDefinitionKey", processInstance.getProcessDefinitionKey());
        res.put("startTime", processInstance.getStartTime() != null ? 
            processInstance.getStartTime().toInstant().toString() : "");
        res.put("status", "RUNNING");
        
        // Find current task
        List<Task> tasks = taskService.createTaskQuery().processInstanceId(processInstance.getId()).list();
        if (!tasks.isEmpty()) {
            res.put("currentTask", tasks.get(0).getName());
        } else {
            res.put("currentTask", "已结束");
        }
        return R.ok(res);
    }

    @GetMapping("/tasks/pending")
    public R<List<Map<String, Object>>> getPendingTasks() {
        List<Task> taskList = taskService.createTaskQuery().list();
        List<Map<String, Object>> tasks = new ArrayList<>();
        
        for (Task task : taskList) {
            Map<String, Object> t = new HashMap<>();
            t.put("taskId", task.getId());
            t.put("taskName", task.getName());
            t.put("assignee", task.getAssignee());
            t.put("processInstanceId", task.getProcessInstanceId());
            t.put("createTime", task.getCreateTime() != null ? 
                task.getCreateTime().toInstant().toString() : "");
            tasks.add(t);
        }

        return R.ok(tasks);
    }

    @PostMapping("/task/complete/{taskId}")
    public R<Boolean> completeTask(@PathVariable String taskId, @Valid @RequestBody(required = false) Map<String, Object> params) {
        if (params == null) {
            params = new HashMap<>();
        }
        taskService.complete(taskId, params);
        return R.ok(true);
    }
}
