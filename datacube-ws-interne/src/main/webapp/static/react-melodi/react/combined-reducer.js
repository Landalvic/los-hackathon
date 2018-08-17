import { combineReducers } from "redux";
import { routerReducer } from "react-router-redux";
import { generalReducer } from "./commun/GeneralReducer";
import { chargementReducer } from "./melodi/ChargementReducer";
import { visualisationReducer } from "./melodi/VisualisationReducer";
import { referentielReducer } from "./melodi/ReferentielReducer";

const combinedReducer = combineReducers({
  routing: routerReducer,
  referentielReducer,
  generalReducer,
  chargementReducer,
  visualisationReducer
});

export default combinedReducer;
