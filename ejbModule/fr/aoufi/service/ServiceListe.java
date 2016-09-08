package fr.aoufi.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import fr.aoufi.dao.DaoFacade;
import fr.aoufi.entity.Auteur;
import fr.aoufi.entity.Document;
import fr.aoufi.entity.Localisation;
import fr.aoufi.entity.Theme;

@Singleton
@LocalBean
public class ServiceListe {

	@EJB
	DaoFacade daoFacade;
	
	public List<Document> getAllDocumentByCote() {
		return daoFacade.getAllDocumentByCote();
	}
	
	public List<Document> getAllDocumentByTitre() {
		return daoFacade.getAllDocumentByTitre();
	}
	
	public List<Localisation> getAllLocalisationById() {
		return daoFacade.getAllLocalisationById();
	}
	
	public List<Localisation> getAllLocalisationByLieu() {
		return daoFacade.getAllLocalisationByLieu();
	}
	
	public List<Auteur> getAllAuteurById() {
		return daoFacade.getAllAuteurById();
	}
	
	public List<Auteur> getAllAuteurByNom() {
		return daoFacade.getAllAuteurByNom();
	}
	
	public List<Document> getAllDocumentByTheme(Theme theme) {
		return daoFacade.getAllDocumentByTheme(theme);
	}
	
	public List<Theme> getAllThemeById() {
		return daoFacade.getAllThemeById();
	}
	
	public List<Theme> getAllThemeByNom() {
		return daoFacade.getAllThemeByNom();
	}
	
	public List<Theme> getAllThemeByDoc(Document document) {
		return daoFacade.getAllThemeByDoc(document);
	}
}
