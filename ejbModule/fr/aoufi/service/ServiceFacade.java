package fr.aoufi.service;

import java.util.List;

import javax.ejb.EJB;
import javax.ejb.Remote;
import javax.ejb.Stateless;

import fr.aoufi.clientServer.IServiceFacade;
import fr.aoufi.clientServer.UserException;
import fr.aoufi.entity.Auteur;
import fr.aoufi.entity.Document;
import fr.aoufi.entity.Localisation;
import fr.aoufi.entity.ThemeDoc;

@Stateless
@Remote(IServiceFacade.class)
public class ServiceFacade implements IServiceFacade {

	@EJB
	ServiceGestion serviceGestion;
	@EJB
	ServiceListe serviceListe;


	@Override
	public Document ajouter(Document document) throws UserException {
		return serviceGestion.ajouter(document);
	}

	@Override
	public Document update (Document document) throws UserException {
		return serviceGestion.update(document);
	}
	
	@Override
	public void remove(Document document) throws UserException {
		serviceGestion.remove(document);
	}
	
	@Override
	public void removeDocument(String cote) throws UserException {
		serviceGestion.removeDocument(cote);
	}
	
	@Override
	public void removeDocumentNative() {
		serviceGestion.removeDocumentNative();
	}
	
	@Override
	public void removeDocument() {
		serviceGestion.removeDocument();
	}
	
	@Override
	public Document getDocument(String cote) throws UserException {
		return serviceGestion.getDocument(cote);
	}

	/* ==========================================  
	 * 			GESTION LOCALISATION
	 * ========================================== */
	@Override
	public Localisation ajouter(Localisation localisation) throws UserException {
		return serviceGestion.ajouter(localisation);
	}
	
	@Override
	public Localisation update (Localisation localisation) throws UserException {
		return serviceGestion.update(localisation);
	}
	
	@Override
	public void remove(Localisation localisation) throws UserException {
		serviceGestion.remove(localisation);
	}
	
	@Override
	public void removeLocalisation(int id) throws UserException {
		serviceGestion.removeLocalisation(id);
	}
	
	@Override
	public void removeLocalisationNative() {
		serviceGestion.removeLocalisationNative();
	}
	
	@Override
	public void removeLocalisation() {
		serviceGestion.removeLocalisation();
	}

	/**
	 * Retourne localisation par id
	 * throws DaoException si pas trouve
	 * @throws UserException 
	 */
	@Override
	public Localisation getLocalisation(int id) throws UserException {
		return serviceGestion.getLocalisation(id);
	}

	/**
	 * Retourne localisation par lieu, emp
	 * null si pas trouve
	 * @throws UserException 
	 */
	@Override
	public Localisation getLocalisation(String lieu, String emp) throws UserException {
		return serviceGestion.getLocalisation(lieu, emp);
	}

	/**
	 * Retourne localisation par lieu, emp de l'objet localisation
	 * null si pas trouve
	 * @throws UserException 
	 */
	@Override
	public Localisation getLocalisation(Localisation localisation) throws UserException {
		return serviceGestion.getLocalisation(localisation);
	}

	/**
	 * Retourne id de localisation
	 * null si pas trouve
	 * @throws UserException 
	 */
	@Override
	public Integer getLocalisationId(String lieu, String emp) throws UserException {
		return serviceGestion.getLocalisationId(lieu, emp);
	}

	/**
	 * Retourne id de localisation
	 * null si pas trouve
	 * @throws UserException 
	 */
	@Override
	public Integer getLocalisationId(Localisation localisation) throws UserException {
		return serviceGestion.getLocalisationId(localisation);
	}

	/**
	 *  Verifie si la localisation est affectee a un document
	 */
	@Override
	public boolean isAffecteLocalisation(Localisation localisation) {		
		return serviceGestion.isAffecteLocalisation(localisation);
	}
	
	
	/* ==========================================  
	 * 			GESTION AUTEUR
	 * ========================================== */
	@Override
	public Auteur ajouter(Auteur auteur) throws UserException {
		return serviceGestion.ajouter(auteur);
	}

	@Override
	public Auteur update(Auteur auteur) {
		return serviceGestion.update(auteur);
	}

	@Override
	public void remove(Auteur auteur) throws UserException {
		serviceGestion.remove(auteur);
		
	}
	
	@Override	
	public void removeAuteur(String id) throws UserException {
		serviceGestion.removeAuteur(id);
	}

	@Override
	public void removeAuteur() throws UserException {
		serviceGestion.removeAuteur();
		
	}

	@Override
	public void removeAuteurNative() {
		serviceGestion.removeAuteurNative();
		
	}

	@Override
	public Auteur getAuteur(String id) throws UserException {
		return serviceGestion.getAuteur(id);
	}
	
	/* ==========================================  
	 * 			GESTION LISTE
	 * ========================================== */
	@Override
	public List<Document> getAllDocumentByCote() {
		return serviceListe.getAllDocumentByCote();
	}
	
	@Override
	public List<Document> getAllDocumentByTitre() {
		return serviceListe.getAllDocumentByTitre();
	}
	
	@Override
	public List<Document> getAllDocumentByTheme(ThemeDoc theme) {
		return serviceListe.getAllDocumentByTheme(theme);
	}
	
	@Override
	public List<Localisation> getAllLocalisationById() {
		return serviceListe.getAllLocalisationById();

	}
	
	@Override
	public List<Localisation> getAllLocalisationByLieu() {
		return serviceListe.getAllLocalisationByLieu();
	}
	
	@Override
	public List<Auteur> getAllAuteurById() {
		return serviceListe.getAllAuteurById();
	}

	@Override
	public List<Auteur> getAllAuteurByNom() {
		return serviceListe.getAllAuteurByNom();
	}

	@Override
	public ThemeDoc add(ThemeDoc theme) throws UserException {
		return serviceGestion.add(theme);
	}

	@Override
	public ThemeDoc update(ThemeDoc theme) {
		return serviceGestion.update(theme);
	}

	@Override
	public void remove(ThemeDoc theme) throws UserException {
		serviceGestion.remove(theme);		
	}

	@Override
	public void removeThemeById(String id) throws UserException {
		serviceGestion.removeThemeById(id);		
	}

	@Override
	public void removeTheme() {
		serviceGestion.removeTheme();		
	}

	@Override
	public void removeThemeNative() {
		serviceGestion.removeThemeNative();	}

	@Override
	public ThemeDoc getTheme(String id) throws UserException {
		return serviceGestion.getTheme(id);
	}

	@Override
	public List<ThemeDoc> getAllThemeById() {
		return serviceListe.getAllThemeById();
	}

	@Override
	public List<ThemeDoc> getAllThemeByNom() {
		return serviceListe.getAllThemeByNom();
	}

	@Override
	public List<ThemeDoc> getAllThemeByDoc(Document document) {
		return serviceListe.getAllThemeByDoc(document);
	}


}
