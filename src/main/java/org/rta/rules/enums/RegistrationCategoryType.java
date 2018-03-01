package org.rta.rules.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum RegistrationCategoryType {

    TRANSPORT(1, "Transport"), NON_TRANSPORT(2, "Non Transport");
		private static final Map<String, RegistrationCategoryType> labelToType = new HashMap<String, RegistrationCategoryType>();
	    private static final Map<Integer, RegistrationCategoryType> valueToUserType = new HashMap<Integer, RegistrationCategoryType>();
	    
	    static {
	        for (RegistrationCategoryType registrationCategoryType : RegistrationCategoryType.values()) {
	            labelToType.put(registrationCategoryType.getLabel(), registrationCategoryType);
	        }
	        for (RegistrationCategoryType registrationCategoryType : EnumSet.allOf(RegistrationCategoryType.class)) {
	            valueToUserType.put(registrationCategoryType.getValue(), registrationCategoryType);
	        }
	    }
	    
	    private RegistrationCategoryType(Integer value, String label) {
	        this.value = value;
	        this.label = label;
	    }

	    private Integer value;
	    private String label;
	            
	    @JsonValue
	    public String toValue() {
	        return this.label;
	    }

	    public Integer getValue() {
	        return value;
	    }

	    public String getLabel() {
	        return label;
	    }
	    
	    public static RegistrationCategoryType getRegistrationCategoryType(String label) {
	        return labelToType.get(label);
	    }
	    
	    public static RegistrationCategoryType getRegistrationCategoryType(Integer value) {
	        return valueToUserType.get(value);
	    }
}

