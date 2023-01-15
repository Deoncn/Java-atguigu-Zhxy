package org.deoncn.zhxy.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.deoncn.zhxy.pojo.Grade;
import org.deoncn.zhxy.service.GradeService;
import org.deoncn.zhxy.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "年级管理器")
@RestController
@RequestMapping("/sms/gradeController")
public class GradeController {

    @Autowired
    private GradeService gradeService;


    //GET
    //	http://localhost:9001/sms/gradeController/getGrades

    @ApiOperation("获取全部年纪")
    @GetMapping("/getGrades")
    public Result getGrades() {
        List<Grade> grades = gradeService.getGrades();
        return Result.ok(grades);
    }


    // DELETE
    //	http://localhost:9001/sms/gradeController/deleteGrade

    @ApiOperation("删除Grade信息")
    @DeleteMapping("/deleteGrade")
    public Result deleteGrade(@ApiParam("要删除的所有的Grade的id.JSON集合") @RequestBody List<Integer> ids) {

        gradeService.removeByIds(ids);

        return Result.ok();
    }


    //    http://localhost:9001/sms/gradeController/saveOrUpdateGrade

    @ApiOperation("新增或修改Grade 有ID属性则是修改 没有则是增加")
    @PostMapping("/saveOrUpdateGrade")
    public Result saveOrUpdateGrade(@ApiParam("JSON的Grade对象") @RequestBody Grade grade) {
        // 接受参数
        // 调用服务层方法完成增减或修改
        gradeService.saveOrUpdate(grade);
        return Result.ok();
    }

    // getGrades/1/3?gradeName=%?
    // {pageNo} {pageSize}

    @ApiOperation("根据年级条件模糊查询，带分页")
    @GetMapping("/getGrades/{pageNo}/{pageSize}")
    public Result getGrades(@ApiParam("分页查询页码数") @PathVariable("pageNo") Integer pageNo, @ApiParam("分页查询页大小") @PathVariable("pageSize") Integer pageSize, @ApiParam("分页查询模糊匹配的名称") String gradeName) {

        // 分页 待条件查询~
        Page<Grade> page = new Page<>(pageNo, pageSize);
        // 通过服务层进行查询
        IPage<Grade> pageRs = gradeService.getGradeByOpr(page, gradeName);


        // 封装Rusult 对象并返回
        return Result.ok(pageRs);
    }


}
