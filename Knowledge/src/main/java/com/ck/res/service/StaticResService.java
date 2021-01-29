package com.ck.res.service;

import com.ck.common.helper.JdbcQueryHelper;
import com.ck.common.properties.CommonProperties;
import com.ck.ds.domain.DsManager;
import com.ck.res.dao.StaticResRepository;
import com.ck.res.enums.ResValidEnum;
import com.ck.res.po.StaticResPo;
import com.ck.res.properties.StaticResProperties;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.*;

@Service
public class StaticResService {

    @Autowired
    private StaticResRepository resRepo;

    @Autowired
    private StaticResProperties resProperties;

    @Autowired
    private CommonProperties commonProperties;

    @Autowired
    private DsManager dsManager;

    /**
     * 关联文件
     *
     * @param relaId
     * @param resIds
     */
    public void matchRes(Long relaId, List<Long> resIds) {
        if (resIds == null || resIds.isEmpty()) {
            return;
        }
        List<StaticResPo> resPos = resRepo.findAllById(resIds);
        resPos.forEach(po -> {
            po.setRelaId(relaId);
            po.setValid(ResValidEnum.VALID.getValue());
        });
        resRepo.saveAll(resPos);
    }

    /**
     * 删除无效文件（一天内未关联的文件），交给定时任务
     */
    public void deleteInvalidRes() throws FileSystemException, URISyntaxException {
        List<StaticResPo> resPos = resRepo.findByValid(ResValidEnum.INVALID.getValue());
        long now = System.currentTimeMillis();
        for (StaticResPo res : resPos) {
            if (res.getCreateDate().getTime() + 86400000L < now) {
                deleteByPo(res);
            }
        }
    }

    /**
     * 根据文件ID删除指定文件
     *
     * @param id
     */
    public void deleteFile(Long id) throws FileSystemException, URISyntaxException {
        deleteByPo(resRepo.getOne(id));
    }

    /**
     * 根据resPo删除文件
     * 存在公用文件时只删除记录，不删除文件
     *
     * @param res
     * @throws FileSystemException
     * @throws URISyntaxException
     */
    private void deleteByPo(StaticResPo res) throws FileSystemException, URISyntaxException {
        if (resRepo.findByMdCode(res.getMdCode()).size() > 1) {
            resRepo.delete(res);
            return;
        }
        try (FileObject target = getDirFileObject().resolveFile(res.getMdCode())) {
            if (!target.exists()) {
                throw new RuntimeException("欲删除的文件不存在!");
            }
            target.delete();
        }
        resRepo.delete(res);
    }

    /**
     * 获取文件目录对象
     *
     * @return
     * @throws FileSystemException
     * @throws URISyntaxException
     */
    public FileObject getDirFileObject() throws FileSystemException, URISyntaxException {
        StandardFileSystemManager vfsmgr = new StandardFileSystemManager();
        FileSystemOptions opts = new FileSystemOptions();
        vfsmgr.init();
        SftpFileSystemConfigBuilder.getInstance().setTimeout(opts, 2000000);
        SftpFileSystemConfigBuilder.getInstance().setStrictHostKeyChecking(opts, "no");
        SftpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(opts, false);
        FileObject dir;
        if ("true".equals(resProperties.getUseSftp())) {
            dir = vfsmgr.resolveFile(new URI("sftp", "", "", 22, resProperties.getResRoot(), null, null).toString(), opts);
        } else {
            dir = vfsmgr.resolveFile(resProperties.getResRoot());
        }
        if (!dir.exists()) {
            dir.createFolder();
        }
        return dir;
    }

    /**
     * 添加资源文件
     *
     * @param file
     * @return
     * @throws IOException
     * @throws URISyntaxException
     */
    public Long addRes(MultipartFile file) throws URISyntaxException, IOException {
        StaticResPo resPo = new StaticResPo();
        doAddRes(file, resPo);
        return resPo.getId();
    }

    public StaticResPo addRichTextImage(MultipartFile file) throws URISyntaxException, IOException {
        StaticResPo resPo = new StaticResPo();
        doAddRes(file, resPo);
        return resPo;
    }

    private void doAddRes(MultipartFile file, StaticResPo resPo) throws IOException, URISyntaxException {
        resPo.setName(file.getOriginalFilename());
        resPo.setCreateDate(new Date());
        resPo.setSize(file.getSize());
        resPo.setMdCode(DigestUtils.md5DigestAsHex(file.getInputStream()));
        resPo.setValid(ResValidEnum.INVALID.getValue());
        resRepo.save(resPo);
        try (FileObject target = getDirFileObject().resolveFile(resPo.getMdCode());
             InputStream is = file.getInputStream()) {
            if (target.exists()) {
                target.delete();
            }
            try (OutputStream os = target.getContent().getOutputStream()) {
                target.createFile();
                IOUtils.copy(is, os);
            }
            resPo.setResUrl(target.getURL().toString());
            resPo.setPath(resProperties.getResRoot() + File.separator + resPo.getMdCode());
            resRepo.save(resPo);
        } catch (FileSystemException e) {
            resRepo.delete(resPo);
            throw e;
        } catch (URISyntaxException e) {
            resRepo.delete(resPo);
            throw e;
        } catch (IOException e) {
            resRepo.delete(resPo);
            throw e;
        }
    }

