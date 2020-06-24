package it.polito.tdp.seriea.model;

public class MatchSemplice {
	
	private Team homeTeam;
	private Team awayTeam;
	private String ftr;
	
	
	public MatchSemplice(Team homeTeam, Team awayTeam, String ftr) {
		super();
		this.homeTeam = homeTeam;
		this.awayTeam = awayTeam;
		this.ftr = ftr;
	}

	public Team getHomeTeam() {
		return homeTeam;
	}

	public void setHomeTeam(Team homeTeam) {
		this.homeTeam = homeTeam;
	}

	public Team getAwayTeam() {
		return awayTeam;
	}

	public void setAwayTeam(Team awayTeam) {
		this.awayTeam = awayTeam;
	}

	public String getFtr() {
		return ftr;
	}

	public void setFtr(String ftr) {
		this.ftr = ftr;
	}
	
	
	

}
