package fr.aoufi.dao;


import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;

import javax.ejb.LocalBean;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;

import org.hibernate.exception.ConstraintViolationException;
import org.hibernate.id.IdentifierGenerationException;

import fr.aoufi.entity.Auteur;
import fr.aoufi.entity.Document;
import fr.aoufi.entity.Localisation;
import fr.aoufi.entity.Theme;
import fr.aoufi.ressources.Param;
import fr.aoufi.userException.AuteurAffecteException;
import fr.aoufi.userException.DiversException;
import fr.aoufi.userException.DoublonException;
import fr.aoufi.userException.IdException;
import fr.aoufi.userException.InexistantException;
import fr.aoufi.userException.LocalisationAffecteeException;

//java.lang.Object
//extended by java.lang.Throwable
//    extended by java.lang.Exception
//        extended by java.lang.RuntimeException
//            extended by org.hibernate.HibernateException
//                extended by org.hibernate.JDBCException
//
//All Implemented Interfaces:
//  Serializable
//
//Direct Known Subclasses:
//  ConstraintViolationException, DataException, GenericJDBCException, 
// JDBCConnectionException, LockAcquisitionException, PessimisticLockException, QueryTimeoutException, SQLGrammarException 

// ATTENTION : il faut ajouter hibernate3.jar pour avoir acces au exception de org.hibernate

/**
 * Gestion de la bibliotheque
 * methode CUD sur Document, Localisation, 1 Auteur, n Theme
 */

@LocalBean
@Singleton
public class DaoGestion {

	@PersistenceContext(unitName=Param.CONTEXT_PERSISTANCE)
	private EntityManager 		em;


	/* ==========================================  
	 * 			GESTION DOCUMENT
	 * ========================================== */
	/**
	 * Ne persist pas si : document null ou si localisation deja affectee
	 * sinon : Persist localisation si pas en base, Auteur si pas en base (cascade) et Persist document
	 * 
	 * Controle les RG sur le document
	 * Ne s'occupe pas de la logique metier ( ex : renvoyer le document a null : fait dans la couche logique applicative)
	 * 
	 * @param document
	 * @return document
	 * @throws DoublonException
	 * @throws LocalisationAffecteeException
	 */
	public Document add(Document document) throws DoublonException, LocalisationAffecteeException  {
		System.out.println("*** DaoGestion - add(document) - DEBUT - document : " + document);

		// si document est null : return null
		if (document != null) {
			
			// traitement de la localisation
			Localisation localisation = document.getLocalisation();
			if (localisation != null) {
				// on recherche si la localisation est deja affectee a un autre document
				// si deja affectée, on sort
				if (isAffecteLocalisation(localisation)) throw new LocalisationAffecteeException();
				// on persist la localisation (si elle existe deja - la methode add(localisation) s'en occupe)
				localisation = add(localisation);
				// on affecte l'objet du contexte (contenant l'id) au document
				document.setLocalisation(localisation);
			}

			// traitement de l'auteur
			// si l'auteur existe en base, il faut mettre l'entity dans le contexte de persistance
			// pour que hibernate n'essaie pas de le creer et se rende compte qu'il existe deja
			if (document.getAuteur()!= null)  {
				Auteur auteur = em.find(Auteur.class, document.getAuteur().getId()); // retourne null si pas trouve
				if (auteur != null) document.setAuteur(auteur);
			}

			// traitement des themes
			// si les themes existent en base, il faut mettre les entity dans le contexte de persistance
			// pour que hibernate n'essaie pas de les creer et se rende compte qu'ils existent deja
			if (document.getThemes()!= null)  {
				// On recupere la liste des themes
				// on ne peut pas directement travailler avec un iterator sur la collection car la methode add(theme) 
				//  ajoute le theme à la fin de la collection et dans la boucle, il est re traité à la fin
				// on travaille donc sur la position des objets dans la liste
				// de plus, avec un iterator sur l'ArrayList on a ConcurrentModificationException au moment de la modif
				ArrayList<Theme> listeThemes = (ArrayList<Theme>) document.getThemes();
				for (int i = 0; i < listeThemes.size(); i++) {
					Theme theme = listeThemes.get(i);
					Theme themex = em.find(Theme.class, theme.getId());	// on recupere le proxy correspondant
					// si trouve on remplace à la meme position l'instance de theme par le proxy
					// sinon on laisse la valeur presente, elle sera cree (CascadeType.Persist)
					if (themex != null)  listeThemes.set(i,themex);				
				}
			}

			// tout est pret, on persist le document
			try {
				em.persist(document);
				em.flush();
			}
			catch (PersistenceException e) {	
				// recherche de la cause
				Throwable t = e.getCause();
				while ((t != null) && !(t instanceof SQLIntegrityConstraintViolationException)) {
					t = t.getCause();
				}
				// si c'est une ConstraintViolationException
				if (t instanceof SQLIntegrityConstraintViolationException) {
					throw new DoublonException();
				}
			}				
		} 
		return document;
	}

