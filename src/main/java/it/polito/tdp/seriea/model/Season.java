package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.List;

public class Season implements Comparable<Season>{
	private int season;
	private String description;
	private List<MatchSemplice> matches;
	private Integer puntiClassifica;
	private Integer valoreDifferenzaPesoArchi;

	public Integer getValoreDifferenzaPesoArchi() {
		return valoreDifferenzaPesoArchi;
	}

	public void setValoreDifferenzaPesoArchi(Integer valoreDifferenzaPesoArchi) {
		this.valoreDifferenzaPesoArchi = valoreDifferenzaPesoArchi;
	}

	public Season(int season, String description) {
		super();
		this.season = season;
		this.description = description;
		this.matches = new ArrayList<MatchSemplice>();
		this.puntiClassifica = 0;
	}

	public Integer getPuntiClassifica() {
		return puntiClassifica;
	}

	public void setPuntiClassifica(Integer puntiClassifica) {
		this.puntiClassifica = puntiClassifica;
	}

	public List<MatchSemplice> getMatches() {
		return matches;
	}

	public void setMatches(List<MatchSemplice> matches) {
		this.matches = matches;
	}

	/**
	 * @return the season
	 */
	public int getSeason() {
		return season;
	}

	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param season
	 * the season to set
	 */
	public void setSeason(int season) {
		this.season = season;
	}

	/**
	 * @param description
	 * the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + season;
		return result;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Season other = (Season) obj;
		if (season != other.season)
			return false;
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return this.season+" - "+this.puntiClassifica;
	}

	@Override
	public int compareTo(Season o) {
		return this.season-o.getSeason();
	}

}
