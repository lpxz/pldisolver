
public class Write extends Access{

	public Write(String locP,String thP , int localindexP)
	{
	     loc = locP;
	     thread = thP;
	     localIndex = localindexP;
	}
	
  
	public boolean equals(Write other)
	{
		return loc == other.loc && thread == other.thread && localIndex == other.localIndex;
	}


}
