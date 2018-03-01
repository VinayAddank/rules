package org.rta.rules.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonValue;

public enum PermitType {
    AITC(1, "ALL INDIA TOURIST CAB"), AITP(2, "ALL INDIA TOURIST PERMIT"), CCP(3, "CONTRACT CARRIAGE PERMIT"), CSP(4,
            "COUNTER SIGNATURE PERMIT"), CSPP(5, "COUNTER SIGNATURE PERMIT PUCCKA"), EIB(6,
                    "EDUCATIONAL INSTITUTE PERMIT"), GCP(7, "GOODS CARRIAGE PERMIT"), NP(8, "NATIONAL PERMIT"), PSVP(9,
                            "PRIVATE SERVICE VEHICLE PERMIT"), SBP(10, "SPARE BUS PERMIT"), SCP(11,
                                    "STAGE CARRIAGE PERMIT"), SP(12, "SETWIN PERMIT"), SSP(13, "SPECIAL PERMIT"), SSPSO(
                                            14, "SPECIAL PERMIT FOR SPECIAL OCCASIONS"), STL(15,
                                                    "SHORT TERM LICENSES"), TCCP(16,
                                                            "TEMPORARY CONTRACT CARRIAGE PERMIT"), TCP(17,
                                                                    "TAXI CAB PERMIT"), TGCP(18,
                                                                            "TEMPORARY GOODS CARRIAGE PERMIT"), TSCP(19,
                                                                                    "TEMPORARY STAGE CARRIAGE PERMIT");

    private static final Map<String, PermitType> labelToType = new HashMap<String, PermitType>();
    private static final Map<Integer, PermitType> valueToType = new HashMap<Integer, PermitType>();

    static {
        for (PermitType permitType : PermitType.values()) {
            labelToType.put(permitType.getLabel(), permitType);
        }
        for (PermitType permitType : EnumSet.allOf(PermitType.class)) {
            valueToType.put(permitType.getValue(), permitType);
        }
    }

    private PermitType(Integer value, String label) {
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

    public static PermitType getPermitType(String label) {
        return labelToType.get(label);
    }

    public static PermitType getPermitType(Integer value) {
        return valueToType.get(value);
    }
}

