package com.ck.role.po;


import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import java.util.Date;

@Entity(name = "ck_user_role_map")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class UesrRoleMapPo {

    private Long id;

    @Column
    private Long userId;

    @Column
    private Long roleId;

    @Column(name = "create_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }
}
