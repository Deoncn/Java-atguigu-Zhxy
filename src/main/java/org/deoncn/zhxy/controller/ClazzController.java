package org.deoncn.zhxy.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.deoncn.zhxy.pojo.Clazz;
import org.deoncn.zhxy.service.ClazzService;
import org.deoncn.zhxy.util.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api(tags = "班级管理器")
@RestController
@RequestMapping("/sms/clazzController")
public class ClazzController {


    @Autowired
    private ClazzService clazzService;


    //GET
    //	http://localhost:9001/sms/clazzController/getClazzs
    @ApiOperation("查询所有班级信息")
    @GetMapping("/getClazzs")
    public Result getClazzs(){
        List<Clazz> clazzes= clazzService.getClazzs();

        return Result.ok(clazzes);
    }


    // GET
    //	http://localhost:9001/sms/clazzController/getClazzsByOpr/1/3

    @ApiOperation("分页待条件查询班级信息")
    @GetMapping("/getClazzsByOpr/{pageNo}/{pageSize}")
    public Result getClazzsByOpr(@ApiParam("分页查询页码数") @PathVariable("pageNo") Integer pageNo,@ApiParam("分页查询页面大小") @PathVariable("pageSize") Integer pageSize,@ApiParam("分页查询的查询条件") Clazz clazz){
        Page<Clazz> page = new Page<>(pageNo, pageSize);

        IPage<Clazz> iPage = clazzService.getClassByOpr(page,clazz);

        return Result.ok(iPage);
    }


    //POST
    //	http://localhost:9001/sms/clazzController/saveOrUpdateClazz


    @ApiOperation("新增或修改班级信息")
    @PostMapping("/saveOrUpdateClazz")
    public Result saveOrUpdateClazz(@ApiParam("JSON的Clazz对象") @RequestBody Clazz clazz){

        clazzService.saveOrUpdate(clazz);
        return Result.ok();
    }

    //DELETE
    //	http://localhost:9001/sms/clazzController/deleteClazz

    @ApiOperation("删除班级clazz信息")
    @DeleteMapping("/deleteClazz")
    public Result deleteClazz(@ApiParam("要删除的所有的clazz的id.JSON集合") @RequestBody List<Integer> ids){

        clazzService.removeByIds(ids);

        return Result.ok();
    }
}
