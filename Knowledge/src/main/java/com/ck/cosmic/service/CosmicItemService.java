package com.ck.cosmic.service;

import com.ck.cosmic.dao.CosmicItemRepository;
import com.ck.ds.domain.DsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CosmicItemService {

    @Autowired
    private DsManager dsManager;

    @Autowired
    private CosmicItemRepository itemRepo;

    public Object getDemand(Long demandId) {
        return null;
    }
}
