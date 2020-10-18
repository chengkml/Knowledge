package com.ck.knowledge.service;

import com.ck.knowledge.dao.res.StaticResRepository;
import com.ck.knowledge.enums.ResValidEnum;
import com.ck.knowledge.po.res.StaticResPo;
import com.ck.knowledge.properties.StaticResProperties;
import org.apache.commons.vfs2.FileObject;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.impl.StandardFileSystemManager;
import org.apache.commons.vfs2.provider.sftp.SftpFileSystemConfigBuilder;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StaticResService {

    @Autowired
    private StaticResRepository resRepo;

    @Autowired
    private StaticResProperties resProperties;

    public void matchRes(Long relaId, List<Long> resIds) {
        if (resIds == null || resIds.isEmpty()) {
            return;
        }
        List<StaticResPo> resPos = resRepo.findAllById(resIds);
        resPos.forEach(po -> po.setRelaId(relaId));
        resRepo.saveAll(resPos);
    }

    public void deleteInvalidRes() {
        List<StaticResPo> resPos = resRepo.findByValid(ResValidEnum.INVALID.getValue());
        long now = System.currentTimeMillis();
        List<StaticResPo> toDelete = new ArrayList<>();
        resPos.forEach(po -> {
            if (po.getCreateDate().getTime() + 86400000L < now) {
                toDelete.add(po);
            }
        });
        resRepo.deleteAll(toDelete);
    }

    public Long deleteFile(Long id) {
        StaticResPo res = resRepo.getOne(id);
        resRepo.delete(res);
        FileObject target = null;
        try {
            target = getDirFileObject().resolveFile(String.valueOf(id));
            if (!target.exists()) {
                throw new RuntimeException("欲删除的文件不存在!");
            }
            target.delete();
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                target.close();
            } catch (FileSystemException e) {
                e.printStackTrace();
            }
        }
        return id;
    }

    private FileObject getDirFileObject() throws FileSystemException, URISyntaxException {
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

    public Long addRes(MultipartFile file) {
        StaticResPo resPo = new StaticResPo();
        resPo.setName(file.getOriginalFilename());
        resPo.setCreateDate(new Date());
        resPo.setSize(file.getSize());
        resPo.setValid(ResValidEnum.INVALID.getValue());
        resRepo.save(resPo);
        FileObject target = null;
        try {
            target = getDirFileObject().resolveFile(String.valueOf(resPo.getId()));
            try (InputStream is = file.getInputStream();
                 OutputStream os = target.getContent().getOutputStream()) {
                if (target.exists()) {
                    target.delete();
                }
                target.createFile();
                IOUtils.copy(is, os);
            } catch (FileSystemException e) {
                throw new FileSystemException(e);
            } catch (IOException e) {
                throw new IOException(e);
            }
            resPo.setResUrl(target.getURL().toString());
            resPo.setPath(resProperties.getResRoot() + File.separator + resPo.getId());
            resRepo.save(resPo);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            try {
                target.close();
            } catch (FileSystemException e) {
                e.printStackTrace();
            }
        }
        return resPo.getId();
    }

    public List<Long> batchAddRes(MultipartFile[] files) {
        List<Long> resIds = new ArrayList<>();
        for (MultipartFile file : files) {
            resIds.add(addRes(file));
        }
        return resIds;
    }

    public void deleteByRelaId(Long relaId) {
        List<StaticResPo> resPos = resRepo.findByRelaId(relaId);
        if (!resPos.isEmpty()) {
            resRepo.deleteAll(resPos);
        }
    }
}
