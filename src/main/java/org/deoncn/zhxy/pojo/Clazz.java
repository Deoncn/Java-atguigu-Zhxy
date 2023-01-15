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
@TableName("sc_clazz")
public class Clazz {
    @TableId(value = "id",type = IdType.AUTO)
    private Integer id;
    private String name;
    private Integer number;
    private String introduction;
    private String headmaster;
    private String email;
    private String telephone;
    private String gradeName;
}
