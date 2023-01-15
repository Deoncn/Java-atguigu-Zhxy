package org.deoncn.zhxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sc_student")
public class Student {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String sno;
    private String name;
    private char gender;

    private String password;
    private String email;
    private String telephone;
    private String address;

    private String introduction;
    private String portraitPath;
    private String clazzName;
}
