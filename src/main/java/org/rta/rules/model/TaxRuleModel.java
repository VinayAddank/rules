package org.rta.rules.model;

public class TaxRuleModel {

	private int regCategory;
	private String taxType;
	private double tax;
	private String ownerType;
	private boolean isPHCertificate;
	private boolean isPHDriverLicense;
	private String vehicleClass;
	private double invoiceAmount;
	private int noOfVehicle;
	private String fuelType;
	private double taxAmount;
	private int monthType;
	private String vehicleClassCategory ;
	private int seatingCapacity;
	private int gvw;
	private int ulw;
	private double hsrpAmount;
	private boolean isDisabled;
	private boolean isInvalidCarriage;
	private String permitType ;
	private String permitSubType;
	private double cessFee;
	private double hpaFee;
	private boolean iSuzo;
	private double totalAmt;
	private String serviceCode;
	private boolean greenTax;
	private double greenTaxAmt;
	private boolean isPermitValid;
	private String stateCode;
	private int vehicleAge;
	private boolean taxExempted;
	private double quarterAmt;
	private int incRLW ;
	private double incAmt ;
	private String weightType;
	private int fromRLW;
	private int fromULW;
	private boolean isCessFeeValid;
	private int quarterlyTaxType;
	private long taxValidUpto;
	private double penalty;
	private double serviceFee;
	private double taxAmtArrears;
	private double oldTaxAmt;
	private double penaltyArrears;
	private long prIssueDate;
	private boolean isQuaterlyTaxExmpted4First5Year;
	private double totalTaxAmount;
	private long permitValidTo;
	private int km;
	private String areaRoute;
	private String oldTaxType;
	private boolean vcrFlag;
	private String pliedAs;
	private long vcrBookedDt;
	private String vehicleSiezed;
	private Integer periodicTaxType;
	private long trIssueDate;
	
