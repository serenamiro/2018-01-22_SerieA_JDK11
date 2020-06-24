package it.polito.tdp.seriea.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;

import it.polito.tdp.seriea.db.SerieADAO;

public class Model {
	
	private SerieADAO dao;
	private List<Season> mappa;
	private Map<String, Team> teams;
	private Graph<Season, DefaultWeightedEdge> grafo;
	private List<Adiacenza> adiacenze;
	
	List<Season> parziale;
	List<Season> bestPercorso;
	private int LunghezzaBestPercorso;
	
	public Model() {
		this.dao = new SerieADAO();
		teams = new HashMap<String, Team>();
		mappa = new ArrayList<Season>();
		adiacenze = new ArrayList<Adiacenza>();
	}

	public List<Team> getTeams(){
		return dao.listTeams(teams);
	}
	
	public void getSeasonByTeam(Team t) {
		this.mappa = dao.getSeasonsByTeam(t);
	}
	
	public void getMatchesSeason(Team t) {
		for(Season s : mappa) {
			List<MatchSemplice> lista = dao.getMatchSeason(t, s.getSeason(), teams);
			s.setMatches(lista);
		}
	}
	
	public Integer calcolaPuntiSquadra(Team t, List<MatchSemplice> lista) {
		Integer punteggio = 0;
		for(MatchSemplice ms : lista) {
			if(ms.getHomeTeam().getTeam().equals(t.getTeam())) {
				// LA SQUADRA SCELTA GIOCA IN CASA
				switch(ms.getFtr()) {
				case "H":
					punteggio += 3;
					break;
				case "D":
					punteggio += 1;
				}
			} else {
				// LA SQUADRA SCELTA GIOCA FUORI CASA
				switch(ms.getFtr()) {
				case "A":
					punteggio += 3;
					break;
				case "D":
					punteggio += 1;
				}
			}
		}
		return punteggio;
	}
	
	public void getPuntiClassifica(Team t) {
		for(Season s : mappa) {
			s.setPuntiClassifica(calcolaPuntiSquadra(t, s.getMatches()));
		}
	}
	
	public List<Season> getSeasonPunti(Team t){
		getSeasonByTeam(t);
		getMatchesSeason(t);
		getPuntiClassifica(t);
		Collections.sort(mappa);
		return mappa;
	}
	
	public void creaGrafo() {
		this.grafo = new SimpleDirectedWeightedGraph<Season, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		Graphs.addAllVertices(this.grafo, mappa);
		
		this.adiacenze = calcolaAdiacenze();
		for(Adiacenza a : adiacenze) {
			Graphs.addEdgeWithVertices(this.grafo, a.getS1(), a.getS2(), a.getPeso());
		}
		System.out.format("Grafo creato con %d vertici e %d archi\n", this.grafo.vertexSet().size(), this.grafo.edgeSet().size());
	}
	
	public List<Adiacenza> calcolaAdiacenze() {
		List<Adiacenza> adiacenze = new ArrayList<Adiacenza>();
		for(Season s1 : mappa) {
			for(Season s2 : mappa) {
				if(s1.getSeason() > s2.getSeason()) {
					if(s1.getPuntiClassifica()>s2.getPuntiClassifica()) {
						Adiacenza a = new Adiacenza(s1, s2, s1.getPuntiClassifica()-s2.getPuntiClassifica());
						adiacenze.add(a);
					} else {
						Adiacenza a = new Adiacenza(s2, s1, s2.getPuntiClassifica()-s1.getPuntiClassifica());
						adiacenze.add(a);
					}
				}
			}
		}
		return adiacenze;
	}
	
	public int nVertici() {
		return this.grafo.vertexSet().size();
	}

	public int nArchi() {
		return this.grafo.edgeSet().size();
	}
	
	public Season calcolaAnnataDoro() {
		Season best = null;
		Integer valoreSoglia = 0;
		for(Season s : grafo.vertexSet()) {
			if(getDifferenzaPesi(s)>valoreSoglia) {
				best = s;
				valoreSoglia = getDifferenzaPesi(s);
			}
		}
		best.setValoreDifferenzaPesoArchi(valoreSoglia);
		return best;
	}

	private Integer getDifferenzaPesi(Season s) {
		
		Integer entranti = 0;
		Integer uscenti = 0;
		
		for(DefaultWeightedEdge e : grafo.incomingEdgesOf(s)) {
			entranti += (int)grafo.getEdgeWeight(e);
		}
		
		for(DefaultWeightedEdge e : grafo.outgoingEdgesOf(s)) {
			uscenti += (int)grafo.getEdgeWeight(e);
		}
		
		return entranti-uscenti;
	}
	
	
	public List<Season> calcolaCamminoVirtuoso(){
		parziale = new ArrayList<>();
		bestPercorso = new ArrayList<>();
		LunghezzaBestPercorso = 0;
		
		// DEVO LAVORARE SULLE STAGIONI CONSECUTIVE
		Collections.sort(mappa);
		
		// LA RICORSIONE AL LIVELLO INIZIALE DEVE ESSERE LANCIATA DIVERSAMENTE DAGLI ALTRI CASI
		for(Season s : this.grafo.vertexSet()) {
			parziale.add(s);
			cerca(parziale, 1);
			parziale.remove(0);
		}
				
		return bestPercorso;
	}
	
	/*
	 * RICORSIONE
	 * 
	 * Soluzione parziale : lista di season (lista di vertici)
	 * Livello di ricorsione : lunghezza della lista
	 * Casi terminali : non trova altri vertici da aggiungere -> verifica se il cammino ha lunghezza max
	 * 					tra quelli visti
	 * Generazione delle soluzioni : vertici connessi all'ultimo vertice del percorso (con
	 * 				arco orientato nel verso giusto), non ancora parte del percorso, consecutivi (relativi
	 * 				a stagioni consecutive)
	 */

	private void cerca(List<Season> parziale, int livello) {
		
		boolean trovato = false;
		
		Season ultimo = parziale.get(livello-1);
				
		for(Season prossimo: Graphs.predecessorListOf(this.grafo, ultimo)) {
			if(!parziale.contains(prossimo)) {
				// LE STAGIONI DEVONO ESSERE CONSECUTIVE
				if(mappa.indexOf(ultimo)+1 == mappa.lastIndexOf(prossimo)){
					// candidato accettabile -> fai ricorsione
					trovato = true;
					parziale.add(prossimo);
					cerca(parziale, livello+1);
					parziale.remove(livello);
				}
			}
		}
		
		// CASO TERMINALE
		if(!trovato) {
			if(parziale.size()>bestPercorso.size()) {
				bestPercorso = new ArrayList<>(parziale);
			}	
		}
		
	}

	private boolean miglioramento(List<Season> parziale) {
		boolean check = true;
		
		for(int i = 0; i<parziale.size()-1; i++) {
			if(parziale.get(i).getPuntiClassifica()>parziale.get(i+1).getPuntiClassifica()) {
				check = false;
			}
		}
		
		return check;
	}
	
}
