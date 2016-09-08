package fr.aoufi.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Version;

import fr.aoufi.ressources.Param;
/**
 * bean metier
 * 
 *RG : on peut modifier l'auteur d'un livre (instance d'auteur) mais il ne faut pas
 *     modifier les données de l'auteur au travers du livre
 *     Il faut dans ce cas faire un merge de l'auteur
 *     
 *     la localisation peut etre modifiee a partir du livre
 */
@Entity 
@Table(name=Param.TABLE_DOCUMENT)
public class Document implements Serializable {

	@Version
	private static final long	serialVersionUID	= 1L;

	@Id
	@Column(name = "cote", length=10, nullable = false)
	private String cote;

	@Column(name="titre",  length=60, nullable=false)
	private String titre;

	@Column(name="descr",  length=100, nullable=true)
	private String descriptif;

	@Column(name="nbExemp", nullable=true)
	private int nbExemplaireDispo;

	@OneToOne ( cascade = {CascadeType.REFRESH}, fetch=FetchType.EAGER)
	@JoinColumn(name = "idLocalisation", unique = true, nullable = true)
	private Localisation localisation;

	@ManyToOne(cascade = {CascadeType.REFRESH, CascadeType.PERSIST, CascadeType.MERGE}, fetch=FetchType.LAZY)  
	@JoinColumn(name = "idAuteur", unique = false, nullable = true) 
	private Auteur auteur;

	// relation Document (Many) -> Theme (Many)
	// relation principale 
	// la persistance d'1 document entraine celle de ses themes
	// docTheme (idDocument) est cle etrangere sur id (Document)
	// docTheme (idTheme)    est cle etrangere sur id (Theme)
	// RG : persist document : cascade theme
	// RG : remove doc : pas de cascade
	// RG : merge doc  : pas de cascade (le theme se modifie à part)

	// Document est le proprietaire de la relation document - theme
	// Lorsqu'on supprime des documents, les lignes de la table d'association sont supprimees aussi
	// les themes ne le sont pas sauf si CascadeType.REMOVE
	// cela n'est pas vrai pour la relation inverse (mappedby) dans theme
	@ManyToMany(cascade= { CascadeType.REFRESH, CascadeType.PERSIST})
	@JoinTable(name=Param.TABLE_DOC_THEME,
			   joinColumns = @JoinColumn(name="idDocument"),
			   inverseJoinColumns = @JoinColumn(name="idTheme"))
	private List<Theme> themes = new ArrayList<Theme>();

	public Document() {
		super();
	}

	public Document(String cote, String titre) {
		this.cote = cote;
		this.titre = titre;
		this.descriptif = "non renseigné";
		this.nbExemplaireDispo = 0;
	}

	public Document(String cote, String titre, String descriptif, int nbExemplaireDispo) {
		this(cote,titre);
		this.descriptif = descriptif;
		this.nbExemplaireDispo = nbExemplaireDispo;
	}

	public Document(String cote, String titre, String descriptif, int nbExemplaireDispo, Localisation localisation) {
		this(cote,titre,descriptif,nbExemplaireDispo);
		this.localisation = localisation;
	}

	public String getCote() {
		return cote;
	}
	
	public void setCote(String cote) {
		this.cote = cote;
	}
	
	public String getTitre() {
		return titre;
	}
	
	public void setTitre(String titre) {
		this.titre = titre;
	}
	public String getDescriptif() {
		return descriptif;
	}
	
	public void setDescriptif(String descriptif) {
		this.descriptif = descriptif;
	}
	
	public int getNbExemplaireDispo() {
		return nbExemplaireDispo;
	}
	public void setNbExemplaireDispo(int nbExemplaireDispo) {
		this.nbExemplaireDispo = nbExemplaireDispo;
	}
	
	public Localisation getLocalisation() {
		return localisation;
	}
	
	public void setLocalisation(Localisation localisation) {
		this.localisation = localisation;
	}
	
	public Auteur getAuteur() {
		return auteur;
	}

	public void setAuteur(Auteur auteur) {
		this.auteur = auteur;
	}

	public List<Theme> getThemes() {
		return themes;
	}

	public void setThemes(List<Theme> themes) {
		this.themes = themes;
	}

	public void addTheme(Theme theme) {
		if (theme != null) {
			if (themes == null) themes = new ArrayList<Theme>();
			if (!themes.contains(theme)) {
				themes.add(theme);
				theme.add(this);
			} 
		}
	}
	
	@Override
	public String toString() {
		String chaine = "Document [" + cote + ", " + titre + ", "
				+ descriptif + ", " + nbExemplaireDispo
				+ ", " + localisation + ", " + auteur + "]";
		if (themes   != null) {
			chaine += ", Theme [";
			for (Theme theme : themes) {
				chaine = chaine + theme.getId() + " " + theme.getNom() + " " + theme.getDescription() + "," ;
			}
		}
		chaine += "]";
		return chaine;
	}

	@Override
	public boolean equals(Object obj) {
		boolean idem = false;
		if (obj instanceof Document) {
			Document document = (Document)obj;
			if (document.getCote().equals(this.getCote()) && document.getTitre().equals(this.getTitre())) idem = true;
		}
		return idem;
	}

	// utilise pout obtenir un data Transfert Object
	// Neccessaire pour transformer un proxy (hibernate) en objet transiant (pas dans le contexte)
	// lors de la communication avec l'application cliente
	// sinon, hibernate gère les collections dans un type persistentBag propre a hibernate
	// Pour eviter la propagation du persistentBag (ArrayList<Theme>) dans la couche cliente qui ne le connait pas
	// Il faut le transformer en ArrayList
	public Document getDto () {
		
		Document docDto = new Document(this.getCote(), this.getTitre(), this.getDescriptif(),
				this.getNbExemplaireDispo());
		docDto.setLocalisation(this.getLocalisation());
		if (this.getAuteur() != null) docDto.setAuteur(this.getAuteur().getDto());


		// on ajoute les themes du persistantBag dans le nouveau docDto
		if (this.getThemes() != null) {
			ArrayList<Theme> listeDto = new ArrayList<Theme>();
			for (Theme theme : this.getThemes()) {
				Theme themeDto = theme.getDto();
				// Theme themeDto = new Theme(theme.getId(), theme.getNom(), theme.getDescription());
				// themeDto.setDocuments(theme.getDocuments()); => persistentBag (la liste des documents dans le theme)
				listeDto.add(themeDto);
			}
			docDto.setThemes(listeDto);
		}
		return docDto;
	}
	
	public Document getDtoSansTheme () {

		Document docDto = new Document(this.getCote(), this.getTitre(), this.getDescriptif(),
				this.getNbExemplaireDispo());
		docDto.setLocalisation(this.getLocalisation());
		// on cherche le DTO de auteur car auteur peut contenir une java.sql.Date qui n'est pas connue chez le client
		
		if (this.getAuteur() != null) docDto.setAuteur(this.getAuteur().getDto());
		
		// on met la liste des themes a vide pour éviter de tourner en rond
		docDto.setThemes(new ArrayList<Theme>());
		return docDto;
	}


}
