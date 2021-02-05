package com.ck.cosmic.po;

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

@Entity(name = "ck_cosmic_check_rule")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CosmicCheckRulePo {

    private Long id;

    @Column(name="sort_num")
    private Integer sortNum;

    @Column(length = 128)
    private String element;

    @Column(length = 1024)
    private String rule;

    @Column(name = "create_date")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createDate;

    @Column(name = "create_user")
    private Long createUser;

    @Id
    @GeneratedValue
    public Long getId() {
        return id;
    }

}
