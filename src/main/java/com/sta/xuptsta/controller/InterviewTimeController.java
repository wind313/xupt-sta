package com.sta.xuptsta.controller;

import com.sta.xuptsta.result.Result;
import com.sta.xuptsta.service.InterviewTimeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.validation.constraints.NotNull;

@Tag(name = "面试时间")
@RestController
@RequestMapping("/interviewTime")
public class InterviewTimeController {

    @Autowired
    private InterviewTimeService interviewTimeService;

    @Operation(summary = "获取面试时间", description = "获取面试时间,type：1一面，2二面")
    @GetMapping("/get/{type}")
    public Result get(@NotNull(message = "面试类型不能为空") @PathVariable Integer type) {

        return Result.ok(interviewTimeService.get(type));
    }
}
