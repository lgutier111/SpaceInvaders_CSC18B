package com.GutierrezLeo.CIS81B.GroupProject.SpaceInvaders;

import java.io.Serializable;

// Class to define the top scores file.
public class TopScores implements Serializable{
	
	// Declare class variables
	public String initials;
	public int score;

	// Set and Get methods for the initials and scores
	public TopScores(String initials, int score){
		
		this.initials = initials;
		this.score = score;
	}

	// Set initials
	public void setInitials(String initials){
		this.initials = initials;
	}
	
	// Get initials
	public String getInitials(){
		return initials;
	}
	
	// Set Score
	public void setScore(int score){
		this.score = score;
	}
	
	// Get Score
	public int getScore(){
		return score;
	}
	
	// The toString method for the scores file
	@Override
	public String toString(){
		return String.format("%s %d,",
				initials, score);
	}
}
