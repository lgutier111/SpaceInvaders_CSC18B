package com.GutierrezLeo.CIS81B.GroupProject.SpaceInvaders;

import java.util.Comparator;

public class VerifyScore implements Comparator<TopScores> {
	
	public int compare (TopScores score1, TopScores score2){
	
		System.out.println("You are in the VerifyScore class");
		System.out.println("It is comparing 2 scores");
		
		int s1 = score1.getScore();
		int s2 = score2.getScore();
		
		if (s1 > s2) {
			return -1;
		} else if (s1 < s2) {
			return +1;
		} else {
			return 0;
		}
	}
}
