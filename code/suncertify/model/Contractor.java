package suncertify.model;

public class Contractor {

	private String deleted;
	private String name;
	private String location;
	private String specialities;
	private String size;
	private String rate;
	private String owner;

	public String isDeleted() {
		return deleted;
	}

	public void setDeleted(String deleted) {
		this.deleted = deleted;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name.trim();
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location.trim();
	}

	public String getSpecialities() {
		return specialities;
	}

	public void setSpecialities(String specialities) {
		this.specialities = specialities.trim();
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size.trim();
	}

	public String getRate() {
		return rate;
	}

	public void setRate(String rate) {
		this.rate = rate.trim();
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner.trim();
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder("[");
		sb.append("Deleted: ");
		sb.append(deleted);
		sb.append(", Name: ");
		sb.append(name);
		sb.append(", Location: ");
		sb.append(location);
		sb.append(", Specialities: ");
		sb.append(specialities);
		sb.append(", Size: ");
		sb.append(size);
		sb.append(", Rate: ");
		sb.append(rate);
		sb.append(", Owner: ");
		sb.append(owner);
		sb.append("]");
		return sb.toString();
	}

	public String[] getRecord() {
		return new String[] { name, location, specialities, size, rate, owner };
	}
}
