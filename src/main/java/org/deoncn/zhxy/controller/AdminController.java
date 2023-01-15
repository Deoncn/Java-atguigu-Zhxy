package org.deoncn.zhxy.controller;


import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.deoncn.zhxy.pojo.Admin;
import org.deoncn.zhxy.service.AdminService;
import org.deoncn.zhxy.util.MD5;
import org.deoncn.zhxy.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "管理员接口")
@RestController
@RequestMapping("/sms/adminController")
public class AdminController {

    @Autowired
    AdminService adminService;

    //GET
    //	http://localhost:9001/sms/adminController/getAllAdmin/1/3

    @ApiOperation("分页带条件查询管理员信息")
    @GetMapping("/getAllAdmin/{pageNo}/{pageSize}")
    public Result getAllAdmin(@ApiParam("页码数") @PathVariable("pageNo") Integer pageNo, @ApiParam("页大小") @PathVariable("pageSize") Integer pageSize, @ApiParam("管理员名字") String adminName) {

        // 分页 待条件查询
        Page<Admin> pageParam = new Page<Admin>(pageNo, pageSize);
        // 通过服务层进行查询
        IPage<Admin> iPages = adminService.getGradeByOpr(pageParam, adminName);


        // 封装Rusult 对象并返回
        return Result.ok(iPages);
    }


    // POST
    //	http://localhost:9001/sms/adminController/saveOrUpdateAdmin

    @ApiOperation("修改新增管理员信息")
    @PostMapping("/saveOrUpdateAdmin")
    public Result saveOrUpdateAdmin(@ApiParam("JSON的Admin对象") @RequestBody Admin admin) {

        Integer id = admin.getId();
        if (id == null || 0 == id) {
            admin.setPassword(MD5.encrypt(admin.getPassword()));
        }

        adminService.saveOrUpdate(admin);
        return Result.ok();
    }

    //DELETE
    //	http://localhost:9001/sms/adminController/deleteAdmin

    @ApiOperation("删")
    @DeleteMapping("/deleteAdmin")
    public Result deleteAdmin(@ApiParam("JSON的要删除的Admin对象") @RequestBody List<Integer> ids) {
        adminService.removeByIds(ids);
        return Result.ok();
    }

}
