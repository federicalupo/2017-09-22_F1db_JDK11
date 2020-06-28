package it.polito.tdp.formulaone.model;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;

import it.polito.tdp.formulaone.db.FormulaOneDAO;
import it.polito.tdp.formulaone.model.Evento.Tipo;

public class Simulatore {
	
	//mondo
	private List<Pilota> piloti; //non tutti hanno tempi giro
	private FormulaOneDAO dao;
	private Race gara;
	private Map<Integer, Pilota> primi;
	
	//input
	private Double probabilita;
	private Long sosta; //secondi
	
	//coda
	private PriorityQueue<Evento> coda;
	
	
	
	//output
	private Map<Pilota, Integer> traguardo;  //hashcode+equals
	
	public void init(Race r, Double probabilita, Long sosta) {
		this.dao = new FormulaOneDAO();
		this.primi = new HashMap<>();
		this.traguardo = new HashMap<>();
		piloti = dao.piloti(r);
		this.probabilita = probabilita;
		this.sosta = sosta;
		coda = new PriorityQueue<>();
		this.gara = r;
		
		
		for(Pilota p : piloti) {
			this.traguardo.put(p, 0);
			Evento e = new Evento(Tipo.PARTENZA, LocalTime.of(0, 0, 0) , p, 1);
			coda.add(e);
		}
		
		
	}
	public void run() {
		while(!coda.isEmpty()) {
			processEvent(coda.poll());
		}
		
	}
	private void processEvent(Evento e) {
		switch(e.getTipo()) {
		case PARTENZA:
			
			//pausa
			Double random = Math.random();
			
			if(random.compareTo(this.probabilita)<0) {
				
				Evento ev = new Evento(Tipo.PAUSA, e.getTempi().plusSeconds(this.sosta), e.getPilota(), e.getnGiro());
				coda.add(ev);
				
			}else {
				Long millisecondi = dao.getMillisecondi(gara, e.getPilota(), e.getnGiro());
				
				if(millisecondi!=null) { // c'è il dato  
					Long secondi = Duration.ofMillis(millisecondi).toSeconds(); //importante
					
					
					Evento ev = new Evento(Tipo.FINE_GIRO, e.getTempi().plusSeconds(secondi), e.getPilota(), e.getnGiro());
					coda.add(ev);
				}
			}
			
			break;
			
		case FINE_GIRO:
			
			if(!primi.containsKey(e.getnGiro())) { //sei primo
				primi.put(e.getnGiro(), e.getPilota());  //aggiorna i giri
				this.traguardo.put(e.getPilota(), this.traguardo.get(e.getPilota())+1); //aggiorna punteggio
			}
			
			//altro giro
			
			Evento ev = new Evento(Tipo.PARTENZA, e.getTempi(), e.getPilota(), e.getnGiro()+1);
			coda.add(ev);
			
			
			
			break;
			
		case PAUSA:
			Long millisecondi = dao.getMillisecondi(gara, e.getPilota(), e.getnGiro());
			if(millisecondi!=null) {
				Long secondi = Duration.ofMillis(millisecondi).toSeconds(); //importante
				
				Evento evento = new Evento(Tipo.FINE_GIRO, e.getTempi().plusSeconds(secondi), e.getPilota(), e.getnGiro());
				coda.add(evento);
			}
			
			break;
			
		}
		
	}
	public Map<Pilota, Integer> getTraguardo() {
		return traguardo;
	}
	
	

}
