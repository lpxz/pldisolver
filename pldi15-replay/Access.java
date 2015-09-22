
public abstract class Access {
	public String loc ; 
    public String thread;
    
    public int localIndex ;
    
    public String toString()
    {
    	return thread+localIndex;
    }
}
