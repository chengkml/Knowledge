package com.ck.journal.service;

import com.ck.journal.dao.CategoryRepository;
import com.ck.journal.dao.JournalRepository;
import com.ck.journal.enums.CategoryTypeEnum;
import com.ck.journal.po.CategoryPo;
import com.ck.journal.po.JournalPo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Title: JournalService
 * @Author: Chengkai
 * @Date: 2019/8/20 13:22
 * @Version: 1.0
 */
@Service
public class JournalService {

    @Autowired
    private JournalRepository journalRepo;

    @Autowired
    private CategoryRepository cateRepo;

    public void save(JournalPo po) {
        Date now = new Date();
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int date = cal.get(Calendar.DATE);
        CategoryPo yearPo = cateRepo.findYearByValue(CategoryTypeEnum.YEAR.name(),year);
        if(yearPo==null){
            yearPo = new CategoryPo();
            yearPo.setCreateDt(now);
            yearPo.setType(CategoryTypeEnum.YEAR.name());
            yearPo.setValue(year);
            yearPo.setLabel(year+CategoryTypeEnum.YEAR.label());
            cateRepo.saveAndFlush(yearPo);
        }
        CategoryPo monthPo = cateRepo.findByTypeAndValue(CategoryTypeEnum.MONTH.name(),month,yearPo.getId());
        if(monthPo==null){
            monthPo = new CategoryPo();
            monthPo.setCreateDt(now);
            monthPo.setType(CategoryTypeEnum.MONTH.name());
            monthPo.setValue(month);
            monthPo.setLabel(month+CategoryTypeEnum.MONTH.label());
            monthPo.setParent(yearPo.getId());
            cateRepo.saveAndFlush(monthPo);
        }
        CategoryPo dayPo = cateRepo.findByTypeAndValue(CategoryTypeEnum.DAY.name(),date,monthPo.getId());
        if(dayPo==null){
            dayPo = new CategoryPo();
            dayPo.setCreateDt(now);
            dayPo.setType(CategoryTypeEnum.DAY.name());
            dayPo.setValue(date);
            dayPo.setLabel(date+CategoryTypeEnum.DAY.label());
            dayPo.setParent(monthPo.getId());
            cateRepo.saveAndFlush(dayPo);
        }
        po.setCategory(year+CategoryTypeEnum.YEAR.label()+month+CategoryTypeEnum.MONTH.label()+date+CategoryTypeEnum.DAY.label());
        po.setCreateDt(now);
        journalRepo.save(po);
    }

    public Page<JournalPo> search(String keyWord, int pageSize, int pageNum, Long startTime, Long endTime, String category) {
        Sort sort = new Sort(Sort.Direction.DESC,"createDt");
        Pageable page = PageRequest.of(pageNum,pageSize,sort);
        SimpleDateFormat sp = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Specification sf = new Specification<JournalPo>(){
            @Override
            public Predicate toPredicate(Root<JournalPo> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
                List<Predicate> predicates = new ArrayList<>();
                predicates.add(cb.like(root.get("category").as(String.class),category+"%"));
                if(startTime!=null){
                    String startTimeStr = sp.format(new Date(startTime));
                    predicates.add(cb.greaterThanOrEqualTo(root.get("createDt").as(String.class), startTimeStr));
                }
                if(endTime!=null){
                    String endTimeStr = sp.format(new Date(endTime));
                    predicates.add(cb.lessThanOrEqualTo(root.get("createDt").as(String.class), endTimeStr));
                }
                if(StringUtils.isNotBlank(keyWord)){
                    List<Predicate> subPredicates = new ArrayList<>();
                    subPredicates.add(cb.like(root.<String>get("title"),"%"+keyWord+"%"));
                    subPredicates.add(cb.like(root.<String>get("content"),"%"+keyWord+"%"));
                    predicates.add(cb.or(subPredicates.toArray(new Predicate[subPredicates.size()])));
                }
                return cq.where(predicates.toArray(new Predicate[predicates.size()])).getRestriction();
            }
        };
        Page<JournalPo> res = journalRepo.findAll(sf,page);
        List<JournalPo> pos = res.getContent();
        for(JournalPo po : pos){
            po.setCreateDate(sp.format(po.getCreateDt()));
            String temp = po.getContent().replaceAll("<.+>","");
            temp = temp.replaceAll("\n","");
            po.setSummary(temp.length()>40?temp.substring(0,40)+"...":temp);
        }
        return res;
    }
}
