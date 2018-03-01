package org.rta.rules.service.impl;

import org.apache.log4j.Logger;
import org.rta.rules.dao.HomeTaxDAO;
import org.rta.rules.entity.HomeTaxEntity;
import org.rta.rules.service.HomeTaxService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

import javax.transaction.Transactional;

@Service
public class HomeTaxServiceImpl implements HomeTaxService {
private static final Logger log = Logger.getLogger(HomeTaxServiceImpl.class);

@Autowired
private HomeTaxDAO homeTaxDAO;

@Override
@Transactional
public List<HomeTaxEntity> getHomeTax() {
	
	return homeTaxDAO.getAll();
}

}
