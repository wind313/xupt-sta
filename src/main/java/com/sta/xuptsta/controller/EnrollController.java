package com.sta.xuptsta.controller;

import com.sta.xuptsta.pojo.dto.EnrollDTO;
import com.sta.xuptsta.result.Result;
import com.sta.xuptsta.service.EnrollService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

@RestController
@Tag(name = "报名")
@RequestMapping("/enroll")
public class EnrollController {

    @Autowired
    private EnrollService enrollService;

    @PostMapping("/add")
    @Operation(summary = "报名", description = "填写报名信息")
    public Result add(@Valid @RequestBody EnrollDTO enrollDto) {
        enrollService.add(enrollDto);
        return Result.ok();
    }

    @PutMapping("/update")
    @Operation(summary = "修改报名信息", description = "修改报名信息")
    public Result modify(@Valid @RequestBody EnrollDTO enrollDto) {
        enrollService.modify(enrollDto);
        return Result.ok();
    }

    @GetMapping("/get")
    @Operation(summary = "获取报名信息", description = "获取报名信息")
    public Result get() {
        return Result.ok(enrollService.get());
    }

    @PutMapping("/selectSecond/{timeId}")
    @Operation(summary = "选择二面时间", description = "选择二面时间")
    public Result selectSecond(@NotNull(message = "时间id不能为空") @PathVariable Long timeId) {
        enrollService.selectSecond(timeId);
        return Result.ok();
    }

}
