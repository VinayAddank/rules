package org.rta.rules.model;
/**
 *	@Author sohan.maurya created on Nov 7, 2016.
 */
public class CompulsoryAttachments {

    public static Integer COMPULSORY_ANY_CASE = 1;
    public static Integer NOT_COMPULSORY_ANY_CASE = 0;

    private Integer status;
    private Boolean isFinanced;
    private Boolean isInvoiceValueMore4L;
    private Boolean isCustomerDisabled;
    private Boolean isMinorAge;
    private Integer vehicleType;
    private Integer ownershipType;
    private Integer registrationCategory;


    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Boolean getIsFinanced() {
        return isFinanced;
    }

    public void setIsFinanced(Boolean isFinanced) {
        this.isFinanced = isFinanced;
    }

    public Boolean getIsInvoiceValueMore4L() {
        return isInvoiceValueMore4L;
    }

    public void setIsInvoiceValueMore4L(Boolean isInvoiceValueMore4L) {
        this.isInvoiceValueMore4L = isInvoiceValueMore4L;
    }

    public Boolean getIsCustomerDisabled() {
        return isCustomerDisabled;
    }

    public void setIsCustomerDisabled(Boolean isCustomerDisabled) {
        this.isCustomerDisabled = isCustomerDisabled;
    }

    public Boolean getIsMinorAge() {
        return isMinorAge;
    }

    public void setIsMinorAge(Boolean isMinorAge) {
        this.isMinorAge = isMinorAge;
    }

    public Integer getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(Integer vehicleType) {
        this.vehicleType = vehicleType;
    }

    public Integer getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(Integer ownershipType) {
        this.ownershipType = ownershipType;
    }

    public Integer getRegistrationCategory() {
        return registrationCategory;
    }

    public void setRegistrationCategory(Integer registrationCategory) {
        this.registrationCategory = registrationCategory;
    }


}