	/**
	 * - on persist la localisation (si elle existe deja, cela ne fera rien)
	 * - on verifie que la localisation n'est pas deja affectee à un autre document
	 * - on merge (persist si document pas existant)
	 */
	public Document update (Document document) throws LocalisationAffecteeException  {
		System.out.println("*** DaoGestion - DEBUT - update(document) - document : " + document);
			
		Localisation 	localisation 		= null;
		Auteur 			auteur 				= null;

		// si document est null : return
		if (document != null) {
			
			// on recherche si la localisation est deja affectee a un autre document
			if (isAffecteLocalisation(document)) throw new LocalisationAffecteeException();

			// traitement de la localisation
			// on update la localisation, si elle n'existe pas, update se charge de la créer
			// et on affecte l'objet du contexte (contenant l'id) au document
			localisation = document.getLocalisation();
			if (localisation != null) {
				document.setLocalisation(update(localisation));
			} 

			// traitement de l'auteur
			// on update l'auteur, si il n'existe pas, update se charge de le créer
			// retourne null si pas d'id
			// on affecte l'objet du contexte  au document
			auteur = document.getAuteur();
			if (auteur != null)  {
				auteur = update(auteur);
				document.setAuteur(auteur);
			}

			// tout va bien, on modifie le document
			em.merge(document);
			em.flush();
		}
		return document;
	}

	/**
	 * Remove(document)
	 * On ne gere pas l'auteur - on ne verifie pas que l'auteur existe - pas de cascade
	 * 
	 */
	public void remove(Document document) throws InexistantException  {
		if (document != null) {
			removeDocument(document.getCote());
		}
	}

	public void removeDocument(String cote) throws InexistantException  {
		System.out.println("******  DaoGestion - removeDocument(cote) " + cote);
		Document documentx = null;
		try {
			documentx = getDocument(cote);
			em.remove(documentx);
			em.flush();
		} catch (InexistantException e) {
			throw e;
		} catch (PersistenceException e) {				
			// recherche de la cause
			Throwable t = e.getCause();
			while ((t != null) && !(t instanceof SQLIntegrityConstraintViolationException)) {
				t = t.getCause();
			}
			if (t instanceof SQLIntegrityConstraintViolationException) {
				// TODO
			}
		}
	}

	public void removeDocumentNative() {
		try {
			em.createNativeQuery("delete from " + Param.TABLE_DOC_THEME).executeUpdate();
			em.createNativeQuery("delete from " + Param.TABLE_DOCUMENT).executeUpdate();
			em.flush();
		} catch (Exception e) {
			System.err.println("*** Err DaoGestion : removeDocumentNative");
			e.printStackTrace();
		}
	}

	public void removeDocument() {
		try {
			//TODO pourquoi doit-on ajouter cela ???
			em.createNativeQuery("delete from user1." + Param.TABLE_DOC_THEME).executeUpdate();
			em.createQuery("delete from Document").executeUpdate();
		} catch (Exception e) {
			System.err.println("*** Err DaoGestion : removeDocument");
			e.printStackTrace();
		}
	}

	public Document getDocument(String cote) throws InexistantException {

		//		Pour éviter les pb de chargement du lazy loading, on force le chargement de la localisation par une requete HQL
		Document document = null;
		try { 
			document 	= (Document) em.createQuery(ReqHQL.GET_DOCUMENT).setParameter(1,cote).getSingleResult();
		} catch (PersistenceException e) {	
			if (e.getClass().equals(NoResultException.class)) {
				throw new InexistantException();
			}
		}
		return document;
	}

	/* ==========================================  
	 * 			GESTION LOCALISATION
	 * ========================================== */