	public int getRegCategory() {
		return regCategory;
	}
	public void setRegCategory(int regCategory) {
		this.regCategory = regCategory;
	}
	public String getTaxType() {
		return taxType;
	}
	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}
	public double getTax() {
		return tax;
	}
	public void setTax(double tax) {
		this.tax = tax;
	}
	public String getOwnerType() {
		return ownerType;
	}
	public void setOwnerType(String ownerType) {
		this.ownerType = ownerType;
	}
	public boolean getIsPHCertificate() {
		return isPHCertificate;
	}
	public void setIsPHCertificate(boolean isPHCertificate) {
		this.isPHCertificate = isPHCertificate;
	}
	public boolean getIsPHDriverLicense() {
		return isPHDriverLicense;
	}
	public void setIsPHDriverLicense(boolean isPHDriverLicense) {
		this.isPHDriverLicense = isPHDriverLicense;
	}
	public String getVehicleClass() {
		return vehicleClass;
	}
	public void setVehicleClass(String vehicleClass) {
		this.vehicleClass = vehicleClass;
	}
	public double getInvoiceAmount() {
		return invoiceAmount;
	}
	public void setInvoiceAmount(double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
	}
	
	public String getFuelType() {
		return fuelType;
	}
	public void setFuelType(String fuelType) {
		this.fuelType = fuelType;
	}
	public double getTaxAmount() {
		return taxAmount;
	}
	public void setTaxAmount(double taxAmount) {
		this.taxAmount = taxAmount;
	}
	public int getMonthType() {
		return monthType;
	}
	public void setMonthType(int monthType) {
		this.monthType = monthType;
	}
	public String getVehicleClassCategory() {
		return vehicleClassCategory;
	}
	public void setVehicleClassCategory(String vehicleClassCategory) {
		this.vehicleClassCategory = vehicleClassCategory;
	}
	public int getSeatingCapacity() {
		return seatingCapacity;
	}
	public void setSeatingCapacity(int seatingCapacity) {
		this.seatingCapacity = seatingCapacity;
	}
	public long getGvw() {
		return gvw;
	}
	public long getUlw() {
		return ulw;
	}
	public boolean getIsDisabled() {
		return isDisabled;
	}
	public void setIsDisabled(boolean isDisabled) {
		this.isDisabled = isDisabled;
	}
	public boolean getIsInvalidCarriage() {
		return isInvalidCarriage;
	}
	public void setIsInvalidCarriage(boolean isInvalidCarriage) {
		this.isInvalidCarriage = isInvalidCarriage;
	}
	public String getServiceCode() {
		return serviceCode;
	}
	public void setServiceCode(String serviceCode) {
		this.serviceCode = serviceCode;
	}
	public double getCessFee() {
		return cessFee;
	}
	public void setCessFee(double cessFee) {
		this.cessFee = cessFee;
	}
	public double getHpaFee() {
		return hpaFee;
	}
	public void setHpaFee(double hpaFee) {
		this.hpaFee = hpaFee;
	}
	public boolean getISuzo() {
		return iSuzo;
	}
	public void setISuzo(boolean iSuzo) {
		this.iSuzo = iSuzo;
	}
	public double getTotalAmt() {
		return totalAmt;
	}
	public void setTotalAmt(double totalAmt) {
		this.totalAmt = totalAmt;
	}
	public double getHsrpAmount() {
		return hsrpAmount;
	}
	public void setHsrpAmount(double hsrpAmount) {
		this.hsrpAmount = hsrpAmount;
	}
	public String getPermitType() {
		return permitType;
	}
	public void setPermitType(String permitType) {
		this.permitType = permitType;
	}
	public boolean isGreenTax() {
		return greenTax;
	}
	public void setGreenTax(boolean greenTax) {
		this.greenTax = greenTax;
	}
	public double getGreenTaxAmt() {
		return greenTaxAmt;
	}
	public void setGreenTaxAmt(double greenTaxAmt) {
		this.greenTaxAmt = greenTaxAmt;
	}
	public void setGvw(int gvw) {
		this.gvw = gvw;
	}
	public void setUlw(int ulw) {
		this.ulw = ulw;
	}
	public boolean grtIsPermitValid() {
		return isPermitValid;
	}
	public void setsPermitValid(boolean isPermitValid) {
		this.isPermitValid = isPermitValid;
	}
	public String getPermitSubType() {
		return permitSubType;
	}
	public void setPermitSubType(String permitSubType) {
		this.permitSubType = permitSubType;
	}
	public String getStateCode() {
		return stateCode;
	}
	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}
	public int getVehicleAge() {
		return vehicleAge;
	}
	public void setVehicleAge(int vehicleAge) {
		this.vehicleAge = vehicleAge;
	}
	public boolean getTaxExempted() {
		return taxExempted;
	}
	public void setTaxExempted(boolean taxExempted) {
		this.taxExempted = taxExempted;
	}
	public int getNoOfVehicle() {
		return noOfVehicle;
	}
	public void setNoOfVehicle(int noOfVehicle) {
		this.noOfVehicle = noOfVehicle;
	}
	public double getQuarterAmt() {
		return quarterAmt;
	}
	public void setQuarterAmt(double quarterAmt) {
		this.quarterAmt = quarterAmt;
	}
	public int getIncRLW() {
		return incRLW;
	}
	public void setIncRLW(int incRLW) {
		this.incRLW = incRLW;
	}
	public double getIncAmt() {
		return incAmt;
	}
	public void setIncAmt(double incAmt) {
		this.incAmt = incAmt;
	}
	public String getWeightType() {
		return weightType;
	}
	public void setWeightType(String weightType) {
		this.weightType = weightType;
	}
	public int getFromRLW() {
		return fromRLW;
	}
	public void setFromRLW(int fromRLW) {
		this.fromRLW = fromRLW;
	}
	public int getFromULW() {
		return fromULW;
	}
	public void setFromULW(int fromULW) {
		this.fromULW = fromULW;
	}
	public boolean getIsCessFeeValid() {
		return isCessFeeValid;
	}
	public void setIsCessFeeValid(boolean isCessFeeValid) {
		this.isCessFeeValid = isCessFeeValid;
	}
	public int getQuarterlyTaxType() {
		return quarterlyTaxType;
	}
	public void setQuarterlyTaxType(int quarterlyTaxType) {
		this.quarterlyTaxType = quarterlyTaxType;
	}
	public double getPenalty() {
		return penalty;
	}
	public void setPenalty(double penalty) {
		this.penalty = penalty;
	}
	public double getServiceFee() {
		return serviceFee;
	}
	public void setServiceFee(double serviceFee) {
		this.serviceFee = serviceFee;
	}
	public double getTaxAmtArrears() {
		return taxAmtArrears;
	}
	public void setTaxAmtArrears(double taxAmtArrears) {
		this.taxAmtArrears = taxAmtArrears;
	}
	public double getOldTaxAmt() {
		return oldTaxAmt;
	}
	public void setOldTaxAmt(double oldTaxAmt) {
		this.oldTaxAmt = oldTaxAmt;
	}
	public double getPenaltyArrears() {
		return penaltyArrears;
	}
	public void setPenaltyArrears(double penaltyArrears) {
		this.penaltyArrears = penaltyArrears;
	}
	
	public long getPrIssueDate() {
		return prIssueDate;
	}
	public void setPrIssueDate(long prIssueDate) {
		this.prIssueDate = prIssueDate;
	}
	
	public boolean getIsQuaterlyTaxExmpted4First5Year() {
		return isQuaterlyTaxExmpted4First5Year;
	}
	public void setIsQuaterlyTaxExmpted4First5Year(boolean isQuaterlyTaxExmpted4First5Year) {
		this.isQuaterlyTaxExmpted4First5Year = isQuaterlyTaxExmpted4First5Year;
	}
	public double getTotalTaxAmount() {
		return totalTaxAmount;
	}
	public void setTotalTaxAmount(double totalTaxAmount) {
		this.totalTaxAmount = totalTaxAmount;
	}
	
	public long getPermitValidTo() {
		return permitValidTo;
	}
	public void setPermitValidTo(long permitValidTo) {
		this.permitValidTo = permitValidTo;
	}
		
	public int getKm() {
		return km;
	}
	public void setKm(int km) {
		this.km = km;
	}
	
	public String getAreaRoute() {
		return areaRoute;
	}
	public void setAreaRoute(String areaRoute) {
		this.areaRoute = areaRoute;
	}
	public String getOldTaxType() {
		return oldTaxType;
	}
	public void setOldTaxType(String oldTaxType) {
		this.oldTaxType = oldTaxType;
	}
	public boolean getVcrFlag() {
		return vcrFlag;
	}
	public void setVcrFlag(boolean vcrFlag) {
		this.vcrFlag = vcrFlag;
	}
	public String getPliedAs() {
		return pliedAs;
	}
	public void setPliedAs(String pliedAs) {
		this.pliedAs = pliedAs;
	}
	public long getVcrBookedDt() {
		return vcrBookedDt;
	}
	public void setVcrBookedDt(long vcrBookedDt) {
		this.vcrBookedDt = vcrBookedDt;
	}
	public String getVehicleSiezed() {
		return vehicleSiezed;
	}
	public void setVehicleSiezed(String vehicleSiezed) {
		this.vehicleSiezed = vehicleSiezed;
	}
	public Integer getPeriodicTaxType() {
		return periodicTaxType;
	}
	public void setPeriodicTaxType(Integer periodicTaxType) {
		this.periodicTaxType = periodicTaxType;
	}
	
	public long getTrIssueDate() {
		return trIssueDate;
	}
	public void setTrIssueDate(long trIssueDate) {
		this.trIssueDate = trIssueDate;
	}
	public long getTaxValidUpto() {
		return taxValidUpto;
	}
	public void setTaxValidUpto(long taxValidUpto) {
		this.taxValidUpto = taxValidUpto;
	}
	@Override
	public String toString() {
		return "TaxRuleModel [regCategory=" + regCategory + ", taxType=" + taxType + ", tax=" + tax + ", ownerType="
				+ ownerType + ", isPHCertificate=" + isPHCertificate + ", isPHDriverLicense=" + isPHDriverLicense
				+ ", vehicleClass=" + vehicleClass + ", invoiceAmount=" + invoiceAmount + ", noOfVehicle=" + noOfVehicle
				+ ", fuelType=" + fuelType + ", taxAmount=" + taxAmount + ", monthType=" + monthType
				+ ", vehicleClassCategory=" + vehicleClassCategory + ", seatingCapacity=" + seatingCapacity + ", gvw="
				+ gvw + ", ulw=" + ulw + ", hsrpAmount=" + hsrpAmount + ", isDisabled=" + isDisabled
				+ ", isInvalidCarriage=" + isInvalidCarriage + ", permitType=" + permitType + ", permitSubType="
				+ permitSubType + ", cessFee=" + cessFee + ", hpaFee=" + hpaFee + ", iSuzo=" + iSuzo + ", totalAmt="
				+ totalAmt + ", serviceCode=" + serviceCode + ", greenTax=" + greenTax + ", greenTaxAmt=" + greenTaxAmt
				+ ", isPermitValid=" + isPermitValid + ", stateCode=" + stateCode + ", vehicleAge=" + vehicleAge
				+ ", taxExempted=" + taxExempted + ", quarterAmt=" + quarterAmt + ", incRLW=" + incRLW + ", incAmt="
				+ incAmt + ", weightType=" + weightType + ", fromRLW=" + fromRLW + ", fromULW=" + fromULW
				+ ", isCessFeeValid=" + isCessFeeValid + ", quarterlyTaxType=" + quarterlyTaxType + ", taxValidUpto="
				+ taxValidUpto + ", penalty=" + penalty + ", serviceFee=" + serviceFee + ", taxAmtArrears="
				+ taxAmtArrears + ", oldTaxAmt=" + oldTaxAmt + ", penaltyArrears=" + penaltyArrears + ", prIssueDate="
				+ prIssueDate + ", isQuaterlyTaxExmpted4First5Year=" + isQuaterlyTaxExmpted4First5Year
				+ ", totalTaxAmount=" + totalTaxAmount + ", permitValidTo=" + permitValidTo + ", km=" + km
				+ ", areaRoute=" + areaRoute + ", oldTaxType=" + oldTaxType + ", vcrFlag=" + vcrFlag + ", pliedAs="
				+ pliedAs + ", vcrBookedDt=" + vcrBookedDt + ", vehicleSiezed=" + vehicleSiezed + ", periodicTaxType="
				+ periodicTaxType + ", trIssueDate=" + trIssueDate + "]";
	}
	
	
}
