package fr.aoufi.dao;

import java.util.ArrayList;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import fr.aoufi.entity.Auteur;
import fr.aoufi.entity.Document;
import fr.aoufi.entity.Localisation;
import fr.aoufi.entity.Theme;
import fr.aoufi.ressources.Param;

/**
 * Cette classe reste dans le contexte hibernate
 * Les DTO sont construits dans la facade 
 * 
 * Tant qu'on ne sort pas de la couche dao, on garde le lien avec les entity du contexte
 * 
 * @author 
 *
 */
@LocalBean
@Singleton
public class DaoListe {
	
	@PersistenceContext(unitName=Param.CONTEXT_PERSISTANCE)
	private EntityManager 		em;

	/* ==========================================  
	 * 			GESTION DOCUMENT
	 * ========================================== */
	public List<Document> getAllDocumentByCote() {
		Query query = em.createQuery(ReqHQL.ALL_DOCUMENT_BY_COTE);
		return recupObjet(query, Document.class);
	}
	
	public List<Document> getAllDocumentByTitre() {
		Query query = em.createQuery(ReqHQL.ALL_DOCUMENT_BY_TITRE);
		return recupObjet(query, Document.class);
	}
	
	public List<Document> getAllDocumentByTheme(Theme theme) {
		List<Document> liste = null;
		if (theme != null) {
			Query query = em.createQuery(ReqHQL.ALL_DOCUMENT_BY_THEME).setParameter(1,theme.getId());
			liste = recupObjet(query, Document.class);
		} else {
			liste = new ArrayList<Document>();
		}
		return liste;
	}	

	/* ==========================================  
	 * 			GESTION LOCALISATION
	 * ========================================== */		
	public List<Localisation> getAllLocalisationById() {
		Query query = em.createQuery(ReqHQL.ALL_LOCALISATION_BY_ID);
		return recupObjet(query, Localisation.class);
	}
	
	public List<Localisation> getAllLocalisationByLieu() {
		Query query = em.createQuery(ReqHQL.ALL_LOCALISATION_BY_LIEU);
		return recupObjet(query, Localisation.class);
	}

	/* ==========================================  
	 * 			GESTION AUTEUR
	 * ========================================== */	
	public List<Auteur> getAllAuteurById() {
		Query query = em.createQuery(ReqHQL.ALL_AUTEUR_BY_ID);
		return recupObjet(query, Auteur.class);
	}
	
	public List<Auteur> getAllAuteurByNom() {		
		Query query = em.createQuery(ReqHQL.ALL_AUTEUR_BY_NOM);
		return recupObjet(query, Auteur.class);
	}
	
	/* ==========================================  
	 * 			GESTION THEME
	 * ========================================== */	
	public List<Theme> getAllThemeById() {
		Query query = em.createQuery(ReqHQL.ALL_THEME_BY_ID);
		return recupObjet(query, Theme.class);
	}

	public List<Theme> getAllThemeByNom() {
		Query query = em.createQuery(ReqHQL.ALL_THEME_BY_NOM);
		return recupObjet(query, Theme.class);
	}

	public List<Theme> getAllThemeByDoc(Document document) {
		List<Theme> liste = null;
		if (document != null) {
			Query query = em.createQuery(ReqHQL.ALL_THEME_BY_DOC);
			query.setParameter(1,document.getCote());
			liste = recupObjet(query, Theme.class);
		} else {
			liste = new ArrayList<Theme>();
		}
		return liste;
	}

	/**
	 * Methode generic pour recuperer le resultat d'un query
	 *  et retourner une liste du type demande
	 *  
	 * @param query
	 * @param classe
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public  <T> List<T> recupObjet(Query query, Class<T> classe) {
		List<T> liste = new ArrayList<T>();
		for (Object o : query.getResultList())	{   
			try {
				liste.add((T)o);
			} catch (ClassCastException e) {}
		}
		return liste;
	}
	
}
