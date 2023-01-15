package org.deoncn.zhxy.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.deoncn.zhxy.pojo.Student;
import org.deoncn.zhxy.service.StudentService;
import org.deoncn.zhxy.util.MD5;
import org.deoncn.zhxy.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "学生管理器")
@RestController
@RequestMapping("/sms/studentController")
public class StudentController {


    @Autowired
    private StudentService studentService;

    // http://localhost:9001/sms/studentController/delStudentById
    @ApiOperation("删除、批量删除")
    @DeleteMapping("/delStudentById")
    public Result delStudentById(@ApiParam("要删除的学生编号的对象JSON数组") @RequestBody List<Integer> ids) {

        studentService.removeByIds(ids);
        return Result.ok();
    }


    // POST
    //	http://localhost:9001/sms/studentController/addOrUpdateStudent
    @ApiOperation("添加、修改")
    @PostMapping("/addOrUpdateStudent")
    public Result addOrUpdateStudent(@ApiParam("JSON的Student对象信息") @RequestBody Student student) {
        Integer id = student.getId();
        if (null == id || 0 == id) {
            student.setPassword(MD5.encrypt(student.getPassword()));
        }

        studentService.saveOrUpdate(student);

        return Result.ok();
    }


    // GET
    //	http://localhost:9001/sms/studentController/getStudentByOpr/1/3

    @ApiOperation("根据学生条件模糊查询，带分页")
    @GetMapping("/getStudentByOpr/{pageNo}/{pageSize}")
    public Result getStudentByOpr(@ApiParam("页码数") @PathVariable Integer pageNo, @ApiParam("页大小") @PathVariable Integer pageSize, @ApiParam("查询的条件") Student student) {
        // 分页信息封装page 对象
        Page<Student> pageParam = new Page<>(pageNo, pageSize);
        // 通过服务层进行查询
        IPage<Student> studentPage = studentService.getStudentByOpr(pageParam, student);
        // 封装result 返回
        return Result.ok(studentPage);
    }

}
