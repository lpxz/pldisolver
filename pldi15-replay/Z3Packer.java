import java.util.HashSet;



public class Z3Packer {

	
   public static HashSet<String> orderVars = new HashSet<String>();

	public static String orderVar(Access node) {
		String ret = ( "O" + node.toString()).intern();
		orderVars.add(ret);
		return ret;
	}
	
	public static StringBuilder orderVarConstraint()
	{
		StringBuilder ret = new StringBuilder(); 
		for (String str : orderVars) {
			ret.append("(declare-const ").append(str).append(" Int)\n");
		}
		return ret;
		
	}
	
	
	public static String hb(Access candidate, Access rnode) {

		String var_candidate =  orderVar(candidate);
		String var_rnode =  orderVar(rnode);
		return  "(> " + var_rnode + " " + var_candidate + ")\n"; // w1->r, w2 before w1 or after r.	;
	}
	
	
	public static String assertIt(String constraint) {

		return "(assert \n" + constraint + ")\n\n";
	}
	
	
	public static String or(String rwCon, String valueCon) {

		 return "(or " + rwCon + " " + valueCon + ")\n";
	}
	
	public static void main(String[] args) {
		// TODO Auto-generated method stub

	}

}
