import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Thread2Seq {

	public static HashMap<String, List<Access>> thread2Seq = new HashMap<String, List<Access>>();

	public static void add2(String thread, Access acc) {
		List<Access> ret = thread2Seq.get(thread);
		
		
		
		
		if (ret == null) {
			ret = new ArrayList<Access>();
			thread2Seq.put(thread, ret);
		}
		ret.add(acc);
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
