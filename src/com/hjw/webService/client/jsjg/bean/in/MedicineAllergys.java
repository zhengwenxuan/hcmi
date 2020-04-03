package com.hjw.webService.client.jsjg.bean.in;

import java.util.ArrayList;
import java.util.List;

public class MedicineAllergys {

	private List<MedicineAllergy> MedicineAllergy = new ArrayList<>();

	public List<MedicineAllergy> getMedicineAllergy() {
		return MedicineAllergy;
	}

	public void setMedicineAllergy(List<MedicineAllergy> medicineAllergy) {
		MedicineAllergy = medicineAllergy;
	}
}
