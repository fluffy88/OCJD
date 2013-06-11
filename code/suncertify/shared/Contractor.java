package suncertify.shared;

import java.io.Serializable;

/**
 * This class is the Domain object class for the application. It represents a Contractor record in the database.
 * 
 * @author Sean Dunne
 */
public class Contractor implements Serializable {

	private static final long serialVersionUID = 1746986159728433641L;

	private final int recordId;
	private String name;
	private String location;
	private String specialites;
	private String noStaff;
	private String rate;
	private String custId;

	/**
	 * Construct a new Contractor object instantiating all it's fields.
	 * 
	 * @param recordId
	 *            The ID of this record.
	 * @param name
	 *            The Contractor name.
	 * @param location
	 *            The Contractor location.
	 * @param specialites
	 *            The work the Contractor specializes in.
	 * @param noStaff
	 *            The number of people that work for this Contractor.
	 * @param rate
	 *            The cost per hour of hiring this Contractor.
	 * @param custId
	 *            The ID of the customer that has booked this Contractor.
	 */
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

	/**
	 * A construct provide for convenience to create a new Contractor object instantiating all it's fields, using an array. The array must
	 * contain only 6 elements and the elements must be found in the order same order as
	 * {@link Contractor#Contractor(int, String, String, String, String, String, String)}.
	 * 
	 * @param recordId
	 *            The ID of this record.
	 * @param record
	 *            A String array containing the 6 data fields.
	 */
	public Contractor(final int recordId, final String[] record) {
		this(recordId, record[0], record[1], record[2], record[3], record[4], record[5]);
	}

	/**
	 * This method is used to convert a Contracter object back to a String array. It is used to get the data of this Contractor in a format
	 * ready for writing to the database.
	 * 
	 * @return A String array containing this Contractors data.
	 */
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

	/**
	 * Get the record ID.
	 * 
	 * @return This Contractors record ID number.
	 */
	public int getRecordId() {
		return this.recordId;
	}

	/**
	 * Get the Contractor name.
	 * 
	 * @return This Contractors name.
	 */
	public String getName() {
		return this.name;
	}

	/**
	 * Get the Contractors location.
	 * 
	 * @return This Contractors location.
	 */
	public String getLocation() {
		return this.location;
	}

	/**
	 * Get the Contractors specialties.
	 * 
	 * @return This Contractors specialties.
	 */
	public String getSpecialites() {
		return this.specialites;
	}

	/**
	 * Get the Contractors number of staff.
	 * 
	 * @return The number of staff working for this Contractor.
	 */
	public String getNoStaff() {
		return this.noStaff;
	}

	/**
	 * Get the Contractors rate per hour.
	 * 
	 * @return This Contractors name.
	 */
	public String getRate() {
		return this.rate;
	}

	/**
	 * Get the ID of the customer who has booked this Contractor.
	 * 
	 * @return This customer ID holding this Contractor.
	 */
	public String getCustomerId() {
		return this.custId;
	}

	/**
	 * Set the Contractor name.
	 * 
	 * @param name
	 *            The new name of this Contractor.
	 */
	public void setName(final String name) {
		this.name = name;
	}

	/**
	 * Set the Contractors location.
	 * 
	 * @param name
	 *            The new location of this Contractor.
	 */
	public void setLocation(final String location) {
		this.location = location;
	}

	/**
	 * Set the Contractors specialties.
	 * 
	 * @param name
	 *            The new specialties of this Contractor.
	 */
	public void setSpecialites(final String specialites) {
		this.specialites = specialites;
	}

	/**
	 * Set the number of staff this Contractor has.
	 * 
	 * @param name
	 *            The new number of staff for this Contractor.
	 */
	public void setNoStaff(final String noStaff) {
		this.noStaff = noStaff;
	}

	/**
	 * Set the Contractors hourly rate.
	 * 
	 * @param name
	 *            The new hourly rate of this Contractor.
	 */
	public void setRate(final String rate) {
		this.rate = rate;
	}

	/**
	 * Set the ID of the customer holding this Contractor.
	 * 
	 * @param name
	 *            The ID new of the new customer holding this Contractor.
	 */
	public void setCustomerId(final String custId) {
		this.custId = custId;
	}
}