package org.rta.rules.dao;

import java.util.List;

import org.hibernate.SQLQuery;
import org.rta.rules.constant.VehicleSubClass;
import org.rta.rules.dao.base.BaseDAO;
import org.rta.rules.entity.HomeTaxEntity;
import org.rta.rules.enums.TaxType;
import org.rta.rules.model.TaxRuleModel;
import org.springframework.stereotype.Repository;

@Repository
public class HomeTaxDAOImpl extends BaseDAO<HomeTaxEntity> implements HomeTaxDAO {

	public HomeTaxDAOImpl() {
		super(HomeTaxEntity.class);
	}

	@Override
	public List<HomeTaxEntity> getHomeTax(TaxRuleModel taxRuleModel) { 

		StringBuilder q;
		q = new StringBuilder(
				"select * from home_tax where"); 
		q.append(" vehicle_sub_class =:vehicleSubClass  ");
		q.append(" and tax_type=:taxType   ");
		q.append(" and state_code=:stateCode  ");	
		if(VehicleSubClass.COCT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || VehicleSubClass.TOVT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || VehicleSubClass.MAXT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || (VehicleSubClass.MTLT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) && TaxType.QUARTERLY_TAX.getCode().equalsIgnoreCase(taxRuleModel.getTaxType()))){
			if(taxRuleModel.getPermitType() != null)
				q.append(" and (permit_code=:permitCode)  ");
			else
				q.append(" and (permit_code is null)  ");		
		}
		
		if(VehicleSubClass.SCRT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory())){
			if(taxRuleModel.getPermitSubType() == null || taxRuleModel.getPermitType() == null)
					q.append(" and (permit_code='SBP')  ");
		}
		
		if(VehicleSubClass.COCT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || VehicleSubClass.TOVT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || VehicleSubClass.SCRT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || VehicleSubClass.SCRT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory())){
			if(taxRuleModel.getPermitSubType() != null)
				q.append(" and (permit_sub_type=:permitSubType)  ");
			else
				q.append(" and (permit_sub_type is null) ");	
		}
		q.append(" and to_date is null  ");
		if(taxRuleModel.getTaxType().equalsIgnoreCase(TaxType.LIFE_TAX.getCode()))
			q.append(" and (ownership_type is null or ownership_type=:ownershipType)  ");
		q.append(" and (:vehicleAge >= from_age  and :vehicleAge <= to_age ) ");
		if(VehicleSubClass.ARKT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || VehicleSubClass.MAXT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || (VehicleSubClass.MTLT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) && TaxType.QUARTERLY_TAX.getCode().equalsIgnoreCase(taxRuleModel.getTaxType())))
			q.append(" and (to_seat = 0 or(:seatCapacity >= from_seat  and :seatCapacity <= to_seat))   ");
		q.append(" and (to_ulw = 0 or(:ulw >= from_ulw  and :ulw <= to_ulw))   ");
		q.append(" and (to_rlw = 0 or(:rlw >= from_rlw  and :rlw <= to_rlw))   ");
		q.append(" and (to_invoice_amt = 0 or to_invoice_amt is null or(:invoiceAmt >= from_invoice_amt  and :invoiceAmt <= to_invoice_amt))    ");
		if(taxRuleModel.getPermitSubType() != null && VehicleSubClass.SCRT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()))
		{
			q.append(" and (to_km = 0 or to_km is null or(:km >= from_km  and :km <= to_km))   ");
			q.append(" and (area_route = null or area_route=:areaRoute)  ");
		}
		SQLQuery sqlQuery = getSession().createSQLQuery(q.toString()); 
		sqlQuery.setParameter("vehicleSubClass", taxRuleModel.getVehicleClassCategory());     
		sqlQuery.setParameter("taxType", taxRuleModel.getTaxType());
		sqlQuery.setParameter("stateCode", taxRuleModel.getStateCode());
		if(VehicleSubClass.COCT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || VehicleSubClass.TOVT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || VehicleSubClass.MAXT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || (VehicleSubClass.MTLT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) && TaxType.QUARTERLY_TAX.getCode().equalsIgnoreCase(taxRuleModel.getTaxType()))){
			if(taxRuleModel.getPermitType() != null)
				sqlQuery.setParameter("permitCode", taxRuleModel.getPermitType());
		}
		if(VehicleSubClass.COCT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || VehicleSubClass.TOVT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || VehicleSubClass.SCRT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || VehicleSubClass.SCRT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory())){
			if(taxRuleModel.getPermitSubType() != null)
				sqlQuery.setParameter("permitSubType", taxRuleModel.getPermitSubType());
		}
		if(taxRuleModel.getTaxType().equalsIgnoreCase(TaxType.LIFE_TAX.getCode()))
			sqlQuery.setParameter("ownershipType", taxRuleModel.getOwnerType());
		sqlQuery.setParameter("vehicleAge", taxRuleModel.getVehicleAge());
		if(VehicleSubClass.ARKT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || VehicleSubClass.MAXT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) || (VehicleSubClass.MTLT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory()) && TaxType.QUARTERLY_TAX.getCode().equalsIgnoreCase(taxRuleModel.getTaxType())))
			sqlQuery.setParameter("seatCapacity", taxRuleModel.getSeatingCapacity());
		sqlQuery.setParameter("ulw", taxRuleModel.getUlw());
		sqlQuery.setParameter("rlw", taxRuleModel.getGvw());
		sqlQuery.setParameter("invoiceAmt", taxRuleModel.getInvoiceAmount());
		if(taxRuleModel.getPermitSubType() != null && VehicleSubClass.SCRT.equalsIgnoreCase(taxRuleModel.getVehicleClassCategory())){
			sqlQuery.setParameter("km", taxRuleModel.getKm());
			sqlQuery.setParameter("areaRoute", taxRuleModel.getAreaRoute());
		}
		sqlQuery.addEntity(HomeTaxEntity.class);
		List<HomeTaxEntity> homeTaxEntitys = (List<HomeTaxEntity>) sqlQuery.list();
		System.out.println(homeTaxEntitys.size());
		return homeTaxEntitys;
	}
}
