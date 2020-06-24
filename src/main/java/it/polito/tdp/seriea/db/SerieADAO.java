package it.polito.tdp.seriea.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.polito.tdp.seriea.model.Match;
import it.polito.tdp.seriea.model.MatchSemplice;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;

public class SerieADAO {

	public List<Season> listAllSeasons() {
		String sql = "SELECT season, description FROM seasons";
		List<Season> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Season(res.getInt("season"), res.getString("description")));
			}

			conn.close();
			return result;

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	public List<Team> listTeams(Map<String, Team> mappa) {
		String sql = "SELECT team FROM teams";
		List<Team> result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				result.add(new Team(res.getString("team")));
				mappa.put(res.getString("team"), new Team(res.getString("team")));
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Season> getSeasonsByTeam(Team t){
		String sql = "SELECT distinct Season " + 
				"FROM matches " + 
				"WHERE (HomeTeam=? OR AwayTeam=?)";
		
		List<Season>  result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, t.getTeam());
			st.setString(2, t.getTeam());
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Season s = new Season(res.getInt("Season"), null);
				result.add(s);
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
	}

	public List<MatchSemplice> getMatchSeason(Team t, Integer anno, Map<String, Team> mappa){
		String sql = "SELECT HomeTeam, AwayTeam, FTR " + 
				"FROM matches " + 
				"WHERE (HomeTeam=? OR AwayTeam=?) AND Season=?";
		
		List<MatchSemplice>  result = new ArrayList<>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setString(1, t.getTeam());
			st.setString(2, t.getTeam());
			st.setInt(3, anno);
			ResultSet res = st.executeQuery();

			while (res.next()) {
				Team home = mappa.get(res.getString("HomeTeam"));
				Team away = mappa.get(res.getString("AwayTeam"));
				MatchSemplice m = new MatchSemplice(home, away, res.getString("FTR"));
				result.add(m);
			}

			conn.close();
			return result;
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
		
		
	}
	
}

