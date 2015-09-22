import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;


public class Loc2Writes {

	public static HashMap<String, Set<Write>> loc2Writes = new  HashMap<String, Set<Write>>();
	
	
	public static void add2(String thread, Write acc) {
		Set<Write> ret = loc2Writes.get(thread);
		if (ret == null) {
			ret = new HashSet<Write>();
			loc2Writes.put(thread, ret);
		}
		ret.add(acc);
	}
	
	public static void main(String[] args) {

	}

}
