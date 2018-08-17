import { waitingTrue, waitingFalse } from "../commun/GeneralReducer";
import { initGet, reduxReferentiel } from "../_utils";
import { WS_CONTEXT_PATH } from "../_properties";

const initialState = {
  concepts: {
    byId: {},
    allIds: []
  },
  mesures: {
    byId: {},
    allIds: []
  },
  conceptSchemes: {
    byId: {},
    allIds: []
  }
};

export const referentielReducer = function(state = initialState, action) {
  switch (action.type) {
    case "SET_CONCEPTS": {
      return {
        ...state,
        concepts: reduxReferentiel(action.concepts, "iri")
      };
    }
    case "SET_MESURES": {
      return {
        ...state,
        mesures: reduxReferentiel(action.mesures, "iri")
      };
    }
    case "SET_CONCEPT_SCHEMES": {
      return {
        ...state,
        conceptSchemes: reduxReferentiel(action.conceptSchemes, "iri")
      };
    }
    default:
      return state;
  }
};

export const fetchConcepts = () => {
  return (dispatch, getState) => {
    if (getState().referentielReducer.concepts.allIds.length === 0) {
      dispatch(waitingTrue());
      fetch(WS_CONTEXT_PATH + "/concepts", initGet())
        .then(response => response.json())
        .then(json => {
          dispatch(setConcepts(json));
          dispatch(waitingFalse());
        })
        .catch(error => {
          dispatch(waitingFalse());
          console.log(error);
        });
    }
  };
};

export const setConcepts = concepts => {
  return {
    type: "SET_CONCEPTS",
    concepts
  };
};

export const fetchMesures = () => {
  return (dispatch, getState) => {
    if (getState().referentielReducer.mesures.allIds.length === 0) {
      dispatch(waitingTrue());
      fetch(WS_CONTEXT_PATH + "/codelist/CL_Measure", initGet())
        .then(response => response.json())
        .then(json => {
          dispatch(setMesures(json));
          dispatch(waitingFalse());
        })
        .catch(error => {
          dispatch(waitingFalse());
          console.log(error);
        });
    }
  };
};

export const setMesures = mesures => {
  return {
    type: "SET_MESURES",
    mesures
  };
};

export const fetchConceptSchemes = () => {
  return (dispatch, getState) => {
    if (getState().referentielReducer.conceptSchemes.allIds.length === 0) {
      dispatch(waitingTrue());
      fetch(WS_CONTEXT_PATH + "/code-liste/concept-scheme", initGet())
        .then(response => response.json())
        .then(json => {
          dispatch(setConceptSchemes(json));
          dispatch(waitingFalse());
        })
        .catch(error => {
          dispatch(waitingFalse());
          console.log(error);
        });
    }
  };
};

export const setConceptSchemes = conceptSchemes => {
  return {
    type: "SET_CONCEPT_SCHEMES",
    conceptSchemes
  };
};
