package control;

/**
 * @author qfdk
 * Subject.java
 * 2015年10月12日
 */
public interface Subject {
	/**
	 * @param o
	 */
	void register(Observer o);
	/**
	 * @param o
	 */
	void unregister(Observer o);
	/**
	 * appler la methode update
	 */
	void notifyObservater();
}
