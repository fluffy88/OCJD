package suncertify;

/**
 * An interface that defines how the three application modes interact.
 * 
 * @author Sean Dunne
 */
public interface Application {

	/**
	 * This method is the initial method that gets called when an application is
	 * created. This method is responsible for ensuring all prerequisites to the
	 * starting of the application are correctly handled.
	 */
	void launch();

	/**
	 * This method gets called when the application prerequisites have been
	 * satisfied and the user is ready to start the applications main
	 * functionality.
	 */
	void start();

}