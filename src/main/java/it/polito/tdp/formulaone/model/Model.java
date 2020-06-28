package it.polito.tdp.formulaone.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;
import org.jgrapht.Graphs;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import it.polito.tdp.formulaone.db.FormulaOneDAO;

public class Model {
	
	private FormulaOneDAO dao;
	private Graph<Race, DefaultWeightedEdge> grafo;
	private Map<Integer, Race> idMap;
	private List<Arco> archi;
	
	public Model() {
		this.dao = new FormulaOneDAO();
	}

	public List<Season> tendina(){
		return dao.getAllSeasons();
	}
	
	public void creaGrafo(Season stagione) {
		this.grafo = new SimpleWeightedGraph<Race, DefaultWeightedEdge>(DefaultWeightedEdge.class);
		idMap = new HashMap<>();
		
		Graphs.addAllVertices(this.grafo, dao.getVertici(stagione, idMap));
		
		archi = dao.getArchi(idMap);
		
		for(Arco a : archi) {
			Graphs.addEdge(this.grafo, a.getRace1(), a.getRace2(), a.getPeso());
		}
	}
	
	public Integer nVertici() {
		return this.grafo.vertexSet().size();
	}
	public Integer nArchi() {
		return this.grafo.edgeSet().size();	
	}
	
	public List<Arco> archiMax(){
		List<Arco> archiFiltro  = new ArrayList<Arco>();
		
		Collections.sort(archi); //peso decrescente
		
		archiFiltro.add(archi.get(0)); //max
		Integer pesoMax= archi.get(0).getPeso();
		
		for(Arco a : archi) {
			if(!archiFiltro.contains(a) && (a.getPeso().equals(pesoMax))){
				archiFiltro.add(a);
			}
		
		}
		
	
		return archiFiltro;
	}
	
	public List<Race> getTendinaVertici(){
		List<Race> vertici = new ArrayList<>(this.grafo.vertexSet());
		
		return vertici;
	}
	
	public Map<Pilota, Integer> punteggiPilota(Race r, Double probabilita, Long sosta){
		Simulatore sim = new Simulatore();
		sim.init(r, probabilita, sosta);
		sim.run();
		return sim.getTraguardo();
		
	}
}
