package com.ck.res.controller;

import com.ck.common.aop.Get;
import com.ck.common.aop.Post;
import com.ck.common.helper.StringHelper;
import com.ck.common.properties.CommonProperties;
import com.ck.res.dao.StaticResRepository;
import com.ck.res.po.StaticResPo;
import com.ck.res.service.StaticResService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@Api("静态资源接口")
@RestController
@RequestMapping({"/res"})
public class StaticResController {

    private static Logger LOG = LoggerFactory.getLogger(StaticResController.class);

    @Autowired
    private CommonProperties commonProperties;

    @Autowired
    private StaticResService resServ;

    @Autowired
    private StaticResRepository resRepo;

    @GetMapping("download")
    @ApiOperation("下载资源")
    public void download(@RequestBody Long fileId, HttpServletResponse response) {
        StaticResPo res = resRepo.getOne(fileId);
        Objects.requireNonNull(res, "未查询到文件记录！");
        try (InputStream is = resServ.getInputStream(res);
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
    public Object upload(@RequestParam(value = "file") MultipartFile[] files) throws IOException, URISyntaxException {
        return resServ.batchAddRes(files);
    }

    @ApiOperation("上传富文本图片资源")
    @PostMapping(value = "upload/rich/text/image", produces = "application/json")
    public Object uploadRichTextImage(@RequestParam(value = "upload") MultipartFile file) {
        Map<String, Object> res = new HashMap<>();
        try {
            File dir = new File(commonProperties.getTempDir());
            if (!dir.exists()) {
                dir.mkdirs();
            }
            StaticResPo resPo = resServ.addRichTextImage(file);
            String fileName = file.getOriginalFilename();
            String suffixName = fileName.substring(fileName.lastIndexOf("."));
            saveToRichTextTemp(file, dir, resPo.getMdCode(), suffixName);
            res.put("uploaded", 1);
            res.put("fileName", fileName);
            res.put("url", resPo.getMdCode() + suffixName);
            res.put("resId", resPo.getId());
        } catch (Exception e) {
            LOG.error("富文本图片文件上传异常!", e);
            res.put("uploaded", 0);
            res.put("error", "文件上传异常，异常原因：" + ExceptionUtils.getRootCauseMessage(e));
        }
        return res;
    }

    @ApiOperation("删除资源")
    @Post("delete")
    public Object delete(@RequestBody Long fileId) throws FileSystemException, URISyntaxException {
        resServ.deleteFile(fileId);
        return fileId;
    }

    private void saveToRichTextTemp(MultipartFile file, File dir, String mdCode, String suffixName) throws IOException {
        File targetFile = new File(dir, mdCode + suffixName);
        if (!targetFile.exists()) {
            targetFile.createNewFile();
        }
        try (InputStream is = file.getInputStream();
             OutputStream os = new FileOutputStream(targetFile)) {
            IOUtils.copy(is, os);
        } catch (IOException e) {
            throw e;
        }
    }

    @Get("page")
    @ApiOperation("查询资源列表")
    public Object list(@RequestParam("pageNum") int pageNum,
                       @RequestParam("pageSize") int pageSize,
                       @RequestParam(value = "keyWord", defaultValue = "") String keyWord) {
        return resServ.search(keyWord,pageNum,pageSize);
    }

}