    /**
     * 批量添加文件
     *
     * @param files
     * @return
     */
    public List<Long> batchAddRes(MultipartFile[] files) throws IOException, URISyntaxException {
        List<Long> resIds = new ArrayList<>();
        for (MultipartFile file : files) {
            resIds.add(addRes(file));
        }
        return resIds;
    }

    /**
     * 根据关联ID删除文件
     *
     * @param relaId
     * @throws FileSystemException
     * @throws URISyntaxException
     */
    public void deleteByRelaId(Long relaId) throws FileSystemException, URISyntaxException {
        List<StaticResPo> resPos = resRepo.findByRelaId(relaId);
        for (StaticResPo res : resPos) {
            deleteByPo(res);
        }
    }

    /**
     * 清理文件目录，交给定时任务
     */
    public void cleanDirFromRoot() throws FileSystemException, URISyntaxException {
        try (FileObject target = getDirFileObject()) {
            if (!target.exists() || target.getChildren().length == 0) {
                return;
            }
            FileObject[] children = target.getChildren();
            Map<String, FileObject> resMap = new HashMap<>();
            for (FileObject child : children) {
                resMap.put(child.getName().getBaseName(), child);
            }
            List<String> validRes = new ArrayList<>();
            resRepo.findByMdCodeIn(new ArrayList<>(resMap.keySet())).forEach(resPo -> validRes.add(resPo.getMdCode()));
            Iterator<Map.Entry<String, FileObject>> it = resMap.entrySet().iterator();
            while (it.hasNext()) {
                Map.Entry<String, FileObject> e = it.next();
                if (!validRes.contains(e.getKey())) {
                    e.getValue().delete();
                }
            }
        }
    }

    public List<Long> loadRes(Long relaId) throws IOException, URISyntaxException {
        List<StaticResPo> resPos = resRepo.findByRelaId(relaId);
        List<Long> resIds = new ArrayList<>();
        File dir = new File(commonProperties.getTempDir());
        for (StaticResPo res : resPos) {
            resIds.add(res.getId());
            String suffixName = res.getName().substring(res.getName().lastIndexOf("."));
            File targetFile = new File(dir, res.getMdCode() + suffixName);
            if (!targetFile.exists()) {
                targetFile.createNewFile();
            }
            try (FileObject target = getDirFileObject().resolveFile(res.getMdCode());
                 InputStream is = target.getContent().getInputStream();
                 OutputStream os = new FileOutputStream(targetFile)) {
                IOUtils.copy(is, os);
            }
        }
        return resIds;
    }

    public InputStream getInputStream(StaticResPo res) throws FileSystemException, URISyntaxException {
        FileObject file = getDirFileObject().resolveFile(res.getMdCode());
        if (!file.exists()) {
            throw new RuntimeException("文件不存在！");
        }
        return file.getContent().getInputStream();
    }

    public Page<StaticResPo> search(String keyWord, int pageNum, int pageSize) {
        StringBuilder listSql = new StringBuilder("select r.id, r.create_date, r.name, r.path, r.rela_id, r.res_url, r.size, r.valid, r.md_code from ck_res r where 1=1 ");
        StringBuilder countSql = new StringBuilder("select count(1) from ck_res r where 1=1 ");
        Map<String, Object> params = new HashMap<>();
        JdbcQueryHelper.lowerLike("keyWord", keyWord, "and lower(r.name) like :keyWord ", params, dsManager.getLocalDsType(), listSql, countSql);
        JdbcQueryHelper.order("create_date", "desc", listSql);
        List<StaticResPo> list = new ArrayList<>();
        dsManager.getNamedJdbcTemplate().queryForList(JdbcQueryHelper.getLimitSql(dsManager.getNamedJdbcTemplate(), listSql, pageNum, pageSize), params).forEach(map -> {
            StaticResPo res = new StaticResPo();
            res.setId(MapUtils.getLong(map, "id"));
            res.setCreateDate((Date) MapUtils.getObject(map, "create_date"));
            res.setName(MapUtils.getString(map, "name"));
            res.setPath(MapUtils.getString(map, "path"));
            res.setRelaId(MapUtils.getLong(map, "rela_id"));
            res.setResUrl(MapUtils.getString(map, "res_url"));
            res.setSize(MapUtils.getLong(map, "size"));
            res.setValid(MapUtils.getString(map, "valid"));
            res.setMdCode(MapUtils.getString(map, "md_code"));
            list.add(res);
        });
        return JdbcQueryHelper.toPage(dsManager.getNamedJdbcTemplate(), countSql, params, list, pageNum, pageSize);
    }
}
