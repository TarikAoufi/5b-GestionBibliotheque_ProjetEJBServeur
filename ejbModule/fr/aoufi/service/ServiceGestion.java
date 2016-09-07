package fr.aoufi.service;

import javax.ejb.EJB;
import javax.ejb.LocalBean;
import javax.ejb.Singleton;

import fr.aoufi.clientServer.UserException;
import fr.aoufi.dao.DaoFacade;
import fr.aoufi.entity.Auteur;
import fr.aoufi.entity.Document;
import fr.aoufi.entity.Localisation;
import fr.aoufi.entity.Theme;
import fr.aoufi.ressources.Erreur;
import fr.aoufi.userException.AuteurAffecteException;
import fr.aoufi.userException.DoublonException;
import fr.aoufi.userException.IdException;
import fr.aoufi.userException.InexistantException;
import fr.aoufi.userException.LocalisationAffecteeException;

@Singleton
@LocalBean
public class ServiceGestion {

	@EJB
	DaoFacade daoFacade;

	/* ==========================================  
	 * 			GESTION DOCUMENT
	 * ========================================== */
	public Document ajouter(Document document) throws UserException  {
		if (document != null) {
			if (document.getCote() == null || document.getCote().trim().isEmpty()) 
				throw new UserException(Erreur.DOC_COTE_OBLIGATOIRE.action(),Erreur.DOC_COTE_OBLIGATOIRE.getCode());
			if (document.getTitre() == null || document.getTitre().trim().isEmpty()) 
				throw new UserException(Erreur.DOC_TITRE_OBLIGATOIRE.action(),Erreur.DOC_TITRE_OBLIGATOIRE.getCode());
			try {
				// System.out.println("ServiceGestion - ajouter(Document document) - document : " + document);
				document = daoFacade.add(document);
			} 		
			catch (DoublonException e ) {
				document = null;
				throw new UserException(Erreur.DOC_DOUBLON.action(),Erreur.DOC_DOUBLON.getCode());
			}
			catch (LocalisationAffecteeException e) {
				document.setLocalisation(null);
				throw new UserException(Erreur.DOC_LOC_AFFECTEE.action(),Erreur.DOC_LOC_AFFECTEE.getCode());
			}
		}
		System.out.println("*******************   Service Gestion : " + document);
		return document;

	}

	public Document update (Document document) throws UserException  {

		try {
			document = daoFacade.update(document);
		} catch (LocalisationAffecteeException e) {
			throw new UserException(Erreur.DOC_LOC_AFFECTEE.action(),Erreur.DOC_LOC_AFFECTEE.getCode());
		}

		return document;
	}

	public void remove(Document document) throws UserException {
		try {
			daoFacade.remove(document);
		} catch (InexistantException e) {
			throw new UserException(Erreur.DOC_INEXISTANT.action(),Erreur.DOC_INEXISTANT.getCode());
		}

	}

	public void removeDocument(String cote) throws UserException {

		try {
			daoFacade.removeDocument(cote);
		} catch (InexistantException e) {
			throw new UserException(Erreur.DOC_INEXISTANT.action(),Erreur.DOC_INEXISTANT.getCode());
		}

	}

	public void removeDocumentNative() {
		daoFacade.removeDocumentNative();
	}

	public void removeDocument() {
		daoFacade.removeDocument();
	}

	public Document getDocument(String cote) throws UserException {
		Document document;
		try {
			document = daoFacade.getDocument(cote);
		} catch (InexistantException e) {
			throw new UserException(Erreur.DOC_INEXISTANT.action(),Erreur.DOC_INEXISTANT.getCode());
		}
		return document;
	}

	/* ==========================================  
	 * 			GESTION LOCALISATION
	 * ========================================== */
	public Localisation ajouter(Localisation localisation) throws UserException {
		try {
			localisation = daoFacade.add(localisation);
		} catch (DoublonException e ) {
			localisation = null;
			throw new UserException(Erreur.LOC_DOUBLON.action(),Erreur.LOC_DOUBLON.getCode());
		}
		return localisation;
	}

