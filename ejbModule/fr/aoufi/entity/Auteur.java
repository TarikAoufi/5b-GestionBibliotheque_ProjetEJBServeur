package fr.aoufi.entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Version;

import fr.aoufi.ressources.Param;

@Entity
@Table(name=Param.TABLE_AUTEUR)
public class Auteur implements Serializable {

	@Version
	private static final long serialVersionUID = 1L;
	@Id
	@Column(name = "idAuteur", nullable = false)
	private String id;
	private String nom;
	private String prenom;
	private String nationalite;
	@Temporal(TemporalType.DATE)
	private Date dateNaissance;
	
	// Auteur ne contient pas de collection de Document
	
	public Auteur() {
		super();
	}
	
	public Auteur(String id, String nom, String prenom, String nationalite,
			Date dateNaissance) {
		this.id = id;
		this.nom = nom;
		this.prenom = prenom;
		this.nationalite = nationalite;
		this.dateNaissance = dateNaissance;
	}
	
	@Override
	public String toString() {
		return "Auteur [" + getId() + ", " + getNom() + ", " + getPrenom()
				+ ", " + getNationalite()
				+ ", " + getDateNaissance() + "]";
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getId() {
		return id;
	}
	
	public String getNom() {
		return nom;
	}
	
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	public String getPrenom() {
		return prenom;
	}
	
	public void setPrenom(String prenom) {
		this.prenom = prenom;
	}
	
	public String getNationalite() {
		return nationalite;
	}
	
	public void setNationalite(String nationalite) {
		this.nationalite = nationalite;
	}
	
	public Date getDateNaissance() {
		return dateNaissance;
	}
	
	public void setDateNaissance(Date dateNaissance) {
		this.dateNaissance = dateNaissance;
	}
	
	@Override
	public boolean equals(Object obj) {
		boolean idem = false;
		if (obj instanceof Document) {
			Auteur auteur = (Auteur)obj;
			if (auteur.getId().equals(this.getId())) idem = true;
		}
		return idem;
	}
	
	// l'instance est bonne
	// le risque est que la date soit une java.sql.Date qui n'est pas connue chez le client
	// on la transforme en java.util.Date
	public Auteur getDto() {
		Auteur auteur = new Auteur(this.getId(), this.getNom(), this.getPrenom(), this.getNationalite(),null);
		if (this.getDateNaissance()!= null) auteur.setDateNaissance(new java.util.Date(this.getDateNaissance().getTime()));
		return auteur;
	}
}
