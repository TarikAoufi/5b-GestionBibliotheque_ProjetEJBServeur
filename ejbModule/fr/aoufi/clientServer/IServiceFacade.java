package fr.aoufi.clientServer;

import java.util.List;

import fr.aoufi.entity.Auteur;
import fr.aoufi.entity.Document;
import fr.aoufi.entity.Localisation;
import fr.aoufi.entity.ThemeDoc;

public interface IServiceFacade {

	
	/* ==========================================  
	 * 			GESTION DOCUMENT
	 * ========================================== */
	public Document				ajouter(Document document) 		throws UserException;
	public Document 			update(Document document) 		throws UserException;
	public void 				remove(Document document) 		throws UserException;
	public void 				removeDocument(String cote) 	throws UserException;
	public void 				removeDocumentNative();
	public void 				removeDocument();
	public Document 			getDocument(String cote) 		throws UserException;

	
	/* ==========================================  
	 * 			GESTION LOCALISATION
	 * ========================================== */
	public Localisation			ajouter(Localisation localisation) 				throws UserException;
	public Localisation 		update(Localisation localisation) 				throws UserException;
	public void 				remove(Localisation localisation) 				throws UserException;
	public void 				removeLocalisation(int id) 						throws UserException;
	public void 				removeLocalisationNative();
	public void 				removeLocalisation();
	
	public Localisation 		getLocalisation(int id) 						throws UserException;
	public Localisation 		getLocalisation(String lieu, String emp) 		throws UserException;
	public Localisation 		getLocalisation(Localisation localisation) 		throws UserException;
	public Integer 				getLocalisationId(String lieu, String emp) 		throws UserException;
	public Integer 				getLocalisationId(Localisation localisation) 	throws UserException;

	public boolean isAffecteLocalisation(Localisation localisation) ;
	

	/* ==========================================  
	 * 			GESTION AUTEUR
	 * ========================================== */
	public Auteur ajouter(Auteur auteur) 		throws UserException;
	public Auteur update (Auteur auteur);
	public void remove(Auteur auteur) 			throws UserException;
	public void removeAuteur(String id) 		throws UserException;
	public void removeAuteur() 					throws UserException;
	public void removeAuteurNative();
	public Auteur getAuteur(String id) 			throws UserException;
	
		
	/* ==========================================  
	 * 			GESTION THEME
	 * ========================================== */	
	public ThemeDoc add(ThemeDoc theme) 		throws UserException;
	public ThemeDoc update (ThemeDoc theme);	
	public void remove(ThemeDoc theme) 			throws UserException;
	public void removeThemeById(String id) 		throws UserException;
	public void removeTheme();
	public void removeThemeNative();
	public ThemeDoc getTheme(String id) 		throws UserException;

		
	/* ==========================================  
	 * 			GESTION LISTE
	 * ========================================== */	
	public List<Document> 		getAllDocumentByCote();
	public List<Document> 		getAllDocumentByTitre();
	public List<Document> 		getAllDocumentByTheme(ThemeDoc theme);
	public List<Localisation> 	getAllLocalisationById();
	public List<Localisation> 	getAllLocalisationByLieu();
	public List<Auteur> 		getAllAuteurById();
	public List<Auteur> 		getAllAuteurByNom();
	public List<ThemeDoc> 			getAllThemeById();
	public List<ThemeDoc> 			getAllThemeByNom();
	public List<ThemeDoc> 			getAllThemeByDoc(Document document);

}
