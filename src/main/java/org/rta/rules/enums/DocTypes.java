package org.rta.rules.enums;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

/**
 *	@Author sohan.maurya created on Nov 7, 2016.
 */
public enum DocTypes {

    DOC_OWNER_PHOTO(1, "Owner Photograph"),
    DOC_USER_SIGNATURE(2, "User Signature"),
    DOC_PAN_CARD(3, "PAN Card"), 
    DOC_PRESENT_ADDRESS_PROOF(4, "Present Address Proof of Owner"),
    DOC_PRESENT_ADDRESS_PROOF_BACKVIEW(5, "Present Address Proof (Back View)"),
    DOC_PHOTO_ID_PROOF(6, "Photo ID Proof"),    DOC_CHASSIS_NO(7, "Chassis No."),
    DOC_CHASSIS_NO_DISTANT_VIEW(8, "Chassis No.  Distance View"),
    DOC_ENGINE_NO(9, "Engine No."),
    DOC_INDIVIDUAL_INVOICE(10, "Invoice"),  
    DOC_INSURANCE_POLICY_COPY(11, "Insurance Policy Copy"),
    DOC_VEHICLE_RIGHT(12, "Vehicle Right side view"),
    DOC_VEHICLE_FRONT(13, "Vehicle Front View"),
    DOC_VEHICLE_REAR(14, "Vehicle Rear side View"),
    DOC_VEHICLE_LEFT(15, "Vehicle Left side View"),
    DOC_VEHICLE_SIDE_AFTER_MODIFICATION(16, "Vehicle side view after modification"), 
    DOC_VEHICLE_FRONT_AFTER_MODIFICATION(17, "Vehicle Front View after modification"), 
    DOC_VEHICLE_REAR_AFTER_MODIFICATION(18, "Vehicle  Rear side View after modification"), 
    DOC_VEHICLE_LEFT_AFTER_MODIFICATION(19, "Vehicle Left side  View after modification"),
    DOC_INTERIOR_VEHICLE_IMAGE_1(20, "Interior Vehicle Images 1"),
    DOC_INTERIOR_VEHICLE_IMAGE_2(21, "Interior Vehicle Images 2"),
    DOC_INTERIOR_VEHICLE_IMAGE_3(22, "Interior Vehicle Images 3"),
    DOC_FOOT_BOARD_IMAGE_OF_VEHICLE(23, "Foot board Image of  Vehicle"),
    DOC_EMERGENCY_EXIT_IMAGE(24, "Emergency Exit Image"),
    DOC_LETTER_FROM_DISTRICT_EDUCATIONAL_OFFICER (25, "Letter from District Educational Officer"),
    DOC_REGISTRATION_CERTIFICATE_EDUCATIONAL_INSTITUTE(26, "Registration certificate Educational Institute"),
    DOC_FORM22(27, "Form 22"),
    DOC_ROC(28, "ROC"),
    DOC_RTA_APPROVAL_LETTER(29, "RTA Approval letter"),
    DOC_LETTER_OF_AUTHORIZATION(30, "Letter of Authorization"),
    DOC_PASSPORT(31, "Passport"),
    DOC_VISA_COPY(32, "Visa Copy"),
    DOC_COPY_OF_PROCEEDINGS(33, "Copy of Proceedings"),
    DOC_FORM20DO(34, "Form 20 OR DO"),
    DOC_DIFFERENTLY_ABLED_CERTIFICATE(35, "Differently Abled Certificate"),
    DOC_EXTRA_1(36, "Extra Image 1"),
    DOC_EXTRA_2(37, "Extra Image 2"),
    DOC_TAX_INDIVIDUAL_INVOICE(38, "Tax Invoice"), 
    DOC_MINOR_INDIVIDUAL(39, "Minor Individual"), 
    DOC_FORM_21(40, "Form 21"), 
    DOC_DL(41, "Driving License"), MODEL_NAME(42, "Model Name"), 
    DOC_DEALER_SIGNATURE(43, "Dealer Signature"), 
    DOC_FORM20DO_COPY(44, "Form 20 OR DO (Original Copy)"), 
    DOC_HELMET_INVOICE_COPY(45, "Helmet Invoice Copy"), 
    DOC_VEHICLE_FRONT_BEFORE_MODIFICATION(46, "Vehicle front view before modification"), 
    DOC_VEHICLE_SIDE_BEFORE_MODIFICATION(47, "Vehicle side view before modification"), 
    DOC_EXTRA1_AFTER_MODIFICATION(48, "Extra1 after modification"), 
    DOC_EXTRA2_AFTER_MODIFICATION(49, "Extra2 after modification"), DOC_SCRAPE_CERTIFICATE(50, "Scrape Certificate"),
    DOC_LETTER_FOR_APPEALING_FOR_REVOCATION(51, "Letter for appealing for revocation"),
    DOC_COURT_ORDER_FOR_REDUCTION_SUSPENSION_PERIOD(52, "Court order for reduction of suspension period"),
    DOC_PUC_NUMBER_PLATE_ALONG_VEHICLE(53, "Number Plate along with Vehicle"),
    DOC_PUC_VEHICLE_AFTER_INJECTING_MACHINE_SILENCER_1(54, "Vehicle after injecting machine in silencer 1"),
    DOC_PUC_VEHICLE_AFTER_INJECTING_MACHINE_SILENCER_2(55, "Vehicle after injecting machine in silencer 2"),
    DOC_PUC_VEHICLE_AFTER_INJECTING_MACHINE_SILENCER_3(56, "Vehicle after injecting machine in silencer 3"),
    DOC_PUC_VEHICLE_AFTER_INJECTING_MACHINE_SILENCER_4(57, "Vehicle after injecting machine in silencer 4"),
    DOC_PUC_VEHICLE_AFTER_INJECTING_MACHINE_SILENCER_5(58, "Vehicle after injecting machine in silencer 5"),
    DOC_PUC_VEHICLE_AFTER_INJECTING_MACHINE_SILENCER_6(59, "Vehicle after injecting machine in silencer 6"),
    DOC_PUC_VEHICLE_AFTER_INJECTING_MACHINE_SILENCER_7(60, "Vehicle after injecting machine in silencer 7"),
    DOC_PUC_VEHICLE_AFTER_INJECTING_MACHINE_SILENCER_8(61, "Vehicle after injecting machine in silencer 8"),
    DOC_PUC_VEHICLE_AFTER_INJECTING_MACHINE_SILENCER_9(62, "Vehicle after injecting machine in silencer 9"),
    DOC_PUC_VEHICLE_AFTER_INJECTING_MACHINE_SILENCER_10(63,"Vehicle after injecting machine in silencer 10"),
    DOC_PUC_IMAGE_OF_REPORT(64, "Image of the report"), DOC_MEDICAL_CERTIFICATE(65, "Medical Certificate"),
    DOC_CHASSIS_NO_CCO(66, "Chassis No."), DOC_VEHICLE_SIDE_CCO(67, "Vehicle Side view"),
    DOC_VEHICLE_FRONT_CCO(68, "Vehicle Front View"), DOC_NON_PAYMENT_PROOF_1(69, "Proof for Non-Payment 1"), 
    DOC_NON_PAYMENT_PROOF_2(70, "Proof for Non-Payment 2"), DOC_NON_PAYMENT_PROOF_3(71, "Proof for Non-Payment 3"),
    DOC_COMMERCIAL_TAX_CERTIFICATE_RECEIPT(72, "Commercial Tax certificate Receipt"),
    DOC_COURT_ORDER_RC_SUSPENSION(73, "Court Order Suspension of RC"), DOC_RC_COPY(74, "Copy of RC"),
    DOC_PROOF_COMPANY_REPRESENTATIVE(75, "Proof that representative is part of the company"), DOC_PREMISES_PHOTO_1(76, "Premises Photo 1"),
    DOC_PREMISES_PHOTO_2(77, "Premises Photo 2"), DOC_VEHICLE_LOAN_AGREEMENT(78, "Vehicle Loan Agreement"),
    DOC_MANUFACTURER_LATTER(79, "Manufacturer Letter"),DOC_LEGAL_HEIR_CERTIFICATE(80,"Legal Heir Certificate"),
    DOC_CONSENT_LATTER_FAMILY_MEMBERS(81,"Consent latter from family members"),DOC_AFFIDAVIT(82, "Affidavit"),
    DOC_DEATH_CERTIFICATE(83, "Death Certificate"),DOC_LEGAL_HEIR_VEHICLE_PHOTO(84, "Legal Heir with Vehicle Photo"),
    DOC_AUCTION_PROCEEDINGS(85, "Auction Proceedings"), DOC_AUCTION_WINNER_LETTER(86, "Auction Winner letter"),
    DOC_SHOW_CAUSE_NOTICE(87, "Show Cause Notice"), DOC_AUCTION_CERTIFICATE(88, "Auction Certificate"),
    DOC_STOP_SIGN_BOARD(89, "Stop Sign Board"), DOC_GAS_KIT_IMAGE(90, "Gas Kit Image"), DOC_GAS_KIT_CERTIFICATE(91, "Gas Kit Certificate"),
    DOC_COURSE_CERTIFICATE(92, "Course Certificate"), DOC_FIR_COPY(93, "FIR Copy"),
    DOC_COURT_ORDER_RC_CANCELLATION(94, "Court Order Cancellation of RC"), DOC_SUPPORT_DOCUMENT_1(95, "support document 1"),
    DOC_SUPPORT_DOCUMENT_2(96, "support document 2"), DOC_VEHICLE_STEERING(97, "vehicle steering"),
	DOC_RESIDENTIAL_PROOF_ESTATE_OFFICER(98, "Residential Proof from Estate Officer"), DOC_MILITARY_DL(99, "Military DL"),
	DOC_PERMISSION_LETTER_EX_MILITARY_OFFICERS(100, "Permission Letter In case of Ex- Military officers"),
	DOC_WORK_PLACE_OR_STUDY_PROOF(101, "Place of work Or Study Proof"),	DOC_FOREIGN_DL_COPY(102, "Foreign DL Copy"),
	DOC_PERMISSION_LETTER_ISSUING_AUTHORITY(103, "Permission letter from Issuing authority"),
	DOC_VEHICLE_FRONT_VIEW_WITH_OWNER(104, "Vehicle front view with Owner"), DOC_DL_CARD_COPY(105, "DL Card Copy"),
	DOC_REPRESENTATIVE_AADHAAR_PROOF(106, "Representative Aadhaar Proof"), DOC_QUALIFICATION_PROOF(107, "Qualification Proof"),
	DOC_REPRESENTATIVE_PHOTO_PROOF(108, "Representative Photo Proof"), DOC_SUPPORTING_PROOF(109, "Supporting proof"),
	DOC_BOOT_SPACE_PHOTO(110,"Boot space photo"),DOC_BOOT_SPACE_PHOTO_AFTER_INSTALLATION_OF_GAS_KIT(111,"Boot space photo after installation of gas kit"),
	DOC_VAPORIZER_INSTALLED_TO_ENGINE_PHOTO(112,"Vaporizer installed to Engine photo"), DOC_SWITCH_INSTALLED_AT_STEERING_PHOTO(113,"Switch installed at steering photo"),
	DOC_SECOND_VEHICLE_NOC(114, "Second Vehicle NOC"),
	DOC_VEHICLE_FRONT_WITH_NUMBER_PLATE(115, "Vehicle Front View with Number Plate."),
	DOC_VEHICLE_SIDE_VIEW_WITH_OVERHANGING_MEASUREMENT(116, "Vehicle Side view with overhanging measurement (Measuring with tape)"),
	DOC_WEIGHT_BRIDGE_SLIP_AFTER_MODIFICATION(117, "Weighbridge slip for body built vehicle"),
	DOC_SEATING_PHOTOS_AFTER_MODIFICATION(118,"Seating photos after the seating alteration"),
	DOC_NEW_ENGINE_NUMBER_PHOTO(119,"New Engine Number Photo."),
	DOC_ENGINE_WITH_MAKER_NAME(120,"Engine with Maker Name.");
	
	
	private static final Map<Integer, String> lookup = new HashMap<Integer, String>();
    private static final Map<String, DocTypes> labelToDocTypes = new HashMap<String, DocTypes>();
    private static final Map<Integer, DocTypes> valueToDocTypes = new HashMap<Integer, DocTypes>();

    static {
        for (DocTypes docType : EnumSet.allOf(DocTypes.class)) {
            lookup.put(docType.getValue(), docType.getLabel());
        }
        for (DocTypes docType : EnumSet.allOf(DocTypes.class)) {
            labelToDocTypes.put(docType.getLabel(), docType);
        }
        for (DocTypes docType : EnumSet.allOf(DocTypes.class)) {
            valueToDocTypes.put(docType.getValue(), docType);
        }
    }
    public static String getLabel(Integer value) {
        return lookup.get(value);
    }

    public static DocTypes getDocTypes(String label) {
        return  label==null || label.equals("")  ? null : labelToDocTypes.get(label.toUpperCase());
    }

    public static DocTypes getDocTypes(Integer value) {
        return valueToDocTypes.get(value);
    }
     private int value;
     private String label;
     private DocTypes(int id, String label) {
        this.value = id;
        this.label = label;
    }
    public int getValue() {
        return value;
    }
    public void setValue(int id) {
        this.value = id;
    }
    public String getLabel() {
        return label;
    }
    public void setLabel(String label) {
        this.label = label;
    }
    
}
