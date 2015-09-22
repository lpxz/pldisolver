import java.util.HashMap;


public class FlowDep {

	/**
	 * @param args
	 */
	public static HashMap<Read, Write> readFromWrite = new HashMap<Read, Write>();
	public static void add2(Read r, Write w)
	{
		readFromWrite.put(r,w);
		
	}
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
