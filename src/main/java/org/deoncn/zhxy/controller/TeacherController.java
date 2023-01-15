package org.deoncn.zhxy.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.deoncn.zhxy.pojo.Teacher;
import org.deoncn.zhxy.service.TeacherService;
import org.deoncn.zhxy.util.MD5;
import org.deoncn.zhxy.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "教师管理器")
@RestController
@RequestMapping("/sms/teacherController")
public class TeacherController {

    @Autowired
    TeacherService teacherService;

    // GET
    // sms/teacherController/getTeachers/1/3
    // sms/teacherController/getTeachers/1/3?name=111&clazzName=三年二班
    @ApiOperation("获取教师信息带条件")
    @GetMapping("/getTeachers/{pageNo}/{pageSize}")
    public Result getTeachers(@ApiParam("页码数") @PathVariable Integer pageNo, @ApiParam("页大小") @PathVariable Integer pageSize, @ApiParam("查询的条件") Teacher teacher) {
        Page<Teacher> pageParam = new Page<>(pageNo, pageSize);
        IPage<Teacher> page = teacherService.getTeacherByOpr(pageParam, teacher);
        return Result.ok(page);
    }


    //POST
    //	http://localhost:9001/sms/teacherController/saveOrUpdateTeacher
    @ApiOperation("添加或修改教师信息")
    @PostMapping("/saveOrUpdateTeacher")
    public Result saveOrUpdateTeacher(@ApiParam("要保存或修改的JSON的Teacher对象") @RequestBody Teacher teacher) {
        // 接受参数
        // 调用服务层方法完成增减或修改
        if (teacher.getId() == null || teacher.getId() == 0) {
            teacher.setPassword(MD5.encrypt(teacher.getPassword()));
        }

        teacherService.saveOrUpdate(teacher);
        return Result.ok();
    }


    @ApiOperation("删除")
    @DeleteMapping("/deleteTeacher")
    public Result deleteTeacher(@ApiParam("要删除的所有的Teacher的id.JSON集合") @RequestBody List<Integer> ids) {

        teacherService.removeByIds(ids);
        return Result.ok();
    }


}
