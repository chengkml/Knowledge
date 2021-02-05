package com.ck.cosmic.service;

import com.ck.cosmic.dao.CosmicDemandRepository;
import com.ck.ds.domain.DsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CosmicDemandService {

    @Autowired
    private DsManager dsManager;

    @Autowired
    private CosmicDemandRepository demandRepo;

}
