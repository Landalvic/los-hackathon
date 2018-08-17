const initialState = {
  lienFichier: null,
  infosFichier: null,
  colonnes: [],
  choixGestionnaire: [],
  choixParDefaut: [],
  panierValidationSource: [],
  panierValidationFichier: [],
  datacubeSelected: null,
  dsd: { mesures: [], slicesKeys: [], dimensions: [], attributs: [] }
};

export const chargementReducer = function(state = initialState, action) {
  switch (action.type) {
    case "SET_INFOS_FICHIER": {
      return Object.assign({}, state, {
        infosFichier: action.infosFichier,
        choixGestionnaire: []
      });
    }
    case "SET_COLONNES": {
      return Object.assign({}, state, {
        colonnes: action.colonnes
      });
    }
    case "SET_DSD": {
      if (action.dsd) {
        return Object.assign({}, state, {
          dsd: action.dsd
        });
      } else {
        return Object.assign({}, state, {
          dsd: initialState.dsd
        });
      }
    }
    case "SET_DATACUBE_CHARGEMENT": {
      return Object.assign({}, state, {
        datacubeSelected: action.datacube ? action.datacube : null
      });
    }
    case "CHANGE_CHOIX_GESTIONNAIRE": {
      let newArray = state.choixGestionnaire.slice();
      newArray[action.index] = action.choix;
      return Object.assign({}, state, {
        choixGestionnaire: newArray
      });
    }
    case "CHANGE_CHOIX_PAR_DEFAUT": {
      let newArray = state.choixParDefaut.slice();
      newArray[action.index] = action.choix;
      return Object.assign({}, state, {
        choixParDefaut: newArray
      });
    }
    case "CHANGE_PANIER_VALIDATION_SOURCE": {
      const liste = state.panierValidationSource.filter(iri => iri !== action.iri);
      if (liste.length === state.panierValidationSource.length) {
        liste.push(action.iri);
      }
      return Object.assign({}, state, {
        panierValidationSource: liste
      });
    }
    case "CHANGE_PANIER_VALIDATION_FICHIER": {
      const liste = state.panierValidationFichier.filter(iri => iri !== action.iri);
      if (liste.length === state.panierValidationFichier.length) {
        liste.push(action.iri);
      }
      return Object.assign({}, state, {
        panierValidationFichier: liste
      });
    }
    default:
      return state;
  }
};

export const changePanierValidationSource = iri => {
  return {
    type: "CHANGE_PANIER_VALIDATION_SOURCE",
    iri
  };
};

export const setDSD = dsd => {
  return {
    type: "SET_DSD",
    dsd
  };
};

export const changePanierValidationFichier = iri => {
  return {
    type: "CHANGE_PANIER_VALIDATION_FICHIER",
    iri
  };
};

export const setInfosFichier = infosFichier => {
  return {
    type: "SET_INFOS_FICHIER",
    infosFichier
  };
};

export const setDatacube = datacube => {
  return {
    type: "SET_DATACUBE_CHARGEMENT",
    datacube
  };
};

export const setColonnes = colonnes => {
  return {
    type: "SET_COLONNES",
    colonnes
  };
};

export const changeChoixGestionnaire = (choix, index) => {
  return {
    type: "CHANGE_CHOIX_GESTIONNAIRE",
    choix,
    index
  };
};

export const changeChoixParDefaut = (choix, index) => {
  return {
    type: "CHANGE_CHOIX_PAR_DEFAUT",
    choix,
    index
  };
};
