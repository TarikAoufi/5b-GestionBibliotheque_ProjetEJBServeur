package fr.aoufi.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.PrePersist;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.persistence.Version;

import fr.aoufi.ressources.Param;

@Entity 
@Table(name=Param.TABLE_THEME)
public class Theme implements Serializable {

	@Version
	private static final long serialVersionUID = 1L;

	@Id
	@Column(name = "idTheme")
	private String id;

	@Column(name="nom",  length=30, nullable=false)
	private String nom;

	@Column(name="descr",  length=60, nullable=true)
	private String description;

	// Le theme n'est pas le proprietaire de la relation document - theme
	// si on supprime un document de la collection, alors il faut gerer soit meme la suppression de l'association
	@ManyToMany( mappedBy = "themes", fetch=FetchType.LAZY)
	private Collection<Document> documents = new ArrayList<Document>();

	// avant remove : on parcourt la collection de documents et on enleve les themes à la main
	@PreRemove
	private void removeThemeFromDocument() {
		if (documents != null) {
			for (Document document : documents) {
				document.getThemes().remove(this);
			}
		}
	}
	
	@PrePersist
	private void addThemeInDocument() {
		if (documents != null) {
			for (Document document : documents) {
				document.addTheme(this);
			}
		}
	}

	//	@Transient
	//	private String pasPersist;

	public Theme() { }

	public Theme(String id, String nom, String description) {
		this.id = id;
		this.nom = nom;
		this.description = description;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return id;
	}

	public void setNom(String nom) {
		this.nom = nom;
	}

	public String getNom() {
		return nom;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getDescription() {
		return description;
	}

	public Collection<Document> getDocuments() {
		return documents;
	}

	public void setDocuments(Collection<Document> documents) {
		this.documents = documents;
	}

	public void add(Document document) {
		if (document != null) {
			if (documents == null) documents = new ArrayList<Document>();
			if (!documents.contains(document)) {
				documents.add(document);
				document.addTheme(this);
			} 
		}
	}

	@Override
	public String toString() {
		String chaine = ""; 
		chaine += "Theme [" + getId() + ", " + getNom() + ", "
				+ getDescription();
		
		if (documents   != null) {
			chaine += ", Document [";
			for (Document document : documents) {
				chaine = chaine + document.getCote() + " " + document.getTitre() + " " + document.getDescriptif() + "," ;
			}
		}
		chaine += "]";
		return chaine;
	}

	// Voir classe Document pour les explications
	public Theme getDto () {

		Theme themeDto = new Theme(this.getId(), this.getNom(), this.getDescription());

		// on ajoute les documents du persistantBag dans le nouveau themeDto mais on ne charge pas
		// les themes des documents 
		
		if (this.getDocuments()!= null) {
			ArrayList<Document> listeDto = new ArrayList<Document>();
			for (Document document : this.getDocuments()) {
				listeDto.add(document.getDtoSansTheme());	
			}
			themeDto.setDocuments(listeDto);
		}
		return themeDto;
	}

}