	/**
	 * ajout de localisation
	 * si elle existe deja on retourne l'objet du contexte 
	 * @param localisation
	 * @return
	 * @throws DoublonException
	 * @throws InexistantException 
	 * @throws DiversException 
	 */
	public Localisation add(Localisation localisation) throws DoublonException {
		System.out.println("*** DaoGestion - DEBUT - add(localisation) : localisation : " + localisation);

		try {
			Localisation loc = getLocalisation(localisation);
			localisation = loc; // on recupere l'objet du contexte
		} catch (InexistantException e) {  // si localisation inexistante en base			
			try { 
				em.persist(localisation);
				em.flush(); 
			} catch (PersistenceException pe) {	
				Throwable t = pe.getCause();
				while ((t != null) && !(t instanceof SQLIntegrityConstraintViolationException)) {
					t = t.getCause();
				}
				if (t instanceof SQLIntegrityConstraintViolationException) { // si c'est une ConstraintViolationException
					throw new DoublonException();
				}
			}
		}		
		return localisation;
	}


	/**
	 * RG : 
	 * 		
	 *   si la localisation passée en paramètre possède un id != 0 alors on modifie
	 *   sinon 2 cas
	 *    si la localisation (meme lieu et emplacement) existe alors on la recupere
	 *    sinon on la cree
	 * @throws DoublonException 
	 *       
	 *       
	 */
	public Localisation update (Localisation localisation) {
		if (localisation != null) {

			if (localisation.getIdLocalisation()!= 0) {
				localisation = em.merge(localisation);
			}
			else {
				try {
					localisation = getLocalisation(localisation.getLieu(), localisation.getEmp());
				} catch (InexistantException e) {
					localisation = em.merge(localisation);
				}
			}
		}
		return localisation;
	}

	public void remove(Localisation localisation) throws InexistantException, LocalisationAffecteeException  {
		if (localisation != null) {
			Localisation localisationx = getLocalisation(localisation.getIdLocalisation());
			if (localisationx != null) {
				if (isAffecteLocalisation(localisationx)) throw new LocalisationAffecteeException();
				else em.remove(localisationx);
			}
			else throw new InexistantException();
		}
	}


	public void removeLocalisation(int id) throws InexistantException, LocalisationAffecteeException {
		Localisation localisationx = getLocalisation(id);
		remove(localisationx);
	}

	public void removeLocalisationNative() {
		em.createNativeQuery("delete from " + Param.TABLE_LOCALISATION).executeUpdate();
		em.flush();
	}

	public void removeLocalisation() {
		em.createQuery("delete from Localisation").executeUpdate();
	}

	/**
	 * Retourne localisation par id
	 * si pas trouve : new InexistantException()
	 * @throws InexistantException 
	 */
	public Localisation getLocalisation(int id) throws InexistantException {
		Localisation localisation = null;
		try { 
			localisation = em.find(Localisation.class, id);
		} catch (PersistenceException e) {				
			// recherche de la cause
			Throwable t = e.getCause();
			while ((t != null) && !(t instanceof NoResultException)) {
				t = t.getCause();
			}
			if (t instanceof NoResultException) {
				throw new InexistantException();
			}
		}
		//		if (localisation == null) throw new InexistantException(String.format("Localisation id[%s] inconnue", id), 1);
		return localisation;
	}

	/**
	 * Retourne localisation par lieu, emp
	 * si pas trouve : new InexistantException() - localisation = null
	 * @throws InexistantException 
	 */
	public Localisation getLocalisation(String lieu, String emp) throws InexistantException {
		System.out.println("*** DaoGestion - getLocalisation(lieu,emp) ");
		Localisation localisation = null;
		String SQL_QUERY 	="select l from Localisation l where l.lieu = ? and l.emp = ? ";
		Query query = em.createQuery(SQL_QUERY);
		query.setParameter(1,lieu);
		query.setParameter(2,emp);

		try { 
			localisation = (Localisation) query.getSingleResult();
		} catch (PersistenceException e) {	
			if (e.getClass().equals(NoResultException.class)) {
				System.out.println("*** DaoGestion - getLocalisation(lieu,emp) - throw : " + e.getClass());
				throw new InexistantException();
			}
		} 
		System.out.println("*** DaoGestion - getLocalisation(lieu,emp) - localisation :  " + localisation);

		return localisation;
	}


	/**
	 * Retourne localisation par lieu, emp de l'objet localisation
	 * null si pas trouve
	 * @throws InexistantException 
	 */
	public Localisation getLocalisation(Localisation localisation) throws InexistantException {
		System.out.println("*** DaoGestion - DEBUT - getLocalisation(localisation) : localisation : " + localisation);
		if (localisation != null) localisation = getLocalisation(localisation.getLieu(),localisation.getEmp());

		return localisation;
	}

