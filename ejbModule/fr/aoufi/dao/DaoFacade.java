package fr.aoufi.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import fr.aoufi.entity.Auteur;
import fr.aoufi.entity.Document;
import fr.aoufi.entity.Localisation;
import fr.aoufi.entity.Theme;
import fr.aoufi.userException.AuteurAffecteException;
import fr.aoufi.userException.DoublonException;
import fr.aoufi.userException.IdException;
import fr.aoufi.userException.InexistantException;
import fr.aoufi.userException.LocalisationAffecteeException;

/**
 * C'est cette couche qui gere les sessions hibernate
 * C'est donc elle qui decide de transformer un proxy en DTO (data transfert object)
 * 
 * Cette classe permet de rediriger les demandes 
 * et de renvoyer des instances java qui ne contiennent pas d'instance hibernate  pour  :
 * 
 * 		getDocument 	ArrayList<Theme> remplace org.hibernate.collection.internal.PersistentBag
 * 		getTheme    	ArrayList<Document> remplace org.hibernate.collection.internal.PersistentBag
 * 		getAuteur   	java.util.Date remplace java.sql.date
 * 		add(Document document)
 * 		etc ...	
 * Elle sera utilisee par toutes les classes externes à la couche DAO
 * 
 */
@Singleton
@LocalBean
public class DaoFacade {
	
	@EJB
	DaoGestion daoGestion;
	@EJB
	DaoListe   daoListe;

	public Document add(Document document) throws DoublonException,	LocalisationAffecteeException {
		if (document != null) {
			document = daoGestion.add(document);
			document = document.getDto();
		}
		return document;
	}

	public Document update(Document document) throws LocalisationAffecteeException {
		if (document != null) {
			document = daoGestion.update(document);
			document = document.getDto();
		}
		return document;
	}

	public void remove(Document document) throws InexistantException {
		if (document != null) {
			daoGestion.remove(document);
		}
	}

	public void removeDocument(String cote) throws InexistantException {
		daoGestion.removeDocument(cote);
		
	}

	public void removeDocument() {
		daoGestion.removeDocument();
		
	}

	public void removeDocumentNative() {
		daoGestion.removeDocumentNative();
		
	}

	public Document getDocument(String cote) throws InexistantException {
		Document document =  daoGestion.getDocument(cote);
		if (document != null) document = document.getDto();
		return document;
	}

	public Localisation add(Localisation localisation) throws DoublonException {
		if (localisation != null) {
			localisation = daoGestion.add(localisation);
			localisation = localisation.getDto();
		}
		return localisation;
	}

	public Localisation update(Localisation localisation) throws DoublonException {
		if (localisation != null) {
			localisation = daoGestion.update(localisation);
			localisation = localisation.getDto();
		}
		return localisation;
	}

	public void remove(Localisation localisation) throws InexistantException, LocalisationAffecteeException {
		daoGestion.remove(localisation);
	}

	public void removeLocalisation(int id) throws InexistantException, LocalisationAffecteeException {
		daoGestion.removeLocalisation(id);
	}

	public void removeLocalisationNative() {
		daoGestion.removeLocalisationNative();
	}

	public void removeLocalisation() {
		daoGestion.removeLocalisation();
	}

	public Localisation getLocalisation(int id) throws InexistantException {
		return daoGestion.getLocalisation(id);
	}

	public Localisation getLocalisation(String lieu, String emp) throws InexistantException {
		return daoGestion.getLocalisation(lieu, emp);
	}

	public Localisation getLocalisation(Localisation localisation) throws InexistantException {
		return daoGestion.getLocalisation(localisation);
	}

	public Integer getLocalisationId(String lieu, String emp) throws InexistantException {
		return daoGestion.getLocalisationId(lieu, emp);
	}

	public Integer getLocalisationId(Localisation localisation) throws InexistantException {
		return daoGestion.getLocalisationId(localisation);
	}

	public boolean isAffecteLocalisation(Localisation localisation) {
		return daoGestion.isAffecteLocalisation(localisation);
	}

