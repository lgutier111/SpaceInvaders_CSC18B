package com.GutierrezLeo.CIS81B.GroupProject.SpaceInvaders;

import java.io.Serializable;

public class TopScores implements Serializable{
	
	public String initials;
	public int score;
	
	public TopScores(String initials, int score){
		
		System.out.println("You are in the TopScores constructor");
		
		this.initials = initials;
		this.score = score;
	}

	public String getInitials(){
		return initials;
	}
	
	public int getScore(){
		return score;
	}
	
	@Override
	public String toString(){
		return String.format("%s %d,",
				initials, score);
	}
}
