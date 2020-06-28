package it.polito.tdp.formulaone.model;

public class Pilota {
	private Integer driverId;
	private String ref;
	
	public Pilota(Integer driverId, String ref) {
		super();
		this.driverId = driverId;
		this.ref = ref;
	}
	public Integer getDriverId() {
		return driverId;
	}
	public String getRef() {
		return ref;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((driverId == null) ? 0 : driverId.hashCode());
		return result;
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pilota other = (Pilota) obj;
		if (driverId == null) {
			if (other.driverId != null)
				return false;
		} else if (!driverId.equals(other.driverId))
			return false;
		return true;
	}
	@Override
	public String toString() {
		return  driverId + " " + ref;
	}
	
	

}