	public Auteur add(Auteur auteur) throws DoublonException, IdException {
		return daoGestion.add(auteur);
	}

	public Auteur update(Auteur auteur) {
		return daoGestion.update(auteur);
	}

	public void remove(Auteur auteur) throws InexistantException, AuteurAffecteException {
		daoGestion.remove(auteur);
	}

	public void removeAuteur(String id) throws AuteurAffecteException, InexistantException {
		daoGestion.removeAuteur(id);
	}
	
	public void removeAuteur() throws AuteurAffecteException {
		daoGestion.removeAuteur();
	}

	public void removeAuteurNative() {
		daoGestion.removeAuteurNative();
	}

	public Auteur getAuteur(String id) throws InexistantException {
		Auteur auteur =  daoGestion.getAuteur(id);
		if (auteur != null) auteur = auteur.getDto();
		return auteur;
	}

	public Theme add(Theme theme) throws DoublonException, IdException, LocalisationAffecteeException {
		if (theme != null) {
			theme = daoGestion.add(theme);
			theme = theme.getDto();
		}
		return theme;
	}

	public Theme update(Theme theme) {
		if (theme != null) {
			theme = daoGestion.update(theme);
			theme = theme.getDto();
		}
		return theme;
	}

	public void remove(Theme theme) throws InexistantException {
		daoGestion.remove(theme);
	}

	public void removeThemeById(String id) throws InexistantException {
		daoGestion.removeThemeById(id);
	}

	public void removeTheme() {
		daoGestion.removeTheme();
	}

	public void removeThemeNative() {
		daoGestion.removeThemeNative();
	}

	public Theme getTheme(String id) throws InexistantException {
		Theme theme = daoGestion.getTheme(id);
		if (theme != null) theme = theme.getDto();
		return theme;
	}

	public List<Document> getAllDocumentByCote() {
		ArrayList<Document> liste = new ArrayList<Document>();
		for (Document d : daoListe.getAllDocumentByCote()) {   
			liste.add(d.getDto());
		}
		return liste;
	}

	public List<Document> getAllDocumentByTitre() {
		ArrayList<Document> liste = new ArrayList<Document>();
		for (Document d : daoListe.getAllDocumentByTitre()) {   
			liste.add(d.getDto());
		}
		return liste;
	}

	public List<Localisation> getAllLocalisationById() {
		return daoListe.getAllLocalisationById();
	}

	public List<Localisation> getAllLocalisationByLieu() {
		return daoListe.getAllLocalisationByLieu();
	}

	public List<Auteur> getAllAuteurById() {
		ArrayList<Auteur> liste = new ArrayList<Auteur>();
		for (Auteur a : daoListe.getAllAuteurById()) {   
			liste.add(a.getDto());
		}
		return liste;
	}

	public List<Auteur> getAllAuteurByNom() {
		ArrayList<Auteur> liste = new ArrayList<Auteur>();
		for (Auteur a : daoListe.getAllAuteurByNom()) {   
			liste.add(a.getDto());
		}
		return liste;
	}

	public List<Document> getAllDocumentByTheme(Theme theme) {
		ArrayList<Document> liste = new ArrayList<Document>();
		if (theme != null) {
			for (Document d : daoListe.getAllDocumentByTheme(theme)) {   
				liste.add(d.getDto());
			}
		}
		return liste;
	}

	public List<Theme> getAllThemeById() {
		ArrayList<Theme> liste = new ArrayList<Theme>();
		for (Theme t : daoListe.getAllThemeById()) {   
			liste.add(t.getDto());
		}
		return liste;
	}

	public List<Theme> getAllThemeByNom() {
		ArrayList<Theme> liste = new ArrayList<Theme>();
		for (Theme t : daoListe.getAllThemeById()) {   
			liste.add(t.getDto());
		}
		return liste;
	}

	public List<Theme> getAllThemeByDoc(Document document) {
		ArrayList<Theme> liste = new ArrayList<Theme>();
		if (document != null) {
			for (Theme t : daoListe.getAllThemeById()) {   
				liste.add(t.getDto());
			}
		}		
		return liste;
	}
}
