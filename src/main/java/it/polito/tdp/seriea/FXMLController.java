package it.polito.tdp.seriea;

import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.seriea.model.Model;
import it.polito.tdp.seriea.model.Season;
import it.polito.tdp.seriea.model.Team;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ChoiceBox<Team> boxSquadra;

    @FXML
    private Button btnSelezionaSquadra;

    @FXML
    private Button btnTrovaAnnataOro;

    @FXML
    private Button btnTrovaCamminoVirtuoso;

    @FXML
    private TextArea txtResult;

    @FXML
    void doSelezionaSquadra(ActionEvent event) {
    	txtResult.clear();
    	Team t = boxSquadra.getValue();
    	if(t==null) {
    		txtResult.appendText("Selezionare una squadra per proseguire.\n");
    		return;
    	}
    	List<Season> res = model.getSeasonPunti(t);
    	if(res == null) {
    		txtResult.appendText("Non è stato possibile caricare i punti.\n");
    	} else {
    		txtResult.appendText("STAGIONI - PUNTI CLASSIFICA: \n");
    		for(Season s : res) {
    			txtResult.appendText(s.toString()+"\n");
    		}
    	}
    }

    @FXML
    void doTrovaAnnataOro(ActionEvent event) {
    	txtResult.clear();
    	Team t = boxSquadra.getValue();
    	if(t==null) {
    		txtResult.appendText("Selezionare una squadra per proseguire.\n");
    		return;
    	}
    	model.creaGrafo();
    	txtResult.appendText("Grafo creato\n");
		txtResult.appendText("vertici: "+model.nVertici()+"\n");
		txtResult.appendText("archi: "+model.nArchi()+"\n");
		
		Season annataDOro= model.calcolaAnnataDoro();
		if(annataDOro != null) {
			txtResult.appendText("\nAnnata d'oro:\n");
			txtResult.appendText(annataDOro.getSeason()+" (differenza peso "+annataDOro.getValoreDifferenzaPesoArchi()+")\n");
		} else {
			txtResult.appendText("Non è stata calcolata l'annata d'oro.\n");
		}
		
    }

    @FXML
    void doTrovaCamminoVirtuoso(ActionEvent event) {
    	txtResult.clear();
    	Team t = boxSquadra.getValue();
    	if(t==null) {
    		txtResult.appendText("Selezionare una squadra per proseguire.\n");
    		return;
    	}
    	List<Season> best = model.calcolaCamminoVirtuoso();
    	if(best!=null) {
    		for(Season s : best) {
    			txtResult.appendText(s.getSeason()+" - "+s.getPuntiClassifica()+"\n");
    		}
    	} else {
    		txtResult.appendText("Non è stato possibile calcolare l'annata migliore.\n");
    	}

    }

    @FXML
    void initialize() {
        assert boxSquadra != null : "fx:id=\"boxSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnSelezionaSquadra != null : "fx:id=\"btnSelezionaSquadra\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaAnnataOro != null : "fx:id=\"btnTrovaAnnataOro\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert btnTrovaCamminoVirtuoso != null : "fx:id=\"btnTrovaCamminoVirtuoso\" was not injected: check your FXML file 'SerieA.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'SerieA.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		boxSquadra.getItems().addAll(model.getTeams());
	}
}
