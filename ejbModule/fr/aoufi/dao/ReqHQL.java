package fr.aoufi.dao;

public class ReqHQL {

	// join fetch pour forcer le chargement de la localisation si elle existe (gestion comme eager)
	protected static final String ALL_DOCUMENT_BY_COTE 		= 
			"select d from Document d left outer join fetch d.localisation left outer join fetch d.auteur order by d.cote asc";
	protected static final String ALL_DOCUMENT_BY_TITRE 	= 
			"select d from Document d left outer join fetch d.localisation left outer join fetch d.auteur order by d.titre asc";
	protected static final String ALL_DOCUMENT_BY_THEME 	= 
			"select d from Document d left outer join fetch d.localisation left outer join fetch d.auteur"
					+ " join d.themes as theme where theme.id = ? order by d.titre asc";	
	
	protected static final String ALL_LOCALISATION_BY_ID 	= "select l from Localisation l order by l.idLocalisation asc";
	protected static final String ALL_LOCALISATION_BY_LIEU 	= "from Localisation l order by l.lieu asc";
	
	protected static final String ALL_AUTEUR_BY_ID 			= "from Auteur a order by a.id asc";
	protected static final String ALL_AUTEUR_BY_NOM 		= "from Auteur a order by a.nom asc";
	
	// Attention aux jointures externes si liste document est nulle
	protected static final String ALL_THEME_BY_ID  			= 
			"select distinct(t) from Theme as t left outer join t.documents as doc left outer join doc.auteur order by t.id asc";
	protected static final String ALL_THEME_BY_NOM 			= 
			"select distinct(t) from Theme as t left outer join t.documents as doc left outer join doc.auteur order by t.nom asc";
	protected static final String ALL_THEME_BY_DOC 			= 
			"select distinct(t) from Theme as t join t.documents as doc where doc.cote = ? order by t.id asc";

	protected static final String GET_DOCUMENT 				= 
			"select d, l, a from Document d left outer join fetch d.localisation l left outer join fetch d.auteur a where d.cote=?";

}
