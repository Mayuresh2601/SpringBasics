package com.objectivedependencies;

public class Student {
	
	private int id;;
	private MathCheat cheating;
	
	public void setId(int id) {
		this.id = id;
	}

	public void setCheating(MathCheat cheating) {
		this.cheating = cheating;
	}
	
	public void cheats() {
		System.out.print(id+" ");
		cheating.mathCheat();
	}

}
