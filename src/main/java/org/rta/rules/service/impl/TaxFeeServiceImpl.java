package org.rta.rules.service.impl;

import java.util.List;

import javax.transaction.Transactional;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.kie.api.runtime.KieSession;
import org.rta.rules.KieSessions;
import org.rta.rules.constant.VehicleSubClass;
import org.rta.rules.dao.HomeTaxDAO;
import org.rta.rules.entity.HomeTaxEntity;
import org.rta.rules.enums.FuelType;
import org.rta.rules.enums.MonthType;
import org.rta.rules.enums.PermitSubType;
import org.rta.rules.enums.RegistrationCategoryType;
import org.rta.rules.enums.ServiceType;
import org.rta.rules.enums.TaxType;
import org.rta.rules.model.FeeRuleModel;
import org.rta.rules.model.TaxRuleModel;
import org.rta.rules.service.TaxFeeService;
import org.rta.rules.utils.DateUtil;
import org.rta.rules.utils.NumberParser;
import org.rta.rules.utils.StringsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TaxFeeServiceImpl implements TaxFeeService {
	private static final Log log = LogFactory.getLog(TaxFeeServiceImpl.class);

	@Autowired
	private HomeTaxDAO homeTaxDAO;

	@Transactional
	public TaxRuleModel getTaxDetails(TaxRuleModel taxRuleModel) {
		
		List<HomeTaxEntity> homeTaxEntitys = null;
		HomeTaxEntity homeTaxEntity = null;
		taxRuleModel.setWeightType("-");
		taxRuleModel = getVehicleAge(taxRuleModel);
		homeTaxEntitys = homeTaxDAO.getHomeTax(taxRuleModel);
		if(homeTaxEntitys != null && homeTaxEntitys.size() > 0)            
			homeTaxEntity = homeTaxEntitys.get(0); 
		log.info("::homeTaxEntitys:: " + homeTaxEntity);
		if(ServiceType.getServiceType(taxRuleModel.getServiceCode()) != null 
				&& (ServiceType.PERMIT_FRESH.getCode().equalsIgnoreCase(taxRuleModel.getServiceCode()) || ServiceType.PERMIT_VARIATIONS.getCode().equalsIgnoreCase(taxRuleModel.getServiceCode()))){
			taxRuleModel = getPermitDiffTax(taxRuleModel , homeTaxEntity);
			taxRuleModel.setTaxAmount(NumberParser.getRoundNextTen(taxRuleModel.getTaxAmount()));
		}else{
			boolean isTaxExmepted = getExemptedTax(taxRuleModel);
			if(isTaxExmepted){
				taxRuleModel.setTaxAmount(0);
				taxRuleModel.setTax(0);
			}else{	
				taxRuleModel.setQuarterAmt(homeTaxEntity.getTaxAmt());  
				taxRuleModel.setTax(homeTaxEntity.getTaxPercent());
				taxRuleModel.setIncAmt(homeTaxEntity.getIncAmt());
				taxRuleModel.setIncRLW(homeTaxEntity.getIncRlw());
				if(homeTaxEntity.getIncRlw() > 0 && (homeTaxEntity.getToRlw() == 0)){
					taxRuleModel.setFromULW(homeTaxEntity.getFromUlw());
					taxRuleModel.setWeightType("ULW");
				}	
				if(homeTaxEntity.getIncRlw() > 0 && (homeTaxEntity.getToUlw() == 0)){
					taxRuleModel.setFromRLW(homeTaxEntity.getFromRlw());
					taxRuleModel.setWeightType("RLW");
				}	
				KieSession kSession = null; 
				kSession = getTaxDetails(kSession , taxRuleModel); 
				kSession.insert(taxRuleModel);
				kSession.fireAllRules();
				if(taxRuleModel.getISuzo()){
					log.info(":::getTax::ISUZU::drools::::");
					taxRuleModel.setTax(0.0);
					taxRuleModel.setTaxAmount(0.0);
				}else{
					taxRuleModel = calEIBTNdPSVTAsOBTWhenPermitExp(taxRuleModel , kSession);
					taxRuleModel.setTaxAmount(NumberParser.getRoundNextTen(taxRuleModel.getTaxAmount()));
					taxRuleModel.setCessFee(NumberParser.getRoundNextTen(taxRuleModel.getCessFee()));
					taxRuleModel.setTotalAmt(taxRuleModel.getTaxAmount());
				}	
			}	
		}
		return taxRuleModel;
	}
	
	public boolean getExemptedTax(TaxRuleModel taxRuleModel){
		KieSession kSession = null;
		kSession = KieSessions.getKieSessionForTaxExempted(taxRuleModel);
		kSession.insert(taxRuleModel);
		kSession.fireAllRules();
		return taxRuleModel.getTaxExempted();
	}


	public KieSession getTaxDetails(KieSession kSession , TaxRuleModel taxRuleModel){
		kSession = KieSessions.getKieSessionForTax(taxRuleModel);
		return kSession;
	}
	
	public KieSession getNonTransportTaxDetails(KieSession kSession , HomeTaxEntity homeTaxEntity , TaxRuleModel taxRuleModel){
		kSession = KieSessions.getKieSessionForNTTax(taxRuleModel);
		return kSession;
	}
	
	public KieSession getTransportTaxDetails(KieSession kSession , HomeTaxEntity homeTaxEntity , TaxRuleModel taxRuleModel){
		kSession = KieSessions.getKieSessionForTTax(taxRuleModel);
		return kSession;
	}
	
	public FeeRuleModel getFeeAmount(FeeRuleModel feeRuleModel) {
		log.info(":::::::getFeeAmount::drools::start::: " + feeRuleModel.getServiceCode() +" - "+ feeRuleModel.getVehicleClassCategory() +" - "+ feeRuleModel.getOldClassOfVehicle());
		KieSession kSession = null;
		if(feeRuleModel.getPermitType() == null)
			feeRuleModel.setPermitType("");
		switch (RegistrationCategoryType.getRegistrationCategoryType(feeRuleModel.getRegCategory())) {
		case NON_TRANSPORT:
			kSession = getNONTransportKieSession(feeRuleModel);
			kSession.insert(feeRuleModel);
			kSession.fireAllRules();
			break;
		case TRANSPORT:
			kSession = getTransportKieSession(feeRuleModel);
			kSession.insert(feeRuleModel);
			kSession.fireAllRules();
			break;
		}
		kSession = getHSRPKieSession(feeRuleModel);
		kSession.insert(feeRuleModel);
		kSession.fireAllRules();
		return feeRuleModel;
	}

	private KieSession getHSRPKieSession(FeeRuleModel feeRuleModel){
		KieSession kSession = null;
		kSession = KieSessions.getKieSessionForHSRP(feeRuleModel);
		return kSession;
	}
	
	private KieSession getTransportKieSession(FeeRuleModel feeRuleModel){
		KieSession kSession = null;
		kSession = KieSessions.getKieSessionForTFee(feeRuleModel);
		return kSession;
	}


	private KieSession getNONTransportKieSession(FeeRuleModel feeRuleModel){
		KieSession kSession = null;
		kSession = KieSessions.getKieSessionForNTFee(feeRuleModel);
		return kSession;
	}


	@Override
	public FeeRuleModel getLicenseFeeAmount(FeeRuleModel feeModel) {
		KieSession kSession = null;
		kSession = KieSessions.getKieSessionForLicenseFee(feeModel);
		kSession.insert(feeModel);
		kSession.fireAllRules();
		return feeModel;
	}

	private  TaxRuleModel getPermitDiffTax(TaxRuleModel taxRuleModel , HomeTaxEntity homeTaxEntity) {
		try{			
			double diffAmt = 0;
			int currentMonth = (4 - getQuarterCurrentMonth(DateUtil.toCurrentUTCTimeStamp()));
			if(ServiceType.PERMIT_FRESH.getCode().equalsIgnoreCase(taxRuleModel.getServiceCode()) || ServiceType.PERMIT_VARIATIONS.getCode().equalsIgnoreCase(taxRuleModel.getServiceCode())){
			    double newAmt= getTaxPerSeat(homeTaxEntity.getTaxAmt(),taxRuleModel);
				if(newAmt > taxRuleModel.getOldTaxAmt())
				diffAmt = (((newAmt / 3) * currentMonth) - ((taxRuleModel.getOldTaxAmt() / 3) * (currentMonth - 1)));
				if(diffAmt > 0){
					taxRuleModel.setTaxAmount(diffAmt);
				}
			}else{
				log.error(":getPermitDiffTax: HomeTax:zero: ");
				taxRuleModel.setTaxAmount(0);
			}
		}catch(Exception e){
			e.printStackTrace();
			log.error(":::::::Error while getting home tax details:::::::");	
		}
		return taxRuleModel;
	}

	public double getTaxPerSeat(double taxAmt,TaxRuleModel taxRuleModel){  
		int seat=0;
		
		if(VehicleSubClass.COCT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || VehicleSubClass.TOVT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory())){
			seat=taxRuleModel.getSeatingCapacity() - 1;	
			if(VehicleSubClass.TOVT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) && "AITP".equalsIgnoreCase(taxRuleModel.getPermitType()))	{
				seat=taxRuleModel.getSeatingCapacity() - 2;	
			}
			if(taxRuleModel.getPermitSubType() !=null && taxRuleModel.getPermitSubType().equalsIgnoreCase(PermitSubType.HOME_DISTRICT.getCode())){
				taxAmt=seat*1000;
			}
			if(taxRuleModel.getPermitSubType() !=null && taxRuleModel.getPermitSubType().equalsIgnoreCase(PermitSubType.NEIGHBOURING_DISTRICT.getCode())){
				taxAmt=seat*1250;
			}
			if(taxRuleModel.getPermitSubType() !=null && taxRuleModel.getPermitSubType().equalsIgnoreCase(PermitSubType.THROUGHOUT_STATE.getCode())){
				taxAmt=seat*3750;
			}
		}
		if(VehicleSubClass.MAXT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory())){
			seat=taxRuleModel.getSeatingCapacity() - 1;			
			if("AITP~AITC".equalsIgnoreCase(taxRuleModel.getPermitType())){
				taxAmt=seat*1350;
			}
			else{
				taxAmt=seat*650;
			}

		}
		if(VehicleSubClass.OBT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory())){
			seat=taxRuleModel.getSeatingCapacity() - 1;			
			taxAmt=seat*396.9;							
		}
		return taxAmt;
	}

	/**
	 * DIFFERENTIAL TAX CALC::: Except PAY TAX service by default we are calculating tax for 1 quarter and then we are deciding 
	 * whether user needs to pay tax for one,two or three months by subtracting unused months of old tax paid in same quarter.
	 * If current quarter's new tax  > old tax then we are subtracting unused months otherwise differential tax will be 0.
	 */
	@Override
	@Transactional
	public TaxRuleModel getTaxDetailsForCitizen(TaxRuleModel taxRuleModel) {
		int oldMonthType = taxRuleModel.getMonthType();
		List<HomeTaxEntity> homeTaxEntitys = null;
		HomeTaxEntity homeTaxEntity = null;
		taxRuleModel = getVehicleAge(taxRuleModel);
		taxRuleModel.setWeightType("-");
		homeTaxEntitys = homeTaxDAO.getHomeTax(taxRuleModel);
		if(homeTaxEntitys != null && homeTaxEntitys.size() > 0)
			homeTaxEntity = homeTaxEntitys.get(0);
		if(homeTaxEntity == null){
			log.error(":::HomeTax Empty:::::");
			return null;
		}
		taxRuleModel.setQuarterAmt(homeTaxEntity.getTaxAmt());
		taxRuleModel.setTax(homeTaxEntity.getTaxPercent());
		taxRuleModel.setIncAmt(homeTaxEntity.getIncAmt());
		taxRuleModel.setIncRLW(homeTaxEntity.getIncRlw());
		if(homeTaxEntity.getIncRlw() > 0 && (homeTaxEntity.getToRlw() == 0)){
			taxRuleModel.setFromULW(homeTaxEntity.getFromUlw());
			taxRuleModel.setWeightType("ULW");
		}	
		if(homeTaxEntity.getIncRlw() > 0 && (homeTaxEntity.getToUlw() == 0)){
			taxRuleModel.setFromRLW(homeTaxEntity.getFromRlw());
			taxRuleModel.setWeightType("RLW");
		}	
		
		try {
			if(taxRuleModel.getPeriodicTaxType()!= null && taxRuleModel.getPeriodicTaxType() != 0)
			switch(TaxType.getTaxType(taxRuleModel.getPeriodicTaxType())){
			case ANNUAL_TAX:
				taxRuleModel.setOldTaxAmt((taxRuleModel.getOldTaxAmt()/12)*3);
				break;
			case HALFYEARLY_TAX:
				taxRuleModel.setOldTaxAmt((taxRuleModel.getOldTaxAmt()/6)*3);
				break;
			case QUARTERLY_TAX:
				taxRuleModel = calcOldTaxIfTrIssueDateIsInSameQuarter(taxRuleModel);
				break;
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		
		switch(ServiceType.getServiceType(taxRuleModel.getServiceCode())){
		case DIFFERENTIAL_TAX:
			taxRuleModel = getDiffTaxDetails(taxRuleModel);
			break;
		case VEHICLE_ATLERATION:
			taxRuleModel = getDiffTaxForAlterationDetails(taxRuleModel);
			break;
		case REGISTRATION_RENEWAL:
		case FC_RENEWAL: 
			taxRuleModel = getGreenTaxFitnessNdRegistRenewal(taxRuleModel);
			break;
		case PERMIT_FRESH:
		case PERMIT_VARIATIONS:
			taxRuleModel = getPermitDiffTax(taxRuleModel , homeTaxEntity);
			break;
		case PAY_TAX:
				if(taxRuleModel.getVcrFlag())
				taxRuleModel = getPayTaxForVCR(taxRuleModel);
			else
				taxRuleModel = getPayTax(taxRuleModel);	
		}
		if((taxRuleModel.getPeriodicTaxType()== null || taxRuleModel.getQuarterlyTaxType() == 0) && (!ServiceType.PAY_TAX.getCode().equalsIgnoreCase(taxRuleModel.getServiceCode()))){
			taxRuleModel.setMonthType(oldMonthType);
		}
				
		taxRuleModel.setTaxAmount(NumberParser.getRoundNextTen(taxRuleModel.getTaxAmount()));
		taxRuleModel.setTaxAmtArrears(NumberParser.getRoundNextTen(taxRuleModel.getTaxAmtArrears()));
		taxRuleModel.setPenalty(NumberParser.getRoundNextTen(taxRuleModel.getPenalty()));
		taxRuleModel.setPenaltyArrears(NumberParser.getRoundNextTen(taxRuleModel.getPenaltyArrears()));
		taxRuleModel.setCessFee(NumberParser.getRoundNextTen(taxRuleModel.getCessFee()));
		taxRuleModel.setTotalTaxAmount(taxRuleModel.getTaxAmount() + taxRuleModel.getTaxAmtArrears() + taxRuleModel.getPenalty() + taxRuleModel.getPenaltyArrears());
		taxRuleModel.setTotalAmt(taxRuleModel.getTotalTaxAmount() + taxRuleModel.getCessFee() + taxRuleModel.getServiceFee());
		
		return taxRuleModel;
	}

	public TaxRuleModel getPayTax(TaxRuleModel taxRuleModel){
 		KieSession kSession = null;
 		if(taxRuleModel.getPeriodicTaxType()== null || taxRuleModel.getQuarterlyTaxType() == 0)
 			taxRuleModel.setMonthType(3);
 		else
 			taxRuleModel.setMonthType(taxRuleModel.getQuarterlyTaxType());
		kSession = getTaxDetails(kSession , taxRuleModel);
		kSession.insert(taxRuleModel);
		kSession.fireAllRules();
		//taxRuleModel.setTaxAmount(taxRuleModel.getTaxAmount());
		log.info(":::1::: " + taxRuleModel.getTaxAmount() + " - " + taxRuleModel.getQuarterlyTaxType());
		taxRuleModel = monthCal(taxRuleModel);
		if(VehicleSubClass.EIBT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory())){
		taxRuleModel = calEIBTNdPSVTAsOBTWhenPermitExp(taxRuleModel , kSession);
		}
		else
		taxRuleModel = calculatePenaltyNdServiceFee(taxRuleModel); 
		return taxRuleModel;
 	}
	
	/**
	 * Penalty, penalty arrears and service fee calculation.
	 */
	private TaxRuleModel calculatePenaltyNdServiceFee(TaxRuleModel taxRuleModel){
		log.debug(" ::calculatePenaltyNdServiceFee:::: ");
		int currentQuarterMonth = getQuarterCurrentMonth(DateUtil.toCurrentUTCTimeStamp());
		double taxAmt = taxRuleModel.getQuarterAmt();
		log.info(":::6::: " +"Quarter amt - "+ taxAmt );
		double pen25 = (taxAmt * 25)/100;
		double pen50 = (taxAmt * 50)/100;
		if(taxRuleModel.getTaxValidUpto() > 0 && (!DateUtil.isSameOrGreaterDate(taxRuleModel.getTaxValidUpto(),DateUtil.toCurrentUTCTimeStamp()))){	
			if(currentQuarterMonth == 2)
				taxRuleModel.setPenalty(pen25);	
			if(currentQuarterMonth == 3)
				taxRuleModel.setPenalty(pen50);	
			taxRuleModel.setPenaltyArrears((taxRuleModel.getTaxAmtArrears() * 50) / 100);
		}
		log.info(":::7::: Tax amt " + taxRuleModel.getTaxAmount() +"::Penalty:: "+taxRuleModel.getPenalty()+"::Penalty arrears::  "+taxRuleModel.getPenaltyArrears());
		if(taxRuleModel.getTaxAmount() <= 500)
			taxRuleModel.setServiceFee(10);
		if(taxRuleModel.getTaxAmount() > 500)
			taxRuleModel.setServiceFee(20);
		return taxRuleModel;
	}

	private TaxRuleModel monthCal(TaxRuleModel taxRuleModel){
		log.debug(" ::Tax arrears calculation:::: ");
		log.info(":::2::: Tax Valid upto ::" + taxRuleModel.getTaxValidUpto());
		int monthType = calculateMonthType(taxRuleModel.getTaxValidUpto());
		log.info(":::4::: Total revious quarter's months ::" + monthType);
		double taxAmtArrears =  ( taxRuleModel.getTaxAmount() / taxRuleModel.getMonthType() ) * monthType ;
		log.info(":::5::: Tax arrears ::" + taxAmtArrears);
		taxRuleModel.setTaxAmtArrears(taxAmtArrears);
		return taxRuleModel;
	}

	public int calculateMonthType( long validUpTo){
		int monthsBetweenTaxExpNdCurrDt = 0;
		Long currentDate = DateUtil.toCurrentUTCTimeStamp(); 
		if(validUpTo > 0 && (!DateUtil.isSameOrGreaterDate(validUpTo, currentDate))){
			int currentQuarterMonth = getQuarterCurrentMonth(currentDate);
			monthsBetweenTaxExpNdCurrDt = DateUtil.monthsBetween(validUpTo , currentDate);
			monthsBetweenTaxExpNdCurrDt = monthsBetweenTaxExpNdCurrDt - currentQuarterMonth; 
			if(monthsBetweenTaxExpNdCurrDt < 0)
				monthsBetweenTaxExpNdCurrDt = 0;
		}
		return monthsBetweenTaxExpNdCurrDt;
	}

	

	public TaxRuleModel getDiffTaxDetails(TaxRuleModel taxRuleModel){ 
		KieSession kSession = null;
		kSession = getTaxDetails(kSession , taxRuleModel);
		kSession.insert(taxRuleModel);
		kSession.fireAllRules();
		taxRuleModel = calEIBTNdPSVTAsOBTWhenPermitExp(taxRuleModel , kSession);
		double diffTax = 0.0d;
		int currentMonth = (4 - getQuarterCurrentMonth(DateUtil.toCurrentUTCTimeStamp()));
		if(taxRuleModel.getTaxType().equalsIgnoreCase(TaxType.QUARTERLY_TAX.getCode())){	
			if(taxRuleModel.getTaxAmount() > taxRuleModel.getOldTaxAmt())
			diffTax = (((taxRuleModel.getTaxAmount()/3)*currentMonth) - ((taxRuleModel.getOldTaxAmt() / 3) * (currentMonth - 1)));		
		}
		else{
			
			diffTax = taxRuleModel.getTaxAmount();
		}
		log.info("::Differential Amt::::: " + diffTax +"::Old Tax Amount Amt per quarter::::: " +taxRuleModel.getOldTaxAmt() + "::Newe  Tax Amount Amt per quarter::::: "+taxRuleModel.getTaxAmount());

		if(diffTax <= 0)
			taxRuleModel.setTaxAmount(0);
		else
			taxRuleModel.setTaxAmount(diffTax);
		return taxRuleModel;
 	} 

	
	public TaxRuleModel getDiffTaxForAlterationDetails(TaxRuleModel taxRuleModel){
		KieSession kSession = null;
		kSession = getTaxDetails(kSession , taxRuleModel);
		kSession.insert(taxRuleModel);
		kSession.fireAllRules();
		taxRuleModel = calEIBTNdPSVTAsOBTWhenPermitExp(taxRuleModel , kSession);
		if(taxRuleModel.getTaxType().equalsIgnoreCase(TaxType.QUARTERLY_TAX.getCode()) && taxRuleModel.getOldTaxType() != null 
				&& taxRuleModel.getOldTaxType().equalsIgnoreCase(TaxType.LIFE_TAX.getCode())){
			return taxRuleModel;
		}
		else if(taxRuleModel.getTaxType().equalsIgnoreCase(TaxType.LIFE_TAX.getCode()) && taxRuleModel.getOldTaxType() != null 
				&& taxRuleModel.getOldTaxType().equalsIgnoreCase(TaxType.QUARTERLY_TAX.getCode())){
			return taxRuleModel;

		}
		else{
			int currentMonth = (4 - getQuarterCurrentMonth(DateUtil.toCurrentUTCTimeStamp()));
			double diffTax = 0.0d;
			if(taxRuleModel.getTaxAmount() > taxRuleModel.getOldTaxAmt())
				diffTax = (((taxRuleModel.getTaxAmount()/3)*currentMonth) - ((taxRuleModel.getOldTaxAmt() / 3) * (currentMonth - 1)));
			if(diffTax <= 0)
				taxRuleModel.setTaxAmount(0);
			else
				taxRuleModel.setTaxAmount(diffTax);
		}
		return taxRuleModel;
	}
 	
 	private TaxRuleModel getGreenTaxFitnessNdRegistRenewal(TaxRuleModel taxRuleModel){
		
 		taxRuleModel.setTaxAmount(0);
		taxRuleModel.setTaxAmtArrears(0);
		taxRuleModel.setPenalty(0);
		taxRuleModel.setPenaltyArrears(0);
		taxRuleModel.setTotalTaxAmount(0);
		taxRuleModel.setTotalAmt(0);
 		KieSession kSession = null;
		kSession = KieSessions.getKieSessionForGreenTax(taxRuleModel);
		kSession.insert(taxRuleModel);
		kSession.fireAllRules();
		return taxRuleModel;
	}

 	private TaxRuleModel getVehicleAge(TaxRuleModel taxRuleModel)   
 	{   
 		int vehicleAge = 0;
 		if(taxRuleModel.getPrIssueDate() > 0){
				vehicleAge = DateUtil.getDiffInYear(taxRuleModel.getPrIssueDate());		
			}
 		if(taxRuleModel.getTaxType().equals(TaxType.LIFE_TAX.getCode()) && (taxRuleModel.getFuelType().equalsIgnoreCase(FuelType.ELECTRIC.getLabel()) || taxRuleModel.getFuelType().equalsIgnoreCase(FuelType.BATERY.getLabel()) || taxRuleModel.getFuelType().equalsIgnoreCase(FuelType.CNG.getLabel()) || taxRuleModel.getFuelType().equalsIgnoreCase(FuelType.SOLAR_ENERGY.getLabel())) ){
  			if(vehicleAge <= 5 )
 				taxRuleModel.setVehicleAge(5);		
 			else 
 				taxRuleModel.setVehicleAge(vehicleAge);	

 		}
 		
 		if(taxRuleModel.getTaxType().equals(TaxType.QUARTERLY_TAX.getCode()) && (taxRuleModel.getFuelType().equalsIgnoreCase(FuelType.ELECTRIC.getLabel()) || taxRuleModel.getFuelType().equalsIgnoreCase(FuelType.BATERY.getLabel()) || taxRuleModel.getFuelType().equalsIgnoreCase(FuelType.CNG.getLabel()) || taxRuleModel.getFuelType().equalsIgnoreCase(FuelType.SOLAR_ENERGY.getLabel()))){
  			if(vehicleAge <= 5 )
 			 taxRuleModel.setIsQuaterlyTaxExmpted4First5Year(true);;		
 		}
 		return taxRuleModel;
 	}
 	/**
 	 * For EIBT and PSVT if Permit is expired or no permit then tax is calculated as OBT tax.
 	 * e.g. If permit is valid for previous quarter and while paying tax permit is expired then 
 	 * previous quarter should be calculated as EIBT or PSV and current quarter will be 
 	 * calculated as OBT.  
 	 */
 	private TaxRuleModel calEIBTNdPSVTAsOBTWhenPermitExp(TaxRuleModel taxRuleModel , KieSession kSession)
 	{   
 		double remainingEIBTorPSVTTax=0;
 		List<HomeTaxEntity> homeTaxEntitys = null;
 		HomeTaxEntity homeTaxEntity = null;
 		String oldVehicleClassCatagory = taxRuleModel.getVehicleClassCategory();
 		int seat = taxRuleModel.getSeatingCapacity() - 1;
 		if(VehicleSubClass.EIBT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory())){
 			taxRuleModel.setVehicleClassCategory(VehicleSubClass.OBT);
 			homeTaxEntitys = homeTaxDAO.getHomeTax(taxRuleModel);
 			homeTaxEntity = homeTaxEntitys.get(0);
 			double obtTaxPerQuarter = homeTaxEntity.getTaxAmt() * seat;
 			boolean isCessFeeAsPerOBT = false;
 			if( taxRuleModel.getPermitValidTo() > 0 && DateUtil.isSameOrGreaterDate(taxRuleModel.getPermitValidTo(), taxRuleModel.getTaxValidUpto())){
 				if(DateUtil.isSameOrGreaterDate(taxRuleModel.getPermitValidTo(), DateUtil.toCurrentUTCTimeStamp())){    				
 					taxRuleModel = calculatePenaltyNdServiceFee(taxRuleModel);
 				}
 				else
 				{
 					isCessFeeAsPerOBT = true;
 					int eibtTaxableQarters = (int)Math.ceil((DateUtil.monthsBetween(taxRuleModel.getTaxValidUpto(), taxRuleModel.getPermitValidTo()) / 3.0));
 					if(eibtTaxableQarters > 0)
 						eibtTaxableQarters--;
 					int advancedPayingQuarters = (taxRuleModel.getMonthType() / 3) - 1;
 					if(advancedPayingQuarters < 0)
 						advancedPayingQuarters = 0;
 					int taxableQuartersUptoCurrentQuarter = (int)Math.ceil(DateUtil.monthsBetween(taxRuleModel.getTaxValidUpto(), DateUtil.toCurrentUTCTimeStamp())/3.0);
 					taxableQuartersUptoCurrentQuarter--;
 					remainingEIBTorPSVTTax = (taxRuleModel.getQuarterAmt()) * ((eibtTaxableQarters));  
 					int previousPermitExpiredQuaterMonth =(4 - getQuarterCurrentMonth(taxRuleModel.getPermitValidTo()));   					
 					double permitExpiredQuaterMonthTax = previousPermitExpiredQuaterMonth*((obtTaxPerQuarter)/3) + ((3 -previousPermitExpiredQuaterMonth)*(taxRuleModel.getQuarterAmt()/3));
 					int quarterAsOBTTaxable = taxableQuartersUptoCurrentQuarter - eibtTaxableQarters;  
 					//Checking permit is expired in current quarter or previous quarters
 					if(quarterAsOBTTaxable > 0)
							quarterAsOBTTaxable = quarterAsOBTTaxable - 1;
 					if(getCurrentQarter(taxRuleModel.getPermitValidTo()) == getCurrentQarter(DateUtil.toCurrentUTCTimeStamp())){
 						taxRuleModel.setTaxAmount(permitExpiredQuaterMonthTax + (obtTaxPerQuarter * advancedPayingQuarters));  							
 						taxRuleModel.setTaxAmtArrears((obtTaxPerQuarter)*quarterAsOBTTaxable + (remainingEIBTorPSVTTax * eibtTaxableQarters));
 						taxRuleModel.setQuarterAmt(permitExpiredQuaterMonthTax);
 					}
 					else {
 						taxRuleModel.setTaxAmount(obtTaxPerQuarter + (obtTaxPerQuarter * advancedPayingQuarters));  							
						taxRuleModel.setTaxAmtArrears((obtTaxPerQuarter)* (quarterAsOBTTaxable) + permitExpiredQuaterMonthTax + (remainingEIBTorPSVTTax * eibtTaxableQarters)); 
 						taxRuleModel.setQuarterAmt(homeTaxEntity.getTaxAmt() * seat);
 					}
 					//calculating penality	
 					int pen = 0; 					
 					int currentMonthType = getQuarterCurrentMonth(DateUtil.toCurrentUTCTimeStamp()); 
 					if(currentMonthType == 2){
 						pen= (int)(taxRuleModel.getQuarterAmt() * 25) / 100;
 					}
 					if(currentMonthType == 3){
 						pen= (int)(taxRuleModel.getQuarterAmt() * 50) / 100;
 					}					
 					taxRuleModel.setPenalty(pen); 
 					taxRuleModel.setPenaltyArrears((taxRuleModel.getTaxAmtArrears() * 50)/100); 
 					if(taxRuleModel.getTaxAmount() <= 500) 
 						taxRuleModel.setServiceFee(10);
 					if(taxRuleModel.getTaxAmount() > 500)
 						taxRuleModel.setServiceFee(20); 						
 				}
 			}
 			else{
 				isCessFeeAsPerOBT = true;
 				taxRuleModel.setTaxAmount((obtTaxPerQuarter / 3) * taxRuleModel.getMonthType());
 				taxRuleModel.setQuarterAmt(obtTaxPerQuarter);
 				taxRuleModel = monthCal(taxRuleModel);
 				taxRuleModel = calculatePenaltyNdServiceFee(taxRuleModel);
 			}
 			if(taxRuleModel.getIsCessFeeValid() && isCessFeeAsPerOBT){
 				taxRuleModel.setCessFee(0);
 				taxRuleModel.setQuarterAmt(obtTaxPerQuarter);
 				kSession = getTaxDetails(kSession , taxRuleModel);
 				kSession.insert(taxRuleModel);
 				kSession.fireAllRules();
 			}
 			taxRuleModel.setVehicleClassCategory(oldVehicleClassCatagory);
 		}

 		return taxRuleModel;
 	}
 	
 	/**
 	 * For EIBT and PSVT if Permit is expired or no permit then tax is calculated as OBT tax.
 	 * e.g. If permit is valid for previous quarter and while paying tax permit is expired then 
 	 * previous quarter should be calculated as EIBT or PSV and current quarter will be 
 	 * calculated as OBT.  
 	 */
 	private TaxRuleModel calEIBTNdPSVTAsOBTWhenPermitExpforVCR(TaxRuleModel taxRuleModel , KieSession kSession)
 	{   int advancedPayingQuarter = (taxRuleModel.getQuarterlyTaxType()/3) - 1;
 		List<HomeTaxEntity> homeTaxEntitys = null;
 		HomeTaxEntity homeTaxEntity = null;
 		Long currentDate = DateUtil.toCurrentUTCTimeStamp();
 		//Long currentDate = 1503792000L;
 		String oldVehicleClassCatagory = taxRuleModel.getVehicleClassCategory(); 		
 		if(taxRuleModel.getQuarterlyTaxType() == 0)
 			taxRuleModel.setMonthType(3);
 		else
 			taxRuleModel.setMonthType(taxRuleModel.getQuarterlyTaxType());
 		kSession = getTaxDetails(kSession , taxRuleModel);
 		kSession.insert(taxRuleModel);
 		kSession.fireAllRules();
 		double originalCOVQuarterAmt = taxRuleModel.getQuarterAmt();
 		double taxAmt = originalCOVQuarterAmt;
 		double taxArrears = 0.0d;
 		double penalty = 0.0d;
 		double penaltyArrears = 0.0d;
 		double vehiclePliedAsTaxAmt = 0.0d;
 		double taxPaidBetweenVCRDtNdTaxValidUpTo = 0.0d;
 		double obtTaxPerSeat = 0.0d;
 		if(!StringsUtil.isNullOrEmpty(taxRuleModel.getPliedAs())) {	
 			taxRuleModel.setVehicleClassCategory(taxRuleModel.getPliedAs());
 			String oldPermitSubType = taxRuleModel.getPermitSubType();
 			if(VehicleSubClass.SCRT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory())){
 				taxRuleModel.setPermitSubType(null);
 			}
 			homeTaxEntitys = homeTaxDAO.getHomeTax(taxRuleModel);
 			homeTaxEntity = homeTaxEntitys.get(0);
 			taxRuleModel.setTaxAmount(0);
 			taxRuleModel.setQuarterAmt(homeTaxEntity.getTaxAmt());;
 			kSession = getTaxDetails(kSession , taxRuleModel);
 			kSession.insert(taxRuleModel);
 			kSession.fireAllRules();
 			taxRuleModel.setVehicleClassCategory(oldVehicleClassCatagory);
 			taxRuleModel.setPermitSubType(oldPermitSubType);
 			vehiclePliedAsTaxAmt = taxRuleModel.getQuarterAmt();
 		}
 		taxRuleModel.setVehicleClassCategory(VehicleSubClass.OBT);
 		homeTaxEntitys = homeTaxDAO.getHomeTax(taxRuleModel);
 		homeTaxEntity = homeTaxEntitys.get(0);
 		obtTaxPerSeat = homeTaxEntity.getTaxAmt() * (taxRuleModel.getSeatingCapacity() - 1); 
 		taxRuleModel.setVehicleClassCategory(oldVehicleClassCatagory);
 		int months = DateUtil.monthsBetween(taxRuleModel.getTaxValidUpto() , currentDate);
 		months--;
 		if(months < 0)
 			months = 0;
 		int totalPrevTaxPayableQuarters = months / 3;	
 		log.info(":::1::: " + taxRuleModel.getTaxAmount() + " - " + taxRuleModel.getQuarterlyTaxType());
 		//Permit validity is greater than  tax validity
 		if( taxRuleModel.getPermitValidTo() > 0 && DateUtil.isGreaterDate(taxRuleModel.getPermitValidTo(), taxRuleModel.getTaxValidUpto())){ 		
 			//CASE 1: When CVR booked date  is greater than tax valid date.
 			if(DateUtil.isGreaterDate(taxRuleModel.getVcrBookedDt(), taxRuleModel.getTaxValidUpto())){
 				if(DateUtil.isGreaterDate(taxRuleModel.getPermitValidTo(), currentDate)){ 				
 					if(!StringsUtil.isNullOrEmpty(taxRuleModel.getPliedAs())) {	
 						if(taxRuleModel.getVehicleSiezed().equalsIgnoreCase("N"))
 						{   
 							if(getCurrentQarter(taxRuleModel.getVcrBookedDt()) == getCurrentQarter(currentDate)){	 					
 								taxAmt = vehiclePliedAsTaxAmt;
 							}
 							else{	 				
 								taxArrears = vehiclePliedAsTaxAmt;
 								penaltyArrears = taxArrears * 2;
 								totalPrevTaxPayableQuarters--;
 							}
 						}
 						else
 						{  
 							//Seized CASE: Calculating months and quarter.
 							int vcrBookedQuarterMonth = getQuarterCurrentMonth(taxRuleModel.getVcrBookedDt());
 							int currentQuarterMonth = getQuarterCurrentMonth(currentDate);
 							if(getCurrentQarter(taxRuleModel.getVcrBookedDt()) == getCurrentQarter(currentDate)){		
 								if(vcrBookedQuarterMonth== 1)
 									taxAmt = vehiclePliedAsTaxAmt;                
 								if(vcrBookedQuarterMonth == 2)
 									taxAmt = (vehiclePliedAsTaxAmt/3)*2; 				
 								if(vcrBookedQuarterMonth == 3)
 									taxAmt = (vehiclePliedAsTaxAmt/3); 		
 							}
 							else{
 								int monthsBetweenVCRNdCurrDt = DateUtil.monthsBetween(taxRuleModel.getVcrBookedDt() , currentDate);
 								//Calculating seized quarters, tax arrears  from VCR booked Quarter's date and current quarter's, when VCR Booked quarter date < current date quarter.
 								// VCR booked Quarter's months and current quarter's months is excluded and managed separately from seized quarters.
 								if(vcrBookedQuarterMonth== 1){
 									taxArrears = (vehiclePliedAsTaxAmt/3);
 									monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - 3;
 								}
 								if(vcrBookedQuarterMonth == 2){
 									taxArrears = (vehiclePliedAsTaxAmt/3)*2;
 									monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - 2;
 								}
 								if(vcrBookedQuarterMonth == 3){
 									taxArrears = vehiclePliedAsTaxAmt; 	
 									monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - 1;
 								}
 								monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - currentQuarterMonth;
 								penaltyArrears = taxArrears * 2;
 								totalPrevTaxPayableQuarters = totalPrevTaxPayableQuarters - (monthsBetweenVCRNdCurrDt/3); 		                
 							}					

 						}
 					}	
 					if(getQuarterCurrentMonth(currentDate) == 2)
 						penalty = taxAmt;
 					if(getQuarterCurrentMonth(currentDate) == 3)
 						penalty = taxAmt * 2; 


 					taxArrears = taxArrears +  originalCOVQuarterAmt * totalPrevTaxPayableQuarters;
 					penaltyArrears = penaltyArrears + (originalCOVQuarterAmt * totalPrevTaxPayableQuarters) * 2;
 				}
 				else
 				{ 	
 					taxAmt = obtTaxPerSeat;
 					int noOfMothsBtweenCurrDtNdPermitExpDt = DateUtil.monthsBetween(taxRuleModel.getPermitValidTo() , currentDate);
 					noOfMothsBtweenCurrDtNdPermitExpDt++;
 					int permitExpMonth = getQuarterCurrentMonth(taxRuleModel.getPermitValidTo()); 				
 					if(!StringsUtil.isNullOrEmpty(taxRuleModel.getPliedAs())) {	
 						if(taxRuleModel.getVehicleSiezed().equalsIgnoreCase("N"))
 						{   
 							if(getCurrentQarter(taxRuleModel.getVcrBookedDt()) == getCurrentQarter(currentDate)){	 					
 								taxAmt = vehiclePliedAsTaxAmt;
 								noOfMothsBtweenCurrDtNdPermitExpDt = noOfMothsBtweenCurrDtNdPermitExpDt - getQuarterCurrentMonth(currentDate); 
  									}
 							else{	 				
 								taxArrears = vehiclePliedAsTaxAmt;
 								penaltyArrears = taxArrears * 2;
 								totalPrevTaxPayableQuarters--;
 								noOfMothsBtweenCurrDtNdPermitExpDt = noOfMothsBtweenCurrDtNdPermitExpDt - (4 - permitExpMonth);
 							}
 						}
 						else
 						{  
 							//Seized CASE: Calculating months and quarter.
 							int vcrBookedQuarterMonth = getQuarterCurrentMonth(taxRuleModel.getVcrBookedDt());
 							int currentQuarterMonth = getQuarterCurrentMonth(currentDate);
 							if(getCurrentQarter(taxRuleModel.getVcrBookedDt()) == getCurrentQarter(currentDate)){		
 								if(vcrBookedQuarterMonth== 1)
 									taxAmt = vehiclePliedAsTaxAmt;                
 								if(vcrBookedQuarterMonth == 2)
 									taxAmt = (vehiclePliedAsTaxAmt/3)*2; 				
 								if(vcrBookedQuarterMonth == 3)
 									taxAmt = (vehiclePliedAsTaxAmt/3); 		
 							}
 							else{
 								int monthsBetweenVCRNdCurrDt = DateUtil.monthsBetween(taxRuleModel.getVcrBookedDt() , currentDate);
 								//Calculating seized quarters, tax arrears  from VCR booked Quarter's date and current quarter's, when VCR Booked quarter date < current date quarter.
 								// VCR booked Quarter's months and current quarter's months is excluded and managed separately from seized quarters.
 								monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - currentQuarterMonth;
 	 		 					if(vcrBookedQuarterMonth == 1)
 	 		 						taxAmt = vehiclePliedAsTaxAmt; 
 	 		                    if(vcrBookedQuarterMonth == 2)
 	 		 						taxAmt = (vehiclePliedAsTaxAmt / 3) * 2; 				
 	 		 					if(vcrBookedQuarterMonth == 3)
 	 		 						taxAmt = (vehiclePliedAsTaxAmt / 3); 
 								if(vcrBookedQuarterMonth== 1){
 									taxArrears = (vehiclePliedAsTaxAmt/3);
 									monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - 3;
 								}
 								if(vcrBookedQuarterMonth == 2){
 									taxArrears = (vehiclePliedAsTaxAmt/3)*2;
 									monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - 2;
 								}
 								if(vcrBookedQuarterMonth == 3){
 									taxArrears = vehiclePliedAsTaxAmt; 	
 									monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - 1;
 								}
 								monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - currentQuarterMonth;
 								penaltyArrears = taxArrears * 2;
 								totalPrevTaxPayableQuarters = totalPrevTaxPayableQuarters - (monthsBetweenVCRNdCurrDt/3); 		                
 							}					

 						}					
 						
 					}

 								        	    
 					if((getCurrentQarter(taxRuleModel.getVcrBookedDt()) == getCurrentQarter(currentDate))){
 						if(StringsUtil.isNullOrEmpty(taxRuleModel.getPliedAs()) && (getCurrentQarter(taxRuleModel.getPermitValidTo()) == getCurrentQarter(currentDate))) 					
 						taxAmt = taxAmt + (obtTaxPerSeat)* (4 - permitExpMonth) + ( originalCOVQuarterAmt)* (3- (4 - permitExpMonth));
 						noOfMothsBtweenCurrDtNdPermitExpDt = noOfMothsBtweenCurrDtNdPermitExpDt - getQuarterCurrentMonth(currentDate); 						
 					}
 					else
 					{   if(StringsUtil.isNullOrEmpty(taxRuleModel.getPliedAs()) && ((getCurrentQarter(taxRuleModel.getPermitValidTo()) == getCurrentQarter(taxRuleModel.getVcrBookedDt())))){
 						taxArrears = taxArrears + (obtTaxPerSeat / 3)* (4 - permitExpMonth) + ( originalCOVQuarterAmt / 3)* (3- (4 - permitExpMonth));
 						totalPrevTaxPayableQuarters --;
 						penaltyArrears = penaltyArrears + taxArrears * 2;
 						noOfMothsBtweenCurrDtNdPermitExpDt = noOfMothsBtweenCurrDtNdPermitExpDt - (4 - permitExpMonth);
 					    }
 						if(StringsUtil.isNullOrEmpty(taxRuleModel.getPliedAs()) && (getCurrentQarter(taxRuleModel.getPermitValidTo()) == getCurrentQarter(currentDate)))
 						{
 							taxAmt = (obtTaxPerSeat / 3)* (3 - permitExpMonth) + ( (originalCOVQuarterAmt / 3)* (3- (3 - permitExpMonth)));
 	 						noOfMothsBtweenCurrDtNdPermitExpDt = noOfMothsBtweenCurrDtNdPermitExpDt - getQuarterCurrentMonth(currentDate); 		
 						}
 						 						
 					}
 					totalPrevTaxPayableQuarters = totalPrevTaxPayableQuarters - (noOfMothsBtweenCurrDtNdPermitExpDt / 3);
 					if(totalPrevTaxPayableQuarters < 0)
 						totalPrevTaxPayableQuarters = 0;
 					taxArrears = taxArrears + obtTaxPerSeat * (noOfMothsBtweenCurrDtNdPermitExpDt / 3);
 					penaltyArrears = penaltyArrears + (obtTaxPerSeat * (noOfMothsBtweenCurrDtNdPermitExpDt / 3)) * 2;

 					if(getQuarterCurrentMonth(currentDate) == 2)
 						penalty = taxAmt;
 					if(getQuarterCurrentMonth(currentDate) == 3)
 						penalty = taxAmt * 2; 


 					taxArrears = taxArrears +  originalCOVQuarterAmt * totalPrevTaxPayableQuarters;
 					penaltyArrears = penaltyArrears + (originalCOVQuarterAmt * totalPrevTaxPayableQuarters) * 2;
 				}

 			}
 			else{
 				//CASE 2: When CVR booked date  is less than tax valid date.
 				if((taxRuleModel.getPliedAs() == null || taxRuleModel.getPliedAs().equals("")) && taxRuleModel.getVehicleSiezed().equalsIgnoreCase("N")){
 					if(DateUtil.isSameOrGreaterDate(taxRuleModel.getTaxValidUpto(), DateUtil.toCurrentUTCTimeStamp()))
 	 		 			 taxRuleModel.setTaxAmount(0);
 	 				else
 	 				taxRuleModel = calEIBTNdPSVTAsOBTWhenPermitExp(taxRuleModel , kSession);
 	 					 return taxRuleModel;
 				}
 				int payDtMonthPart = getQuarterCurrentMonth(currentDate);
 				int vcrMonthPart = getQuarterCurrentMonth(taxRuleModel.getVcrBookedDt());
 				if(DateUtil.isGreaterDate(currentDate, taxRuleModel.getTaxValidUpto())){
 					if((vehiclePliedAsTaxAmt - taxRuleModel.getOldTaxAmt()) > 0)
 						taxArrears = vehiclePliedAsTaxAmt - taxRuleModel.getOldTaxAmt(); 				
 					penaltyArrears = (taxArrears)*2; 
 					if(taxRuleModel.getVehicleSiezed().equalsIgnoreCase("Y")){
 						//Seized CASE: Calculating tax already paid from VCR date and tax valid up to date .
 						int monthsBetweenSiezedDtNdTaxValidUpTo = DateUtil.monthsBetween(taxRuleModel.getTaxValidUpto() , taxRuleModel.getVcrBookedDt());
 						monthsBetweenSiezedDtNdTaxValidUpTo--;
 						if(monthsBetweenSiezedDtNdTaxValidUpTo < 0)
 							monthsBetweenSiezedDtNdTaxValidUpTo = 0;
 						taxPaidBetweenVCRDtNdTaxValidUpTo = (taxRuleModel.getOldTaxAmt() / 3 ) * monthsBetweenSiezedDtNdTaxValidUpTo;
 						//During previous taxable quarters vehicle was seized
 						totalPrevTaxPayableQuarters = 0;
 					}
 					if(!DateUtil.isSameOrGreaterDate(taxRuleModel.getPermitValidTo(), currentDate)){
 						int noOfMothsBtweenCurrDtNdPermitExpDt = DateUtil.monthsBetween(taxRuleModel.getPermitValidTo() , currentDate);
 						int permitExpMonth = getQuarterCurrentMonth(taxRuleModel.getPermitValidTo()); 			        	    
 						if(!(getCurrentQarter(taxRuleModel.getPermitValidTo()) == getCurrentQarter(currentDate))){
 							taxArrears = taxArrears + (obtTaxPerSeat)* (4 - permitExpMonth) + ( originalCOVQuarterAmt)* (3- (4 - permitExpMonth));
 							totalPrevTaxPayableQuarters --;
 							noOfMothsBtweenCurrDtNdPermitExpDt = noOfMothsBtweenCurrDtNdPermitExpDt - (4 - permitExpMonth);
 							penaltyArrears = penaltyArrears + taxArrears * 2;
 						}
 						else
 						{   
 							taxAmt = taxAmt + (obtTaxPerSeat)* (4 - permitExpMonth) + ( originalCOVQuarterAmt)* (3- (4 - permitExpMonth));	
 							noOfMothsBtweenCurrDtNdPermitExpDt = noOfMothsBtweenCurrDtNdPermitExpDt - payDtMonthPart;
 						}
 						
 						totalPrevTaxPayableQuarters = totalPrevTaxPayableQuarters - (noOfMothsBtweenCurrDtNdPermitExpDt / 3);
 						if(totalPrevTaxPayableQuarters < 0)
 							totalPrevTaxPayableQuarters = 0;
 						taxArrears = taxArrears + obtTaxPerSeat * (noOfMothsBtweenCurrDtNdPermitExpDt / 3);
 						penaltyArrears = penaltyArrears + (obtTaxPerSeat * (noOfMothsBtweenCurrDtNdPermitExpDt / 3)) * 2;

 					}     	        

 					penaltyArrears = penaltyArrears + (originalCOVQuarterAmt * totalPrevTaxPayableQuarters) * 2;
 					taxArrears = taxArrears +  originalCOVQuarterAmt * totalPrevTaxPayableQuarters;
 					//Subtracting tax already paid from VCR date and tax valid up to from taxAmt and Tax Arrears when vehicle was seized.
 					if((taxAmt - taxPaidBetweenVCRDtNdTaxValidUpTo) < 0) {
 						taxArrears = taxArrears - (taxPaidBetweenVCRDtNdTaxValidUpTo - taxAmt);
 						taxAmt = 0;
 						penalty = 0;
 						if(taxArrears < 0){
 							taxArrears = 0;
 							penaltyArrears = 0;
 						} 		
 					}
 					else{
 						taxAmt = taxAmt - taxPaidBetweenVCRDtNdTaxValidUpTo;
 					} 					
 					if(!StringsUtil.isNullOrEmpty(taxRuleModel.getPliedAs())){
 						if(payDtMonthPart == 2){
 							penalty = taxAmt;  
 						}
 						if(payDtMonthPart == 3){ 						
 							penalty = taxAmt * 2; 
 						}
 					}
 					else{
 						if(payDtMonthPart == 2){
 							penalty = (taxAmt * 25) /100;  
 						}
 						if(payDtMonthPart == 3){ 						
 							penalty = (taxAmt * 25) /100;  
 						}
 					}

 				}else{
 					//Calculating differential tax when VCR is written and having tax validity.
 					if(getCurrentQarter(currentDate) == getCurrentQarter(taxRuleModel.getVcrBookedDt())){
 						taxAmt = vehiclePliedAsTaxAmt - taxRuleModel.getOldTaxAmt();
 						if(vcrMonthPart == 2){
 							penalty = taxAmt; 
 						}
 						if(vcrMonthPart == 1){ 						
 							penalty = taxAmt*2; 
 						}
 					}else{
 						//as a plied as for full qurater
 						taxArrears = vehiclePliedAsTaxAmt - taxRuleModel.getOldTaxAmt();
 						penaltyArrears = taxArrears*2;
 					} 

 				} 
 			}
 		}
 		else{ 			
 			//Permit validity is less than or equal to  tax validity
 			taxAmt = obtTaxPerSeat;
 			if(DateUtil.isGreaterDate(taxRuleModel.getVcrBookedDt(), taxRuleModel.getTaxValidUpto())){
 				if(!StringsUtil.isNullOrEmpty(taxRuleModel.getPliedAs())) {	
 					if(taxRuleModel.getVehicleSiezed().equalsIgnoreCase("N"))
 					{
 						if(getCurrentQarter(taxRuleModel.getVcrBookedDt()) == getCurrentQarter(currentDate)){	 					
 							taxAmt = vehiclePliedAsTaxAmt;
 						}
 						else{	 				
 							taxArrears = vehiclePliedAsTaxAmt;
 							penaltyArrears = taxArrears * 2;
 							totalPrevTaxPayableQuarters--;
 						}
 					}
 					else
 					{  
 						//Seized CASE: Calculating months and quarter.
 						int vcrBookedQuarterMonth = getQuarterCurrentMonth(taxRuleModel.getVcrBookedDt());
 						int currentQuarterMonth = getQuarterCurrentMonth(currentDate);
 						if(getCurrentQarter(taxRuleModel.getVcrBookedDt()) == getCurrentQarter(currentDate)){		
 							if(vcrBookedQuarterMonth== 1)
 								taxAmt = vehiclePliedAsTaxAmt;                
 							if(vcrBookedQuarterMonth == 2)
 								taxAmt = (vehiclePliedAsTaxAmt/3)*2; 				
 							if(vcrBookedQuarterMonth == 3)
 								taxAmt = (vehiclePliedAsTaxAmt/3); 		
 						}
 						else{
 							int monthsBetweenVCRNdCurrDt = DateUtil.monthsBetween(taxRuleModel.getVcrBookedDt() , currentDate);
 							//Calculating seized quarters, tax arrears  from VCR booked Quarter's date and current quarter's, when VCR Booked quarter date < current date quarter.
 							// VCR booked Quarter's months and current quarter's months is excluded and managed separately from seized quarters.
 							monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - currentQuarterMonth;
 		 					if(vcrBookedQuarterMonth == 1)
 		 						taxAmt = vehiclePliedAsTaxAmt; 
 		                    if(vcrBookedQuarterMonth == 2)
 		 						taxAmt = (vehiclePliedAsTaxAmt / 3) * 2; 				
 		 					if(vcrBookedQuarterMonth == 3)
 		 						taxAmt = (vehiclePliedAsTaxAmt / 3); 
 							if(vcrBookedQuarterMonth== 1){
 								taxArrears = (vehiclePliedAsTaxAmt/3);
 								monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - 3;
 							}
 							if(vcrBookedQuarterMonth == 2){
 								taxArrears = (vehiclePliedAsTaxAmt/3)*2;
 								monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - 2;
 							}
 							if(vcrBookedQuarterMonth == 3){
 								taxArrears = vehiclePliedAsTaxAmt; 	
 								monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - 1;
 							}
 							monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - currentQuarterMonth;
 							penaltyArrears = taxArrears * 2;
 							totalPrevTaxPayableQuarters = totalPrevTaxPayableQuarters - (monthsBetweenVCRNdCurrDt/3); 		                
 						}					

 					}
 				}	
 				if(getQuarterCurrentMonth(currentDate) == 2)
 					penalty = obtTaxPerSeat;
 				if(getQuarterCurrentMonth(currentDate) == 3)
 					penalty = obtTaxPerSeat * 2; 
 				taxArrears = taxArrears +  obtTaxPerSeat * totalPrevTaxPayableQuarters;
 				penaltyArrears = penaltyArrears + (obtTaxPerSeat * totalPrevTaxPayableQuarters) * 2;
 			}
 			else{
 				//CASE 2: When CVR booked date  is less than tax valid date.
 					if((taxRuleModel.getPliedAs() == null || taxRuleModel.getPliedAs().equals("")) && taxRuleModel.getVehicleSiezed().equalsIgnoreCase("N")){
 					if(DateUtil.isSameOrGreaterDate(taxRuleModel.getTaxValidUpto(), DateUtil.toCurrentUTCTimeStamp()))
 		 			 taxRuleModel.setTaxAmount(0);
 					else
 					 taxRuleModel = calEIBTNdPSVTAsOBTWhenPermitExp(taxRuleModel , kSession);
 					 return taxRuleModel;
 				}
 				int payDtMonthPart = getQuarterCurrentMonth(currentDate);
 				int vcrMonthPart = getQuarterCurrentMonth(taxRuleModel.getVcrBookedDt());
 				if(DateUtil.isGreaterDate(currentDate, taxRuleModel.getTaxValidUpto())){
 					if((vehiclePliedAsTaxAmt - taxRuleModel.getOldTaxAmt()) > 0)
 						taxArrears = vehiclePliedAsTaxAmt - taxRuleModel.getOldTaxAmt(); 				
 					penaltyArrears = (taxArrears)*2;  
 					if(taxRuleModel.getVehicleSiezed().equalsIgnoreCase("Y")){
 						//Seized CASE: Calculating tax already paid from VCR date and tax valid up to date .
 						int monthsBetweenSiezedDtNdTaxValidUpTo = DateUtil.monthsBetween(taxRuleModel.getTaxValidUpto() , taxRuleModel.getVcrBookedDt());
 						if(monthsBetweenSiezedDtNdTaxValidUpTo < 0)
 							monthsBetweenSiezedDtNdTaxValidUpTo = 0;
 						taxPaidBetweenVCRDtNdTaxValidUpTo = (taxRuleModel.getOldTaxAmt() / 3 ) * monthsBetweenSiezedDtNdTaxValidUpTo;
 						//During previous taxable quarters vehicle was seized
 						totalPrevTaxPayableQuarters = 0;
 			            if(payDtMonthPart == 2)
 	 						taxAmt = (taxAmt / 3) * 2; 				
 	 					if(payDtMonthPart == 3)
 	 						taxAmt = (taxAmt / 3); 
 					}

 					penaltyArrears = penaltyArrears + (obtTaxPerSeat * totalPrevTaxPayableQuarters) * 2;
 					taxArrears = taxArrears +  obtTaxPerSeat * totalPrevTaxPayableQuarters;
 					//Subtracting tax already paid from VCR date and tax valid up to from taxAmt and Tax Arrears when vehicle was seized.
 					if((taxAmt - taxPaidBetweenVCRDtNdTaxValidUpTo) < 0) {
 						taxArrears = taxArrears - (taxPaidBetweenVCRDtNdTaxValidUpTo - taxAmt);
 						taxAmt = 0;
 						penalty = 0;
 						if(taxArrears < 0){
 							taxArrears = 0;
 							penaltyArrears = 0;
 						} 		
 					}
 					else{
 						taxAmt = taxAmt - taxPaidBetweenVCRDtNdTaxValidUpTo;
 					} 					
 					if(!StringsUtil.isNullOrEmpty(taxRuleModel.getPliedAs())){
 						if(payDtMonthPart == 2){
 							penalty = taxAmt;  
 						}
 						if(payDtMonthPart == 3){ 						
 							penalty = taxAmt * 2; 
 						}
 					}
 					else{
 						if(payDtMonthPart == 2){
 							penalty = (taxAmt * 25) /100;  
 						}
 						if(payDtMonthPart == 3){ 						
 							penalty = (taxAmt * 25) /100;  
 						}
 					}

 				}else{
 					//Calculating differential tax when VCR is written and having tax validity.
 					if(getCurrentQarter(currentDate) == getCurrentQarter(taxRuleModel.getVcrBookedDt())){
 						taxAmt = vehiclePliedAsTaxAmt - taxRuleModel.getOldTaxAmt();
 						if(vcrMonthPart == 2){
 							penalty = taxAmt; 
 						}
 						if(vcrMonthPart == 1){ 						
 							penalty = taxAmt*2; 
 						}
 					}else{
 						//as a plied as for full qurater
 						taxArrears = vehiclePliedAsTaxAmt - taxRuleModel.getOldTaxAmt();
 						penaltyArrears = taxArrears*2;
 					} 

 				} 
 				taxAmt = taxAmt + (obtTaxPerSeat * advancedPayingQuarter) ; 	
 	 			taxArrears = taxArrears +  obtTaxPerSeat * totalPrevTaxPayableQuarters; 
 			}
 						
 		}
 		////////////////////////////////////////////
 		if(advancedPayingQuarter < 0)
 			advancedPayingQuarter = 0;
 		if(totalPrevTaxPayableQuarters < 0)
 			totalPrevTaxPayableQuarters = 0;
 		taxRuleModel.setTaxAmount(taxAmt);
 		taxRuleModel.setPenalty(penalty);
 		taxRuleModel.setTaxAmtArrears(taxArrears);
 		taxRuleModel.setPenaltyArrears(penaltyArrears);
 		//setting pay tax service fee
 		if(taxRuleModel.getTaxAmount() <= 500)
			taxRuleModel.setServiceFee(10);
		if(taxRuleModel.getTaxAmount() > 500)
			taxRuleModel.setServiceFee(20);
 	
 	return taxRuleModel;

 	}
 			
 	public FeeRuleModel getFeeAmountForCitizen(FeeRuleModel feeRuleModel) {
		log.info(":::::::getFeeAmountForCitizen::drools::start:::");
		KieSession kSession = null;
		if(feeRuleModel.getPermitType() == null)
			feeRuleModel.setPermitType("");
		switch (RegistrationCategoryType.getRegistrationCategoryType(feeRuleModel.getRegCategory())) {
		case NON_TRANSPORT:
			kSession = getNONTransportKieSession(feeRuleModel);
			kSession.insert(feeRuleModel);
			kSession.fireAllRules();
			break;
		case TRANSPORT:
			kSession = getTransportKieSession(feeRuleModel);
			kSession.insert(feeRuleModel);
			kSession.fireAllRules();
			break;
		}
		
		switch (ServiceType.getServiceType(feeRuleModel.getServiceCode())) {
        case VEHICLE_ATLERATION:
        	feeRuleModel = getFeeForAlteration(feeRuleModel);
            break;
        case OWNERSHIP_TRANSFER_SALE:
        case OWNERSHIP_TRANSFER_DEATH:	
        	if(feeRuleModel.getPermitTransferNdSurrender() != null)
            feeRuleModel = getFeeForPermitSurrenderNdTransfer(feeRuleModel);
            break;    
		}
		return feeRuleModel;
	}
 	
 	public FeeRuleModel getFeeForPermitSurrenderNdTransfer(FeeRuleModel feeRuleModel){
 		
 		String serviceCode = "";
 		serviceCode = feeRuleModel.getServiceCode();
 		double  serviceCharges = 0.0d;
 		if(feeRuleModel.getPermitTransferNdSurrender().equalsIgnoreCase("transfer")){
 			feeRuleModel.setServiceCode(ServiceType.PERMIT_TRANSFER.getCode());
 			feeRuleModel = getFeeAmount(feeRuleModel);
 			if(feeRuleModel.getPrService() > feeRuleModel.getPermitService())
 				serviceCharges = feeRuleModel.getPrService();
 			else
 				serviceCharges = feeRuleModel.getPermitService();
 			feeRuleModel.setPrService(serviceCharges);
 			feeRuleModel.setTotalPrFee(feeRuleModel.getPrFee() + feeRuleModel.getPermitFee() + serviceCharges);
 			feeRuleModel.setServiceCode(serviceCode);
 		}
 		if(feeRuleModel.getPermitTransferNdSurrender().equalsIgnoreCase("surrender")){
 			feeRuleModel.setServiceCode(ServiceType.PERMIT_SURRENDER.getCode());
 			feeRuleModel = getFeeAmount(feeRuleModel);
 			if(feeRuleModel.getPrService() > feeRuleModel.getPermitService())
 				serviceCharges = feeRuleModel.getPrService();
 			else
 				serviceCharges = feeRuleModel.getPermitService();
 			feeRuleModel.setPrService(serviceCharges);
 			feeRuleModel.setTotalPrFee(feeRuleModel.getPrFee() + feeRuleModel.getPermitFee() + serviceCharges);
 			feeRuleModel.setServiceCode(serviceCode);
 		}
 		return feeRuleModel;
 	}
 	
 	
 	public FeeRuleModel getFeeForAlteration(FeeRuleModel feeRuleModel){
 		
 		
 		double alterationApplicationFee = 0.0d , alterationServiceCharges = 0.0d , 
 				reassignApplicationFee = 0.0d , reassignServiceCharges = 0.0d , totalApplicationFee = 0.0d , serviceCharges = 0.0d;
 		if(feeRuleModel.getAltrationFlag()){
 			
 			String cov = feeRuleModel.getVehicleClassCategory();
 			int oldRegType = feeRuleModel.getRegCategory();
 			feeRuleModel.setRegCategory(feeRuleModel.getOldRegCategory());
 			feeRuleModel.setVehicleClassCategory(feeRuleModel.getOldClassOfVehicle());
 			feeRuleModel.setPrFee(0);
	 		feeRuleModel.setPrService(0);
 			feeRuleModel = getFeeAmount(feeRuleModel); /*getting Fee For Alteration*/
 			feeRuleModel.setVehicleClassCategory(cov);
 			feeRuleModel.setRegCategory(oldRegType);
 			alterationApplicationFee = feeRuleModel.getPrFee();
 			alterationServiceCharges = feeRuleModel.getPrService();
 			
 			if(feeRuleModel.getReassinmentFlag()){
 				feeRuleModel.setPrFee(0);
		 		feeRuleModel.setPrService(0);
		 		feeRuleModel.setServiceCode(ServiceType.VEHICLE_REASSIGNMENT.getCode());
		 		feeRuleModel = getFeeAmount(feeRuleModel);
		 		reassignApplicationFee = feeRuleModel.getPrFee();
		 		reassignServiceCharges = feeRuleModel.getPrService();
		 	}
 			
 			if(alterationServiceCharges > reassignServiceCharges)
	 			serviceCharges = alterationServiceCharges ;
	 		else
	 			serviceCharges = reassignServiceCharges ;
 			
 			if(feeRuleModel.getCardFeeFlag()){
 				feeRuleModel.setServiceCode(ServiceType.FRESH_REGISTRATION.getCode());
 				feeRuleModel = getFeeAmount(feeRuleModel);
 				feeRuleModel.setFitnessFee(0);
 				feeRuleModel.setFitnessService(0);
 				feeRuleModel.setTotalFitnessFee(0);
 			}
 			
 			if(feeRuleModel.getFitnessExpireFlag()){
		 		feeRuleModel.setServiceCode(ServiceType.FC_FRESH.getCode());
		 		feeRuleModel = getFeeAmount(feeRuleModel);
		 	}
 			
 			if(feeRuleModel.getHsrpFlag())
 				feeRuleModel.setHsrpAmount(0);
 			
 			feeRuleModel.setServiceCode(ServiceType.VEHICLE_ATLERATION.getCode());
		 	feeRuleModel.setPrFee(reassignApplicationFee + alterationApplicationFee);
	 		feeRuleModel.setPrService(serviceCharges);
	 		feeRuleModel.setTotalPrFee(feeRuleModel.getPrFee() + feeRuleModel.getPrService());
 		
	 		
 	}
 		return feeRuleModel;
 	}
 	
 	private int getCurrentQarter(Long date){
 		int quarter = 0;       
 		if(date==null){   
 			date=DateUtil.toCurrentUTCTimeStamp();     
 		}        
 		switch (MonthType.getMonthType(DateUtil.getMonth(date))) {
 		case JANUARY:  
 		case FEBRUARY: 
 		case MARCH:
 			quarter = 4;    
 			break;     
 		case APRIL: 
 		case MAY: 
 		case JUNE:
 			quarter = 1;      
 			break;     
 		case JULY:
 		case AUGUST:
 		case SEPTEMBER:
 			quarter = 2;     
 			break;   
 		case OCTOBER:
 		case NOVEMBER:
 		case DECEMBER:
 			quarter = 3;     
 			break;    		
 		}      
 		return quarter;   
 	}


 	private int getQuarterCurrentMonth(Long date) {  
 		int quartltyPart = 0;       
 		if(date==null){   
 			date=DateUtil.toCurrentUTCTimeStamp();     
 		}        
 		switch (MonthType.getMonthType(DateUtil.getMonth(date))) {
 		case JANUARY:    
 		case APRIL:
 		case JULY: 
 		case OCTOBER:
 			quartltyPart = 1;    
 			break;     
 		case FEBRUARY:  
 		case MAY: 
 		case AUGUST: 
 		case NOVEMBER:
 			quartltyPart = 2;           
 			break;       
 		case MARCH:   
 		case JUNE: 
 		case SEPTEMBER: 
 		case DECEMBER:
 			quartltyPart = 3;      
 			break;     

 		}      
 		return quartltyPart;   
 	}

 	/**
 	 * Calculating tax and penalty when VCR is written against COV.
 	 * Two important case:
 	 * CASE 1: When CVR booked date  is greater than tax valid date.
 	 * CASE 2: When CVR booked date  is less than tax valid date.
 	 * In CASE 2 tax is already paid, hence tax adjustment is done in tax amount
 	 * and tax arrears(if advanced tax paid is greater than tax amount).
 	 */
 	public TaxRuleModel getPayTaxForVCR(TaxRuleModel taxRuleModel){
 		int advancedPayingQuarter = (taxRuleModel.getQuarterlyTaxType()/3) - 1;
 		List<HomeTaxEntity> homeTaxEntitys = null;
 		HomeTaxEntity homeTaxEntity = null;
 		Long currentDate = DateUtil.toCurrentUTCTimeStamp();
 		//Long currentDate = 1504051200L;
 		String oldVehicleClassCatagory = taxRuleModel.getVehicleClassCategory(); 		
 		KieSession kSession = null;
 		if(taxRuleModel.getQuarterlyTaxType() == 0)
 			taxRuleModel.setMonthType(3);
 		else
 			taxRuleModel.setMonthType(taxRuleModel.getQuarterlyTaxType());
 		kSession = getTaxDetails(kSession , taxRuleModel);
 		kSession.insert(taxRuleModel);
 		kSession.fireAllRules();
 		
 		if(VehicleSubClass.EIBT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory())){
 			taxRuleModel = calEIBTNdPSVTAsOBTWhenPermitExpforVCR(taxRuleModel , kSession);
 			return taxRuleModel;
 		}
 		double originalCOVQuarterAmt = taxRuleModel.getQuarterAmt();
 		double taxAmt = originalCOVQuarterAmt;
 		double taxArrears = 0.0d;
 		double penalty = 0.0d;
 		double penaltyArrears = 0.0d;
 		double vehiclePliedAsTaxAmt = 0.0d;
 		double taxPaidBetweenVCRDtNdTaxValidUpTo = 0.0d;
 		if(!StringsUtil.isNullOrEmpty(taxRuleModel.getPliedAs())) {	
 			taxRuleModel.setVehicleClassCategory(taxRuleModel.getPliedAs());
 			String oldPermitSubType = taxRuleModel.getPermitSubType();
 			if(VehicleSubClass.SCRT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory())){
 				taxRuleModel.setPermitSubType(null);
 			}
 			homeTaxEntitys = homeTaxDAO.getHomeTax(taxRuleModel);
 			homeTaxEntity = homeTaxEntitys.get(0);
 			taxRuleModel.setTaxAmount(0);
 			taxRuleModel.setQuarterAmt(homeTaxEntity.getTaxAmt());;
 			kSession = getTaxDetails(kSession , taxRuleModel);
 			kSession.insert(taxRuleModel);
 			kSession.fireAllRules();
 			taxRuleModel.setVehicleClassCategory(oldVehicleClassCatagory);
 			taxRuleModel.setPermitSubType(oldPermitSubType);
 			vehiclePliedAsTaxAmt = taxRuleModel.getQuarterAmt();
 		}
 		int months = DateUtil.monthsBetween(taxRuleModel.getTaxValidUpto() , currentDate);
 		months--;
 		if( months < 0)
 			months = 0;
 		int totalPrevTaxPayableQuarters = months / 3;	
 		log.info(":::1::: " + taxRuleModel.getTaxAmount() + " - " + taxRuleModel.getQuarterlyTaxType());
 		//CASE 1: When CVR booked date  is greater than tax valid date.
 		if(DateUtil.isGreaterDate(taxRuleModel.getVcrBookedDt(), taxRuleModel.getTaxValidUpto())){
 			double effectiveAmt = 0.0d;
 			if(!StringsUtil.isNullOrEmpty(taxRuleModel.getPliedAs())) {	
 				effectiveAmt = vehiclePliedAsTaxAmt;
 			}
 			else
 				effectiveAmt = originalCOVQuarterAmt;
 			if(taxRuleModel.getVehicleSiezed().equalsIgnoreCase("N"))
 			{
 				if(getCurrentQarter(taxRuleModel.getVcrBookedDt()) == getCurrentQarter(currentDate)){	 					
 					taxAmt = effectiveAmt;
 				}
 				else{	 				
 					taxArrears = effectiveAmt;
 					penaltyArrears = taxArrears * 2;
 					totalPrevTaxPayableQuarters--;
 				}
 			}
 			else
 			{  
 				//Seized CASE: Calculating months and quarter.
 				int vcrBookedQuarterMonth = getQuarterCurrentMonth(taxRuleModel.getVcrBookedDt());
 				int currentQuarterMonth = getQuarterCurrentMonth(currentDate);
 				if(getCurrentQarter(taxRuleModel.getVcrBookedDt()) == getCurrentQarter(currentDate)){		
 					if(vcrBookedQuarterMonth == 1)
 						taxAmt = effectiveAmt;                
 					if(vcrBookedQuarterMonth == 2)
 						taxAmt = (effectiveAmt / 3) * 2; 				
 					if(vcrBookedQuarterMonth == 3)
 						taxAmt = (effectiveAmt / 3); 		
 				}
 				else{
 					int monthsBetweenVCRNdCurrDt = DateUtil.monthsBetween(taxRuleModel.getVcrBookedDt() , currentDate);
 					monthsBetweenVCRNdCurrDt++;
 					//Calculating seized quarters, tax arrears  from VCR booked Quarter's date and current quarter's, when VCR Booked quarter date < current date quarter.
 					// VCR booked Quarter's months and current quarter's months is excluded and managed separately from seized quarters.
 					monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - currentQuarterMonth;
 					if(currentQuarterMonth == 1)
 						taxAmt = effectiveAmt; 
                    if(currentQuarterMonth == 2)
 						taxAmt = (effectiveAmt / 3) * 2; 				
 					if(currentQuarterMonth == 3)
 						taxAmt = (effectiveAmt / 3); 
 					
 					if(vcrBookedQuarterMonth == 1){
 						taxArrears = (effectiveAmt / 3);
 						monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - 3;
 					}
 					if(vcrBookedQuarterMonth == 2){
 						taxArrears = (effectiveAmt / 3) * 2;
 						monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - 2;
 					}
 					if(vcrBookedQuarterMonth == 3){
 						taxArrears = effectiveAmt; 	
 						monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - 1;
 					}
 					//monthsBetweenVCRNdCurrDt = monthsBetweenVCRNdCurrDt - currentQuarterMonth;
 					penaltyArrears = taxArrears * 2;
 					if(totalPrevTaxPayableQuarters > 0)
 						totalPrevTaxPayableQuarters--;
 					if(monthsBetweenVCRNdCurrDt < 0)
 						monthsBetweenVCRNdCurrDt = 0;
 					totalPrevTaxPayableQuarters = totalPrevTaxPayableQuarters - (monthsBetweenVCRNdCurrDt / 3); 		                
 				}					

 			}

 			if(getQuarterCurrentMonth(currentDate) == 2)
 				penalty = taxAmt;
 			if(getQuarterCurrentMonth(currentDate) == 3)
 				penalty = taxAmt * 2; 
 			taxArrears = taxArrears +  originalCOVQuarterAmt * totalPrevTaxPayableQuarters;
 			penaltyArrears = penaltyArrears + (originalCOVQuarterAmt * totalPrevTaxPayableQuarters) * 2;
 		}
 		else{
 			//CASE 2: When CVR booked date  is less than tax valid date.
 			int bookedDtQuraterMonthPart = 0;
 			if((taxRuleModel.getPliedAs() == null || taxRuleModel.getPliedAs().equals("")) && taxRuleModel.getVehicleSiezed().equalsIgnoreCase("N")){
 				taxRuleModel = getPayTax(taxRuleModel);	
 				return taxRuleModel;
 			}
 			int payDtMonthPart = getQuarterCurrentMonth(currentDate);
 			int vcrMonthPart = getQuarterCurrentMonth(taxRuleModel.getVcrBookedDt());
 			if(DateUtil.isGreaterDate(currentDate, taxRuleModel.getTaxValidUpto())){
 				//Calculating differential tax, tax paid during vehicle seized  when VCR is written and having tax validity and tax pay date is greater than tax valid date.
 				if(!StringsUtil.isNullOrEmpty(taxRuleModel.getPliedAs())){ 
 					if((vehiclePliedAsTaxAmt - taxRuleModel.getOldTaxAmt()) > 0)
 						taxArrears = vehiclePliedAsTaxAmt - taxRuleModel.getOldTaxAmt(); 							
 						penaltyArrears = (taxArrears) * 2; 
 					
 				}
 				if(taxRuleModel.getVehicleSiezed().equalsIgnoreCase("Y")){
 					//Seized CASE: Calculating tax already paid from VCR date and tax valid up to date . 					
 					int monthsBetweenSiezedDtNdTaxValidUpTo = DateUtil.monthsBetween(taxRuleModel.getVcrBookedDt() , taxRuleModel.getTaxValidUpto());
 					if(monthsBetweenSiezedDtNdTaxValidUpTo < 0)
 						monthsBetweenSiezedDtNdTaxValidUpTo = 0;
 					taxPaidBetweenVCRDtNdTaxValidUpTo = (taxRuleModel.getOldTaxAmt() / 3 ) * monthsBetweenSiezedDtNdTaxValidUpTo;
 					//During previous taxable quarters vehicle was seized
 					totalPrevTaxPayableQuarters = 0;
 			        if(payDtMonthPart == 2)
 						taxAmt = (taxAmt / 3) * 2; 				
 					if(payDtMonthPart == 3)
 						taxAmt = (taxAmt / 3); 
 				}
 				
 				penaltyArrears = penaltyArrears + (originalCOVQuarterAmt * totalPrevTaxPayableQuarters) * 2;
 				taxArrears = taxArrears +  originalCOVQuarterAmt * totalPrevTaxPayableQuarters;
 		 		//Subtracting tax already paid from VCR date and tax valid up to from taxAmt and Tax Arrears when vehicle was seized.
 		 		if((taxAmt - taxPaidBetweenVCRDtNdTaxValidUpTo) < 0) {
 		 			taxArrears = taxArrears - (taxPaidBetweenVCRDtNdTaxValidUpTo - taxAmt);
 		 			taxAmt = 0;
 		 			penalty = 0;
 		 			if(taxArrears < 0){
 		 				taxArrears = 0;
 		 				penaltyArrears = 0;
 		 			} 		
 		 		}
 		 		else{
 		 			taxAmt = taxAmt - taxPaidBetweenVCRDtNdTaxValidUpTo;
 		 		}
 				if(!StringsUtil.isNullOrEmpty(taxRuleModel.getPliedAs())){
 				if(payDtMonthPart == 2){
 					penalty = taxAmt;  
 				}
 				if(payDtMonthPart == 3){ 						
 					penalty = taxAmt * 2; 
 				}
 				}
 				else{
 					if(payDtMonthPart == 2){
 	 					penalty = (taxAmt * 25) /100;  
 	 				}
 	 				if(payDtMonthPart == 3){ 						
 	 					penalty = (taxAmt * 25) /100;  
 	 				}
 				}
 		

 			}else{
 				//Calculating differential tax when VCR is written and having tax validity and tax pay date is less than tax valid date.
 				if(getCurrentQarter(currentDate) == getCurrentQarter(taxRuleModel.getVcrBookedDt())){
 					taxAmt = vehiclePliedAsTaxAmt - taxRuleModel.getOldTaxAmt();
 					if(vcrMonthPart == 2){
 						penalty = taxAmt; 
 					}
 					if(vcrMonthPart == 3){ 						
 						penalty = taxAmt * 2; 
 					}
 				}else{
 					//Plied as for full quarter
 					taxArrears = vehiclePliedAsTaxAmt - taxRuleModel.getOldTaxAmt();
 					penaltyArrears = taxArrears * 2;
 				} 
 			} 
 		}
 		if(advancedPayingQuarter < 0)
 			advancedPayingQuarter = 0;
 		if(totalPrevTaxPayableQuarters < 0)
 			totalPrevTaxPayableQuarters = 0;
 		taxAmt = taxAmt + (originalCOVQuarterAmt * advancedPayingQuarter) ; 	
 		taxRuleModel.setTaxAmount(taxAmt);
 		taxRuleModel.setPenalty(penalty);
 		taxRuleModel.setTaxAmtArrears(taxArrears);
 		taxRuleModel.setPenaltyArrears(penaltyArrears);
 		//setting pay tax service fee
 		if(taxRuleModel.getTaxAmount() <= 500)
			taxRuleModel.setServiceFee(10);
		if(taxRuleModel.getTaxAmount() > 500)
			taxRuleModel.setServiceFee(20);
 		return taxRuleModel;
 	}
 	
 	private TaxRuleModel calcOldTaxIfTrIssueDateIsInSameQuarter(TaxRuleModel taxRuleModel)
 	{
 		int noOfMonths = DateUtil.monthsBetween(taxRuleModel.getTrIssueDate(), DateUtil.toCurrentUTCTimeStamp()); 	   
 		if(noOfMonths <= 2 &&  getCurrentQarter(DateUtil.toCurrentUTCTimeStamp()) == getCurrentQarter(taxRuleModel.getTrIssueDate())){
 			int trIssueQuarterMonth = getQuarterCurrentMonth(taxRuleModel.getTrIssueDate());
 			if(trIssueQuarterMonth == 2)
 				taxRuleModel.setOldTaxAmt((taxRuleModel.getOldTaxAmt() / 2) * 3);
 			if(trIssueQuarterMonth == 3)
 				taxRuleModel.setOldTaxAmt(taxRuleModel.getOldTaxAmt() * 3);
 			taxRuleModel.setMonthType(3);
 			log.info(":::::::Inside calcOldTaxIfTrIssueDateIsInSameQuarter ::: Old tax converted to one quarter= "+ taxRuleModel.getOldTaxAmt());
 			
 		}
 		return taxRuleModel;
 	}

}