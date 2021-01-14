package com.ck.bat.service;

import com.ck.bat.dao.BatRepository;
import com.ck.bat.po.BatPo;
import com.ck.bat.vo.RunningProcess;
import com.ck.common.helper.StringHelper;
import com.ck.common.properties.CommonProperties;
import com.ck.common.websocket.CkWebSocketHandler;
import com.ck.common.websocket.wo.BatLog;
import com.ck.res.dao.StaticResRepository;
import com.ck.res.po.StaticResPo;
import com.ck.res.service.StaticResService;
import com.ck.todo.po.TodoItemPo;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.vfs2.FileSystemException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.io.*;
import java.net.URISyntaxException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

@Service
public class BatService {

    @Autowired
    private BatRepository batRepo;

    @Autowired
    private StaticResService resServ;

    @Autowired
    private StaticResRepository resRepo;

    @Autowired
    private CommonProperties commonPros;

    private static final String BAT_CODE = "gbk";

    private Map<String, RunningProcess> processes = new ConcurrentHashMap<>();

    private Executor executor = Executors.newSingleThreadExecutor();
    ;

    public void start(Long batId) throws IOException {
        BatPo bat = batRepo.getOne(batId);
        String line;
        List<StaticResPo> ress = resRepo.findByRelaId(batId);
        if (ress.isEmpty()) {
            throw new RuntimeException("脚本文件不存在！");
        }
        if (ress.size() > 1) {
            throw new RuntimeException("存在多个脚本文件！");
        }
        File tempDir = new File(commonPros.getTempDir());
        if (!tempDir.exists()) {
            tempDir.mkdirs();
        }
        File batTempFile = new File(tempDir, ress.get(0).getName());
        if (!batTempFile.exists()) {
            batTempFile.createNewFile();
        }
        try (BufferedReader batReader = new BufferedReader(new InputStreamReader(new FileInputStream(ress.get(0).getPath()), BAT_CODE));
             BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(batTempFile, false), BAT_CODE))) {
            while ((line = batReader.readLine()) != null) {
                writer.write(line);
            }
        } catch (RuntimeException e) {
            throw e;
        }
        if (StringUtils.isNotBlank(bat.getParams())) {
            bat.setParams(batTempFile.getPath() + "," + bat.getParams());
        }
        ProcessBuilder builder = new ProcessBuilder(bat.getParams().split(","));
        Process process = builder.start();
        executor.execute(() -> {
            RunningProcess rp = new RunningProcess(bat.getName(), bat.getLabel(), process);
            processes.put(rp.getId(), rp);
            String logLine;
            try (BufferedReader logReader = new BufferedReader(new InputStreamReader(process.getInputStream(), BAT_CODE))) {
                while ((logLine = logReader.readLine()) != null) {
                    CkWebSocketHandler.sendMsgToAll(new BatLog(logLine + "\n"));
                }
                process.waitFor();
                batTempFile.delete();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                processes.remove(rp);
            }
        });
    }

    public Long saveBat(BatPo batPo) {
        if (batPo.getId() == null) {
            batPo.setCreateDate(new Date());
        }
        batRepo.save(batPo);
        if (batPo.getFileId() != null) {
            resServ.matchRes(batPo.getId(), Arrays.asList(batPo.getFileId()));
        }
        return batPo.getId();
    }

    @Transactional
    public Long deleteBat(Long batId) throws FileSystemException, URISyntaxException {
        BatPo batPo = batRepo.getOne(batId);
        batRepo.delete(batPo);
        resServ.deleteByRelaId(batId);
        return batId;
    }

    public Page<BatPo> list(String keyWord, int pageNum, int pageSize) {
        Sort sort = new Sort(Sort.Direction.DESC, "createDate");
        Pageable page = PageRequest.of(pageNum, pageSize, sort);
        Specification sf = (Specification<TodoItemPo>) (root, cq, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.isNotBlank(keyWord)) {
                List<Predicate> subPredicates = new ArrayList<>();
                subPredicates.add(cb.or(cb.like(root.get("name"), "%" + keyWord + "%"), cb.like(root.get("label"), "%" + keyWord + "%")));
                predicates.add(cb.or(subPredicates.toArray(new Predicate[subPredicates.size()])));
            }
            return cq.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
        };
        Page<BatPo> pageRes = batRepo.findAll(sf, page);
        pageRes.getContent().forEach(bat -> {
            List<StaticResPo> resPos = resRepo.findByRelaId(bat.getId());
            if (!resPos.isEmpty()) {
                bat.setBat(resPos.get(0));
            }
        });
        return pageRes;
    }

    public List<RunningProcess> listProcess() {
        return new ArrayList<>(processes.values());
    }

    public String stopProcess(String processId) {
        RunningProcess process = processes.get(processId);
        Objects.requireNonNull(process, StringHelper.format("未查询到进程：{}", processId));
        process.getProcess().destroyForcibly();
        return processId;
    }
}
