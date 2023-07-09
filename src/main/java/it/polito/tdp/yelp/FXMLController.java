/**
 * Sample Skeleton for 'Scene.fxml' Controller Class
 */

package it.polito.tdp.yelp;

import java.net.URL;
import java.util.ResourceBundle;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.yelp.model.Business;
import it.polito.tdp.yelp.model.Model;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class FXMLController {
	
	private Model model;

    @FXML // ResourceBundle that was given to the FXMLLoader
    private ResourceBundle resources;

    @FXML // URL location of the FXML file that was given to the FXMLLoader
    private URL location;

    @FXML // fx:id="btnCreaGrafo"
    private Button btnCreaGrafo; // Value injected by FXMLLoader

    @FXML // fx:id="btnDistante"
    private Button btnDistante; // Value injected by FXMLLoader

    @FXML // fx:id="btnCalcolaPercorso"
    private Button btnCalcolaPercorso; // Value injected by FXMLLoader

    @FXML // fx:id="txtX2"
    private TextField txtX2; // Value injected by FXMLLoader

    @FXML // fx:id="cmbCitta"
    private ComboBox<String> cmbCitta; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB1"
    private ComboBox<Business> cmbB1; // Value injected by FXMLLoader

    @FXML // fx:id="cmbB2"
    private ComboBox<Business> cmbB2; // Value injected by FXMLLoader

    @FXML // fx:id="txtResult"
    private TextArea txtResult; // Value injected by FXMLLoader
    
    @FXML
    void doCreaGrafo(ActionEvent event) {
    	
    	if (this.cmbCitta.getValue() == null) {
    		txtResult.setText("Scegli una città!");
    		return;
    	}
    	
    	
    	
    	SimpleWeightedGraph<Business,DefaultWeightedEdge> graph = model.creaGrafo(this.cmbCitta.getValue());
    	
    	txtResult.setText("Grafo creato con " + graph.vertexSet().size() + " vertici e " + graph.edgeSet().size() + " archi.\n\n");
    	
    	this.btnDistante.setDisable(false);
    	this.btnCalcolaPercorso.setDisable(false);
    	
    	this.cmbB1.getItems().clear();
    	this.cmbB2.getItems().clear();
    	
    	this.cmbB1.getItems().addAll(graph.vertexSet());
    	this.cmbB2.getItems().addAll(graph.vertexSet());
    	
    	
    }

    @FXML
    void doCalcolaLocaleDistante(ActionEvent event) {
    	
    	if (this.cmbB1.getValue() == null) {
    		txtResult.setText("Scegli un locale (b1)!");
    		return;
    	}
    	
    	txtResult.setText("LOCALE PIU' DISTANTE: \n" + model.trovaDistante(cmbB1.getValue()) + " = " + model.getPesoMax());
    	
    }

    @FXML
    void doCalcolaPercorso(ActionEvent event) {
    	
    	if (this.cmbB1.getValue() == null) {
    		txtResult.setText("Scegli un locale (b1)!");
    		return;
    	}
    	
    	if (this.cmbB2.getValue() == null) {
    		txtResult.setText("Scegli un locale (b2)!");
    		return;
    	}
    	
    	if (this.txtX2.getText() == null) {
    		txtResult.setText("Inserisci una soglia");
    	}
    	
    	try {
    		double x = Double.parseDouble(txtX2.getText());
    	}
    	catch(Exception e) {
    		txtResult.setText("Inserisci un valore numerico!");
    		return;
    	}
    	
    	txtResult.setText("PERCORSO: " + (model.distanzaTot(model.trovaPercorso(cmbB1.getValue(), cmbB2.getValue(), Double.parseDouble(txtX2.getText())))) + '\n');
    	
    	if ( model.trovaPercorso(cmbB1.getValue(), cmbB2.getValue(), Double.parseDouble(txtX2.getText())).size() == 0)
    		txtResult.appendText("Non esiste un percorso con le specifiche desiderate");
    	else
	    	for (Business b : model.trovaPercorso(cmbB1.getValue(), cmbB2.getValue(), Double.parseDouble(txtX2.getText()))) {
	    		txtResult.appendText(b.toString() + '\n');
	    	}

    }


    @FXML // This method is called by the FXMLLoader when initialization is complete
    void initialize() {
        assert btnCreaGrafo != null : "fx:id=\"btnCreaGrafo\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnDistante != null : "fx:id=\"btnDistante\" was not injected: check your FXML file 'Scene.fxml'.";
        assert btnCalcolaPercorso != null : "fx:id=\"btnCalcolaPercorso\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtX2 != null : "fx:id=\"txtX2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbCitta != null : "fx:id=\"cmbCitta\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB1 != null : "fx:id=\"cmbB1\" was not injected: check your FXML file 'Scene.fxml'.";
        assert cmbB2 != null : "fx:id=\"cmbB2\" was not injected: check your FXML file 'Scene.fxml'.";
        assert txtResult != null : "fx:id=\"txtResult\" was not injected: check your FXML file 'Scene.fxml'.";

    }
    
    public void setModel(Model model) {
    	this.model = model;
    	
    	cmbCitta.getItems().clear();
    	
    	cmbCitta.getItems().addAll(model.getAllCities());
    	
    	this.btnDistante.setDisable(true);
    	this.btnCalcolaPercorso.setDisable(true);
    }
}
