package it.polito.tdp.formulaone;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

import it.polito.tdp.formulaone.model.Arco;
import it.polito.tdp.formulaone.model.Model;
import it.polito.tdp.formulaone.model.Pilota;
import it.polito.tdp.formulaone.model.Race;
import it.polito.tdp.formulaone.model.Season;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Season> boxAnno;

    @FXML
    private Button btnSelezionaStagione;

    @FXML
    private ComboBox<Race> boxGara;

    @FXML
    private Button btnSimulaGara;

    @FXML
    private TextField textInputK;

    @FXML
    private TextField textInputK1;

    @FXML
    private TextArea txtResult;

    @FXML
    void doSelezionaStagione(ActionEvent event) {
    	this.txtResult.clear();
    	Season stagione = this.boxAnno.getValue();
    	model.creaGrafo(stagione);
    	
    	this.txtResult.appendText("Grafo creato!\n#vertici: "+model.nVertici()+"\n#archi: "+model.nArchi()+"\n");
    	
    	this.txtResult.appendText("Arco/Archi di peso max: \n");
    	
    	for(Arco a : model.archiMax()) {
    		this.txtResult.appendText(a.toString()+"\n");
    	}
    	
    	this.boxGara.getItems().clear();
    	this.boxGara.getItems().addAll(model.getTendinaVertici());
    	this.boxGara.setValue(model.getTendinaVertici().get(0));

    }

    @FXML
    void doSimulaGara(ActionEvent event) {
    	
    	this.txtResult.clear();
    	Race r = this.boxGara.getValue();
    	
    	try {
	    	Double prob = Double.valueOf(this.textInputK.getText());
	    	Long sosta = Long.valueOf(this.textInputK1.getText());
	    	
	    	this.txtResult.appendText("Punteggi per pilota: \n");
	    	Map<Pilota, Integer> mappa = model.punteggiPilota(r, prob, sosta);
	    	
	    	for(Pilota p: mappa.keySet()) {
	    		this.txtResult.appendText(p.toString()+" "+mappa.get(p)+"\n");
	    	}
	    	
    	
    	
    	}catch(NumberFormatException nfe) {
    		this.txtResult.appendText("Inserire valori corretti");
    	}

    }

    @FXML
    void initialize() {
        assert boxAnno != null : "fx:id=\"boxAnno\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert btnSelezionaStagione != null : "fx:id=\"btnSelezionaStagione\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert boxGara != null : "fx:id=\"boxGara\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert btnSimulaGara != null : "fx:id=\"btnSimulaGara\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert textInputK != null : "fx:id=\"textInputK\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert textInputK1 != null : "fx:id=\"textInputK1\" was not injected: check your FXML file 'FormulaOne.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'FormulaOne.fxml'.";

    }

	public void setModel(Model model) {
		this.model = model;
		this.boxAnno.getItems().addAll(this.model.tendina());
		this.boxAnno.setValue(model.tendina().get(0));
	}
}
