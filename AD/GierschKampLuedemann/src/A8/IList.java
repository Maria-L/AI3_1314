package A8;


public interface IList {
	public Object head();
	public IList tail();
	public int length();
	public Object top();
	public void head(Object n);
	public boolean insert(Object n, int i);
	public String toString();
	public boolean isIncreasingMonoton();
}