	public Localisation update (Localisation localisation) throws UserException  {

		try {
			localisation = daoFacade.update(localisation);
		} catch (DoublonException e) {
			throw new UserException(Erreur.LOC_INEXISTANT.action(),Erreur.LOC_INEXISTANT.getCode());
		}

		return localisation;
	}

	public void remove(Localisation localisation) throws UserException {
		try {
			daoFacade.remove(localisation);
		} catch (InexistantException e) {
			throw new UserException(Erreur.LOC_INEXISTANT.action(),Erreur.LOC_INEXISTANT.getCode());
		} catch (LocalisationAffecteeException e) {
			throw new UserException(Erreur.DOC_LOC_AFFECTEE.action(),Erreur.DOC_LOC_AFFECTEE.getCode());
		}

	}

	public void removeLocalisation(int id) throws UserException {
		try {
			daoFacade.removeLocalisation(id);
		} catch (InexistantException e) {
			throw new UserException(Erreur.LOC_INEXISTANT.action(),Erreur.LOC_INEXISTANT.getCode());
		} catch (LocalisationAffecteeException e) {
			throw new UserException(Erreur.DOC_LOC_AFFECTEE.action(),Erreur.DOC_LOC_AFFECTEE.getCode());
		}

	}

	public void removeLocalisationNative() {
		daoFacade.removeLocalisationNative();
	}

	public void removeLocalisation() {
		daoFacade.removeLocalisation();
	}

	/**
	 * Retourne localisation par id
	 * si pas trouve
	 * @throws UserException 
	 */
	public Localisation getLocalisation(int id) throws UserException {
		Localisation localisation = null;
		try {
			localisation = daoFacade.getLocalisation(id);
		} catch (InexistantException e) {
			throw new UserException(Erreur.LOC_INEXISTANT.action(),Erreur.LOC_INEXISTANT.getCode());
		}

		return localisation;
	}

	/**
	 * Retourne localisation par lieu, emp
	 * @throws UserException 
	 */
	public Localisation getLocalisation(String lieu, String emp) throws UserException {
		try {
			return daoFacade.getLocalisation(lieu, emp);
		} catch (InexistantException e) {
			throw new UserException(Erreur.LOC_INEXISTANT.action(),Erreur.LOC_INEXISTANT.getCode());
		}
	}


	/**
	 * Retourne localisation par lieu, emp de l'objet localisation
	 * @throws UserException 
	 */
	public Localisation getLocalisation(Localisation localisation) throws UserException {
		try {
			return daoFacade.getLocalisation(localisation);
		} catch (InexistantException e) {
			throw new UserException(Erreur.LOC_INEXISTANT.action(),Erreur.LOC_INEXISTANT.getCode());
		}
	}

	/**
	 * Retourne id de localisation
	 * @throws UserException 
	 */
	public Integer getLocalisationId(String lieu, String emp) throws UserException {

		try {
			return daoFacade.getLocalisationId(lieu, emp);
		} catch (InexistantException e) {
			throw new UserException(Erreur.LOC_INEXISTANT.action(),Erreur.LOC_INEXISTANT.getCode());
		}
	}

	/**
	 * Retourne id de localisation
	 * @throws UserException 
	 */
	public Integer getLocalisationId(Localisation localisation) throws UserException {
		try {
			return daoFacade.getLocalisationId(localisation);
		} catch (InexistantException e) {
			throw new UserException(Erreur.LOC_INEXISTANT.action(),Erreur.LOC_INEXISTANT.getCode());
		}
	}

	/**
	 *  Verifie si la localisation est affectee a un document
	 */
	public boolean isAffecteLocalisation(Localisation localisation) {		
		return daoFacade.isAffecteLocalisation(localisation);
	}

