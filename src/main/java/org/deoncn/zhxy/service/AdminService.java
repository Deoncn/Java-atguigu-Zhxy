package org.deoncn.zhxy.service;



import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import org.deoncn.zhxy.pojo.Admin;
import org.deoncn.zhxy.pojo.LoginForm;


public interface AdminService extends IService<Admin> {

    Admin login(LoginForm loginForm);

    Admin getAdminById(Long userId);

    IPage<Admin> getGradeByOpr(Page<Admin> pageParam, String adminName);
}
