package fr.aoufi.entity;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.persistence.Version;

import fr.aoufi.ressources.Param;

/**
 * bean metier
 * 
 *
 */
@Entity
@Table(name=Param.TABLE_LOCALISATION)
public class Localisation implements Serializable {
	
	@Version
	private static final long	serialVersionUID	= 1L;
	
	@Id
	@Column(name = "idLocalisation", nullable = false)
	@GeneratedValue(strategy=GenerationType.AUTO, generator="seqLoca")
	@SequenceGenerator(name="seqLoca", sequenceName="LOCA_SEQ", initialValue=1, allocationSize=1) // allocation size increment
	private int idLocalisation;

	@Column(length=10, nullable = false)
	private String lieu;
	
	@Column(length=10, nullable = false)
	private String emp;

	public Localisation() { }

	public Localisation(int idLocalisation, String lieu, String emp) {
		this.idLocalisation = idLocalisation;
		this.lieu = lieu;
		this.emp  = emp;	// emplacement
	}
	
	public Localisation(String lieu, String emp) {
		this.lieu = lieu;
		this.emp  = emp;	// emplacement
	}

	public String getLieu() {
		return lieu;
	}
	
	public void setLieu(String lieu) {
		this.lieu = lieu;
	}
	
	public String getEmp() {
		return emp;
	}
	
	public void setEmp(String emp) {
		this.emp = emp;
	}
	
	public int getIdLocalisation() {
		return idLocalisation;
	}
	
	public void setIdLocalisation(int idLocalisation) {
		this.idLocalisation = idLocalisation;
	}
	
	@Override
	public String toString() {
		return "Localisation [" + idLocalisation + ", lieu="
				+ lieu + ", emp=" + emp + "]";
	}
	
	public Localisation getDto() {
		return new Localisation(this.getIdLocalisation(), this.getLieu(), this.getEmp());
	}
	

}