	/**
	 * Retourne id de localisation
	 * null si pas trouve
	 * @throws InexistantException 
	 */
	public Integer getLocalisationId(String lieu, String emp) throws InexistantException {
		Integer id = null;
		Localisation localisation = getLocalisation(lieu,emp);
		if (localisation != null) id = localisation.getIdLocalisation();
		return id;
	}

	/**
	 * Retourne id de localisation
	 * null si pas trouve
	 * @throws InexistantException 
	 */
	public Integer getLocalisationId(Localisation localisation) throws InexistantException {
		Integer id = null;
		Localisation localisationx = getLocalisation(localisation.getLieu(),localisation.getEmp());
		if (localisationx != null) id = localisationx.getIdLocalisation();
		return id;
	}

	/**
	 *  Verifie si la localisation est affectee a un document
	 */
	public boolean isAffecteLocalisation(Localisation localisation) {

		System.out.println("*** DaoGestion - isAffecteLocalisation(localisation) - localisation : " + localisation);
		// on part du principe que la localisation n'est pas affectee a un document
		boolean trouve = false;

		// on recherche la localisation en BDD
		if (localisation != null) {
			Localisation loc;
			try {
				loc = getLocalisation(localisation.getLieu(), localisation.getEmp());
				//	System.out.println("*** DaoGestion - isAffecteLocalisation(localisation) - apres getLocalisation(lieu,emp) - loc : " + loc);

				// si on trouve, on regarde si elle est associee a un document
				Query query = em.createQuery("select d from Document d where d.localisation = ? ");
				query.setParameter(1,loc);
				query.getSingleResult();
				trouve = true;

			} catch (InexistantException e) {
				// erreur en provenance de getLocalisation(lieu,emp) - on ne trouve pas la localisation - on ne fait rien
			} catch (PersistenceException e) {				
				// erreur en provenance du select document - on ne trouve pas le document on ne fait rien
			} 
		}
		//		System.out.println("*** DaoGestion - isAffecteLocalisation(localisation) - fin - trouve : " + trouve);
		return trouve;
	}

	/**
	 *  Verifie que la localisation n'est pas affectee a un autre document que celui donne en parametre
	 */
	public boolean isAffecteLocalisation(Document document) {

		System.out.println("*** DaoGestion - isAffecteLocalisation(document) - document : " + document);
		// on part du principe que la localisation n'est pas affectee a un document
		boolean trouve = false;

		// on recherche la localisation en BDD
		if (document != null) {
			Localisation localisation = document.getLocalisation();
			if (localisation != null) {
				Localisation loc;
				try {
					loc = getLocalisation(localisation.getLieu(), localisation.getEmp());

					// si on trouve, on regarde si elle est associee a un autre document que celui passe en parametre 
					Query query = em.createQuery("select d from Document d where d.localisation = ? and d.cote != ?");
					query.setParameter(1,loc);
					query.setParameter(2,document.getCote());
					query.getSingleResult();
					trouve = true;

				} catch (InexistantException e) {
					// erreur en provenance de getLocalisation(lieu,emp) - on ne trouve pas la localisation - on ne fait rien
				} catch (PersistenceException e) {				
					// erreur en provenance du select document - on ne trouve pas le document on ne fait rien
				} 
			}
		}
		//		System.out.println("*** DaoGestion - isAffecteLocalisation(localisation) - fin - trouve : " + trouve);
		return trouve;
	}


	/* ==========================================  
	 * 			GESTION AUTEUR
	 * ========================================== */
	public Auteur add(Auteur auteur) throws DoublonException, IdException {

		System.out.println("*** DaoGestion - DEBUT - add(auteur) : auteur : " + auteur);

		if (auteur != null) {
			if (auteur.getId() == null || auteur.getId().isEmpty()) throw new IdException();
			try {
				em.persist(auteur);
				em.flush(); 
			} catch (PersistenceException e) {
				Throwable t = e.getCause();
				if (t instanceof IdentifierGenerationException) 			throw new IdException();
				if (t instanceof ConstraintViolationException) 				throw new DoublonException();
				//				System.out.println("*** DaoGestion - DEBUT - add(auteur) : erreur : " + t);
			}
		}

		return auteur;
	}

