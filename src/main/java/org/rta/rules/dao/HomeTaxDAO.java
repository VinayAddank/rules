package org.rta.rules.dao;

import java.util.List;

import org.rta.rules.dao.base.GenericDAO;
import org.rta.rules.entity.HomeTaxEntity;
import org.rta.rules.model.TaxRuleModel;

public interface HomeTaxDAO extends GenericDAO<HomeTaxEntity>{

	public List<HomeTaxEntity> getHomeTax(TaxRuleModel taxRuleModel);
}
