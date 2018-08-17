import { waitingTrue, waitingFalse } from "../commun/GeneralReducer";
import { initGet, initPost } from "../_utils";
import { WS_CONTEXT_PATH } from "../_properties";

const initialState = {
  dataflows: [],
  datacubes: [],
  fichiers: [],
  variables: [],
  datacubeSelected: null,
  series: [],
  modalitesChoisies: [],
  listeDSD: []
};

export const visualisationReducer = function(state = initialState, action) {
  switch (action.type) {
    case "SET_DATAFLOWS": {
      return Object.assign({}, state, {
        dataflows: action.dataflows
      });
    }
    case "SET_DATACUBES": {
      return Object.assign({}, state, {
        datacubes: action.datacubes
      });
    }
    case "SET_LISTE_DSD": {
      return Object.assign({}, state, {
        listeDSD: action.listeDSD
      });
    }
    case "SET_FICHIERS": {
      return Object.assign({}, state, {
        fichiers: action.fichiers
      });
    }
    case "SET_SERIES": {
      return Object.assign({}, state, {
        series: action.series
      });
    }
    case "SET_DATACUBE_VISUALISATION": {
      return Object.assign({}, state, {
        datacubeSelected: action.datacube
      });
    }
    case "SET_VARIABLES": {
      return Object.assign({}, state, {
        variables: action.variables
      });
    }
    case "CHANGE_MODALITE_CHOISIE": {
      const modalitesChoisies = state.modalitesChoisies.filter(item => item.dimension.iri !== action.dimension.iri);
      if (action.modalite) {
        modalitesChoisies.push({ dimension: action.dimension, modalite: action.modalite });
      }
      return Object.assign({}, state, {
        modalitesChoisies
      });
    }
    default:
      return state;
  }
};

export const setDataflows = dataflows => {
  return {
    type: "SET_DATAFLOWS",
    dataflows
  };
};

export const setListeDSD = listeDSD => {
  return {
    type: "SET_LISTE_DSD",
    listeDSD
  };
};

export const setDatacubes = datacubes => {
  return {
    type: "SET_DATACUBES",
    datacubes
  };
};

export const fetchDatacubes = () => {
  return (dispatch, getState) => {
    if (getState().visualisationReducer.datacubes.length === 0) {
      dispatch(waitingTrue());
      fetch(WS_CONTEXT_PATH + "/datacubes", initGet())
        .then(response => response.json())
        .then(json => {
          dispatch(setDatacubes(json));
          dispatch(waitingFalse());
        })
        .catch(error => {
          dispatch(waitingFalse());
          console.log(error);
        });
    }
  };
};

export const setFichiers = fichiers => {
  return {
    type: "SET_FICHIERS",
    fichiers
  };
};

export const setSeries = series => {
  return {
    type: "SET_SERIES",
    series
  };
};

export const setVariables = variables => {
  return {
    type: "SET_VARIABLES",
    variables
  };
};

export const setDatacube = datacube => {
  return {
    type: "SET_DATACUBE_VISUALISATION",
    datacube
  };
};

export const selectionDatacube = (datacube, setDatacubeSelected) => {
  return (dispatch, getState) => {
    dispatch(waitingTrue());
    if (datacube.components) {
      dispatch(setDatacubeSelected(datacube));
      dispatch(waitingFalse());
    } else {
      const iriWrapper = { iri: datacube.iri };
      fetch(WS_CONTEXT_PATH + "/datacube/components", initPost(iriWrapper))
        .then(response => response.json())
        .then(json => {
          datacube.attributs = json.attributs;
          datacube.dimensions = json.dimensions;
          datacube.components = [
            {
              iri: "valeur",
              libelleFr: "Valeur"
            },
            {
              iri: "concept",
              libelleFr: "Concept"
            },
            {
              iri: "http://purl.org/linked-data/sdmx/2009/dimension#timePeriod",
              libelleFr: "Année"
            }
          ];
          datacube.components.push(...json.attributs);
          datacube.components.push(...json.dimensions);
          dispatch(setDatacubeSelected(datacube));
          dispatch(waitingFalse());
        })
        .catch(error => {
          dispatch(waitingFalse());
          console.log(error);
        });
    }
  };
};

export const changeModaliteChoisie = (dimension, modalite) => {
  return {
    type: "CHANGE_MODALITE_CHOISIE",
    dimension,
    modalite
  };
};
