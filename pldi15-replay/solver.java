import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.Vector;




public class solver {

	/*
	 * 
	 *    Thread A                     Thread B
                                       3. y=...  //B.1
                                       4. x=... //B.2
                                       5. ...=x; //B.3
    	1. x=...  // A.1
    	2. ...=y; // A.2
                                       6. ...=x  // B.4

		We know the flow dependences are: 3->2,  4->5 and 1->6
	 * 
	 * 
	 * 
	 * 
	 * */

	// per the example: https://sites.google.com/site/light31415926/counterexample
	public static void main(String[] args) {
		// pseudo loading.
        HashMap<String, HashMap<String, String>> recorded_loc2_dep = new HashMap<String, HashMap<String, String>>();
        HashMap<String, String> dep4x = new HashMap<String, String>();
        recorded_loc2_dep.put("x", dep4x);
        dep4x.put("B.4", "A.1");// note that B.4 is the local counter encoding, it means the 4th of thread B.
        dep4x.put("B.3", "B.2");
        
        HashMap<String, String> dep4y = new HashMap<String, String>();
        recorded_loc2_dep.put("y", dep4y);
        dep4y.put("A.2", "B.1");
        
        
        //=============processing
        for(String loc: recorded_loc2_dep.keySet())
        {
        	HashMap<String, String> deps = recorded_loc2_dep.get(loc);
        	for(String readStr : deps.keySet())
        	{
        		String writeStr = deps.get(readStr);
        		String readTh =getTh(readStr); 
        		Read read = new Read( loc,readTh , getlIndex(readStr));
        		
        		Thread2Seq.add2(readTh, read);
        		
        		String writeTh = getTh(writeStr); 
        		Write write = new Write( loc, writeTh, getlIndex(writeStr));
        		Thread2Seq.add2(writeTh, write);
        		
        		Loc2Writes.add2(loc, write);
        		 
        		FlowDep.add2(read, write);
        	}
        }	
        //= ===================local orders;
        StringBuilder builder = new StringBuilder();
        
        for(String th: Thread2Seq.thread2Seq.keySet())
        {
        	List<Access> nodes_per_thread = Thread2Seq.thread2Seq.get(th);
        	
        	// locally sort it first.
        	
    		Comparator<Access> comparator = new Comparator<Access>() {
    		    public int compare(Access c1, Access c2) {
    		        return  c1.localIndex-c2.localIndex ; // use your logic
    		    }
    		};

    		Collections.sort(nodes_per_thread, comparator); // use the comparator as much as u want
//    		System.out.println(nodes_per_thread);
        	
        	//==================
			for (int i = 0; i < nodes_per_thread.size(); i++) {
				if (i + 1 == nodes_per_thread.size())
					break;
				Access thisnode = nodes_per_thread.get(i);
				Access nextnode = nodes_per_thread.get(i + 1);
				builder.append(Z3Packer.assertIt(Z3Packer.hb(thisnode, nextnode)));
			}        	
        }
        
        //=====================dep non-interference.
        for(Read r: FlowDep.readFromWrite.keySet())
        {
        	Write w = FlowDep.readFromWrite.get(r);
        	Set<Write> otherwrites = Loc2Writes.loc2Writes.get(r.loc); 
        	
        	
        	builder.append(Z3Packer.assertIt(Z3Packer.hb(w, r)));
        	
        	for(Write otherW: otherwrites)
        	{
        		if(!otherW.equals(w))
        		   builder.append(Z3Packer.assertIt(Z3Packer.or(Z3Packer.hb(otherW, w), Z3Packer.hb(r, otherW))));
        	}
        	
        }
        
        //======================add order variable decl to z3.
        StringBuilder finalBuilder = Z3Packer.orderVarConstraint().append(builder);
        finalBuilder.append("(check-sat)\n(get-model)\n(exit)");// learn z3 basics to understand this.
        
        
        //=====================dump to solver

      //constraint file
      		String constraint_outdir =System.getProperty("user.dir")+
      				System.getProperty("file.separator")+"z3";
      		
      		try {
      			File smtFile = newOutFile(constraint_outdir, "test.smt");
      			PrintWriter smtWriter = new PrintWriter(new BufferedWriter(new FileWriter(smtFile, false)));
    			smtWriter.println(finalBuilder);
    		    smtWriter.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
      		
      		
      		// ==== invoke z3 via "z3 -smt2 test.smt" in the shell.
        
        
      		
        
	}
	
public static File newOutFile(String path, String name) throws IOException {
		
		File z3Dir = new File(path);
		//Here comes the existence check
		if(!z3Dir.exists())
			z3Dir.mkdirs();
		
		File f = new File(path, name);
		if(f.exists())
		{
		f.delete();
		}
	
		f.createNewFile();
	
		return f;
	}



	private static int getlIndex(String readStr) {
		
		return Integer.parseInt(readStr.split("\\.")[1]);
	}

	private static String getTh(String readStr) {
		return readStr.split("\\.")[0];
	}
	

}
