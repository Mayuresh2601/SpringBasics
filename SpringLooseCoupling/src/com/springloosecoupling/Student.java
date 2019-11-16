package com.springloosecoupling;

public class Student {
	
	private Cheats cheat;

	
	public void setCheat(Cheats cheat) {
		this.cheat = cheat;
	}


	public void cheat() {
		
		cheat.cheating();
	}

}
