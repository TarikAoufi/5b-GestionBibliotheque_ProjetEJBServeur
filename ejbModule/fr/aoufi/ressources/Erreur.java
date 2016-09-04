package fr.aoufi.ressources;

public enum Erreur {
	
		DOC_DOUBLON(1), 
		DOC_LOC_AFFECTEE(2),
		DOC_INEXISTANT(3),
		DOC_COTE_OBLIGATOIRE(4),
		DOC_TITRE_OBLIGATOIRE(5),
		
		
		LOC_INEXISTANT(30),
		LOC_DOUBLON(31),
		
		AUT_INEXISTANT(40),
		AUT_DOUBLON(41),
		DOC_AUT_AFFECTE(42),
		AUT_ID_INVALID(43),
		AUT_NOM_OBLIGATOIRE(44),
		
		THE_INEXISTANT(50),
		THE_DOUBLON(51),
		THE_ID_INVALID(53),
		
		DIVERS(999);

		private int code;
		
		private Erreur(int code) {
			this.code = code;
		}

		public int getCode() {
			return code;
		}

		public String action() {
			switch(this) {
				case DOC_DOUBLON : 				return "Le document existe déjà";
				case DOC_LOC_AFFECTEE : 		return "La localisation est déjà affectée";
				case DOC_INEXISTANT : 			return "Le document n'existe pas";
				case DOC_COTE_OBLIGATOIRE : 	return "La cote du document doit être renseignée";
				case DOC_TITRE_OBLIGATOIRE : 	return "Le titre du document doit être renseigné";
				
				case LOC_INEXISTANT : 			return "La localisation n'existe pas";
				case LOC_DOUBLON : 				return "La localisation existe déjà";
				
				case AUT_INEXISTANT : 			return "L'auteur n'existe pas";
				case AUT_DOUBLON : 				return "L'auteur existe déjà";
				case DOC_AUT_AFFECTE : 			return "L'auteur est affecté dans un livre";
				case AUT_ID_INVALID : 			return "L'id de l'auteur est invalide";
				case AUT_NOM_OBLIGATOIRE : 		return "Le nom de l'auteur est obligatoire";
				
				case THE_INEXISTANT : 			return "Le theme n'existe pas";
				case THE_DOUBLON : 				return "Le theme existe déjà";
				case THE_ID_INVALID : 			return "L'id du theme est invalide";
				
				case DIVERS : 					return "Erreur non referencee";
				default : 						return "";
			}
	 	}
}
