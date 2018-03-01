package org.rta.rules.service;

import org.rta.rules.model.FeeRuleModel;
import org.rta.rules.model.TaxRuleModel;

public interface TaxFeeService {

    public TaxRuleModel getTaxDetails(TaxRuleModel taxModel);
    
    public FeeRuleModel getFeeAmount(FeeRuleModel feeModel);
    
    public FeeRuleModel getLicenseFeeAmount(FeeRuleModel feeModel);
    
    public TaxRuleModel getTaxDetailsForCitizen(TaxRuleModel taxModel);
    
    public FeeRuleModel getFeeAmountForCitizen(FeeRuleModel feeModel);
}
