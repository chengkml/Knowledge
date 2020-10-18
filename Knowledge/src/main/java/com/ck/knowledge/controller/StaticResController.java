package com.ck.knowledge.controller;

import com.ck.knowledge.aop.Post;
import com.ck.knowledge.dao.res.StaticResRepository;
import com.ck.knowledge.helper.StringHelper;
import com.ck.knowledge.po.res.StaticResPo;
import com.ck.knowledge.service.StaticResService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.net.URLEncoder;

@Api("静态资源接口")
@RestController
@RequestMapping({"/res"})
public class StaticResController {

    @Autowired
    private StaticResService resServ;

    @Autowired
    private StaticResRepository resRepo;

    @GetMapping("download")
    @ApiOperation("下载资源")
    public void download(@RequestBody Long fileId, HttpServletResponse response) {
        StaticResPo res = resRepo.getOne(fileId);
        String path = res.getPath();
        if (StringUtils.isBlank(path)) {
            // TODO
        }
        File file = new File(path);
        if (!file.exists()) {
            throw new RuntimeException(StringHelper.format("文件“{}”不存在！", res.getName()));
        }
        try (FileInputStream is = new FileInputStream(file);
             OutputStream outputStream = response.getOutputStream()) {
            response.setHeader("Content-Disposition", "attachment;Filename=" + URLEncoder.encode(res.getName(), "UTF-8"));
            byte[] bytes = new byte[2048];
            int len = 0;
            while ((len = is.read(bytes)) > 0) {
                outputStream.write(bytes, 0, len);
            }
        } catch (Exception e) {
            throw new RuntimeException(StringHelper.format("文件“{}”下载失败！", res.getName()));
        }
    }

    @ApiOperation("上传资源")
    @Post(value = "upload", produces = "application/json")
    public Object upload(@RequestParam(value = "file") MultipartFile[] files) {
        return resServ.batchAddRes(files);
    }

    @ApiOperation("删除资源")
    @Post("delete")
    public Object delete(@RequestBody Long fileId) {
        return resServ.deleteFile(fileId);
    }

}