	/* ==========================================  
	 * 			GESTION AUTEUR
	 * ========================================== */
	public Auteur ajouter(Auteur auteur) throws UserException {
		try {
			return daoFacade.add(auteur);
		} catch (DoublonException e) {
			throw new UserException(Erreur.AUT_DOUBLON.action(),Erreur.AUT_DOUBLON.getCode());
		} catch (IdException e) {
			throw new UserException(Erreur.AUT_ID_INVALID.action(),Erreur.AUT_ID_INVALID.getCode());
		}
	}

	public Auteur update(Auteur auteur) {
		return daoFacade.update(auteur);
	}

	public void remove(Auteur auteur) throws UserException {
		try {
			daoFacade.remove(auteur);
		} catch (AuteurAffecteException e) {
			throw new UserException(Erreur.DOC_AUT_AFFECTE.action(),Erreur.DOC_AUT_AFFECTE.getCode());
		} catch (InexistantException e) {
			throw new UserException(Erreur.AUT_INEXISTANT.action(),Erreur.AUT_INEXISTANT.getCode());
		}

	}

	public void removeAuteur(String id) throws UserException {
		try {
			daoFacade.removeAuteur(id);
		} catch (AuteurAffecteException e) {
			throw new UserException(Erreur.DOC_AUT_AFFECTE.action(),Erreur.DOC_AUT_AFFECTE.getCode());
		} catch (InexistantException e) {
			throw new UserException(Erreur.AUT_INEXISTANT.action(),Erreur.AUT_INEXISTANT.getCode());
		}

	}

	public void removeAuteur() throws UserException {
		try {
			daoFacade.removeAuteur();
		} catch (AuteurAffecteException e) {
			throw new UserException(Erreur.DOC_AUT_AFFECTE.action(),Erreur.DOC_AUT_AFFECTE.getCode());
		}

	}

	public void removeAuteurNative() {
		daoFacade.removeAuteurNative();

	}

	public Auteur getAuteur(String id) throws UserException {
		try {
			return daoFacade.getAuteur(id);
		} catch (InexistantException e) {
			throw new UserException(Erreur.AUT_INEXISTANT.action(),Erreur.AUT_INEXISTANT.getCode());
		}
	}

	public Theme add(Theme theme) throws UserException {
		try {
			return daoFacade.add(theme);
		} catch (DoublonException e) {
			throw new UserException(Erreur.THE_DOUBLON.action(),Erreur.THE_DOUBLON.getCode());
		} catch (IdException e) {
			throw new UserException(Erreur.THE_ID_INVALID.action(),Erreur.THE_ID_INVALID.getCode());
		} catch (LocalisationAffecteeException e) {
			throw new UserException(Erreur.DOC_LOC_AFFECTEE.action(),Erreur.DOC_LOC_AFFECTEE.getCode());
		}
	}

	public Theme update(Theme theme) {
		return daoFacade.update(theme);
	}

	public void remove(Theme theme) throws UserException {
		try {
			daoFacade.remove(theme);
		} catch (InexistantException e) {
			throw new UserException(Erreur.THE_INEXISTANT.action(),Erreur.THE_INEXISTANT.getCode());
		}

	}

	public void removeThemeById(String id) throws UserException {
		try {
			daoFacade.removeThemeById(id);
		} catch (InexistantException e) {
			throw new UserException(Erreur.THE_INEXISTANT.action(),Erreur.THE_INEXISTANT.getCode());
		}

	}

	public void removeTheme() {
		daoFacade.removeTheme();
	}

	public void removeThemeNative() {
		daoFacade.removeThemeNative();
	}

	public Theme getTheme(String id) throws UserException {
		try {
			return daoFacade.getTheme(id);
		} catch (InexistantException e) {
			throw new UserException(Erreur.THE_INEXISTANT.action(),Erreur.THE_INEXISTANT.getCode());
		}
	}


}