	/**
	 * RG : si l'auteur ne possede pas d'id, auteur null
	 *      sinon aucune verification sur l'auteur      
	 */
	public Auteur update (Auteur auteur) {
		if (auteur != null) {
			if (auteur.getId() == null || auteur.getId().isEmpty()) auteur = null;
			else auteur = em.merge(auteur);
		}
		return auteur;
	}

	public void remove(Auteur auteur) throws AuteurAffecteException, InexistantException   {

		if (auteur == null) throw new InexistantException();

		// on met l'auteur dans le contexte
		auteur = getAuteur(auteur.getId());
		if (auteur == null) throw new InexistantException();

		try {
			//			System.out.println("*** DaoGestion - remove(Auteur auteur) : auteur " + auteur);
			em.remove(auteur);
			em.flush();
		} catch (PersistenceException e) {
			Throwable t = e.getCause();
			System.out.println("*** DaoGestion - remove(Auteur auteur) : erreur " + t);
			if (t instanceof ConstraintViolationException) 				throw new AuteurAffecteException();
		}				

	}

	public void removeAuteur(String id) throws AuteurAffecteException, InexistantException   {		
		// on met l'auteur dans le contexte
		Auteur auteur = getAuteur(id);
		remove(auteur);
	}

	public void removeAuteur() throws AuteurAffecteException {
		try {
			em.createQuery("delete from Auteur").executeUpdate();
		} catch (PersistenceException e) {
			Throwable t = e.getCause();
			System.out.println("*** DaoGestion - removeAuteur : erreur " + t);
			if (t instanceof ConstraintViolationException) 				throw new AuteurAffecteException();
		}		
	}

	public void removeAuteurNative() {
		em.createNativeQuery("delete from " + Param.TABLE_AUTEUR).executeUpdate();
		em.flush();
	}

	public Auteur getAuteur(String id) throws InexistantException {

		Auteur auteur = null;
		if (id != null) {
			auteur = em.find(Auteur.class, id);
			if (auteur == null) throw new InexistantException();
		}
		return auteur;
	}

	/* ==========================================  
	 * 			GESTION THEME
	 * ========================================== */
	public Theme add(Theme theme) throws DoublonException, IdException, LocalisationAffecteeException {
		System.out.println("*** DaoGestion - DEBUT - add(theme) : theme : " + theme);

		if (theme != null) {
			if (theme.getId() == null || theme.getId().isEmpty()) throw new IdException();
			try {
				em.persist(theme);
				em.flush(); 

				// on modifie tous les documents du theme
				if (theme.getDocuments() != null) {
					for (Document document : theme.getDocuments()) {
						update(document);
					}
				}
			} catch (PersistenceException e) {
				Throwable t = e.getCause();
				if (t instanceof IdentifierGenerationException) 			throw new IdException();
				if (t instanceof ConstraintViolationException) 				throw new DoublonException();
				//				System.out.println("*** DaoGestion - add(theme) : erreur : " + t);
			} 
		}
		return theme;
	}

	/**
	 * RG : si le theme ne possede pas d'id, theme null
	 *      sinon aucune verification sur le theme      
	 */
	public Theme update (Theme theme) {
		if (theme != null) {
			if (theme.getId() == null || theme.getId().isEmpty()) theme = null;
			else theme = em.merge(theme);
		}
		return theme;
	}

	// supprime le theme et ses associations aux documents
	public void remove(Theme theme) throws InexistantException   {

		if (theme == null) throw new InexistantException();

		// on met le theme dans le contexte
		theme = getTheme(theme.getId());
		if (theme == null) throw new InexistantException();

		//		System.out.println("*** DaoGestion - remove(Theme theme) : theme " + theme);
		em.remove(theme);
		em.flush();
	}

	// supprime le theme et ses associations aux documents
	public void removeThemeById(String id) throws InexistantException   {
		Theme theme = getTheme(id);
		remove(theme);
	}

	public void removeTheme() {
		try {
			em.createNativeQuery("delete from muller." + Param.TABLE_DOC_THEME).executeUpdate();
			em.createQuery("delete from Theme").executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void removeThemeNative() {
		em.createNativeQuery("delete from muller." + Param.TABLE_DOC_THEME).executeUpdate();
		em.createNativeQuery("delete from " + Param.TABLE_THEME).executeUpdate();
		em.flush();
	}

	public Theme getTheme(String id) throws InexistantException {
		Theme theme = null;
		if (id != null) {
			theme = em.find(Theme.class, id);
			if (theme == null) throw new InexistantException();
		}
		return theme;
	}

}
