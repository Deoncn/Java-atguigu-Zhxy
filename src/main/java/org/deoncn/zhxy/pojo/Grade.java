package org.deoncn.zhxy.pojo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@TableName("sc_grade")
public class Grade {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    @TableId(value = "name",type = IdType.AUTO)
    private String name;
    private String manager;
    private String email;
    private String telephone;
    private String introduction;
}
