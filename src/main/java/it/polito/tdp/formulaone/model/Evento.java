package it.polito.tdp.formulaone.model;

import java.time.LocalTime;

public class Evento implements Comparable<Evento>{
	
	public enum Tipo{
		PARTENZA,
		FINE_GIRO,
		PAUSA
	}
	
	private Tipo tipo;
	private LocalTime tempi;
	private Pilota pilota;
	private Integer nGiro;
	
	public Evento(Tipo tipo, LocalTime tempi, Pilota pilota, Integer nGiro) {
		super();
		this.tipo = tipo;
		this.tempi = tempi;
		this.pilota = pilota;
		this.nGiro = nGiro;
	}

	public Tipo getTipo() {
		return tipo;
	}

	public LocalTime getTempi() {
		return tempi;
	}

	public Pilota getPilota() {
		return pilota;
	}

	public Integer getnGiro() {
		return nGiro;
	}

	@Override
	public int compareTo(Evento o) {
	
		return this.tempi.compareTo(o.getTempi());
	}
	

}
