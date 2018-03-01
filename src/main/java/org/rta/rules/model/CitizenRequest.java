package org.rta.rules.model;

import java.util.List;

import org.rta.rules.enums.AlterationCategory;
import org.rta.rules.enums.ServiceType;
import org.rta.rules.enums.UserType;

/**
 *	@Author sohan.maurya created on Dec 9, 2016.
 */
public class CitizenRequest {

    private ServiceType servicesType;
    private Boolean isAadhar;
    private Integer age;
    private Integer registrationCategoryType;
    private List<AlterationCategory> alterationCategory;
    private Boolean isBadge;
    private Boolean isInsuraceDocs;
    private UserType userType;
    private Integer status;
    private boolean isCompleted;

    public ServiceType getServicesType() {
        return servicesType;
    }

    public void setServicesType(ServiceType servicesType) {
        this.servicesType = servicesType;
    }

    public Boolean getIsAadhar() {
        return isAadhar;
    }

    public void setIsAadhar(Boolean isAadhar) {
        this.isAadhar = isAadhar;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

	public Integer getRegistrationCategoryType() {
		return registrationCategoryType;
	}

	public void setRegistrationCategoryType(Integer registrationCategoryType) {
		this.registrationCategoryType = registrationCategoryType;
	}

	public Boolean getIsBadge() {
		return isBadge;
	}

	public void setIsBadge(Boolean isBadge) {
		this.isBadge = isBadge;
	}

	public Boolean getIsInsuraceDocs() {
		return isInsuraceDocs;
	}

	public void setIsInsuraceDocs(Boolean isInsuraceDocs) {
		this.isInsuraceDocs = isInsuraceDocs;
	}

	public UserType getUserType() {
		return userType;
	}

	public void setUserType(UserType userType) {
		this.userType = userType;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public List<AlterationCategory> getAlterationCategory() {
		return alterationCategory;
	}

	public void setAlterationCategory(List<AlterationCategory> alterationCategory) {
		this.alterationCategory = alterationCategory;
	}

	public boolean isCompleted() {
		return isCompleted;
	}

	public void setCompleted(boolean isCompleted) {
		this.isCompleted = isCompleted;
	}
	
}
