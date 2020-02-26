package kh.java.model;

public class Word {
	
	private String name;
	private String mean1;
	private String mean2;
	
	public Word(){
	}
	
	public Word(String name, String mean1, String mean2) {
		this.name = name;
		this.mean1= mean1;
		this.mean2 = mean2;
	}
	
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getMean1() {
		return mean1;
	}

	public void setMean1(String mean1) {
		this.mean1 = mean1;
	}

	public String getMean2() {
		return mean2;
	}

	public void setMean2(String mean2) {
		this.mean2 = mean2;
	}
	
	@Override
	public String toString() {
		return getName() + "/" + getMean1() + "/" + getMean2();
	}
}