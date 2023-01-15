package org.deoncn.zhxy.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.SneakyThrows;
import org.deoncn.zhxy.pojo.Admin;
import org.deoncn.zhxy.pojo.LoginForm;
import org.deoncn.zhxy.pojo.Student;
import org.deoncn.zhxy.pojo.Teacher;
import org.deoncn.zhxy.service.AdminService;
import org.deoncn.zhxy.service.StudentService;
import org.deoncn.zhxy.service.TeacherService;
import org.deoncn.zhxy.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

/**
 * ClassName:SystemController
 * Package: IntelliJ IDEA
 * Description:
 *
 * @Author: Deoncn
 * @Create: 2022/12/31 - 18:39
 * @Version: v1.0
 */

@Api(tags = "系统控制器")
@RestController
@RequestMapping("/sms/system")
public class SystemController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private StudentService studentService;

    @Autowired
    private TeacherService teacherService;


    @ApiOperation("更新用户密码的处理")
    @PostMapping("/updatePwd/{oldPwd}/{newPwd}")
    public Result updatePwd(@ApiParam("token旧密码") @RequestHeader("token") String token,@ApiParam("旧密码") @PathVariable("oldPwd") String oldPwd,@ApiParam("新密码") @PathVariable("newPwd") String newPwd) {
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            //token过期
            return Result.fail().message("token失效，请重新登录后修改密码");
        }
        // 获取用户ID、用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);


        // oldPwd 明文转换
        oldPwd = MD5.encrypt(oldPwd);
        newPwd = MD5.encrypt(newPwd);

        switch (userType) {
            case 1:
                QueryWrapper<Admin> queryWrapper1 = new QueryWrapper<>();
                queryWrapper1.eq("id", userId.intValue());
                queryWrapper1.eq("password", oldPwd);
                Admin admin = adminService.getOne(queryWrapper1);
                if (admin != null) {
                    // 修改
                    admin.setPassword(newPwd);
                    adminService.saveOrUpdate(admin);
                } else {
                    return Result.fail().message("原密码有问题");
                }
                break;

            case 2:
                QueryWrapper<Student> queryWrapper2 = new QueryWrapper<>();
                queryWrapper2.eq("id", userId.intValue());
                queryWrapper2.eq("password", oldPwd);
                Student student = studentService.getOne(queryWrapper2);
                if (student != null) {
                    // 修改
                    student.setPassword(newPwd);
                    studentService.saveOrUpdate(student);
                } else {
                    return Result.fail().message("原密码有问题");
                }
                break;
            case 3:
                QueryWrapper<Teacher> queryWrapper3 = new QueryWrapper<>();
                queryWrapper3.eq("id", userId.intValue());
                queryWrapper3.eq("password", oldPwd);
                Teacher teacher = teacherService.getOne(queryWrapper3);
                if (teacher != null) {
                    // 修改
                    teacher.setPassword(newPwd);
                    teacherService.saveOrUpdate(teacher);
                } else {
                    return Result.fail().message("原密码有问题");
                }
                break;


        }

        return Result.ok();
    }

    //POST
    //	http://localhost:9001/sms/system/headerImgUpload
    @SneakyThrows
    @ApiOperation("文件上传统一入口")
    @PostMapping("/headerImgUpload")
    public Result headerImgUpload(@ApiParam("要上传的文件") @RequestPart("multipartFile") MultipartFile multipartFile, HttpServletRequest request) {

        String uuid = UUID.randomUUID().toString().replace("-", "").toLowerCase();
        String originalFilename = multipartFile.getOriginalFilename();
        int i = originalFilename.lastIndexOf(".");
        String newFilename = uuid.concat(originalFilename.substring(i));

        // 保存文件 将文件发送到第三方图片服务器上。
        // request.getServletContext().getRealPath("public/upload/");


        /**
         * path1
         * */
        File path1 = new File(ResourceUtils.getURL("classpath:").getPath());
        if(!path1.exists()) {
            path1 = new File("");
        }
        System.out.println(path1);

        File upload = new File(path1.getAbsolutePath(),"static/upload/");
        if(!upload.exists()) {
            upload.mkdirs();
        }
        String parent= upload.getPath();
        System.out.println(parent);

        String resource = parent + '/';

        String resource2 = resource.concat(newFilename);
        System.out.println(resource2);

        try {
            multipartFile.transferTo(new File(resource2));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }



        // 响应的图片路径
        String path = "/upload/".concat(newFilename);
        return Result.ok(path);
    }

    @ApiOperation("通过token口令获取当前登录的用户信息的方法")
    @GetMapping("/getInfo")
    public Result getInfoByToken(@RequestHeader("token") String token) {
        boolean expiration = JwtHelper.isExpiration(token);
        if (expiration) {
            return Result.build(null, ResultCodeEnum.TOKEN_ERROR);
        }
        // 从 token 中解析用户的ID和用户类型
        Long userId = JwtHelper.getUserId(token);
        Integer userType = JwtHelper.getUserType(token);

        Map<String, Object> map = new LinkedHashMap<>();
        switch (userType) {
            case 1:
                Admin admin = adminService.getAdminById(userId);
                map.put("userType", 1);
                map.put("user", admin);
                break;

            case 2:
                Student student = studentService.getStudentById(userId);
                map.put("userType", 2);
                map.put("user", student);
                break;

            case 3:
                Teacher teacher = teacherService.getTeacherById(userId);
                map.put("userType", 3);
                map.put("user", teacher);
                break;
        }

        return Result.ok(map);
    }

    @ApiOperation("登录方法")
    @PostMapping("/login")
    public Result login(@RequestBody LoginForm loginForm, HttpServletRequest request) {
        // 验证码校验
        HttpSession session = request.getSession();
        String sessionVerifiCode = (String) session.getAttribute("verifiCode");
        String loginVerifiCode = loginForm.getVerifiCode();
        if ("".equals(sessionVerifiCode) || null == sessionVerifiCode) {
            return Result.fail().message("验证码失效，请刷新重试");
        }
        if (!sessionVerifiCode.equalsIgnoreCase(loginVerifiCode)) {
            return Result.fail().message("验证码错误，请小心输入后重试");
        }
        // 从session 域中移除现有验证码
        session.removeAttribute("verifiCode");
        // 分用户类型进行

        // 准备一个map 用于存放响应的数据
        Map<String, Object> map = new LinkedHashMap<>();
        switch (loginForm.getUserType()) {
            case 1:
                try {
                    Admin admin = adminService.login(loginForm);
                    if (null != admin) {
                        // 用户的类型和用户的id 转换成一个密文，以token 的名称向客户端反馈
                        map.put("token", JwtHelper.createToken(admin.getId().longValue(), 1));
                    } else {
                        throw new RuntimeException("用户名密码有问题");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }

            case 2:
                try {
                    Student student = studentService.login(loginForm);
                    if (null != student) {
                        // 用户的类型和用户的id 转换成一个密文，以token 的名称向客户端反馈
                        map.put("token", JwtHelper.createToken(student.getId().longValue(), 2));
                    } else {
                        throw new RuntimeException("用户名密码有问题");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }

            case 3:
                try {
                    Teacher teacher = teacherService.login(loginForm);
                    if (null != teacher) {
                        // 用户的类型和用户的id 转换成一个密文，以token 的名称向客户端反馈
                        map.put("token", JwtHelper.createToken(teacher.getId().longValue(), 3));
                    } else {
                        throw new RuntimeException("用户名密码有问题");
                    }
                    return Result.ok(map);
                } catch (RuntimeException e) {
                    e.printStackTrace();
                    return Result.fail().message(e.getMessage());
                }
        }
        return Result.fail().message("查无此用户");
    }

    @ApiOperation("获取验证码图片 二维码？")
    @GetMapping("/getVerifiCodeImage")
    public void getVerifiCodeImage(HttpServletRequest request, HttpServletResponse response) {
        //获取验证码图片
        BufferedImage verifiCodeImage =
                CreateVerifiCodeImage.getVerifiCodeImage();
        //获取验证码字符串
        String verifiCode =
                String.valueOf(CreateVerifiCodeImage.getVerifiCode());
        /*将验证码放入当前请求域*/
        request.getSession().setAttribute("verifiCode", verifiCode);
        try {
            //将验证码图片通过输出流做出响应
            ImageIO.write(verifiCodeImage, "JPEG", response.getOutputStream());
        } catch (IOException e) {
             e.printStackTrace();
            // 除了上述的异常处理方法也可以使用👇
            // throw new RuntimeException(e);

        }
    }


}
