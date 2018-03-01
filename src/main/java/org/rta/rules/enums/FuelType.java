package org.rta.rules.enums;

public enum FuelType {

	CNG(1, "CNG"), ELECTRIC(2, "ELECTRIC"), BATERY(3, "BATERY"), SOLAR_ENERGY(4, "SOLAR ENERGY"), PETROL(5, "PETROL"), DIESEL(6, "DIESEL"), DIESEL_HYBRID(7, "DIESEL/HYBRID");

    private FuelType(Integer value, String label) {
    	this.value = value;
    	this.label = label;
    }

    private Integer value;
    private String label;

    public String getLabel() {
        return label;
    }

    public Integer getValue() {
        return value;
    }

    public void setValue(Integer value) {
        this.value = value;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public static FuelType getFuelType(String label) {
        if (label == CNG.getLabel()) {
            return CNG;
        } else if (label == ELECTRIC.getLabel()) {
            return ELECTRIC;
        }else if (label == BATERY.getLabel()) {
            return BATERY;
        }else if (label == SOLAR_ENERGY.getLabel()) {
            return SOLAR_ENERGY;
        }else if (label == PETROL.getLabel()) {
            return PETROL;
        }else if (label == DIESEL.getLabel()) {
            return DIESEL;
        }else if (label == DIESEL_HYBRID.getLabel()) {
            return DIESEL_HYBRID;
        }
        return null;
    }

    public static FuelType getFuelType(Integer value) {
        if (value == CNG.getValue()) {
            return CNG;
        } else if (value == ELECTRIC.getValue()) {
            return ELECTRIC;
        } else if (value == BATERY.getValue()) {
            return BATERY;
        } else if (value == SOLAR_ENERGY.getValue()) {
            return SOLAR_ENERGY;
        } else if (value == PETROL.getValue()) {
            return PETROL;
        } else if (value == DIESEL.getValue()) {
            return DIESEL;
        } else if (value == DIESEL_HYBRID.getValue()) {
            return DIESEL_HYBRID;
        }
        return null;
    }
}
