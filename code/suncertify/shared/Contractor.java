package suncertify.shared;

import java.io.Serializable;

public class Contractor implements Serializable {

	private static final long serialVersionUID = 1746986159728433641L;

	private final int recordId;
	private String name;
	private String location;
	private String specialites;
	private String noStaff;
	private String rate;
	private String custId;

	public Contractor(final int recordId, final String name, final String location, final String specialites, final String noStaff,
			final String rate, final String custId) {
		this.recordId = recordId;
		this.name = name;
		this.location = location;
		this.specialites = specialites;
		this.noStaff = noStaff;
		this.rate = rate;
		this.custId = custId;
	}

	public Contractor(final int recordId, final String[] record) {
		this.recordId = recordId;
		this.name = record[0];
		this.location = record[1];
		this.specialites = record[2];
		this.noStaff = record[3];
		this.rate = record[4];
		this.custId = record[5];
	}

	public String[] toArray() {
		final String[] data = new String[6];
		data[0] = this.name;
		data[1] = this.location;
		data[2] = this.specialites;
		data[3] = this.noStaff;
		data[4] = this.rate;
		data[5] = this.custId;
		return data;
	}

	public int getRecordId() {
		return this.recordId;
	}

	public String getName() {
		return this.name;
	}

	public String getLocation() {
		return this.location;
	}

	public String getSpecialites() {
		return this.specialites;
	}

	public String getNoStaff() {
		return this.noStaff;
	}

	public String getRate() {
		return this.rate;
	}

	public String getCustomerId() {
		return this.custId;
	}

	public void setName(final String name) {
		this.name = name;
	}

	public void setLocation(final String location) {
		this.location = location;
	}

	public void setSpecialites(final String specialites) {
		this.specialites = specialites;
	}

	public void setNoStaff(final String noStaff) {
		this.noStaff = noStaff;
	}

	public void setRate(final String rate) {
		this.rate = rate;
	}

	public void setCustomerId(final String custId) {
		this.custId = custId;
	}
}