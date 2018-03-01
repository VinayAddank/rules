package org.rta.rules.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import org.rta.rules.entity.base.BaseEntity;

@Entity
@Table(name = "home_tax")
public class HomeTaxEntity extends BaseEntity{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
    @Column(name = "home_tax_id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "home_tax_seq")
    @SequenceGenerator(name = "home_tax_seq", sequenceName = "home_tax_seq", allocationSize = 1)
    private Long homeTaxId;
	

	@Column(name = "tax_type", length = 50)
	private String taxType;

	@Column(name = "from_date")
	private Date fromDate;

	@Column(name = "tax_amt", columnDefinition = "double precision DEFAULT 0")
	private double taxAmt;

	@Column(name = "seat_type", length = 1)
	private Character seatType;

	@Column(name = "vehicle_sub_class", length = 50)
	private String vehicleSubClass;

	@Column(name = "permit_code", length = 50)
	private String permitCode;

	@Column(name = "state_code", length = 50)
	private String stateCode;

	@Column(name = "to_seat", columnDefinition = "Integer DEFAULT 0")
	private Integer toSeat;

	@Column(name = "from_seat", columnDefinition = "Integer DEFAULT 0")
	private Integer fromSeat;

	@Column(name = "to_rlw", columnDefinition = "Integer DEFAULT 0")
	private Integer toRlw;

	@Column(name = "inc_rlw", columnDefinition = "Integer DEFAULT 0")
	private Integer incRlw;

	@Column(name = "from_rlw", columnDefinition = "Integer DEFAULT 0")
	private Integer fromRlw;

	@Column(name = "inc_amt", columnDefinition = "double precision DEFAULT 0")
	private double incAmt;

	@Column(name = "from_ulw", columnDefinition = "Integer DEFAULT 0")
	private Integer fromUlw;

	@Column(name = "to_ulw", columnDefinition = "Integer DEFAULT 0")
	private Integer toUlw;

	@Column(name = "from_cc", columnDefinition = "Integer DEFAULT 0")
	private Integer fromCc;

	@Column(name = "to_cc", columnDefinition = "Integer DEFAULT 0")
	private Integer toCc;

	@Column(name = "from_invoice_amt", columnDefinition = "double precision DEFAULT 0")
	private double fromInvoiceAmt;

	@Column(name = "to_invoice_amt", columnDefinition = "double precision DEFAULT 0")
	private double toInvoiceAmt;

	@Column(name = "from_km", columnDefinition = "Integer DEFAULT 0")
	private Integer fromKm;

	@Column(name = "to_km", columnDefinition = "Integer DEFAULT 0")
	private Integer toKm;

	@Column(name = "from_age", columnDefinition = "double precision DEFAULT 0")
	private double fromAge;

	@Column(name = "to_age", columnDefinition = "double precision DEFAULT 0")
	private double toAge;

	@Column(name = "ownership_type", length = 50)
	private String ownershipType;

	@Column(name = "permit_sub_type", length = 50)
	private String permitSubType;

	@Column(name = "area_route", length = 50)
	private String areaRoute;

	@Column(name = "tax_percent", columnDefinition = "double precision DEFAULT 0")
	private double taxPercent;

	@Column(name = "add_charge", columnDefinition = "double precision DEFAULT 0")
	private double addCharge;

	@Column(name = "to_date")
	private Date toDate;	
	
	public Long getHomeTaxId() {
		return homeTaxId;
	}

	public void setHomeTaxId(Long homeTaxId) {
		this.homeTaxId = homeTaxId;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public double getTaxAmt() {
		return taxAmt;
	}

	public void setTaxAmt(double taxAmt) {
		this.taxAmt = taxAmt;
	}

	public Character getSeatType() {
		return seatType;
	}

	public void setSeatType(Character seatType) {
		this.seatType = seatType;
	}

	public String getVehicleSubClass() {
		return vehicleSubClass;
	}

	public void setVehicleSubClass(String vehicleSubClass) {
		this.vehicleSubClass = vehicleSubClass;
	}

	public String getPermitCode() {
		return permitCode;
	}

	public void setPermitCode(String permitCode) {
		this.permitCode = permitCode;
	}

	public String getStateCode() {
		return stateCode;
	}

	public void setStateCode(String stateCode) {
		this.stateCode = stateCode;
	}

	public Integer getToSeat() {
		return toSeat;
	}

	public void setToSeat(Integer toSeat) {
		this.toSeat = toSeat;
	}

	public Integer getFromSeat() {
		return fromSeat;
	}

	public void setFromSeat(Integer fromSeat) {
		this.fromSeat = fromSeat;
	}

	public Integer getToRlw() {
		return toRlw;
	}

	public void setToRlw(Integer toRlw) {
		this.toRlw = toRlw;
	}

	public Integer getIncRlw() {
		return incRlw;
	}

	public void setIncRlw(Integer incRlw) {
		this.incRlw = incRlw;
	}

	public Integer getFromRlw() {
		return fromRlw;
	}

	public void setFromRlw(Integer fromRlw) {
		this.fromRlw = fromRlw;
	}

	public double getIncAmt() {
		return incAmt;
	}

	public void setIncAmt(double incAmt) {
		this.incAmt = incAmt;
	}

	public Integer getFromUlw() {
		return fromUlw;
	}

	public void setFromUlw(Integer fromUlw) {
		this.fromUlw = fromUlw;
	}

	public Integer getToUlw() {
		return toUlw;
	}

	public void setToUlw(Integer toUlw) {
		this.toUlw = toUlw;
	}

	public Integer getFromCc() {
		return fromCc;
	}

	public void setFromCc(Integer fromCc) {
		this.fromCc = fromCc;
	}

	public Integer getToCc() {
		return toCc;
	}

	public void setToCc(Integer toCc) {
		this.toCc = toCc;
	}

	public double getFromInvoiceAmt() {
		return fromInvoiceAmt;
	}

	public void setFromInvoiceAmt(double fromInvoiceAmt) {
		this.fromInvoiceAmt = fromInvoiceAmt;
	}

	public double getToInvoiceAmt() {
		return toInvoiceAmt;
	}

	public void setToInvoiceAmt(double toInvoiceAmt) {
		this.toInvoiceAmt = toInvoiceAmt;
	}

	public Integer getFromKm() {
		return fromKm;
	}

	public void setFromKm(Integer fromKm) {
		this.fromKm = fromKm;
	}

	public Integer getToKm() {
		return toKm;
	}

	public void setToKm(Integer toKm) {
		this.toKm = toKm;
	}

	public double getFromAge() {
		return fromAge;
	}

	public void setFromAge(double fromAge) {
		this.fromAge = fromAge;
	}

	public double getToAge() {
		return toAge;
	}

	public void setToAge(double toAge) {
		this.toAge = toAge;
	}

	public String getOwnershipType() {
		return ownershipType;
	}

	public void setOwnershipType(String ownershipType) {
		this.ownershipType = ownershipType;
	}

	public String getPermitSubType() {
		return permitSubType;
	}

	public void setPermitSubType(String permitSubType) {
		this.permitSubType = permitSubType;
	}

	public String getAreaRoute() {
		return areaRoute;
	}

	public void setAreaRoute(String areaRoute) {
		this.areaRoute = areaRoute;
	}

	public double getTaxPercent() {
		return taxPercent;
	}

	public void setTaxPercent(double taxPercent) {
		this.taxPercent = taxPercent;
	}

	public double getAddCharge() {
		return addCharge;
	}

	public void setAddCharge(double addCharge) {
		this.addCharge = addCharge;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}


}
