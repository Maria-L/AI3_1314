package A8;


public interface IList {
	public Object head();
	public IList tail();
	public void tail(IList list);
	public int length();
	public Object top();
	public IList mergeSort();
	public IList merge(IList list);
	public void head(Object n);
	public boolean insert(Object n, int i);
	public String toString();
	public boolean isIncreasingMonoton();
	public IList random(int n);
	public IList sortIncreasingMonoton(int n);
	//public IList append (IList last, Object n);
}
