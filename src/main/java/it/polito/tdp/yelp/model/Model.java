package it.polito.tdp.yelp.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import com.javadocmd.simplelatlng.LatLngTool;
import com.javadocmd.simplelatlng.util.LengthUnit;

import it.polito.tdp.yelp.db.YelpDao;

public class Model {
	
	YelpDao dao;
	SimpleWeightedGraph<Business,DefaultWeightedEdge> graph;
	
	public Model() {
		dao = new YelpDao();
	}
	
	public List<String> getAllCities(){
		return dao.getAllCities();
	}
	
	public SimpleWeightedGraph<Business,DefaultWeightedEdge> creaGrafo(String city){
		
		graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
		
		List<Business> vertex = dao.getBusinessCity(city);
		
		Graphs.addAllVertices(graph, vertex);
		
		for (Business b1 : graph.vertexSet())
			for (Business b2 : graph.vertexSet())
				if (!b1.equals(b2) && !graph.containsEdge(b2, b1)) {
					Graphs.addEdge(graph, b1, b2, LatLngTool.distance(b1.getCoordinates(), b2.getCoordinates(), LengthUnit.PRIMARY));
				}
		
		return graph;
	}
	
	private double pesoMax;
	
	public Business trovaDistante(Business b) {
		
		pesoMax = 0;
		
		for (Business vicino : Graphs.neighborListOf(graph, b)) {
			if (graph.getEdgeWeight(graph.getEdge(vicino, b)) > pesoMax)
				pesoMax = graph.getEdgeWeight(graph.getEdge(vicino, b));
		}
		
		for (Business vicino : Graphs.neighborListOf(graph, b)) {
			if (graph.getEdgeWeight(graph.getEdge(vicino, b)) == pesoMax)
				return vicino;
		}
		
		return null;
		
	}
	
	private List<Business> soluzione;
	
	public List<Business> trovaPercorso(Business partenza, Business arrivo, double soglia) {
		
		List<Business> parziale = new ArrayList<>();
		soluzione = new ArrayList<>();
		
		parziale.add(partenza);
		
		ricorsiva(partenza,arrivo,parziale,soglia);
		
		return soluzione;
		
	}
	
	private void ricorsiva(Business corrente, Business arrivo, List<Business> parziale, double soglia) {
		
		if (corrente.equals(arrivo) && parziale.size() > soluzione.size()) {
			soluzione = new ArrayList<>(parziale);
		}
		
		Set<Business> vicini = Graphs.neighborSetOf(graph, corrente);
		
		for (Business vicino : vicini) {
			if (!parziale.contains(vicino) && vicino.getStars() > soglia) {
				parziale.add(vicino);
				ricorsiva(vicino,arrivo,parziale,soglia);
				parziale.remove(parziale.size()-1);
			}
		}
		
	}
	
	public double distanzaTot(List<Business> soluzione) {
		
		double tot = 0.0;
		
		for (int i=0; i<soluzione.size()-1; i++) {
			tot += graph.getEdgeWeight(graph.getEdge(soluzione.get(i), soluzione.get(i+1)));
		}
		return tot;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	

	public SimpleWeightedGraph<Business, DefaultWeightedEdge> getGraph() {
		return graph;
	}

	public void setGraph(SimpleWeightedGraph<Business, DefaultWeightedEdge> graph) {
		this.graph = graph;
	}

	public double getPesoMax() {
		return pesoMax;
	}

	public void setPesoMax(double pesoMax) {
		this.pesoMax = pesoMax;
	}
}
