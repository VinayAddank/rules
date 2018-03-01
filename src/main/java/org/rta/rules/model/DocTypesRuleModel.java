package org.rta.rules.model;
/**
 *	@Author sohan.maurya created on Nov 7, 2016.
 */
public class DocTypesRuleModel {

    private Integer id;
    private String name;
    private Boolean isMandatory;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getIsMandatory() {
        return isMandatory;
    }

    public void setIsMandatory(Boolean isMandatory) {
        this.isMandatory = isMandatory;
    }


}
